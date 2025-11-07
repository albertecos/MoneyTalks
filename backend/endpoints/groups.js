const { Database } = require("../Database");
const { v4: uuidv4 } = require('uuid');
const { Components } = require('../schema/components');

function sanitizeForIds(objs, res) {
    for(let obj of objs) {
        // Remove any fields that are not id
        for(let key of Object.keys(obj)) {
            if(key !== 'id') {
                delete obj[key];
            }
        }
        // If id is not a valid uuid, return error
        if(!/^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$/.test(obj.id)) {
            return res.status(400).send({error: 'Invalid format'});
        }
    }
    return objs;
}
var endPoints = [];

endPoints.push({method: 'GET', path: '/groups', oapi: {
    summary: 'Get group by member ID',
    parameters: [
        {
            name: 'memberId',
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
            description: 'No groups found for this member ID'
        }
    }
}, handler: (req, res) => {
    const memberId = req.query.memberId;
    // not implemented yet
    res.status(501).send({error: 'Not implemented'});
}});

endPoints.push({method: 'POST', path: '/groups/create', oapi: {
    summary: 'Create a new group',
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
    const {name, description, members} = req.body;
    if(!name || !description || !Array.isArray(members)) {
        return res.status(400).send({error: 'Please enter a valid name, description and members array'});
    }

    let cleanedMembers = sanitizeForIds(members, res);
    const newGroup = {id: uuidv4(), name, description, members: cleanedMembers};
    Database.getInstance('groups').insert(newGroup);

    res.status(201).json(newGroup);
}});

// TODO: only edit name and description (members management endpoints will be separate)
endPoints.push({path: '/groups/edit', method: 'POST', oapi: {
    summary: 'Edit an existing group',
    requestBody: {
        required: true,
        content: {
            'application/json': {
                schema: Components.schemas.Group
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
    const {id, name, description, members} = req.body;
    if(!id || !name || !description || !Array.isArray(members)) {
        return res.status(400).send({error: 'Please enter a valid id, name, description and members array'});
    }

    const existingGroups = Database.getInstance('groups').select({id});
    if(existingGroups.length === 0) {
        return res.status(404).send({error: 'Group not found'});
    }

    members = sanitizeForIds(members, res);
    Database.getInstance('groups').update(id, {name, description, members});

    res.json({id, name, description, members});
}});

module.exports = endPoints;