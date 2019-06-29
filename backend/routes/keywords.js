var express = require('express');
var router = express.Router();
var bodyParser = require('body-parser');
var urlencodedParser = bodyParser.urlencoded({extended: false});
var Keywords = require('../schemas/keywords_schema.js');

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

// /keywords/delete will delete a keyword with given id
router.post("/remove",urlencodedParser, (req,res) => {

	var json = req.body;
	console.log(json["_id"])
	var v = Keywords.findOneAndDelete(json).exec()
	return res.send({"status": v.error})

});

module.exports = router;