const express = require('express')
const app = express()
const port = 3000
const debug = require('debug')('player:index.js');

app.get('/', (req, res) => {
    debug('Fired hello world');
    res.json({'msg':'Hello world!'});
})

app.listen(port, () => {
    console.log(`Example app listening at http://localhost:${port}`)
})