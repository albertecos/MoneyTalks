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
    // not implemented yet
    res.status(501).send({error: 'Not implemented'});
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
    // not implemented yet
    res.status(501).send({error: 'Not implemented'});
}});

endPoints.push({method: 'POST', path: '/createExpense', oapi: {
    summary: 'Create an expense',
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
    // not implemented yet
    res.status(501).send({error: 'Not implemented'});
}});

endPoints.push({method: 'POST', path: '/payExpense', oapi: {
    summary: 'Pay an expense by group ID',
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
            description: 'Expense paid successfully'
        },
        404: {
            description: 'Failed to pay expense'
        }
    }
}, handler: (req, res) => {
    // not implemented yet
    res.status(501).send({error: 'Not implemented'});
}});

module.exports = endPoints;