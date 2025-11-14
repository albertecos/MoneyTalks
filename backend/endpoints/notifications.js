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

    notifications = notifications.filter(n => n.userId === userId)

    if(notifications.length === 0){
        return res.status(404).send({error: 'No notifications on for this user id'})
    }

    res.json(notifications);
}});

endPoints.push({method: 'POST', path: '/acceptInvite', oapi: {
    summary: 'Accept invite by member ID',
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
            description: 'Invite accepted successfully',
        },
        404: {
            description: 'No notifications found for this notification ID'
        }
    }
}, handler: (req, res) => {
    // not implemented yet
    res.status(501).send({error: 'Not implemented'});
}});

endPoints.push({method: 'POST', path: '/declineInvite', oapi: {
    summary: 'Decline invite by member ID',
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
            description: 'Invite declined successfully',
        },
        404: {
            description: 'No notifications found for this notification ID'
        }
    }
}, handler: (req, res) => {
    // not implemented yet
    res.status(501).send({error: 'Not implemented'});
}});

module.exports = endPoints;