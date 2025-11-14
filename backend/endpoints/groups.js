const { Database } = require("../Database");
const { v4: uuidv4 } = require('uuid');
const { Components } = require('../schema/components');

var endPoints = [];

endPoints.push({method: 'GET', path: '/groups', oapi: {
    summary: 'Get group by user ID',
    parameters: [
        {
            name: 'userId',
            in: 'query',
            required: true,
            schema: {
                type: 'string',
                format: 'uuid'
            }
        }
    ],
    responses: {
        200: {
            description: 'Groups retrieved successfully',
            content: {
                'application/json': {
                    schema: {
                        type: 'array',
                        items: Components.schemas.Group
                    }
                }
            }
        },
        404: {
            description: 'No groups found for this user ID'
        }
    }
}, handler: (req, res) => {
    // not implemented yet
    res.status(501).send({error: 'Not implemented'});
}});

endPoints.push({method: 'POST', path: '/createGroup', oapi: {
    summary: 'Create a new group',
    parameters: [
        {
            name: 'userId',
            in: 'query',
            required: true,
            schema: {
                type: 'string',
                format: 'uuid'
            }
        }
    ],
    requestBody: {
        required: true,
        content: {
            'application/json': {
                schema: Components.schemas.GroupWithoutId
            }
        }
    },
    responses: {
        201: {
            description: 'Group created successfully',
            content: {
                'application/json': {
                    schema: Components.schemas.Group
                }
            }
        }
    }
}, handler: (req, res) => {
    // not implemented yet
    res.status(501).send({error: 'Not implemented'});
}});

endPoints.push({path: '/editGroup', method: 'POST', oapi: {
    summary: 'Edit an existing group',
    requestBody: {
        required: true,
        content: {
            'application/json': {
                schema: Components.schemas.GroupWithoutMembers
            }
        }
    },
    responses: {
        200: {
            description: 'Group updated successfully',
            content: {
                'application/json': {
                    schema: Components.schemas.Group
                }
            }
        },
        404: {
            description: 'Group not found'
        }
    }
}, handler: (req, res) => {
    // not implemented yet
    res.status(501).send({error: 'Not implemented'});
}});

endPoints.push({path: '/leaveGroup', method: 'GET', oapi: {
    summary: 'Leave a group by group ID and user ID',
    parameters: [
        {
            name: 'groupId',
            in: 'query',
            required: true,
            schema: {
                type: 'string',
                format: 'uuid'
            }
        },
        {
            name: 'userId',
            in: 'query',
            required: true,
            schema: {
                type: 'string',
                format: 'uuid'
            }
        }
    ],
    responses: {
        200: {
            description: 'Group left successfully'
        },
        404: {
            description: 'Group not found or user not in group'
        }
    }
}, handler: (req, res) => {
    // Not implemented yet
    res.status(501).send({error: 'Not implemented'});
}});

module.exports = endPoints;