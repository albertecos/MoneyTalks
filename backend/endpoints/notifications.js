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

    notifications = notifications.filter(n => n.userId === userId && !n.seen)

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
        notificationsDb.update(invite.id, {seen: true })
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
            description: 'Invite declined successfully',
        },
        404: {
            description: 'No notifications found for this notification ID'
        }
    }
}, handler: (req, res) => {
    const userId = req.query.userId;

    const notificationsDb = Database.getInstance('notifications');

    const invites = notificationsDb.select({
        userId: userId,
        action: 'INVITE'
    });

    if(!invites || invites.length === 0){
        return res.status(404).send({error: 'No invite notifications found'})
    }

    invites.forEach(invite => {
        notificationsDb.update(invite.id, {seen: true })
    })

    res.send({ ok: true })
}});

module.exports = endPoints;