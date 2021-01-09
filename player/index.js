const express = require('express')
const app = express()
const port = 3000
const debug = require('debug')('player:index.js');
const axios = require('axios');

app.get('/', async(req, res) => {
    debug('Fired hello world');

    const springMsg = await axios.get('http://localhost:8070/');

    debug(springMsg.data);

    res.json({msg:'Hello world!', fromSpring: springMsg.data});
})

app.listen(port, () => {
    console.log(`Example app listening at http://localhost:${port}`)
})