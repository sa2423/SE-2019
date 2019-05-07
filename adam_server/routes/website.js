var express = require('express');
var router = express.Router();
var db = require('../database.js');
var app = express();
var MongoClient = require('mongodb').MongoClient,
    ObjectID = require('mongodb').ObjectID,
    ISODate = require('mongodb').ISODate;
//var Cart = require('../models/cart.js');
//var FileReader = require('filereader')
//var filereader = new FileReader();

var firebase = require("firebase-admin");
	
/* GET home page. */
router.get('/', function (req, res, next) {
	res.render('index', {user: req.user});
});

/* MENU AND SUBPAGES*/
// Added Functionality to populate data by pulling from database, Chris Lombardi 4.9.2019
// Added cart and checkCart functionality. Seerat 4/13/19 --Note: Seerat also removed Pop_Dishes from here
// Added Forced Login to each page, this should resolve issues with null customer ID. Chris 4-16-2019
// Removed Forced Login for all menu pages, Forced Login is required AFTER a customer clicks add to cart. The customer will have to add to cart again after logging in though.
router.get('/menu', function (req, res, next) {
	var customerID;
	if(req.user){
		customerID = req.user._id;  
	}
	var appetizers = []; var entrees = []; var drinks = []; var desserts = []; 	//populate pop_dishes, appetizers, entrees, drinks, and desserts for menu to parse.
	db.getUserCart(customerID, function(userCart){
		var cartItems = (userCart.length == 0) ? [] : userCart[0].dishes;
		db.getDishes(function (allDishes) { //apps is a list of appetizers 
			var checkCartApps = [];
			var checkCartEntrees = [];
			var checkCartDrinks = [];
			var checkCartDesserts = [];

			for (var i = 0; i < allDishes.length; i++) { 
				if (allDishes[i].type == "appetizers") { //A very simple way to populate our apps/entrees..ect
					appetizers.push(allDishes[i]);
				}	

				if (allDishes[i].type == "entrees") {
					entrees.push(allDishes[i]);
				}	
	
				if (allDishes[i].type == "desserts") {
					desserts.push(allDishes[i]);
				}	

				if (allDishes[i].type == "drinks") {
					drinks.push(allDishes[i]);
				}					
			}

			for (var i = 0; i < appetizers.length; i++) {
				checkCartApps.push(false);
				for (var j = 0; j < cartItems.length; j++) {
					if (appetizers[i].title == cartItems[j].title) {
						checkCartApps[checkCartApps.length - 1] = true;
						break;
					}
				}
			}
			for (var i = 0; i < entrees.length; i++) {
				checkCartEntrees.push(false);
				for (var j = 0; j < cartItems.length; j++) {
					if (entrees[i].title == cartItems[j].title) {
						checkCartEntrees[checkCartEntrees.length - 1] = true;
						break;
					}
				}
			}
			for (var i = 0; i < desserts.length; i++) {
				checkCartDesserts.push(false);
				for (var j = 0; j < cartItems.length; j++) {
					if (desserts[i].title == cartItems[j].title) {
						checkCartDesserts[checkCartDesserts.length - 1] = true;
						break;
					}				
				}
			}
			for (var i = 0; i < drinks.length; i++) {
				checkCartDrinks.push(false);
				for (var j = 0; j < cartItems.length; j++) {
					if (drinks[i].title == cartItems[j].title) {
						checkCartDrinks[checkCartDrinks.length - 1] = true;
						break;
					}				
				}
			}	
		console.log(desserts);
		res.render('menu', {appetizers, entrees, drinks, desserts, checkCartApps, checkCartEntrees, checkCartDrinks, checkCartDesserts, user: req.user});  
		});

	});
});

router.get('/login', function(req, res, next) {
	if(req.user){
		res.redirect('/menu', {user: req.user});
	}
	else{
		res.render('login', {user: req.user});
	}
	
});
router.get('/register', function (req, res, next) {
	res.render('register', {user: req.user});
});

