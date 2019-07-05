const PORT = 4001;

const express = require('express');
const app = express();

//const next = require('next');

const bodyParser = require('body-parser');
const morgan = require('morgan');
const path = require('path');

// Adding HTTPS
const fs = require('fs');
const https = require('https');

var cors = require('cors');

// Mongo
var mongoose = require('mongoose');
var session = require('express-session');
var MongoStore = require('connect-mongo')(session);

mongoose.connect('mongodb://localhost/newssites');
var db = mongoose.connection;

// Adding next support
//const dev = process.env.NODE_ENV !== 'production';
//const FINAL = next({dev})


// START FINAL
//FINAL.prepare().then( () => {

//const app = express();

db.on('error', console.error.bind(console, 'connection error'));
db.once('open', () => {
    console.log("[MONGODB] Connected");
});


app.use(bodyParser.json({ limit: '50mb' }));
app.use(bodyParser.urlencoded({ limit: '50mb', extended: true }));
//app.use(express.json())
//app.use(bodyParser.urlencoded({extended: true}));

//  Include routes
var repository = require('./routes/repository.js');
//var entries = require('./routes/entry.js');
//var logins = require('./routes/login.js');
//var ios = require('./routes/io.js');

app.use('/repository', repository);
//app.use('/entry', entries);
//app.use('/login', logins);
//app.use('/io', ios);
//var express = require('express');
var router = express.Router();

/*app.use(function(err, req, res, next) {
    if (err instanceof ValidationError) {
        // At this point you can execute your error handling code
        //res.status(400).send({code: 400, message: err});
        res.send({status: false, data: {}});
        next();
    }
    else next(err); // pass error on if not a validation error
});*/


//Starts the server
app.listen(PORT);
console.log('Server running at port ' + PORT);


// HTTPS
https.createServer(app).listen(4000);

// ENDING FINAL
//});
