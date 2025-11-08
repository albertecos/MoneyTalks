const { Database } = require("../Database");
const { v4: uuidv4 } = require('uuid');
const { Components } = require('../schema/components');

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
    
    let groups = Database.getInstance('groups').all();
    groups = groups.filter(group => group.members.includes(memberId));

    if(groups.length === 0) {
        return res.status(404).send({error: 'No groups found for this member ID'});
    }

    res.json(groups);
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

    const newGroup = {id: uuidv4(), name, description, members};
    Database.getInstance('groups').insert(newGroup);

    res.status(201).json(newGroup);
}});

endPoints.push({path: '/groups/edit', method: 'POST', oapi: {
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
    const {id, name, description} = req.body;
    if(!id || !name || !description) {
        return res.status(400).send({error: 'Please enter a valid id, name, and description'});
    }

    const existingGroups = Database.getInstance('groups').select({id});
    if(existingGroups.length === 0) {
        return res.status(404).send({error: 'Group not found'});
    }

    Database.getInstance('groups').update(id, {name, description});

    res.json(Database.getInstance('groups').select({id})[0]);
}});

endPoints.push({method: 'POST', path: '/groups/addMember', oapi: {
    summary: 'Add a member to a group',
    requestBody: {
        required: true,
        content: {
            'application/json': {
                schema: {
                    type: 'object',
                    properties: {
                        groupId: {type: 'string', format: 'uuid'},
                        memberId: {type: 'string', format: 'uuid'}
                    },
                    required: ['groupId', 'memberId']
                }
            }
        }
    },
    responses: {
        200: {
            description: 'Member added successfully',
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
    const {groupId, memberId} = req.body;
    if(!groupId || !memberId) {
        return res.status(400).send({error: 'Please enter a valid groupId and memberId'});
    }

    const existingGroups = Database.getInstance('groups').select({id: groupId});
    if(existingGroups.length === 0) {
        return res.status(404).send({error: 'Group not found'});
    }

    const existingMembers = Database.getInstance('users').select({id: memberId});
    if(existingMembers.length === 0) {
        return res.status(404).send({error: 'Member not found'});
    }

    const group = existingGroups[0];
    if(!group.members.includes(memberId)) {
        group.members.push(memberId);
        Database.getInstance('groups').update(groupId, {members: group.members});
    }

    res.json(Database.getInstance('groups').select({id: groupId})[0]);
}});

endPoints.push({method: 'POST', path: '/groups/removeMember', oapi: {
    summary: 'Remove a member from a group',
    requestBody: {
        required: true,
        content: {
            'application/json': {
                schema: {
                    type: 'object',
                    properties: {
                        groupId: {type: 'string', format: 'uuid'},
                        memberId: {type: 'string', format: 'uuid'}
                    },
                    required: ['groupId', 'memberId']
                }
            }
        }
    },
    responses: {
        200: {
            description: 'Member removed successfully',
            content: {
                'application/json': {
                    schema: Components.schemas.Group
                }
            }
        },
        404: {
            description: 'Group or member not found'
        }
    }
}, handler: (req, res) => {
    const {groupId, memberId} = req.body;
    if(!groupId || !memberId) {
        return res.status(400).send({error: 'Please enter a valid groupId and memberId'});
    }

    const existingGroups = Database.getInstance('groups').select({id: groupId});
    if(existingGroups.length === 0) {
        return res.status(404).send({error: 'Group not found'});
    }

    const group = existingGroups[0];
    if(group.members.includes(memberId)) {
        group.members = group.members.filter(id => id !== memberId);
        Database.getInstance('groups').update(groupId, {members: group.members});
    } else {
        return res.status(404).send({error: 'Member not found in group'});
    }

    res.json(Database.getInstance('groups').select({id: groupId})[0]);
}});

module.exports = endPoints;