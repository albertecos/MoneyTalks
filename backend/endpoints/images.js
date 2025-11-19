const { Database } = require("../Database");
const { v4: uuidv4 } = require('uuid');
const { Components } = require('../schema/components');
const multer = require('multer');
const path = require('path');
const fs = require('fs');

// Configure multer for file uploads
const storage = multer.diskStorage({
    destination: (req, file, cb) => {
        const uploadDir = path.join(__dirname, '../data/uploads');
        // Ensure directory exists
        if (!fs.existsSync(uploadDir)) {
            fs.mkdirSync(uploadDir, { recursive: true });
        }
        cb(null, uploadDir);
    },
    filename: (req, file, cb) => {
        // Generate unique filename with original extension
        const ext = path.extname(file.originalname);
        const filename = `${uuidv4()}${ext}`;
        cb(null, filename);
    }
});

const upload = multer({
    storage: storage,
    limits: {
        fileSize: 10 * 1024 * 1024 // 10MB limit
    },
    fileFilter: (req, file, cb) => {
        // Accept only image files
        const allowedMimes = ['image/jpeg', 'image/jpg', 'image/png', 'image/gif', 'image/webp'];
        if (allowedMimes.includes(file.mimetype)) {
            cb(null, true);
        } else {
            cb(new Error('Invalid file type. Only image files are allowed.'));
        }
    }
});

var endPoints = [];

endPoints.push({method: 'POST', path: '/upload', oapi: {
    summary: 'Upload an image',
    requestBody: {
        required: true,
        content: {
            'multipart/form-data': {
                schema: {
                    type: 'object',
                    properties: {
                        image: {
                            type: 'string',
                            format: 'binary',
                            description: 'Image file to upload'
                        }
                    }
                }
            }
        }
    },
    responses: {
        200: {
            description: 'Image uploaded successfully',
            content: {
                'application/json': {
                    schema: {
                        type: 'object',
                        properties: {
                            filename: {
                                type: 'string',
                                description: 'Name of the uploaded file'
                            },
                            path: {
                                type: 'string',
                                description: 'URL path to access the image'
                            }
                        }
                    }
                }
            }
        },
        400: {
            description: 'Bad request - no file uploaded or invalid file type'
        },
        500: {
            description: 'Server error'
        }
    }
}, handler: (req, res) => {
    // Use multer middleware
    upload.single('image')(req, res, (err) => {
        if (err instanceof multer.MulterError) {
            // A Multer error occurred when uploading
            return res.status(400).send({ error: `Upload error: ${err.message}` });
        } else if (err) {
            // An unknown error occurred
            return res.status(400).send({ error: err.message });
        }

        // Check if file was uploaded
        if (!req.file) {
            return res.status(400).send({ error: 'No file uploaded' });
        }

        // Return file information
        res.json({
            filename: req.file.filename,
            path: `/image/${req.file.filename}`
        });
    });
}});


endPoints.push({method: 'GET', path: '/image', oapi: {
    summary: 'Get an image by path',
    parameters: [
        {
            name: 'path',
            in: 'query',
            required: true,
            schema: {
                type: 'string'
            },
            description: 'Filename of the image to retrieve'
        }
    ],
    responses: {
        200: {
            description: 'Image file',
            content: {
                'image/jpeg': {},
                'image/png': {},
                'image/gif': {},
                'image/webp': {}
            }
        },
        404: {
            description: 'Image not found'
        }
    }
}, handler: (req, res) => {
    const filename = req.query.path;
    const filepath = path.join(__dirname, '../data/uploads', filename);

    // Check if file exists
    if (!fs.existsSync(filepath)) {
        return res.status(404).send({ error: 'Image not found' });
    }

    // Send the file
    res.sendFile(filepath);
}});

module.exports = endPoints;