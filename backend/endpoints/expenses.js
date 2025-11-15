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

    const memberIds = Database.getInstance('group_members').select({group_id: groupId}).map(m => m.id);
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
    const members = Database.getInstance('group_members').select({group_id: groupId});
    const member = members.find(m => m.user_id === userId);
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

    const { groupId, amount, description } = req.body;

    if (!groupId || !amount || !description) {
        return res.status(400).send({error: 'Please provide groupId, amount, and description for the expense'});
    }

    const member = Database.getInstance('group_members').select({group_id: groupId, user_id: userId})[0];

    Database.getInstance('expenses').insert({
        id: uuidv4(),
        member_id: member.id,
        date: new Date().toISOString(),
        amount: amount,
        description: description,
        action: 'expense'
    });

    // Todo: Notify other group members about the new expense

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

    const member = Database.getInstance('group_members').select({group_id: groupId, user_id: userId})[0];

    Database.getInstance('expenses').insert({
        id: uuidv4(),
        member_id: member.id,
        date: new Date().toISOString(),
        amount: -balance,
        description: 'Payment of owed expenses',
        action: 'payment'
    });

    // Todo: Notify other group members about the payment

    res.status(201).send({message: 'Expense paid successfully'});
}});

module.exports = endPoints;