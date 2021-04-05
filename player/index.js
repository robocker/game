const express = require('express')
const app = express()
const port = 3000
const debug = require('debug')('player:index.js');
const axios = require('axios');
const path = require('path');

app.use(express.static('frontend/build'));

app.get('/', (req, res) => {
    debug('Fired hello world');

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
app.get('/player', (req, res) => {

    axios.get('http://player:3000')
        .then((springMsg)=>{
            debug(springMsg.data);

            res.json({msg:'Hello world!', fromSpring: springMsg.data
                });
        },
        (error)=>{
            res.json({msg: error});
        });


})

app.listen(port, () => {
    console.log(`Example app listening at http://localhost:${port}`)
})