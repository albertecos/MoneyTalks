const { Database } = require("../Database");
const { v4: uuidv4 } = require('uuid');
const { Components } = require('../schema/components');

var endPoints = [];

endPoints.push({method: 'GET', path: '/searchUsers', oapi: {
    summary: 'Search for users',
    parameters: [
        {
            name: 'username',
            in: 'query',
            required: true,
            schema: {
                type: 'string'
            }
        }
    ],
    responses: {
        200: {
            description: 'Users found',
            content: {
                'application/json': {
                    schema: {
                        type: 'array',
                        items: Components.schemas.User
                    }
                }
            }
        },
        404: {
            description: 'No users found'
        }
    }
}, handler: (req, res) => {
    const users = Database.getInstance('users').selectLike({username: req.query.username});
    if(users.length === 0) {
        return res.status(404).send({error: 'No users found'});
    }
    res.json(users);
}});

endPoints.push({method: 'POST', path: '/updateUser', oapi: {
    summary: 'Update an existing user',
    requestBody: {
        required: true,
        content: {
            'application/json': {
                schema: Components.schemas.User
            }
        }
    },
    responses: {
        200: {
            description: 'User updated successfully',
            content: {
                'application/json': {
                    schema: Components.schemas.User
                }
            }
        }
    }
}, handler: (req, res) => {
    const {id, username, full_name, email, password} = req.body;
    if(!id || !username || !full_name || !email || !password) {
        return res.status(400).send({error: 'Please enter a valid id, username, full name, email and password'});
    }

    const existingUsers = Database.getInstance('users').select({id});
    if(existingUsers.length === 0) {
        return res.status(404).send({error: 'User not found'});
    }

    Database.getInstance('users').update(id, {username, full_name, email, password});

    res.json({id, username, full_name, email, password});
}});


endPoints.push({method: 'POST', path: '/login', oapi: {
    summary: 'User login',
    requestBody: {
        required: true,
        content: {
            'application/json': {
                schema: {
                    type: 'object',
                    properties: {
                        email: {type: 'string', format: 'email'},
                        password: {type: 'string'}
                    },
                    required: ['email', 'password']
                }
            }
        },
    },
    responses: {
        200: {
            description: 'Login successful',
            content: {
                'application/json': {
                    schema: Components.schemas.User
                }
            }
        },
        401: {
            description: 'Invalid credentials'
        }
    }
}, handler: (req, res) => {
    const {email, password} = req.body;
    if(!email || !password) {
        return res.status(400).send({error: 'Please enter a valid email and password'});
    }

    const users = Database.getInstance('users').select({email, password});
    if(users.length === 0) {
        return res.status(401).send({error: 'Invalid credentials'});
    }

    res.json(users[0]);
}});

endPoints.push({method: 'POST', path: '/signup', oapi: {
    summary: 'User signup',
    requestBody: {
        required: true,
        content: {
            'application/json': {
                schema: Components.schemas.UserWithoutId
            }
        }
    },
    responses: {
        201: {
            description: 'User created successfully',
            content: {
                'application/json': {
                    schema: Components.schemas.User
                }
            }
        }
    }
}, handler: (req, res) => {
    const {username, full_name, email, password} = req.body;
    if(!username || !full_name || !email || !password) {
        return res.status(400).send({error: 'Please enter a valid username, full name, email and password'});
    }

    let existingUsers = Database.getInstance('users').select({email});
    if(existingUsers.length > 0) {
        return res.status(409).send({error: 'User already exists'});
    }
    existingUsers = Database.getInstance('users').select({username});
    if(existingUsers.length > 0) {
        return res.status(409).send({error: 'Username already taken'});
    }

    const newUser = {id: uuidv4(), username, full_name, email, password, profile_picture: "337d5322-930a-472e-8c0f-ebd04cc9b3ef.jpg"};
    Database.getInstance('users').insert(newUser);

    res.status(201).json(newUser);
}});

module.exports = endPoints;