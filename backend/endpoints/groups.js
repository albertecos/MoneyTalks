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
    const userId = req.query.userId;
    if (!userId) {
        return res.status(400).send({error: 'userId query parameter is required'});
    }

    const groupMembers = Database.getInstance('group_members').select({user_id: userId, accepted: true});
    if(groupMembers.length === 0) {
        return res.status(404).send({error: 'No groups found for this user ID'});
    }

    const groups = groupMembers.map(gm => {
        const group = Database.getInstance('groups').select({id: gm.group_id})[0];
        const members = Database.getInstance('group_members').select({group_id: group.id}).map(memberRecord => {
            const user = Database.getInstance('users').select({id: memberRecord.user_id})[0];
            return {...user, accepted: memberRecord.accepted};
        });
        return {...group, members};
    });

    res.json(groups);
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
    const userId = req.query.userId;
    const groupData = req.body;

    if (!userId) {
        return res.status(400).send({error: 'userId query parameter is required'});
    }

    if (!groupData || !groupData.name || !Array.isArray(groupData.members)) {
        return res.status(400).send({error: 'Invalid group data'});
    }

    let group = {
        id: uuidv4(),
        name: groupData.name,
    };
    const groupsDb = Database.getInstance('groups');
    const groupMembersDb = Database.getInstance('group_members');

    groupsDb.insert(group);

    // if the creator is not in the members list, add them
    if (!groupData.members.includes(userId)) {
        groupData.members.push(userId);
    }

    let members = groupData.members.map(memberId => ({
        id: uuidv4(),
        group_id: group.id,
        user_id: memberId,
        accepted: memberId === userId // auto-accept if the member is the creator
    }));
    members.forEach(member => groupMembersDb.insert(member));

    sendGroupInviteNotifications(group.id, userId, groupData.members);

    res.status(201).json({...group, members: members});
}});

endPoints.push({path: '/editGroup', method: 'POST', oapi: {
    summary: 'Edit an existing group',
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
                schema: Components.schemas.GroupSimplified
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
    const {id, name, members} = req.body;
    const userId = req.query.userId;
    if(!userId || !id || !name || !Array.isArray(members)) {
        return res.status(400).send({error: 'Please enter a valid userId, id, name, and members list'});
    }

    const existingGroups = Database.getInstance('groups').select({id});
    if(existingGroups.length === 0) {
        return res.status(404).send({error: 'Group not found'});
    }

    Database.getInstance('groups').update(id, {name});

    const currentMembersRecords = Database.getInstance('group_members').select({group_id: id});
    const currentMemberIds = currentMembersRecords.map(m => m.user_id);

    members.forEach(memberId => {
        if(!currentMemberIds.includes(memberId)) {
            Database.getInstance('group_members').insert({
                id: uuidv4(),
                group_id: id,
                user_id: memberId,
                accepted: false
            });
        }
    });

    sendGroupInviteNotifications(id, userId, members);

    const updatedGroup = Database.getInstance('groups').select({id})[0];
    res.json(updatedGroup);
}});

function sendGroupInviteNotifications(groupId, inviterId, invitedMemberIds) {
    const group = Database.getInstance('groups').select({id: groupId})[0];
    const usersDb = Database.getInstance('users');
    const notificationDb = Database.getInstance('notifications');

    const inviter = usersDb.select({id: inviterId})[0];
    const inviterName = inviter ? inviter.full_name : 'Unknown user';
    const groupName = group.name;

    invitedMemberIds.forEach(memberId => {
        if (memberId === inviterId) {
            return;
        }

        notificationDb.insert({
            id: uuidv4(),
            action: 'INVITE',
            groupId: group.id,
            groupName,
            userId: memberId,
            amount: null,
            date: new Date().toISOString(),
            interacted: false,
            seen: false,
            description: `${inviterName} invited you to join the group: ${group.name}`
        });
    });
}

endPoints.push({path: '/leaveGroup', method: 'POST', oapi: {
    summary: 'Leave a group by group ID and user ID',
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
                schema: {
                    type: 'object',
                    properties: {
                        groupId: {type: 'string', format: 'uuid'}
                    },
                    required: ['groupId']
                }
            }
        }
    },
    responses: {
        200: {
            description: 'Group left successfully'
        },
        404: {
            description: 'Group not found or user not in group'
        }
    }
}, handler: (req, res) => {
    const {groupId, userId} = req.query;
    if(!groupId || !userId) {
        return res.status(400).send({error: 'Please provide valid groupId and userId'});
    }

    const groupMembers = Database.getInstance('group_members').select({group_id: groupId, user_id: userId});
    if(groupMembers.length === 0) {
        return res.status(404).send({error: 'Group not found or user not in group'});
    }
    
    let balance = groupMembers[0].balance || 0;
    if (balance < 0) {
        return res.status(400).send({error: 'Cannot leave group while you owe money. Please settle your debts before leaving.'});
    } else if (balance > 0) {
        // TODO: give up balance claiming then leaving
    }

    Database.getInstance('group_members').delete(groupMembers[0].id);

    res.json({message: 'Left group successfully'});
}});

module.exports = endPoints;