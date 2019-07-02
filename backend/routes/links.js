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
	const flinks = await Links.find(req.body).exec()
							.catch(err => res.json({status: false, statusMsg: err}) );
	if ( flinks.length == 0 ){
		const link = await Links.create(req.body)
								.catch(err => res.json({status: false, statusMsg: err}) )
		console.log("LInk added: " + link)
		res.status(200).json(link)
	} else {
		console.log("LInk already in database")
		res.status(400).send({statusMsg: "Link already in database"})
	}
	// link.link = "oi"
	// link.save();
	//console.log(link._id);
	

});

// /links/delete will delete a link with given id
router.post("/remove",urlencodedParser, (req,res) => {

	var json = req.body;
	console.log(json["_id"])
	var v = Links.findOneAndDelete(json).exec()
	return res.send({"status": v.error})

});

module.exports = router;