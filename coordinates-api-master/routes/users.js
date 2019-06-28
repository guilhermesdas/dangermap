var express = require('express');
var router = express.Router();
var bodyParser = require('body-parser');
var urlencodedParser = bodyParser.urlencoded({extended: false});
var Repository = require('../schemas/users_schema.js');

// Json example of user data comming via post
var USER_JSONIN = 
{
    name: "name",
    email: "email",
    username: "username",
    password: "password"
}

// /users/ will return all users
router.get("/", (req, res) => {
	console.log("listing all users.");	
	User.find({}, (err,users) => {
		if (err) {
			res.send(err);
		} else {
			res.send(users);
		}		
	})
});

// /users/signup will create a user
router.post("/signup", urlencodedParser, (req, res) => {

    // Take received data in json
	var json = Object.assign({}, USER_JSONIN);
    json = req.body;
    
    // Try to create a user
	var v = User.create(json)
	if (v == null || v == undefined)
		return res.send({ status: false });
	else{
		console.log("new user created:\n", json);
		return res.send({status: true})
	}

});

// /users/signin will authenticate user login
router.post("/signin", urlencodedParser, (req, res) => {

    // Take received data in json
    var json = Object.assign({}, USER_JSONIN);
    json = req.body;
    
    // Authenticate user
	User.authenticate(json.username, json.password, (err, user) => {
        if (err) {
            res.send({ status: false, error: 'Falha no Servidor'});
        } else {
            console.log("================================")
            console.log("USER: "+req.body.username)
            res.send({status: true, data: user})
        }
    });

});

module.exports = router;