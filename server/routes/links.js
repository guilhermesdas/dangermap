var express = require('express');
var router = express.Router();
var bodyParser = require('body-parser');
var urlencodedParser = bodyParser.urlencoded({extended: false});
var Links = require('../schemas/links_schema.js');
var Repository = require('../schemas/repository_schema.js');

// /links/ will return all links
router.get("/", (req, res) => {
	console.log("listing all links.");	
	Links.find({"isBaseURL" : false}, (err,links) => {
		if (err) {
			res.send(err);
		} else {
			res.send(links);
		}		
	})
});

// /links/ will return all links
router.get("/get", (req, res) => {
	console.log("getting one link.")

	console.log(req.body)
	var json = JSON.parse(req.body)


	console.log(json)
	res.send("oi")
	return

	Links.findOne({ "link": res.body["link"] }, (err,links) => {
		if (err) {
			res.send(err);
		} else {
			res.send(links);
		}		
	})
});

// /links/ will return all links
router.get("/seeds", (req, res) => {
	console.log("listing all seeds.");	
	Links.find({"isBaseURL": true}, (err,links) => {
		if (err) {
			res.send(err);
		} else {
			res.send(links);
		}		
	})
});

// /links/add will add a new link
router.post("/add", urlencodedParser, async (req, res) => {

	//console.log("Adding url: " + JSON.parse(req.body))

	// Verify if theres another link in database
	var json = {
		"link" : req.body.link,
		"isBaseURL" : req.body.isBaseURL
	}
	console.log(json)
	const flinks = await Links.find(json).exec()
							.catch(err => res.json({status: false, statusMsg: err}) );
	if ( flinks.length == 0 ){
		const link = await Links.create(json)
								.catch(err => res.json({status: false, statusMsg: err}) )
		console.log("LInk added: " + link)
		res.status(200).json(link)
	} else {
		console.log("LInk already in database")
		res.status(400).send({statusMsg: "Link already in database"})
	}	

});

// /links/delete will delete a link with given id
router.post("/remove",urlencodedParser, (req,res) => {

	var json = req.body;
	console.log(json["_id"])
	var v = Links.findOneAndDelete(json).exec()
	return res.send({"status": v.error})

});

// /links/delete will delete a link with given id
router.post("/removeduplicates",urlencodedParser, async (req,res) => {

	console.log("/removeduplicate")
	var url = req.param("url")

	// Search for links that contain substring url
	var v = await Links.find({ link : { "$regex": url, "$options" : "i" } }).exec()
	console.log(v.length)
	if ( v.length < 2 )
		return res.status(400).json({message: "No duplication"})

	// Remove duplicated news
	for ( var i = 1; i < v.length; i++ ){
		var r = await Repository.findOneAndRemove({ link: v[i]._id }).exec()
		console.log(r)
	}

	// remove duplicated links
	for ( var i = 1; i < v.length; i++ ){
		v[i].remove()
		//var l = await Links.findOneAndRemove({ link: v[i]._id }).exec()
		//console.log(r)
	}

	res.status(200).send(v[0])

});

module.exports = router;