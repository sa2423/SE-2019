module.exports = function(passport){
	var express = require('express');
	var router = express.Router();
	var db = require('../database.js');
	var firebase = require("firebase-admin");
	var MongoClient = require('mongodb').MongoClient,
        ObjectID = require('mongodb').ObjectID,
        ISODate = require('mongodb').ISODate;

	var serviceAccount = require("/home/ubuntu/RestAuto/key.json");
	firebase.initializeApp({
		credential: firebase.credential.cert(serviceAccount),
		databaseURL: "https://adam-d2382.firebaseio.com"
	});
		
// Taras, Seerat, Chris Lombardi, Lieyang, and Alex wrote the code, tested it and debugged it
		
	router.get('/secret', function(req, res){
		if(req.user){
			res.json(req.user);
		}
		else{
			res.redirect('/login');
		}
	});
	
	
	//Chris April 22nd
	router.post('/addItem', function(req, res){
		if(req.user){
			db.addMenuItem(req.body);
		}
		else{
			res.redirect('/login');
		}
	});
	
	
	
	//Chris --Need help, will discuss with Taras tomorrow.
	router.post('/editItem', function(req, res){

		if(req.user){
			var id = req.body.id;
			var name = req.body.name;
			var price = req.body.price;
			var description = req.body.description;

	
			try {
				db.updateItem(id, {'$set': {title: name, price: parseFloat(price), description: description}})
			
				.then(function(result){
					console.log('Item updated');
					res.redirect('/console-menu');
			
				})
				.catch(function(error){
					console.log('error updating Item: ' + error);
					res.json(error);
				});
			} catch (e) {
				return console.error(e);
			}
		}
		else{
			res.json({success: false, error: 0});
		}
        

    });
	
	router.post('/removeItem', function(req, res){
		if(req.user){
			db.removeMenuItem(req.body);
		}
		else{
			res.redirect('/login');
		}
	});

	//Lieyang April 18
	router.post('/editUser', function(req, res){

		if(req.user){
			var user = req.body.user;
			var set = req.body.set;
	
			console.log(user);
	
			try {
				user = {_id: req.user._id};
				set = JSON.parse(set);
				db.updateUser(user, set)
			
				.then(function(result){
					console.log('user updated');
					res.json({success: true, user: set['$set']});
			
				})
				.catch(function(error){
					console.log('error updating user: ' + error);
					res.json(error);
				});
			} catch (e) {
				return console.error(e);
			}
		}
		else{
			res.json({success: false, error: 0});
		}
        

    });

	router.post('/updateUser', function(req, res){
		if(req.user && req.user.role == 0){
			var userId = req.body.id;
			var username = req.body.name;
			var firstName = req.body.first_name;
			var lastName = req.body.last_name;
			var role = req.body.role;

			try {
				user = {_id: userId};
				set = {'$set': {name: username, first_name: firstName, last_name: lastName, role: role}};
				db.updateUser(user, set)
			
				.then(function(result){
					console.log('user updated');
					res.redirect('/console-users');
					//res.json({success: true, user: set['$set']});
			
				})
				.catch(function(error){
					console.log('error updating user: ' + error);
					res.redirect('/console-users');
					//res.json(error);
				});
			} catch (e) {
				return console.error(e);
			}			

		}else{
			res.redirect('/login');
		}
	})


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
				res.json(err);
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
					res.json(err);
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

	//ADD ITEMS TO CART COLLECTION 
	router.get('/add-to-cart/:id', function(req, res){
		if(req.user){
			var customerID =  req.user._id; 
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
		}
		else{
			res.redirect('/login');
		}
		
	});


	//REMOVE ITEMS FROM CART COLLECTION 
	router.get('/remove-from-cart/:id', function(req, res){
		var customerID;
		if(req.user){
			customerID = req.user._id;
		}
	
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

	//ANALYZE RECOMMENDATION QUIZ ANSWERS - SEERAT 4/20/19
	router.post('/submit-rec-quiz/:id', function(req, res, next) {
		// store answers
		let answers = req.body;
		let taste = answers['taste'];
		let meat = answers['meat'];
		let health = answers['health'];
		let carbContent = answers['carbs'];
		let drink = answers['drinks'];
		var customerID = req.params.id;
		// initialize food cluster probabilities 
		// for new customers, all initialized to have the same probability until updated
		let fcp = {
			"customerID" : customerID, 
			"savory" : 1,
			"spicy" : 1,
			"sweet" : 1,
			"lotsCarbs" : 1,
			"someCarbs" : 1,
			"noCarbs" : 1,
			"vegetarian" : 1,
			"redMeat" : 1,
			"whiteMeat" : 1,
			"seafood" : 1,
			"healthy" : 1,
			"fountain" : 1,
			"caffeine" : 1
		};
		// determine how many points to add per category
		// taste category
		if (taste == 'savory') {
			let temp = fcp['savory'];
			temp += 1;
			fcp['savory'] = temp; 
		}
		else if (taste == 'sweet') {
			let temp = fcp['sweet'];
			temp += 1;
			fcp['sweet'] = temp;
		}
		else {
			let temp = fcp['spicy'];
			temp += 1;
			fcp['spicy'] = temp; 
		}
		// meat category
		if (meat == 'vegetarian') {
			let temp = fcp['vegetarian'];
			temp += 1;
			fcp['vegetarian'] = temp;
		}
		else if (meat == 'redMeat') {
			let temp = fcp['redMeat'];
			temp += 1;
			fcp['redMeat'] = temp;
		}
		else if (meat == 'whiteMeat') {
			let temp = fcp['whiteMeat'];
			temp += 1;
			fcp['whiteMeat'] = temp;
		}
		else {
			let temp = fcp['seafood'];
			temp += 1;
			fcp['seafood'] = temp;
		}
		// health category
		if (health == 'healthy') {
			let temp = fcp['healthy'];
			temp += 1;
			fcp['healthy'] = temp;
		}
		else {
			fcp['healthy'] = 0; 
		}
		// carbs category
		if (carbContent == 'noCarbs') {
			let temp = fcp['noCarbs'];
			temp += 1;
			fcp['noCarbs'] = temp; 
		}
		else if (carbContent == 'someCarbs') {
			let temp = fcp['someCarbs'];
			temp += 1;
			fcp['someCarbs'] = temp; 
		}
		else {
			let temp = fcp['lotsCarbs'];
			temp += 1;
			fcp['lotsCarbs'] = temp; 
		}
		// drinks category
		if (drink == 'water' || drink == 'fountain') {
			let temp = fcp['fountain'];
			temp += 1;
			fcp['fountain'] = temp;
		}
		else {
			let temp = fcp['caffeine'];
			temp += 1;
			fcp['caffeine'] = temp 
		}

		// store food cluster probabilities in database and redirect to recommendation page 
		db.insertFoodClusterProbs(fcp)
		.then(function(result){res.redirect('/recommend-me')})
		.catch(function(err){res.redirect('/recommend-me')});
	});

	// helper function for keeping tracking of how many times items appear in arrays 
	function countInArray(array, what) {
	    var count = 0;
	    for (var i = 0; i < array.length; i++) {
	        if (array[i].title == what) {
	            count++;
	        }
	    }
	    return count;
	}
	// helper function for keeping track of food cluster names
	function countInArrayClusters(array, what) {
	    var count = 0;
	    for (var i = 0; i < array.length; i++) {
	        if (array[i] == what) {
	            count++;
	        }
	    }
	    return count;
	}
	// helper function for returning indices of largest values in an array 
	function maxValues(arr) {
    	var max = -Infinity;
    	var maxIndices = [];
    	for (var i = 0; i < arr.length; i++) {
        	if (arr[i] === max) {
          		maxIndices.push(i);
        	} else if (arr[i] > max) {
            	maxIndices = [i];
            	max = arr[i];
        	}
    	}
    	return maxIndices;
 	}
	// queries customers previous orders to update food_cluster_probabilities
	// redirects to original recommend-me page 
	// Seerat 
	router.get('/recommend-me/:id', function(req, res, next) {
		let customerID = req.params.id;
		// get a list of customer orders
		db.getUserOrders(ObjectID(customerID.toString()), 
			function(orders){
				let ordered_dishes = [];
				// determine most commonly ordered items 
				for (var i = 0; i < orders.length; i++) {
					let temp = orders[i].orderItems;
					for (var j = 0; j < temp.length; j++) {
						ordered_dishes.push(temp[j]);
					}
				}

				// store how many items a dish appears in the list in another array
				let dish_frequency = [];
				var dish_names = [];
				for (var i = 0; i < ordered_dishes.length; i++) {
					// checks to see if dish already in array 
					if (!dish_names.includes(ordered_dishes[i].title)) {
						dish_names.push(ordered_dishes[i].title);
						dish_frequency.push(countInArray(ordered_dishes,ordered_dishes[i].title));
					}
					else {
						continue;
					}
				}
				var mostEatenIndices = maxValues(dish_frequency);
				var most_eaten_dishes = [];
				for (var i = 0; i < mostEatenIndices.length; i++) {
					most_eaten_dishes.push(dish_names[mostEatenIndices[i]]); 
				}
				// find food clusters for most frequently ordered dishes 
				db.getDishes(function(allDishes) {
					// determine appropriate updates per food cluster 
					let foodClusters = [];
					for (var i = 0; i < allDishes.length; i++) {
						let temp_ind = most_eaten_dishes.indexOf(allDishes[i].title);
						if (temp_ind != -1) {
							foodClusters.push(allDishes[i].flavor);
							foodClusters.push(allDishes[i].carbContent);
							foodClusters.push(allDishes[i].meat);
							foodClusters.push(allDishes[i].healthy);
						}
					}
					// determine most common food clusters 
					var common_fc = [];
					var fc_freq = [];
					for (var i = 0; i < foodClusters.length; i++) {
						if (foodClusters[i].length > 0) {
							if (!common_fc.includes(foodClusters[i])) {
								common_fc.push(foodClusters[i]);
								fc_freq.push(countInArrayClusters(foodClusters,foodClusters[i]));
							}
							else {
								continue;
							}
						}
					}
					// get customer's food cluster probabilities 
					db.getFoodClusterProbsByID(customerID.toString()).then(function(fcp) {
					// update and store customer's food cluster probabilities 
						fcp = fcp[0]; 
						let max_fc_indices = maxValues(fc_freq);
						for (var i = 0; i < max_fc_indices.length; i++) {
							let temp = common_fc[max_fc_indices[i]];
							switch(temp) {
				    			case 'Savory':
				    			fcp['savory'] += 1; 
				    			break;
				    			case 'Spicy':
				    			fcp['spicy'] += 1; 
				    			break;
				    			case 'Sweet':
				    			fcp['sweet'] += 1; 
				    			break;
				    			case 'Lots':
				    			fcp['lotsCarbs'] += 1; 
				    			break;
				    			case 'Some':
				    			fcp['someCarbs'] += 1; 
				    			break;
				    			case 'None':
				    			fcp['noCarbs'] += 1; 
				    			break;
				    			case 'Vegetarian':
				    			fcp['vegetarian'] += 1; 
				    			break;
				    			case 'redMeat':
				    			fcp['redMeat'] += 1; 
				    			break;
				    			case 'whiteMeat':
				    			fcp['whiteMeat'] += 1; 
				    			break;
				    			case 'Seafood':
				    			fcp['seafood'] += 1; 
				    			break;
				    			case 'Yes':
				    			fcp['healthy'] += 1; 
				    			break;
				    			case 'No':
				    			fcp['healthy'] = 0; 
    						}
						} 
						// store updated food cluster probabilities and redirect to recommendation page 
						db.updateFoodClusterProbs(fcp,customerID.toString()).then(function(result) {
							console.log(result);
							res.redirect('/recommend-me'); 
						})
						.catch(function(err) {
							console.log(err);
							res.redirect('/recommend-me');
						});
					});
				});
			}
		);
	});

	router.post('/login', function(req, res, next) {
		console.log('username: ' + req.body.username);
		passport.authenticate('local', function(err, user, info) {
			if (err) { 
				return res.json({success: false, error: err});
			}
		  	if (!user) {
				return res.json({success: false, error: 'Username or password is incorrect'});
			}
			req.logIn(user, function(err) {
				if (err) {
					if(req.body.json){
						return res.json({success: false, error: err});
					}
					else{
						return res.redirect('/login');
					}
				}
				if(req.body.json){
					return res.json({success: true, user: user});
				}
				else{
					return res.redirect('/');
				}
			});
		})(req, res, next);
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


	//Removes ALL orders under customerID Trying to test.
	router.get("/ChrisTest", function(req, res, next){
		db.getUsers().then(function (users) {
		res.json(users);
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

	const csv = require('csv-parser');  
	const fs = require('fs');

	router.get("/populateData", function(req, res, next){
		MongoClient.connect("mongodb://localhost:27017/adam")
		.then(function(db){
			console.log("Clearing Data...")
			var collection = db.collection("orders");

			collection.remove({})
		})
		.then(function(){
			console.log("Populating Data...")
			var entries = []
			fs.createReadStream('data.csv')  
			.pipe(csv())
			.on('data', (row) => {

				for(var i = 0; i < row.ingredients; i++) {
					var date = new Date(row.date_placed)
					var timestamp = date.toDateString() + " " + date.toTimeString();

					var dish = {
						dish: "5c96835a684e0dc8f54aab59",
						title: "Arrabbiata Pizza",
						details: "",
						price: 35.95
					}

					var order = {
						userId: null,
						orderItems: [dish],
						date_placed: timestamp,
						paid: true,
						completed: true,
						served: true
					};
					
					entries.push(order)
				}
			})
			.on('end', () => {
				MongoClient.connect("mongodb://localhost:27017/adam")
				.then(function(db){
						var collection = db.collection("orders");

						collection.insertMany(entries, function(err, result){
								if(err)
									res.json({success: false});
								else {
									console.log('CSV file successfully processed');
									res.json({success: true});
								}
						});
				})
				.catch(function(error){
					res.json({success: false});
				});
			});
		}).catch(function(err){
			console.log(err)
			res.json({success: false});
		});
	});

	return router;
}


