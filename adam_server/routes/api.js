var express = require('express');
var router = express.Router();
var db = require('../database.js');
var firebase = require("firebase-admin");

const auth = require('./auth');

var serviceAccount = require("/home/ubuntu/RestAuto/key.json");
firebase.initializeApp({
	credential: firebase.credential.cert(serviceAccount),
	databaseURL: "https://adam-d2382.firebaseio.com"
  });
	
	
router.post('/assistance', function(req, res){
	db.getUser(req.body.user_id)
	.then(function(user){
		db.getWaiters().then(function(waiters){

			for(var i = 0; i < waiters.length; i++){
				var payload = {
					data: {
						type: "ASSISTANCE",
						username: user.name
					}
				};

				var options = {
					priority: "high",
					timeToLive: 60 * 60 *24
				};
				
				for(var j = 0; i < waiters[i].firebaseTokens.length; j++){
					firebase.messaging().sendToDevice(waiters[i].firebaseTokens[j], payload, options)
					.then(function(response) {
						console.log("Successfully sent message:", response);
					})
					.catch(function(error) {
						console.log("Error sending message:", error);
					});
				}

				res.json({success: true});
			}

		})
		.catch(function(err){
			console.log("error getting waiters: " + err);
		})
	})
	.catch(function(err){
		res.json(err);
	})
});

router.post('/placeOrder', function(req, res){
	var order = req.body.order;

	console.log(order);

	try {
		order = JSON.parse(order);

		db.insertOrder(order)
		.then(function(result){
			console.log('order inserte');

			db.getChefs().then(function(chefs){
				for(var i = 0; i < chefs.length; i++){
					var payload = {
						notification: {
						  title: "This is a test title",
						  body: "Hello world"
						},
						data: {
							_id: order._id+""
						}
					  };
					  var options = {
						priority: "high",
						timeToLive: 60 * 60 *24
					  };
		
					  for(var j = 0; j < chefs[i].firebaseTokens.length; j++){
						firebase.messaging().sendToDevice(chefs[i].firebaseTokens[j], payload, options)
						.then(function(response) {
						  console.log("Successfully sent message:", response);
						})
						.catch(function(error) {
						  console.log("Error sending message:", error);
						});
					  }	
				}

				//notify the waiters as well

				db.getWaiters()
				.then(function(waiters){
					for(var i = 0; i < waiters.length; i++){
						var payload = {
							notification: {
							  title: "This is a test title",
							  body: "Hello world"
							},
							data: {
								_id: order._id+"",
								type: "ORDER"
							}
						  };
						  var options = {
							priority: "high",
							timeToLive: 60 * 60 *24
						  };
			
						  for(var j = 0; j < waiters[i].firebaseTokens.length; j++){
							firebase.messaging().sendToDevice(waiters[i].firebaseTokens[j], payload, options)
							.then(function(response) {
							  console.log("Successfully sent message:", response);
							})
							.catch(function(error) {
							  console.log("Error sending message:", error);
							});
						  }	
					}
					res.json({success: true});
				})
				.catch(function(err){
					res.json({success: false});
				});
			}).catch(function(err){
				
			})			
		})
		.catch(function(error){
			console.log('error insering order: ' + error);
			res.json(error);
		});
	} catch (e) {
		return console.error(e);
	}

	
});

//ADD ITEMS TO CART COLLECTION - CURRENTLY DOES NOT CHECK FOR DUPLICATES
router.get('/add-to-cart/:id', function(req, res){
	var customerID = "5c967e32d2e79f4afc43fdef"; //hardcoded customer id 
	var menuItemID = req.params.id;
	db.getDish(menuItemID, function(dish){
		db.addToCart(customerID,dish).then(function(result){
			console.log(result);
			res.redirect('/cart');
		})
		.catch(function(err){
			console.log(err);
			res.redirect('/cart');
		});
	});
});


//REMOVE ITEMS FROM CART COLLECTION - CURRENTLY DOES NOT CHECK FOR EMPTY CART 
router.get('/remove-from-cart/:id', function(req, res){
	var customerID = "5c967e32d2e79f4afc43fdef"; //hardcoded customer id 
	var menuItemID = req.params.id;
	db.getDish(menuItemID, function(dish){
		db.removeFromCart(customerID,dish).then(function(result){
			console.log(result);
			res.redirect('/cart');
		})
		.catch(function(err){
			console.log(err);
			res.redirect('/cart');
		});
	});
});

router.get('/logout', function(req, res){
	req.logout();
	res.json({status: 'success'});
});

//Get a list of all users
router.get("/users", function(req, res, next){
	db.getUsers().then(function(users){res.json(users)});
});

//Get a particular user information
router.get("/users/:id", function(req, res, next){
	db.getUser(req.params.id).then(function(user){res.json(user)});
});

//Get a list of orders for a particular user
router.get("/users/:id/orders", function(req, res, next){
	db.getUserOrders(req.params.id, 
		function(orders){
			res.json(orders);
		}
	);
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

router.get("/orders/:id", function(req, res, next){

	db.getOrder(req.params.id).then(function(order){
		res.json(order);
	})
	.catch(function(err){
		res.json(err);
	})
});


router.get("/orders/:id/complete", function(req, res, next){

	db.setOrderCompleted(req.params.id).then(function(){

		db.getOrder(req.params.id)
		.then(function(order){
			db.getWaiters().then(function(waiters){
				for(var i = 0; i < waiters.length; i++){
					var payload = {
						data: {
							_id: order._id+"",
							type: "ORDER"
						}
					  };
					  var options = {
						priority: "high",
						timeToLive: 60 * 60 *24
					  };

					  for(var j = 0; j < waiters[i].firebaseTokens.length; j++){
						firebase.messaging().sendToDevice(waiters[i].firebaseTokens[j], payload, options)
						.then(function(response) {
						  console.log("Successfully sent message:", response);
						})
						.catch(function(error) {
						  console.log("Error sending message:", error);
						});
					  }
				}
				res.json({success: true});
			}).catch(function(err){
				res.json({success: false});
			})
	
		})
		.catch(function(err){
			res.json({success: false});
		});
	}).catch(function(){
		res.json({success: false});
	})

});

router.get("/orders/:id/serve", function(req, res, next){

	db.setOrderServed(req.params.id).then(function(){
		res.json({success: true});
	}).catch(function(){
		res.json({success: false});
	})

});

//Get a list of all assistanceRequests
router.get("/assistanceRequests", function(req, res, next){
	db.getAssistanceRequests().then(function(assistanceRequests){res.json(assistanceRequests)});
});

router.get("/assistanceRequests/:id/remove", function(req, res, next){

	db.setAssistanceRequestRemoved(req.params.id).then(function(){
		res.json({success: true});
	}).catch(function(){
		res.json({success: false});
	})

});

router.get("/users/:user_id/firebase/:token", function(req, res, next){
	console.log("Updating firebase!")
	console.log("UserId: " + req.params.user_id)
	console.log("Token: !" + req.params.token)
	db.updateFirebaseToken(req.params.user_id, req.params.token).then(function(){
		console.log("Updated firebase successfully!")
		res.json({success: true});
	}).catch(function(){
		res.json({success: false});
		console.log("Firebase update failed!")
	})
});

module.exports = router;



