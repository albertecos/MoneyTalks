var Components = {
    schemas: {
        GroupWithoutId: {
            type: 'object',
            properties: {
                name: {type: 'string'},
                description: {type: 'string'},
                members: {
                    type: 'array',
                    items: {
                        type: 'object',
                        properties: {
                            id: {type: 'string', format: 'uuid'}
                        },
                        required: ['id']
                    }
                }
            },
            required: ['name', 'description', 'members']
        },
        Group: {
            type: 'object',
            properties: {
                id: {type: 'string', format: 'uuid'},
                name: {type: 'string'},
                description: {type: 'string'},
                members: {
                    type: 'array',
                    items: {
                        type: 'object',
                        properties: {
                            id: {type: 'string', format: 'uuid'}
                        },
                        required: ['id']
                    }
                }
            },
            required: ['id', 'name', 'description', 'members']
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
        }
    }
};

module.exports.Components = Components;