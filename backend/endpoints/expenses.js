const { Database } = require("../Database");
const { v4: uuidv4 } = require('uuid');
const { Components } = require('../schema/components');

var endPoints = [];

endPoints.push({method: 'GET', path: '/expenseHistory', oapi: {
    summary: 'Get expense history by group ID',
    parameters: [
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
            description: 'Expense history retrieved successfully',
            content: {
                'application/json': {
                    schema: {
                        type: 'array',
                        items: Components.schemas.Expense
                    }
                }
            }
        },
        404: {
            description: 'No expense history found for this group ID'
        }
    }
}, handler: (req, res) => {
    const groupId = req.query.groupId;
    if (!groupId) {
        return res.status(400).send({error: 'groupId query parameter is required'});
    }

    const memberIds = Database.getInstance('group_members').select({group_id: groupId, accepted: true}).map(m => m.id);
    const expenses = Database.getInstance('expenses').all().filter(e => memberIds.includes(e.member_id));
    if(expenses.length === 0) {
        return res.status(404).send({error: 'No expense history found for this group ID'});
    }

    res.json(expenses);
}});

endPoints.push({method: 'GET', path: '/getBalance', oapi: {
    summary: 'Get balance by group ID and user ID',
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
            description: 'Balance retrieved successfully',
            content: {
                'application/json': {
                    schema: {
                        type: 'object',
                        properties: {
                            balance: {type: 'number', format: 'float'}
                        },
                        required: ['balance']
                    }
                }
            }
        },
        404: {
            description: 'No balance found for this group ID and user ID'
        }
    }
}, handler: (req, res) => {
    const groupId = req.query.groupId;
    const userId = req.query.userId;

    if (!groupId || !userId) {
        return res.status(400).send({error: 'groupId and userId query parameters are required'});
    }

    const balance = getBalance(groupId, userId);

    res.json({balance});
}});

function getBalance(groupId, userId) {
    const members = Database.getInstance('group_members').select({group_id: groupId, accepted: true});
    const member = members.find(m => m.user_id === userId);
    if(!member){
        return 0;
    }

    const expenses = Database.getInstance('expenses').all().filter(e => members.map(m => m.id).includes(e.member_id));

    let owed = expenses.filter(e => e.action === 'expense').reduce((sum, e) => sum + e.amount, 0) / members.length;
    let paid = expenses.filter(e => e.action === 'payment' && e.member_id === member.id).reduce((sum, e) => sum + e.amount, 0);
    paid += expenses.filter(e => e.action === 'expense' && e.member_id === member.id).reduce((sum, e) => sum + e.amount, 0);

    const balance = paid - owed;
    return balance;
}

endPoints.push({method: 'POST', path: '/createExpense', oapi: {
    summary: 'Create an expense',
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
                schema: Components.schemas.ExpenseWithoutId
            }
        }
    },
    responses: {
        200: {
            description: 'Expense created successfully'
        },
        404: {
            description: 'Failed to create expense'
        }
    }
}, handler: (req, res) => {
    const userId = req.query.userId;

    if (!userId) {
        return res.status(400).send({error: 'userId query parameter is required'});
    }

    const { groupId, amount, description, payers } = req.body;

    if (!groupId || !amount || !description || payers == undefined || payers.length == 0) {
        return res.status(400).send({error: 'Please provide groupId, amount, and description for the expense'});
    }

    const member = Database.getInstance('group_members').select({group_id: groupId, user_id: userId, accepted: true})[0];

    Database.getInstance('expenses').insert({
        id: uuidv4(),
        member_id: member.id,
        date: new Date().toISOString(),
        amount: amount,
        description: description,
        action: 'expense',
        payers: payers
    });

    const notificationsDb = Database.getInstance('notifications');
    const groupsDb = Database.getInstance('groups');
    const usersDb = Database.getInstance('users');

    const group = groupsDb.select({ id: groupId })[0];
    const groupName = group ? group.name: 'default group name';

    const creatorUser = usersDb.select({ id: userId })[0];
    const creatorName = creatorUser ? creatorUser.full_name : 'unknow user'

    const members = Database.getInstance('group_members').select({ group_id: groupId, accepted: true });

    members.forEach(m => {
        if (m.user_id === userId) {
            return
        }

        notificationsDb.insert({
            id: uuidv4(),
            action: 'EXPENSE',
            groupId,
            groupName,
            userId: m.user_id,
            amount,
            date: new Date().toISOString(),
            interacted: false,
            seen: false,
            description: `${creatorName} added an expense: ${description} (${amount})`
        });
    });

    res.status(201).send({message: 'Expense created successfully'});
}});

endPoints.push({method: 'POST', path: '/payOwed', oapi: {
    summary: 'Pay owed expenses',
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
            description: 'Expense paid successfully'
        },
        404: {
            description: 'Failed to pay expense'
        }
    }
}, handler: (req, res) => {
    const userId = req.query.userId;
    const { groupId } = req.body;

    if (!userId || !groupId) {
        return res.status(400).send({error: 'userId query parameter and groupId in body are required'});
    }

    const balance = getBalance(groupId, userId);

    if (balance >= 0) {
        return res.status(400).send({error: 'No owed expenses to pay'});
    }

    const member = Database.getInstance('group_members').select({group_id: groupId, user_id: userId, accepted: true})[0];

    Database.getInstance('expenses').insert({
        id: uuidv4(),
        member_id: member.id,
        date: new Date().toISOString(),
        amount: -balance,
        description: 'Payment of owed expenses',
        action: 'payment'
    });

    const notificationsDb = Database.getInstance('notifications');
    const groupsDb = Database.getInstance('groups');
    const usersDb = Database.getInstance('users');

    const group = groupsDb.select({ id: groupId })[0];
    const groupName = group ? group.name: 'default group name';

    const payerUser = usersDb.select({ id: userId })[0];
    const payerName = payerUser ? payerUser.full_name : 'unknow user'

    const members = Database.getInstance('group_members').select({ group_id: groupId, accepted: true });

    const totalAmountPaid = -balance;

    members.forEach(m => {
        const isPayer = m.user_id === userId;

        notificationsDb.insert({
            id: uuidv4(),
            action: isPayer ? 'PAYMENT' : 'RECEIVEMENT',
            groupId,
            groupName,
            userId: m.user_id,
            amount: totalAmountPaid,
            date: new Date().toISOString(),
            interacted: false,
            seen: false,
            description: isPayer
                ? `You paid ${totalAmountPaid} to settle your balance in ${groupName}`
                : `${payerName} paid ${totalAmountPaid} in ${groupName}`
        });
    });

    res.status(201).send({message: 'Expense paid successfully'});
}});

module.exports = endPoints;