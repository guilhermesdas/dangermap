var cors = require("cors");
var express = require("express");
var md5 = require('md5');
var app = express();
var port = 3000;
var bodyParser = require('body-parser');
var urlencodedParser = bodyParser.urlencoded({extended: false});
var database = "mongodb://localhost:27017/test";
var mongoose = require("mongoose");
var repository = require('./routes/repository.js.js');
var users = require('./routes/users.js.js');

// Mongodb connection
mongoose.Promise = global.Promise;
mongoose.connect(database);

// App Start
app.use(cors());
app.use(bodyParser.json());
app.listen(port, () => {
	console.log("Eye of Thundera is listening on port " + port);
});

// Import routes
app.use('/repository', repository);
app.use('/users', users)

app.get("/", (req, res) => {
	res.send("Welcome to Eye of Thundera!.");
});