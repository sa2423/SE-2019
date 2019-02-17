var express = require('express');
var router = express.Router();
var db = require('../database.js');

/* GET home page. */
router.get('/', function(req, res, next) {
    res.render('index', { title: 'Codename Adam', description: 'Codename Adam Sample Website', 
    	pictures: [
    	{title: "Save Time", description: "Codename Adam makes placing and fulfilling orders easier. Save more time for things that matter.", filename: "r1.jpg"},
    	{title: "Save Money", description: "Codename Adam allows business owners to hire less staff. Ease the workload of your employees.", filename: "r2.jpeg"},
    	{title: "Save the planet", description: "Codename Adam allows you to eliminate traditional menu and reduce paper waste", filename: "r3.jpeg"}
    	]
	});
});

module.exports = router;
