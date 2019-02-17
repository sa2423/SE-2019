var express = require('express');
var router = express.Router();
var db = require('../database.js');
var request = require('request');


//Get a list of all users
router.get("/users", function(req, res, next){
	db.getUsers(function(users){
		res.json(users);
	});
});

//Get a particular user information
router.get("/users/:id", function(req, res, next){
	db.getUser(req.params.id, function(users){
		res.json(users);
	});
});

//Get a list of orders for a particular user
router.get("/users/:id/orders", function(req, res, next){
	db.getUserOrders(req.params.id, function(orders){
		res.json(orders);
	});
});

//Get the current menu
router.get("/menu", function(req, res, next){
	db.getDishes(function(dishes){
		res.json(dishes);
	});
});

//Get information for one particular user
router.get("/menu/:id", function(req, res, next){
	db.getDish(req.params.id, function(dish){
		res.json(dish);
	});
});

//Get information for one particular user
router.get("/menu/:id/orders", function(req, res, next){
	db.getDishOrders(req.params.id, function(orders){
		res.json(orders);
	});
});

//Get a list of all orders
router.get("/orders", function(req, res, next){

	db.getOrders(function(orders){
		res.json(orders);
	});

});



module.exports = router;



