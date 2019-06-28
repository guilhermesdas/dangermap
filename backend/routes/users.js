var express = require('express');
var router = express.Router();
var bodyParser = require('body-parser');
var urlencodedParser = bodyParser.urlencoded({extended: false});
var User = require('../schemas/users_schema.js');

// Json example of user data comming via post
var USER_JSONIN_SIGNUP = 
{
    name: "name",
    email: "email",
    username: "username",
    password: "password"
}
var USER_JSONIN_SIGNIN = 
{
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
	var json = Object.assign({}, USER_JSONIN_SIGNUP);
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
    var json = Object.assign({}, USER_JSONIN_SIGNIN);
    json = req.body;
    
    // Authenticate user
	User.authenticate(json.username, json.password, (err, user) => {
        if (err) {
            res.send({status: false, error: err.message});
        } else {
            console.log("================================")
            console.log("USER: "+json.username)
            res.send({status: true, data: user})
        }
    });

});

// /users/delte will delete user
router.post("/delete", urlencodedParser, (req, res) => {

    // Take received data in json
    var json = Object.assign({}, USER_JSONIN_SIGNIN);
    json = req.body;
    
    delete_json = {
        "username": json.username,
        "password": json.password
    }

   // Delete user
   User.delete(delete_json, (err) => {
    if (err != null ) {
        console.log(err.message);
        res.send({status: false, error: err.message});
    } else {
        console.log("================================")
        console.log("USER DELETED: "+json.username)
        res.send({status: true})
    }
});
    
});

module.exports = router;