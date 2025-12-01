const { Database } = require("../Database");
const { v4: uuidv4 } = require('uuid');
const { Components } = require('../schema/components');

var endPoints = [];

endPoints.push({method: 'GET', path: '/getNotifications', oapi: {
    summary: 'Get notifications by user ID',
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
            description: 'Notifications retrieved successfully',
            content: {
                'application/json': {
                    schema: {
                        type: 'array',
                        items: Components.schemas.Notification
                    }
                }
            }
        },
        404: {
            description: 'No notifications found for this user ID'
        }
    }
}, handler: (req, res) => {
    const userId = req.query.userId;

    let notifications = Database.getInstance('notifications').all();

    notifications = notifications.filter(n => n.userId === userId && !n.interacted)

    if(notifications.length === 0){
        return res.status(404).send({error: 'No notifications on for this user id'})
    }

    res.json(notifications);
}});

endPoints.push({method: 'POST', path: '/acceptInvite', oapi: {
    summary: 'Accept invite by user ID',
    parameters: [
        {
            name: 'userId',
            in: 'query',
            required: true,
            schema: {
                type: 'string',
                format: 'uuid'
            }
        },
        {
            name: 'groupId',
            in: 'query',
            required: true,
            schema: { type: 'string', format: 'uuid' }
        }
    ],
    responses: {
        200: {
            description: 'Invite accepted successfully',
        },
        404: {
            description: 'No notifications found for this notification ID'
        }
    }
}, handler: (req, res) => {
    const {userId, groupId } = req.query;

    if (!userId || !groupId) {
        return res.status(400).send({ error: 'userId and groupId are required' });
    }

    const notificationsDb = Database.getInstance('notifications');
    const groupMembersDb = Database.getInstance('group_members');

    const invites = notificationsDb.select({
        userId,
        groupId,
        action: 'INVITE'
    });

    if(!invites || invites.length === 0){
        return res.status(404).send({error: 'No invite notifications found'})
    }

    invites.forEach(invite => {
        notificationsDb.update(invite.id, {interacted: true })
    })

    const members = groupMembersDb.select({ group_id: groupId, user_id: userId });

    if(members.length === 0) {
        groupMembersDb.insert({
            id: uuidv4(),
            group_id: groupId,
            user_id: userId,
            accepted: true
        });
    } else {
        members.forEach(member => {
            groupMembersDb.update(member.id, { accepted: true });
        });
    }

    res.send({ ok: true })
}});

endPoints.push({method: 'POST', path: '/declineInvite', oapi: {
    summary: 'Decline invite by user ID',
    parameters: [
        {
            name: 'notificationId',
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
            description: 'Invite declined successfully',
        },
        404: {
            description: 'No notifications found for this notification ID'
        }
    }
}, handler: (req, res) => {
    const { notificationId } = req.query;

    if(!notificationId) {
        return res.status(400).send({ error: 'notificationId is required' })
    }

    const notificationsDb = Database.getInstance('notifications');

    const invites = notificationsDb.select({
        id: notificationId,
        action: 'INVITE'
    });

    if(!invites || invites.length === 0){
        return res.status(404).send({error: 'No invite notifications found'})
    }

    const invite = invites[0]

    notificationsDb.update(invite.id, { interacted: true })

    res.send({ ok: true })
}});



endPoints.push({method: 'POST', path: '/createNotification', oapi: {
    summary: 'Create a notification',
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
                        groupId: {type: 'string', format: 'uuid'},
                        groupName: {type: 'string'},
                        action: { type: 'string'},
                        amount: { type: ['number', 'null']},
                        description: { type: 'string' }
                    },
                    required: ['groupId', 'groupName', 'action', 'description']
                }
            }
        }
    },
    responses: {
        200: {
            description: 'Notification created successfully'
        },
        404: {
            description: 'Failed to create notification'
        }
    }
}, handler: (req, res) => {
    const userId = req.query.userId;

    if (!userId) {
        return res.status(400).send({error: 'userId query parameter is required'});
    }

    const {groupId, groupName, action, amount, description } = req.body;

    if (!groupId || !groupName || !action|| !amount || !description) {
        return res.status(400).send({error: 'Please provide groupId, amount, and description for the expense'});
    }

    Database.getInstance('notifications').insert({
        id: uuidv4(),
        action,
        groupId,
        groupName,
        userId,
        amount: amount ?? null,
        date: new Date().toISOString(),
        interacted: false,
        seen: false,
        description,

    });
    res.status(201).send({message: 'Notification created successfully'});
}});

endPoints.push({method: 'POST', path: '/sendReminder', oapi: {
    summary: 'Send reminder notification to user for a group',
    parameters: [
        {
            name: 'userId',
            in: 'query',
            required: true,
            schema: {
                type: 'string',
                format: 'uuid'
            }
        },
        {
            name: 'groupId',
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
            description: 'Reminder sent successfully'
        },
        404: {
            description: 'Failed to send reminder'
        }
    }
}, handler: (req, res) => {
    const { userId,  groupId } = req.query;

    if (!userId || !groupId) {
        return res.status(400).send({error: 'userId and groupId query parameters are required'});
    }

    const groupsDb = Database.getInstance('groups');
    const usersDb = Database.getInstance('users');
    const notificationsDb = Database.getInstance('notifications');
    const membersDb = Database.getInstance('group_members');

    const group = groupsDb.select({ id: groupId })[0];
    const groupName = group ? group.name: 'default group name';

    const member = membersDb.select({ group_id: groupId, user_id: userId, accepted: true })[0];
    if (!member) {
        return res.status(404).send({ error: 'User is not a member of this group'});
    }

    const balance = member.balance || 0;

    if (balance >= 0){
        return res.status(400).send({ error: 'User does not owe anything in this group'});
    }

    const amountOwed = -balance;

    const user = usersDb.select({ id: userId })[0];
    const userName = user ? (user.full_name || user.username ||'A user') : 'A user';

    notificationsDb.insert({
        id: uuidv4(),
        action: 'REMINDER',
        groupId,
        groupName,
        userId,
        amount: amountOwed,
        date: new Date().toISOString(),
        interacted: false,
        seen: false,
        description: `Reminder: ${userName}, you have pending expenses in the group ${groupName}. Please settle them soon!`
    });

    res.status(200).send({message: 'Reminder sent successfully'});
}});

module.exports = endPoints;