# MoneyTalks Backend
This is the backend server for the MoneyTalks application.

## Prerequisites
- node
- npm

## Install
make sure you are in the `backend` directory, then run:
```bash
npm install
```

## Run
make sure you are in the `backend` directory, then run:
```bash
node main.js
```

The backend server will start on `http://localhost:3000`.

## API Documentation
The API documentation is available at `http://localhost:3000/swaggerui` once the server is running.

## Project Structure
- `main.js`: Entry point of the application.
- `endpoints/`: Contains all the API endpoint definitions.
- `schema/`: Contains OpenAPI schema components.
- `Database.js`: Simple file-based database implementation.