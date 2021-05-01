const express = require('express')
const app = express()
const port = 80
const debug = require('debug')('tankBasic:index.js');
const axios = require('axios');
const path = require('path');

app.use(express.static('frontend/build'));
app.use(express.json());

app.get('/', (req, res) => {
    debug('Tank base url');
    res.json({msg: "I'm tank"});
});

app.post('/move', (req, res) => {
    debug('Tank move');
    debug(req.body);

    res.json({msg:"I'm tank! Let's move!", body: req.body});

})





app.listen(port, () => {
    console.log(`Example app listening at http://localhost:${port}`)
})