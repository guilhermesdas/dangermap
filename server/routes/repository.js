var express = require('express');
var router = express.Router();
var bodyParser = require('body-parser');
var urlencodedParser = bodyParser.urlencoded({extended: false});
var Repository = require('../schemas/repository_schema.js');
var Keywords = require('../schemas/keywords_schema')
var Neighborhood = require('../schemas/neighborhood_schema')
var Links = require('../schemas/links_schema')

// /repository/ will return all repository
router.get("/", (req, res) => {
	console.log("listing all repository.");	
	Repository.find()
	.populate("neighborhood")
	.populate("link")
	.populate("keywords")
	.then(repo => res.json(repo));
});

// /repository/delete will delete a neighborhood with given id
router.post("/remove",urlencodedParser, (req,res) => {

	var json = req.body;
	console.log(json["_id"])
	var v = Repository.findOneAndDelete(json).exec()
	return res.send({"status": v.error})

});

// /repository/delete will delete a neighborhood with given id
router.post("/updatebrief",urlencodedParser, async (req,res) => {

	console.log(req.body)
	var json = req.body;
	//console.log(json)
	var rep = await Repository.findById(json._id).exec()
						.catch( err => res.json({status: false, statusMsg: err}) );
	rep.brief = json.brief
	rep.save()
	console.log("New brief: " + rep.brief)
	res.status(200).send(rep)

});

// search news related with given neighborhood 
router.get("/newsbairro", (req, res) => {
	console.log("get repository by bairro.");	
	console.log(req.param("neighborhood"))

	Repository.find({ neighborhood : req.param("neighborhood") })
	.populate("neighborhood")
	.populate("link")
	.populate("keywords")
	.then(repo => res.json(repo));
});

// /repository/add will add a repository
router.post("/add", urlencodedParser, (req, res) => {

	//var json = Object.assign({}, REPOSITORY_JSONIN);
	var json = req.body;
	console.log(JSON.stringify(json));
	var v = Repository.create(json)
	if (v == null || v == undefined)
		res.send({ status: false });
	else{
		console.log("new repository added:\n", json);
		res.send({status: true})
	}

});

module.exports = router;