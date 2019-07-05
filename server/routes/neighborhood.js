var express = require('express');
var router = express.Router();
var bodyParser = require('body-parser');
var urlencodedParser = bodyParser.urlencoded({extended: false});
var Neighborhood = require('../schemas/neighborhood_schema.js');

// /neighborhood/ will return all neighborhoods from manaus city
router.get("/", (req, res) => {
	console.log("listing all neighborhoods.");	
	Neighborhood.find({}, (err,keywords) => {
		if (err) {
			res.send(err);
		} else {
			res.send(keywords);
		}		
	})
});

// /neighborhood/add/ will a neighborhood from manaus city
router.post("/add", urlencodedParser, (req, res) => {

	//var json = Object.assign({}, KEYWORDS_JSONIN);
	var json = req.body;
    console.log(req.url);
	var v = Neighborhood.create(json)
	if (v == null || v == undefined)
		return res.send({ status: false });
	else{
		console.log("new neighborhood added:\n", json);
		return res.send({status: true})
	}

});

// /neighborgood/delete will delete a neighborhood with given id
router.post("/remove",urlencodedParser, (req,res) => {

	var json = req.body;
	console.log(json["_id"])
	var v = Neighborhood.findOneAndDelete(json).exec()
	return res.send({"status": v.error})

});

module.exports = router;