const fs = require('fs');

class Database {
    static databaseInstances = {};
    data = null;

    static getInstance(name) {
        if (!Database.databaseInstances[name]) {
            Database.databaseInstances[name] = new Database(name);
        }
        return Database.databaseInstances[name];
    }

    // private constructor (use getInstance instead)
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
exports.Database = Database;