router.post('/register', function (req, res, next) {
	var uName = req.body.Username;
	var firstName =req.body.Firstname;
	var lastName =req.body.Lastname;
	var pWord =req.body.Password;
	var eMail =req.body.Email_Address;

	console.log(uName);

	db.insertUser({name: uName, first_name: firstName, last_name: lastName, password: pWord, role: 3, email: eMail})
	.then(function(success){
		res.redirect('/login');
	})
	.catch(function(success){
		res.redirect('/register');
	});
});

// router.get('/profile', function(req, res, next) {
// 	res.render('profile');
// });

router.get('/profile', function (req, res, next) {
    if(req.user){
        var customerID = req.user._id;
        var user_first_name;
        var user_last_name;
        var profile_username;
		var user_pwd;
		console.log(customerID);
        db.getUser(customerID).then(function (userProfile) {
            console.log(userProfile)
            if(userProfile._id = customerID){
                user_first_name = userProfile.first_name;
                user_last_name = userProfile.last_name;
                profile_username = userProfile.name;
                user_pwd = userProfile.password;
            }
            
            res.render('profile', {
                user_first_name,
				user_last_name,
				profile_username,
				user_pwd,
				user: req.user
            });
        });
    }
    else{
        res.redirect('/login');
    }
    
});

router.post('/submit-profile',function(req,res,next){     
	db.updateUser({_id: req.user._id}, {'$set': {first_name: req.body.firstname, last_name: req.body.lastname, password: req.body.pwd, name: req.body.username}})
	.then(function(result){
        if (result.result.ok) {
            res.writeHead(303, {'Location': '/profile?success=true'});
            res.end()
        } else {
            res.writeHead(303, {'Location': '/profile?success=false'});
            res.end()
        }
    }).catch(function(err) {
        res.json(err);
    });
})

router.get('/popular-dishes', function (req, res, next) {
	var customerID;
	if (req.user) {
		customerID = req.user._id; 
	}

	// get cart from database 
	db.getUserCart(customerID, function (userCart) {
	var cartItems = (userCart.length == 0) ? [] : userCart[0].dishes;
		db.getDishes(function (dishes) {
			// checks to see if a dish is in the cart already
			var checkCart = [];
			for (var i = 0; i < dishes.length; i++) {
				checkCart.push(false);
				for (var j = 0; j < cartItems.length; j++) {
					if (dishes[i].title == cartItems[j].title) {
						checkCart[checkCart.length - 1] = true;
						break;
					}
				}
			}
			// renders dish page
			res.render('popular_dishes', {
			menuItems: dishes,
			checkCart,
			user: req.user
			});
		});
	});
});

router.get('/appetizers', function (req, res, next) {
	var customerID;
	if (req.user) {
		customerID = req.user._id 
	}
	var dish_type = "appetizers";
	// get cart from database 
	db.getUserCart(customerID, function (userCart) {
		var cartItems = (userCart.length == 0) ? [] : userCart[0].dishes;
		db.getDishtypes(dish_type, function (dishes) {
			// checks to see if a dish is in the cart already
			var checkCart = [];
			for (var i = 0; i < dishes.length; i++) {
				checkCart.push(false);
				for (var j = 0; j < cartItems.length; j++) {
					if (dishes[i].title == cartItems[j].title) {
						checkCart[checkCart.length - 1] = true;
						break;
					}
				}
			}
			// renders dish page
			res.render('appetizers', {
			menuItems: dishes,
			checkCart,
			user: req.user
			});
		});
	});
});

router.get('/entrees', function (req, res, next) {
	var customerID;
	if (req.user) {
		customerID = req.user._id;  
	}
	var dish_type = "entrees";
	// get cart from database 
	db.getUserCart(customerID, function (userCart) {
		var cartItems = (userCart.length == 0) ? [] : userCart[0].dishes;
		db.getDishtypes(dish_type, function (dishes) {
			// checks to see if a dish is in the cart already
			var checkCart = [];
			for (var i = 0; i < dishes.length; i++) {
				checkCart.push(false);
				for (var j = 0; j < cartItems.length; j++) {
					if (dishes[i].title == cartItems[j].title) {
						checkCart[checkCart.length - 1] = true;
						break;
					}
				}
			}
			// renders dish page
			res.render('entrees', {
			menuItems: dishes,
			checkCart,
			user: req.user
			});
		});
	});
});

