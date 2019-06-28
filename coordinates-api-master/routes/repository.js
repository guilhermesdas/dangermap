var express = require('express');
var router = express.Router();
var bodyParser = require('body-parser');
var urlencodedParser = bodyParser.urlencoded({extended: false});
var Repository = require('../schemas/repository_schema.js');

var REPOSITORY_JSONIN = 
{
	links_id: 1,
	neighborhood_id: 2,
    foundedKeywords_id: [1]
}

router.get("/", (req, res) => {
	console.log("listing all repository.");	
	Repository.find({}, (err,repository) => {
		if (err) {
			res.setHeader('Access-Control-Allow-Origin','*');
			res.send(err);
		} else {
			//res.json(pontos);
			//console.log(repository);
			res.setHeader('Access-Control-Allow-Origin','*');
			res.send(repository);
		}		
	})
});

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