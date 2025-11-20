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
                        username: { type: 'string' },
                        password: {type: 'string'}
                    },
                    required: ['password']
                }
            }
        },
    },
    responses: {
        200: {
                    description: 'Login successful',
                    content: {
                        'application/json': {
                            schema: {
                                type: 'object',
                                properties: {
                                    success: { type: 'boolean' },
                                    message: { type: 'string' },
                                    user: { $ref: '#/components/schemas/User' }
                                }
                            }
                        }
                    }
                },
        401: {
            description: 'Invalid credentials'
        }
    }
}, handler: (req, res) => {
    const {email, username, password} = req.body;
    if(!email && !username || !password) {
        return res.status(400).send({error: 'Please enter a valid email/username and password'});
    }

    const db = Database.getInstance('users');
    const users = db.select({}).filter(u =>
                u.password === password &&
                ((email && u.email === email) || (username && u.username === username))
            );

            if(users.length === 0) return res.status(401).send({error: 'Invalid credentials'});

    res.status(200).json({ success: true, message: 'Login successful', user: users[0] });
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

            const db = Database.getInstance('users');

    //let existingUsers = Database.getInstance('users').select({email});

    if(db.select({email}).length > 0) return res.status(409).send({error: 'User already exists'});
    if(db.select({username}).length > 0) return res.status(409).send({error: 'Username already taken'});



    // TODO: profile picture
    const newUser = {id: uuidv4(), username, full_name, email, password, profile_picture: ""};
    Database.getInstance('users').insert(newUser);

    res.status(201).json(newUser);
}});

module.exports = endPoints;