router.get('/desserts', function (req, res, next) {
	var customerID;
	if (req.user) {
		customerID = req.user._id;  
	}
	var dish_type = "desserts";
	// get cart from database 
	db.getUserCart(customerID, function (userCart) {
		var cartItems = (userCart.length == 0) ? [] : userCart[0].dishes;
		db.getDishtypes(dish_type, function (dishes) {
			// checks to see if a dish is in the cart already
			var checkCart = [];
			for (var i = 0; i < dishes.length; i++) {
				checkCart.push(false);
				for (var j = 0; j < cartItems.length; j++) {
					if (dishes[i].title == cartItems[j].title) {
						checkCart[checkCart.length - 1] = true;
						break;
					}
				}
			}
			// renders dish page
			res.render('desserts', {
			menuItems: dishes,
			checkCart,
			user: req.user
			});
		});
	});
});

router.get('/drinks', function (req, res, next) {
	var customerID;
	if (req.user) {
		customerID = req.user._id; //Get User ID
	}
	var dish_type = "drinks";
	// get cart from database 
	db.getUserCart(customerID, function (userCart) {
		var cartItems = (userCart.length == 0) ? [] : userCart[0].dishes;
		db.getDishtypes(dish_type, function (dishes) {
			// checks to see if a dish is in the cart already
			var checkCart = [];
			for (var i = 0; i < dishes.length; i++) {
				checkCart.push(false);
				for (var j = 0; j < cartItems.length; j++) {
					if (dishes[i].title == cartItems[j].title) {
						checkCart[checkCart.length - 1] = true;
						break;
					}
				}
			}
			// renders dish page
			res.render('drinks', {
			menuItems: dishes,
			checkCart,
			user: req.user
			});
		});
	});
});

// helper function - checks previous orders 
function checkPreviousOrders(array, custID, callback) {
	db.getUserOrders(ObjectID(custID.toString()), function(orders) {
		let ordered_dishes = [];
		// determine ordered items 
		for (var i = 0; i < orders.length; i++) {
			let temp = orders[i].orderItems;
			for (var j = 0; j < temp.length; j++) {
				ordered_dishes.push(temp[j].title);
			}
		}
		// remove duplicates  
		ordered_dishes = [...new Set(ordered_dishes.map(obj => JSON.stringify(obj)))]
                	.map(str => JSON.parse(str));

        let checkPrevOrd = [];
        for (var i = 0; i < array.length; i++) {
        		checkPrevOrd.push(false);
        	for (var j = 0; j < ordered_dishes.length; j++) {
        		if (ordered_dishes[i] == array[j].title) {
        			checkPrevOrd[checkPrevOrd.length - 1] = true;  
        		}
        	}
        }
        console.log(checkPrevOrd.length);
        console.log(array.length);
        callback(checkPrevOrd); 
	});
}

// helper function - checks current cart 
function checkCurrentCart(array, custID, callback) {
	db.getUserCart(custID.toString(), function(cart) {
		var cartItems = (cart.length == 0) ? [] : cart[0].dishes;

		// remove duplicates  
		cartItems = [...new Set(cartItems.map(obj => JSON.stringify(obj)))]
                	.map(str => JSON.parse(str));

		let checkCart = []
		for (var i = 0; i < array.length; i++) {
			checkCart.push(false);
			for (var j = 0; j < cartItems.length; j++) {
				if (array[i].title == cartItems[j].title) {
					checkCart[checkCart.length - 1] = true; 
				}
			}
		}
		console.log(checkCart.length);
		console.log(array.length); 
		callback(checkCart);
	});
}

