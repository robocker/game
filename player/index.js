const express = require('express')
const app = express()
const port = 3000
const debug = require('debug')('player:index.js');
const axios = require('axios');

app.get('/', (req, res) => {
    debug('Fired hello world');

    axios.get('http://engine:8080')
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