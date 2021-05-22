const express = require('express')
const app = express()
const port = 3000
const debug = require('debug')('player:index.js');
const axios = require('axios');
const path = require('path');

app.use(express.static('frontend/build'));
app.use(express.json());

app.get('/', (req, res) => {
    debug('Player base url');

    res.sendFile(path.join(__dirname + '/frontend/build/index.html'));

    // axios.get('http://engine:8080')
    //     .then((springMsg)=>{
    //         debug(springMsg.data);

    //         res.json({msg:'Hello world!', fromSpring: springMsg.data
    //             });
    //     },
    //     (error)=>{
    //         res.json({msg: error});
    //     });


})

app.get('/info', (req, res) => {
    axios.get('http://engine:8080/info/all')
        .then((springMsg)=>{
            debug(springMsg.data);

            res.json({msg:'Hello world!', fromSpring: springMsg.data
                });
        },
        (error)=>{
            res.json({msg: error});
        });


})

app.post('/api/*', (req, res) => {

    debug(req._parsedUrl.path);
    debug(req.body);

    switch(req._parsedUrl.path){

        case '/api/tanks/move':
            sendMove(req, res);
        break;

        default:
        debug("Command not found:"+ req._parsedUrl.path);
    }
    
});

const sendMove = (req, res) => {

    let promises = [];

    for(let id of req.body.ids){

        debug(`http://tank-${id}`);
        debug({... req.body.destination});

        promises.push(axios
            .post(`http://tank-${id}/move`, {... req.body.destination})
        );
    }

    Promise.allSettled(promises)
        .then((tankResponse)=>{
                var result = [];

                for(let i = 0; i < tankResponse.length; i ++){
                    var tankResp = tankResponse[i];

                    varDataToPush = {id: req.body.ids[i], status: tankResp.status, data: tankResp.value.data};
                    debug(varDataToPush);
                    result.push(varDataToPush);
                }

                debug('result:', result);
                res.json({msg:'Hello world!', fromTank: result
                });
        },
        (error)=>{
            if(error.code == 'ENOTFOUND'){
                res.statusCode = 404;
            }
            res.json({msg: error});
        });
}


app.listen(port, () => {
    console.log(`Example app listening at http://localhost:${port}`)
})