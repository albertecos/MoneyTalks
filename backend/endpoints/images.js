const { Database } = require("../Database");
const { v4: uuidv4 } = require('uuid');
const { Components } = require('../schema/components');

var endPoints = [];

endPoints.push({method: 'POST', path: '/upload', oapi: {
    summary: 'Upload an image',
}, handler: (req, res) => {
    // Not implemented yet
    res.status(501).send({error: 'Not implemented'});
}});


endPoints.push({method: 'GET', path: '/image/{path}', oapi: {
    summary: 'Get an image by path',
}, handler: (req, res) => {
    // Not implemented yet
    res.status(501).send({error: 'Not implemented'});
}});

module.exports = endPoints;