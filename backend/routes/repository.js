var express = require('express');
var router = express.Router();
var bodyParser = require('body-parser');
var urlencodedParser = bodyParser.urlencoded({extended: false});
var Repository = require('../schemas/repository_schema.js.js');

// Json example of repository data comming via post
var REPOSITORY_JSONIN = 
{
	links_id: 1,
	neighborhood_id: 2,
    foundedKeywords_id: [1]
}

// /repository/ will return all repository
router.get("/", (req, res) => {
	console.log("listing all repository.");	
	Repository.find({}, (err,repository) => {
		if (err) {
			res.send(err);
		} else {
			res.send(repository);
		}		
	})
});

// /repository/add will add a repository
router.post("/add", urlencodedParser, (req, res) => {

	var json = Object.assign({}, REPOSITORY_JSONIN);
	json = req.body;
	var v = Repository.create(json)
	if (v == null || v == undefined)
		return res.send({ status: false });
	else{
		console.log("new repository added:\n", json);
		return res.send({status: true})
	}

});

module.exports = router;