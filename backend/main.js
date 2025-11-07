const {v4: uuidv4} = require('uuid');
const express = require('express');
const cors = require('cors');
const app = express();
const fs = require('fs');
app.use(cors()); // Enable CORS for all routes
app.use(express.json()); // This middleware is used to parse JSON bodies.


app.use((req, res, next) => {
  console.log("Incoming:", req.method, req.url);
  next();
});

class DataBase {
    data = null;

    constructor(name) {
        this.filePath = `data/${name}.json`;
        this.data = this.readFromFile();
    }

    select(obj) {
        const keys = Object.keys(obj);
        return this.data.filter(item => 
            keys.every(key => item[key] === obj[key])
        );
    }

    selectLike(obj) {
        const keys = Object.keys(obj);
        return this.data.filter(item => 
            keys.every(key => item[key] && item[key].includes(obj[key]))
        );
    }

    insert(newObj) {
        this.data.push(newObj);
        this.writeToFile();
    }

    update(id, updatedObj) {
        const index = this.data.findIndex(item => item.id === id);
        if (index !== -1) {
            this.data[index] = { ...this.data[index], ...updatedObj };
            this.writeToFile();
        }
    }

    delete(id) {
        this.data = this.data.filter(item => item.id !== id);
        this.writeToFile();
    }

    readFromFile() {
        if (!fs.existsSync(this.filePath)) {
            return [];
        }
        const data = fs.readFileSync(this.filePath, 'utf8');
        return JSON.parse(data);
    }

    writeToFile() {
        fs.writeFileSync(this.filePath, JSON.stringify(this.data, null, 2));
    }
}

let usersDb = new DataBase('users');
let groupsDb = new DataBase('groups');

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

app.get('/users/:username', (req, res) => {
    const users = usersDb.selectLike({username: req.params.username});
    if(users.length === 0) {
        return res.status(404).send({error: 'No users found'});
    }
    res.json(users);
});

app.post('/users/update', (req, res) => {
    const {id, username, full_name, email, password} = req.body;
    if(!id || !username || !full_name || !email || !password) {
        return res.status(400).send({error: 'Please enter a valid id, username, full name, email and password'});
    }

    const existingUsers = usersDb.select({id});
    if(existingUsers.length === 0) {
        return res.status(404).send({error: 'User not found'});
    }

    usersDb.update(id, {username, full_name, email, password});

    res.json({id, username, full_name, email, password});
});

app.post('/login', (req, res) => {
    const {email, password} = req.body;
    if(!email || !password) {
        return res.status(400).send({error: 'Please enter a valid email and password'});
    }

    const users = usersDb.select({email, password});
    if(users.length === 0) {
        return res.status(401).send({error: 'Invalid credentials'});
    }

    res.json(users[0]);
});

app.post('/signup', (req, res) => {
    const {username, full_name, email, password} = req.body;
    if(!username || !full_name || !email || !password) {
        return res.status(400).send({error: 'Please enter a valid username, full name, email and password'});
    }

    const existingUsers = usersDb.select({email});
    if(existingUsers.length > 0) {
        return res.status(409).send({error: 'User already exists'});
    }
    existingUsers = usersDb.select({username});
    if(existingUsers.length > 0) {
        return res.status(409).send({error: 'Username already taken'});
    }

    const newUser = {id: uuidv4(), username, full_name, email, password};
    usersDb.insert(newUser);

    res.status(201).json(newUser);
});

app.post('/groups', (req, res) => {
    const {name, description, members} = req.body;
    if(!name || !description || !Array.isArray(members)) {
        return res.status(400).send({error: 'Please enter a valid name, description and members array'});
    }

    members = sanitizeForIds(members, res);
    const newGroup = {id: uuidv4(), name, description, members};
    groupsDb.insert(newGroup);

    res.status(201).json(newGroup);
});

app.post('/groups/edit', (req, res) => {
    const {id, name, description, members} = req.body;
    if(!id || !name || !description || !Array.isArray(members)) {
        return res.status(400).send({error: 'Please enter a valid id, name, description and members array'});
    }

    const existingGroups = groupsDb.select({id});
    if(existingGroups.length === 0) {
        return res.status(404).send({error: 'Group not found'});
    }

    members = sanitizeForIds(members, res);
    groupsDb.update(id, {name, description, members});

    res.json({id, name, description, members});
});

app.listen(3000, "0.0.0.0", () => console.log('Server running on \'0.0.0.0:3000\''));
