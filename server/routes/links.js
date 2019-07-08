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

router.get("/find" , urlencodedParser, async (req,res) => {

	// Param link
	var link = req.query.link;
	//console.log(link)

	const links = await Links.find({ "isBaseURL": false, "link" : { "$regex": link, "$options" : "i" } }).exec()
							.catch(err => res.json({status: false, statusMsg: err}) );

	res.status(200).send(links)

})

// /links/add will add a new link
router.post("/add", urlencodedParser, async (req, res) => {

	//console.log("Adding url: " + JSON.parse(req.body))

	// Verify if theres another link in database
	var json = {
		"link" : req.body.link,
		"isBaseURL" : req.body.isBaseURL
	}

	const flinks = await Links.find({ "isBaseURL": false, "link" : { "$regex": json.link, "$options" : "i" } }).exec()
							.catch(err => res.json({status: false, statusMsg: err}) );
	if ( flinks.length == 0 ){
		const link = await Links.create(json)
								.catch(err => res.json({status: false, statusMsg: err}) )
		//console.log("LInk added: " + link)
		console.log("New link added: " + link)
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

	console.log(req.url)
	
	var links = await Links.find({isBaseURL: false}).sort({"link": 1}).exec()

	for ( var i = 0; i < links.length; i++ ){
		//console.log(links[i])

			// Search for links that contain substring url
		var v = await Links.find({ "isBaseURL": false, "link" : { "$regex": links[i].link, "$options" : "i" } }).exec()

		if ( v.length < 2 )
			continue

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
		return;
	}

	res.status(200).send()

});

module.exports = router;