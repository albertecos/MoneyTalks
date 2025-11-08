const {v4: uuidv4} = require('uuid');
const express = require('express');
const cors = require('cors');
const app = express();
const openapi = require('@wesleytodd/openapi');
app.use(cors()); // Enable CORS for all routes
app.use(express.json()); // This middleware is used to parse JSON bodies.

const { Components } = require('./schema/components');


const oapi = openapi({
    title: "MoneyTalks API",
  version: "0.1.0",
  description: "API for MoneyTalks application",
  servers: [
    {
      url: "http://localhost:3000",
      description: "Local server"
    }
  ],
  components: Components
});

app.use(oapi);
app.use((req, res, next) => {
  console.log("Incoming:", req.method, req.url);
  next();
});


const groupsEndpoints = require('./endpoints/groups');
const usersEndpoints = require('./endpoints/users');

registerGroupEndpoints(groupsEndpoints);
registerGroupEndpoints(usersEndpoints);

function registerGroupEndpoints(endPoints) {
    for (const endpoint of endPoints) {
        if (endpoint.method === 'GET') {
            app.get(endpoint.path, oapi.path(endpoint.oapi), endpoint.handler);
        } else if (endpoint.method === 'POST') {
            app.post(endpoint.path, oapi.path(endpoint.oapi), endpoint.handler);
        }
    }
}

app.use('/swaggerui', oapi.swaggerui())
app.listen(3000, "0.0.0.0", () => console.log('Server running on \'0.0.0.0:3000\''));
