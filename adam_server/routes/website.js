var express = require('express');
var router = express.Router();
var db = require('../database.js');

/* GET home page. */
router.get('/', function(req, res, next) {
	res.render('index');
});

router.get('/menu', function(req, res, next){
	res.render('menu'); 
});

router.get('/recommend-me', function(req, res, next) {
	res.render('recommend');
});

router.get('/cart', function(req, res, next) {
	res.render('cart'); 
});

router.get('/reserve', function(req, res, next) {
	res.render('reserve');
});

module.exports = router;
