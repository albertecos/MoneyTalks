var Components = {
    schemas: {
        Group: {
            type: 'object',
            properties: {
                id: {type: 'string', format: 'uuid'},
                name: {type: 'string'},
                description: {type: 'string'},
                members: {
                    type: 'array',
                    items: {
                        type: 'string',
                        format: 'uuid'
                    }
                }
            },
            required: ['id', 'name', 'description', 'members']
        },
        GroupWithoutId: {
            type: 'object',
            properties: {
                name: {type: 'string'},
                description: {type: 'string'},
                members: {
                    type: 'array',
                    items: {
                        type: 'string',
                        format: 'uuid'
                    }
                }
            },
            required: ['name', 'description', 'members']
        },
        GroupWithoutMembers: {
            type: 'object',
            properties: {
                id: {type: 'string', format: 'uuid'},
                name: {type: 'string'},
                description: {type: 'string'}
            },
            required: ['id', 'name', 'description']
        },
        User: {
            type: 'object',
            properties: {
                id: {type: 'string', format: 'uuid'},
                username: {type: 'string'},
                full_name: {type: 'string'},
                email: {type: 'string', format: 'email'},
                password: {type: 'string'}
            },
            required: ['id', 'username', 'full_name', 'email', 'password']
        },
        UserWithoutId: {
            type: 'object',
            properties: {
                username: {type: 'string'},
                full_name: {type: 'string'},
                email: {type: 'string', format: 'email'},
                password: {type: 'string'}
            },
            required: ['username', 'full_name', 'email', 'password']
        },
    }
};

module.exports.Components = Components;