/* RECOMMEND ME UPDATED BY SEERAT 4/21 */
/* NEED TO ACCOUNT FOR ALREADY IN CART FUNCTIONALITY */
router.get('/recommend-me', function (req, res, next) {
	if(req.user)
	{	
		// should get foodClusterProbs by user ID
		// still have to account for empty results returned by the database 
		var custID = req.user._id;
		db.getFoodClusterProbsByID(custID.toString()).then(function(fcp) {
			if (fcp.length == 0) {
				res.render('recommend', {
					meals: [],
					customerID: custID,
					user: req.user
				});
			}
			fcp = fcp[0];
			// calculate probabilities of all clusters
			fcp = [fcp['savory'],fcp['spicy'],fcp['sweet'],fcp['lotsCarbs'],fcp['someCarbs'],fcp['noCarbs'],fcp['vegetarian'],fcp['redMeat'],fcp['whiteMeat'],fcp['seafood'],fcp['healthy'],fcp['fountain'],fcp['caffeine']];
			var salud = (fcp[10] == 0) ? false : true;
			var veggie = (fcp[6] > 1) ? true : false; 
			// sum all values in array
			let sum = 0;
			for (var i = 0; i < fcp.length; i++) {
				sum += fcp[i];
			}
			// normalize array to get probabilities 
			for (var i = 0; i < fcp.length; i++) {
				fcp[i] = (fcp[i]/sum);
			}
			// determine top three clusters
			let max = fcp[0];
    		let maxIndex = 0;
    		for (var i = 1; i < fcp.length; i++) {
    		    if (fcp[i] > max) {
    		        maxIndex = i;
    		        max = fcp[i];
    		    }
    		}

    		let max2 = -1;
    		let maxInd2 = 0;
    		for (var i = 0; i < fcp.length; i++) {
    			if (i == maxIndex) {
    				continue; 
    			}
    			else {
    		    	if (fcp[i] > max2) {
    		        	maxInd2 = i;
    		        	max2 = fcp[i];
    		    	}
    		    }
    		}

    		let max3 = -1;
    		let maxInd3 = 0;
    		for (var i = 0; i < fcp.length; i++) {
    			if ((i == maxIndex) || (i == maxInd2)) {
    				continue; 
    			}
    			else {
    		    	if (fcp[i] > max3) {
    		        	maxInd3 = i;
    		        	max3 = fcp[i];
    		    	}
    		    }
    		}

    		// determine tags for top three clusters
    		let flavor_query = '';
    		let meat_query = '';
    		let carb_query = '';
    		let health_query = (fcp[10] == 0) ? 'No' : 'Yes'; //healthy food should be prioritized 
    		let drink_query = -1;

    		switch(maxIndex) {
    			case 0:
    			flavor_query = 'Savory';
    			break;
    			case 1:
    			flavor_query = 'Spicy';
    			break;
    			case 2:
    			flavor_query = 'Sweet';
    			break;
    			case 3:
    			carb_query = 'Lots';
    			break;
    			case 4:
    			carb_query = 'Some';
    			break;
    			case 5:
    			carb_query = 'None';
    			break;
    			case 6:
    			meat_query = 'Vegetarian';
    			break;
    			case 7:
    			meat_query = 'redMeat';
    			break;
    			case 8:
    			meat_query = 'whiteMeat';
    			break;
    			case 9:
    			meat_query = 'Seafood';
    			break;
    			case 10:
    			health_query = 'Yes';
    			break;
    			case 11:
    			drink_query = 'Fountain';
    			break;
    			case 12:
    			drink_query = 'Caffeine';
    		}

    		switch(maxInd2) {
    			case 0:
    			flavor_query = 'Savory';
    			break;
    			case 1:
    			flavor_query = 'Spicy';
    			break;
    			case 2:
    			flavor_query = 'Sweet';
    			break;
    			case 3:
    			carb_query = 'Lots';
    			break;
    			case 4:
    			carb_query = 'Some';
    			break;
    			case 5:
    			carb_query = 'None';
    			break;
    			case 6:
    			meat_query = 'Vegetarian';
    			break;
    			case 7:
    			meat_query = 'redMeat';
    			break;
    			case 8:
    			meat_query = 'whiteMeat';
    			break;
    			case 9:
    			meat_query = 'Seafood';
    			break;
    			case 10:
    			health_query = 'Yes';
    			break;
    			case 11:
    			drink_query = 'Fountain';
    			break;
    			case 12:
    			drink_query = 'Caffeine';
    		}

    		switch(maxInd3) {
    			case 0:
    			flavor_query = 'Savory';
    			break;
    			case 1:
    			flavor_query = 'Spicy';
    			break;
    			case 2:
    			flavor_query = 'Sweet';
    			break;
    			case 3:
    			carb_query = 'Lots';
    			break;
    			case 4:
    			carb_query = 'Some';
    			break;
    			case 5:
    			carb_query = 'None';
    			break;
    			case 6:
    			meat_query = 'Vegetarian';
    			break;
    			case 7:
    			meat_query = 'redMeat';
    			break;
    			case 8:
    			meat_query = 'whiteMeat';
    			break;
    			case 9:
    			meat_query = 'Seafood';
    			break;
    			case 10:
    			health_query = 'Yes';
    			break;
    			case 11:
    			drink_query = 'Fountain';
    			break;
    			case 12:
    			drink_query = 'Caffeine';
    		}
    		
			// query meals from top three clusters, making sure there aren't any repeats 
			// returns null array if query does not work out 
			db.getDishesByFlavor(flavor_query).then(function(flav_dishes) {
				db.getDishesByMeat(meat_query).then(function(meat_dishes) {
					db.getDishesByCarbs(carb_query).then(function(carb_dishes) {
						db.getDishesByHealthiness(health_query).then(function(health_dishes) {
							db.getDishesByDrinkType(drink_query).then(function(drinks) {
								// append all recommendations into one array
								var meals = flav_dishes.concat(meat_dishes);
								meals = meals.concat(carb_dishes);
								meals = meals.concat(health_dishes);
								meals = meals.concat(drinks);

								// remove duplicates  
								var result = [...new Set(meals.map(obj => JSON.stringify(obj)))]
                								.map(str => JSON.parse(str));

                				// enforce vegetarian items if selected by user 
                				if (veggie) {
                					var results_updated = [];
                					for (var i = 0; i < result.length; i++) {
										let temp = result[i].meat;
										if (temp == 'Vegetarian') {
											results_updated.push(result[i]);
										}
                					}
                					result = results_updated;
                				}

                				// enforce healthy items if selected by user 
                				if (salud) {
                					results_updated = [];
                					for (var i = 0; i < result.length; i++) {
										let temp = result[i].healthy;
										if (temp == 'Yes') {
											results_updated.push(result[i]);
										}
                					}
                					result = results_updated;
                				}

								// shuffle results 
    							var ctr = result.length, temp, index;
								// While there are elements in the array
    							while (ctr > 0) {
									// Pick a random index
        							index = Math.floor(Math.random() * ctr);
									// Decrease ctr by 1
        							ctr--;
									// And swap the last element with it
        							temp = result[ctr];
        							result[ctr] = result[index];
        							result[index] = temp;
    							}
    							console.log(result.length);

                				// checks to see if any dishes have been previously ordered by user 
                				checkPreviousOrders(result, custID, function(checkPrevOrd) {
                					// checks to see if any dishes are currently in user's cart 
                					checkCurrentCart(result, custID, function(checkCart) {
                						console.log(checkPrevOrd);
                						console.log(checkCart); 
										// render top 10 recommendations
										res.render('recommend', {
											meals: result.slice(0,9),
											customerID: req.user._id,
											checkPrevOrd,
											checkCart,
											user: req.user
										});

                					}); 

                				});
							}).catch(function(err) {
									console.log(err);
								});
						}).catch(function(err) {
							console.log(err);
							});
					}).catch(function(err) {
						console.log(err);
						});
				}).catch(function(err) {
					console.log(err);
					});
			}).catch(function(err) {
				console.log(err);
				});
		}).catch(function(err) {
			console.log(err);
			});
	}
	else{
		res.redirect('/login');
	}
});

