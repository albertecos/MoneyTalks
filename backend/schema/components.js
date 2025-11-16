var Components = {
    schemas: {
        Group: {
            type: 'object',
            properties: {
                id: {type: 'string', format: 'uuid'},
                name: {type: 'string'},
//                members: {
//                    type: 'array',
//                    items: {
//                        type: 'object',
//                        properties: {
//                            id: {type: 'string', format: 'uuid'},
//                            username: {type: 'string'},
//                            profile_picture: {type: 'string', format: 'uri'},
//                            full_name: {type: 'string'},
//                            email: {type: 'string', format: 'email'},
//                            password: {type: 'string'},
//                            accepted: {type: 'boolean'}
//                        },
//                        required: ['id', 'username', 'profile_picture', 'full_name', 'email', 'password', 'accepted']
//                    }
//                }
            },
            required: ['id', 'name', 'members']
        },
        GroupWithoutId: {
            type: 'object',
            properties: {
                name: {type: 'string'},
                members: {
                    type: 'array',
                    items: {
                        type: 'string',
                        format: 'uuid'
                    }
                }
            },
            required: ['name', 'members']
        },
        GroupWithoutMembers: {
            type: 'object',
            properties: {
                id: {type: 'string', format: 'uuid'},
                name: {type: 'string'},
            },
            required: ['id', 'name']
        },
        User: {
            type: 'object',
            properties: {
                id: {type: 'string', format: 'uuid'},
                username: {type: 'string'},
                profile_picture: {type: 'string', format: 'uri'},
                full_name: {type: 'string'},
                email: {type: 'string', format: 'email'},
                password: {type: 'string'}
            },
            required: ['id', 'username', 'profile_picture', 'full_name', 'email', 'password']
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
        Notification: {
            type: 'object',
            properties: {
                id: {type: 'string', format: 'uuid'},
                date: {type: 'string', format: 'date-time'},
                description: {type: 'string'},
                seen: {type: 'boolean'},
                inviteId: {type: 'string', format: 'uuid'}
            },
            required: ['id', 'date', 'description', 'seen']
        },
        Expense: {
            type: 'object',
            properties: {
                id: {type: 'string', format: 'uuid'},
                groupId: {type: 'string', format: 'uuid'},
                description: {type: 'string'},
                amount: {type: 'number', format: 'float'},
                date: {type: 'string', format: 'date-time'},
                action: {type: 'string'}, // either 'expense' or 'payment'
            },
            required: ['id', 'groupId', 'description', 'amount', 'date', 'action']
        },
        ExpenseWithoutId: {
            type: 'object',
            properties: {
                groupId: {type: 'string', format: 'uuid'},
                description: {type: 'string'},
                amount: {type: 'number', format: 'float'},
            },
            required: ['groupId', 'description', 'amount', 'action']
        }
    }
};

module.exports.Components = Components;