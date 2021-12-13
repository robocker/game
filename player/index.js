const express = require('express')
const app = express()
const port = 3000
const debug = require('debug')('player:index.js');
const axios = require('axios');
const path = require('path');
var expressWs = require('express-ws')(app);

app.use(express.static('dist'));
app.use(express.json());

// app.use(function (req, res, next) {
//     debug('middleware');
//     req.testing = 'testing';
//     return next();
//   });

app.get('/', (req, res) => {
    debug('Player base url');

    res.sendFile(path.join(__dirname + '/dist/index.html'));
})

app.post('/api/*', (req, res) => {

    debug(req._parsedUrl.path);
    debug(req.body);

    switch(req._parsedUrl.path){

        case '/api/tanks/move':
            sendMove(req, res);
        break;

        default:
        debug("Post command not found:"+ req._parsedUrl.path);
    }

});

app.get('/api/*', (req, res) => {

    debug(req._parsedUrl.path);
    debug(req.body);

    switch(req._parsedUrl.path){

        case '/api/info':
            axios.get('http://engine:8080/info/all')
            .then((springMsg)=>{
                debug(springMsg.data);

                res.json(springMsg.data);
            },
            (error)=>{
                res.json({msg: error});
            });

        break;

        default:
        debug("Get command not found:"+ req._parsedUrl.path);
    }

});

app.ws('/', function(ws, req) {
    ws.on('message', function(msg) {
        debug(msg);
    });
    // debug('socket', req.testing);

    let count = 0;
    setInterval(()=>{
        count++;
        ws.send(`Hi Player. I'm backend${count}`);

    },1000)

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
                res.json({msg:'response from move', fromTank: result
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