router.get('/rec-quiz', function (req, res, next) {
	var customerID = req.user._id; 
	res.render('quiz', {
		customerID,
		user: req.user
	});
});

/* CART FUNCTIONALITY */
router.get('/cart', function (req, res, next) {
	if(req.user){
		var customerID = req.user._id; 
		db.getUserCart(customerID, function (userCart) {
			var cartItems = (userCart.length == 0) ? [] : userCart[0].dishes;
			var totalPrice = 0;
			for (var i = 0; i < cartItems.length; i++) {
				totalPrice += cartItems[i].price;
			}
			res.render('cart', {
				cartItems,
				totalPrice,
				user: req.user
			});
		});
	}
	else{
		res.redirect('/login');
	}
	
});

// Added get reservation functionality to determine open reservation slots, Alex Gu 4.14.19
router.get('/reserve', function (req, res, next) {
	if (req.user) {
		var customerID = req.user._id; //customer id 
		// Get Reservations 
		db.getReservations().then(function(reservations) {
			// Need to strip identifying data. Otherwise this is super insecure and violates so many privacy laws
			var cleaned = [];
			for (var i = 0; i < reservations.length; i++) {
				let res = {}
				res.date = reservations[i].date || reservations[i].Date || "01/01/1970"
				res.time = reservations[i].time || reservations[i].Time || "1:00 AM"
				cleaned.push(res)
			}

			// Assuming restaurant hours. I'm doing it this dumb way to make hours more flexible. Can probably be configured later in admin console?
			var hours = ["7:00 AM", "8:00 AM", "9:00 AM" , "10:00 AM", "11:00 AM", "12:00 PM", "1:00 PM", "2:00 PM", "3:00 PM", "4:00 PM", "5:00 PM", "6:00 PM", "7:00 PM", "8:00 PM"]

			var success = req.query.success

			console.log(success)

			res.render('reserve', {
				cleaned, hours, success, user: req.user
			});
		});
	} else {
		res.redirect('/login')
	}
});

