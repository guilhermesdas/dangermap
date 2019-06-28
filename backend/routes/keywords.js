var express = require('express');
var router = express.Router();
var bodyParser = require('body-parser');
var urlencodedParser = bodyParser.urlencoded({extended: false});
var Keywords = require('../schemas/keywords_schema.js');

// Json example of keywords data comming via post
var KEYWORDS_JSONIN = 
{
	keyword: "das",
	blacklist: false
}

// /keywords/ will return all keywords
router.get("/", (req, res) => {
	console.log("listing all keywords.");	
	Keywords.find({}, (err,keywords) => {
		if (err) {
			res.send(err);
		} else {
			res.send(keywords);
		}		
	})
});

// /keywords/add will add a new keyword
router.post("/add", urlencodedParser, (req, res) => {

	//var json = Object.assign({}, KEYWORDS_JSONIN);
	var json = req.body;
	var v = Keywords.create(json)
	if (v == null || v == undefined)
		return res.send({ status: false });
	else{
		console.log("new keyword added:\n", json);
		return res.send({status: true})
	}

});

module.exports = router;