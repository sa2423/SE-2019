var express = require('express');
var router = express.Router();
var db = require('../database.js');

/* GET home page. */
router.get('/', function(req, res, next) {
	res.render('index');
});

/* MENU AND SUBPAGES*/
router.get('/menu', function(req, res, next){
	res.render('menu'); 
});

router.get('/popular-dishes', function(req, res, next){
	res.render('popular_dishes');
});

router.get('/appetizers', function(req, res, next){
	res.render('appetizers');
});

router.get('/entrees', function(req, res, next){
	res.render('entrees');
});

router.get('/desserts', function(req, res, next){
	res.render('desserts');
});

router.get('/drinks', function(req, res, next){
	res.render('drinks');
});

/* RECOMMEND ME FEATURE */
router.get('/recommend-me', function(req, res, next) {
	res.render('recommend');
});

/* CART */
router.get('/cart', function(req, res, next) {
	res.render('cart'); 
});

/* RESERVE TABLE */
router.get('/reserve', function(req, res, next) {
	res.render('reserve');
});

module.exports = router;