// Jimmy - 4/7/19 - still need to redirect to homepage once entered into database
router.post('/submit-reservation',function(req,res,next){ 

	var reservation = req.body;
	db.getReservations().then(function(reservations){
		for(var i = 0; i<reservations.length; i++) {
			if(reservations[i].date == reservation.date && reservations[i].time == reservation.time) {
				res.writeHead(303, {'Location': '/reserve?success=false'});
				res.end()
				return
			}
		}
	}).catch(function(err) {
		res.json(err);
	});
	

	db.insertReservation(req.body).then(function(result){
		if (result.result.ok) {
			res.writeHead(303, {'Location': '/reserve?success=true'});
			res.end()
		} else {
			res.writeHead(303, {'Location': '/reserve?success=false'});
			res.end()
		}
	}).catch(function(err) {
		res.json(err);
	});
})

/* SUBMITS ORDER FROM WEBSITE SEERAT, JIMMY & TARAS */
router.post('/submit-cart-website', function (req, res, next) {
	// code that stores form responses in JSON format
	if (req.user) {
		var customerID = req.user._id; 
		var d = new Date();
		var timestamp = d.toDateString() + " " + d.toTimeString();
		db.getUserCart(customerID, function (userCart) {
			var cartItems = (userCart.length == 0) ? [] : userCart[0].dishes;
			var orderedItems = [];
			for (var i = 0; i < cartItems.length; i++) {
				var temp = {
					dish: cartItems[i]._id,
					title: cartItems[i].title,
					details: "",
					price: cartItems[i].price
				};
				orderedItems.push(temp);
			}
			var order = {
				userId: customerID,
				orderItems: orderedItems,
				date_placed: timestamp,
				paid: false,
				completed: false,
				served: false
			};
			db.insertOrder(order).then(function (result) {

					db.getChefs().then(function (chefs) {
						for (var i = 0; i < chefs.length; i++) {
							var payload = {
								notification: {
									title: "This is a test title",
									body: "Hello world"
								},
								data: {
									_id: order._id + ""
								}
							};
							var options = {
								priority: "high",
								timeToLive: 60 * 60 * 24
							};

							for (var j = 0; j < chefs[i].firebaseTokens.length; j++) {
								firebase.messaging().sendToDevice(chefs[i].firebaseTokens[j], payload, options)
									.then(function (response) {
										console.log("Successfully sent message:", response);
									})
									.catch(function (error) {
										console.log("Error sending message:", error);
									});
							}
						}
						//notify the waiters as well

						db.getWaiters()
							.then(function (waiters) {
								for (var i = 0; i < waiters.length; i++) {
									var payload = {
										notification: {
											title: "This is a test title",
											body: "Hello world"
										},
										data: {
											_id: order._id + "",
											type: "ORDER"
										}
									};
									var options = {
										
										priority: "high",
										timeToLive: 60 * 60 * 24
									};

									for (var j = 0; j < waiters[i].firebaseTokens.length; j++) {
										firebase.messaging().sendToDevice(waiters[i].firebaseTokens[j], payload, options)
											.then(function (response) {
												console.log("Successfully sent message:", response);
												db.removeAllFromCart(customerID)
											})
											.catch(function (error) {
												console.log("Error sending message:", error);
											});
									}
								}
								res.redirect('/');
							})
							.catch(function (err) {
								res.redirect('/');
							});
					}).catch(function (err) {
						res.redirect('/');
					})
					res.redirect('/');
				})
				.catch(function (err) {
					res.redirect('/');
				});
		});
	} else {
		console.log("User was not logged in");
		res.redirect('/login');
	}
});

