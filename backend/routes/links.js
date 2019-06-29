var express = require('express');
var router = express.Router();
var bodyParser = require('body-parser');
var urlencodedParser = bodyParser.urlencoded({extended: false});
var Links = require('../schemas/links_schema.js');

// /links/ will return all links
router.get("/", (req, res) => {
	console.log("listing all links.");	
	Links.find({}, (err,links) => {
		if (err) {
			res.send(err);
		} else {
			res.send(links);
		}		
	})
});

// /links/add will add a new link
router.post("/add", urlencodedParser, (req, res) => {

	var json = req.body;
	var v = Links.create(json)
	if (v == null || v == undefined)
		return res.send({ status: false });
	else{
		console.log("new link added:\n", json);
		return res.send({status: true})
	}

});

// /links/delete will delete a link with given id
router.post("/remove",urlencodedParser, (req,res) => {

	var json = req.body;
	console.log(json["_id"])
	var v = Links.findOneAndDelete(json).exec()
	return res.send({"status": v.error})

});

module.exports = router;