router.get('/console', function (req, res, next) {
	if (req.user && req.user.role == 0) {
			res.render('console', {user: req.user});
	} else {
		res.redirect('/login')
	}
});

router.get('/console-menu', function (req, res, next) {
	if (req.user && req.user.role == 0) {
		db.getDishes(function (allDishes) {
			res.render('console-menu', {dishes: allDishes, user: req.user});
		});
	} else {
		res.redirect('/')
	}
});

router.get('/editItem', function (req, res, next) {
	if (req.user && req.user.role == 0) {
		var itemId = req.query.id
		db.getDish(itemId, function (dish) {
			res.render('console-menuitem', {dish, newItem: false, user: req.user});
		});
	} else {
		res.redirect('/')
	}
});

router.get('/addItem', function (req, res, next) {
	if (req.user && req.user.role == 0) {
		res.render('console-menuitem', {dish: {}, newItem: true, user: req.user});
	} else {
		res.redirect('/')
	}
});

router.get('/console-users', function (req, res, next) {
	if (req.user && req.user.role == 0) {
		db.getUsers().then(function (users) {
			res.render('console-users', {users, user: req.user});
		})
		.then(function(err){
			res.json(err);
		});
	} else {
		res.redirect('/');
	}
});

router.get('/console-permissions', function (req, res, next) {
	if (req.user && req.user.role == 0) {
		db.getUsersSort(function (users) {
			res.render('console-permissions', {user: req.user});
		});
	} else {
		res.redirect('/');
	}
});


router.get('/console-stats', function (req, res, next) {
	if (req.user && req.user.role == 0) {
		db.getOrders(function (orders) {
			res.render('console-stats', {orders, user: req.user});
		});
	} else {
		res.redirect('/');
	}
});

router.get('/console-trends', function (req, res, next) {
	if (true){//req.user && req.user.role == 0) {
		//db.getOrders(function (orders) {
		db.getDishes(function (allDishes) {
			var ingredients = ["mushroom", "spinach", "feta", "sausage", "bell_pepper", "sugar", "artichoke", "chicken", "bread", "tomato", "mozzarrella", "olive_oil", "calimari", "steak", "potato", "onion", "veal", "marsala", "fettuccine", "eggplant", "parmesan", "marinara", "spaghetti", "crab", "arugula", "lemon", "shrimp", "garlic", "lobster", "mussel", "clam", "cod", "zucchini", "summer_squash", "red_pepper", "peas", "pasta_shells", "ricotta", "pepperoni", "gorgonzola", "provolone", "broccoli"] 
			res.render('console-trends', {dishes: allDishes, ingredients, user: req.user});
		});
	} else {
		res.redirect('/');
	}
});

router.get('/predict-dish', function (req, res, next) {
	if (true){//req.user && req.user.role == 0) {
		var itemId = req.query.dish
		db.getOrders(function (orders) {
			res.render('console-dish', {orders, itemId, user: req.user});
		});
	} else {
		res.redirect('/');
	}
});

router.get('/predict-ingredients', function (req, res, next) {
	if (true){//req.user && req.user.role == 0) {
		var ingredient = req.query.ingredient
		db.getOrders(function (orders) {
			db.getDishes(function (allDishes) {
				res.render('console-ingredients', {orders, dishes: allDishes, ingredient, user: req.user});
			});
		});
	} else {
		res.redirect('/');
	}
});

router.get('/logout', function(req, res){
	req.logout();
	res.redirect('/');
});

module.exports = router;