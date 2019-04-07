var express = require('express');
var router = express.Router();
var db = require('../database.js');
var app = express();
//var Cart = require('../models/cart.js');
//var FileReader = require('filereader')
//var filereader = new FileReader();

var firebase = require("firebase-admin");
	
/* GET home page. */
router.get('/', function (req, res, next) {
	res.render('index');
});

/* MENU AND SUBPAGES*/
// CURRENTLY DOES NOT SUPPORT CART FUNCTIONALITY - NEED TO ADD 
router.get('/menu', function (req, res, next) {
	// menu variables 
	var pop_dishes = [{
			title: "Spicy Chicken Rigatoni",
			price: 18.75,
			description: "Chicken breast, garlic, crushed red pepper and peas in spicy rosa sauce",
			filename: "chicken_rigatoni.jpg"
		},
		{
			title: "Stuffed Shells",
			price: 14.95,
			description: "Pasta shells filled with spicy Italian sausage, spinach, ricotta and Parmesan",
			filename: "stuffed_shells.jpg"
		},
		{
			title: "Arrabbiata Pizza",
			price: 25.95,
			description: "Spicy Italian sausage, pepperoni, caramelized red onions, Gorgonzola, mozzarella, provolone",
			filename: "arrabbiata.jpg"
		}
	];
	var appetizers = [{
			title: "Stuffed Mushrooms",
			price: 11.5,
			description: "Mushrooms stuffed with spinach and feta",
			filename: "stuffed_mushrooms.jpg"
		},
		{
			title: "Sausage & Peppers",
			price: 9.95,
			description: "Organic sausages stuffed with caramelized peppers",
			filename: "sausage_peppers.jpg"
		},
		{
			title: "Spinach & Artichoke al Forno",
			price: 11.5,
			description: "Spinach & artichoke dip, piping hot and fresh from the oven!",
			filename: "spinach_artichoke.jpg"
		}
	];
	var entrees = [{
			title: "Prime Ribeye",
			price: 38.95,
			description: "16 oz. Bone in, Crispy Vesuvio Potatoes, Chianti Onions",
			filename: "prime_rib.jpg"
		},
		{
			title: "Lighter Take Veal Marsala",
			price: 29.5,
			description: "Mushrooms & Marsala Sauce with Hand-Cut Fettuccine",
			filename: "veal_marsala.jpg"
		},
		{
			title: "Eggplant Parmesan",
			price: 15.95,
			description: "Mozzarella & Marinara Sauce with Spaghetti Pomodoro",
			filename: "eggplant_parmesan.jpg"
		}
	];
	var drinks = [{
			title: "Cappuccino",
			price: 3.00,
			description: "Made from Double espresso and hot milk. Topped with foamed milk",
			filename: "cappuccino.jpg"
		},
		{
			title: "Hot Tea",
			price: 3.00,
			description: "English Breakfast Tea",
			filename: "hot_tea.jpg"
		},
		{
			title: "Flavored Iced Tea",
			price: 3.00,
			description: "Comes in raspberry and peach flavors",
			filename: "flavored_iced_tea.jpg"
		}
	];
	var desserts = [{
			title: "Double Chocolate Brownie",
			price: 8.25,
			description: "Vanilla Bean Ice Cream, Fresh Strawberries, Hot Fudge",
			filename: "double_choc_brownie.jpg"
		},
		{
			title: "Tiramisu",
			price: 8.25,
			description: "Ladyfingers soaked in Espresso with Mascarpone Cheese",
			filename: "tiramisu.jpg"
		},
		{
			title: "Creme Brulee",
			price: 8.25,
			description: "Hint of Citrus, Caramelized Sugar, Fresh Berries",
			filename: "creme_brulee.jpg"
		}
	];

	// rendering page 
	res.render('menu', {
		pop_dishes,
		appetizers,
		entrees,
		drinks,
		desserts
	});
});

router.get('/popular-dishes', function (req, res, next) {
	var customerID = "5c967e32d2e79f4afc43fdef"; //hardcoded customer id 
	// get cart from database 
	db.getUserCart(customerID, function (userCart) {
		var cartItems = userCart[0].dishes;
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
			checkCart
			});
		});
	});
});

router.get('/appetizers', function (req, res, next) {
	var customerID = "5c967e32d2e79f4afc43fdef"; //hardcoded customer id 
	var dish_type = "appetizers";
	// get cart from database 
	db.getUserCart(customerID, function (userCart) {
		var cartItems = userCart[0].dishes;
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
			checkCart
			});
		});
	});
});

router.get('/entrees', function (req, res, next) {
	var customerID = "5c967e32d2e79f4afc43fdef"; //hardcoded customer id 
	var dish_type = "entrees";
	// get cart from database 
	db.getUserCart(customerID, function (userCart) {
		var cartItems = userCart[0].dishes;
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
			checkCart
			});
		});
	});
});

router.get('/desserts', function (req, res, next) {
	var customerID = "5c967e32d2e79f4afc43fdef"; //hardcoded customer id 
	var dish_type = "desserts";
	// get cart from database 
	db.getUserCart(customerID, function (userCart) {
		var cartItems = userCart[0].dishes;
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
			checkCart
			});
		});
	});});

router.get('/drinks', function (req, res, next) {
	var customerID = "5c967e32d2e79f4afc43fdef"; //hardcoded customer id 
	var dish_type = "drinks";
	// get cart from database 
	db.getUserCart(customerID, function (userCart) {
		var cartItems = userCart[0].dishes;
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
			checkCart
			});
		});
	});});

/* RECOMMEND ME FEATURE NEED TO DEFINE */
router.get('/recommend-me', function (req, res, next) {
	res.render('recommend');
});

router.get('/rec-quiz', function (req, res, next) {
	res.render('quiz');
});

/* CART FUNCTIONALITY */
router.get('/cart', function (req, res, next) {
	var customerID = "5c967e32d2e79f4afc43fdef"; //hardcoded customer id 
	db.getUserCart(customerID, function (userCart) {
		var cartItems = userCart[0].dishes;
		var totalPrice = 0;
		for (var i = 0; i < cartItems.length; i++) {
			totalPrice += cartItems[i].price;
		}
		res.render('cart', {
			cartItems,
			totalPrice
		});
	});
});

/* ONLY WORKS WITH SESSION PARAMETER 
router.get('/add-to-cart/:id',function(req, res, next){
	var productId = req.params.id;
  	var cart = new Cart(req.session.cart? req.session.cart : {});
  	db.getDish(req.params.id, function(product){
		res.json(product);
	});
    cart.add(product,product._id);
    req.session.cart = cart;
    console.log(req.session.cart);
    res.redirect('/cart');
 });

router.get('/reduce/:id',function(req, res, next){
  var productId=req.params.id;
  var cart = new Cart(req.session.cart? req.session.cart : {});
  cart.reduceByOne(productId);
  req.session.cart=cart;
  res.redirect('/cart');
});

router.get('/remove/:id',function(req, res, next){
  var productId=req.params.id;
  var cart = new Cart(req.session.cart? req.session.cart : {});
  cart.removeItem(productId);
  req.session.cart=cart;
  res.redirect('/cart');
});
*/

/* RESERVATION FUNCTIONALITY NEED TO DEFINE - JIMMY */
router.get('/reserve', function (req, res, next) {
	res.render('reserve');
});

// Jimmy - 4/7/19 - still need to redirect to homepage once entered into database
router.post('/submit-reservation',function(req,res,next){
	db.insertReservation(req.body).then(function(result){
		res.json(result);
	}).catch(function(err) {
		res.json(err);
	});
})

/* SUBMITS ORDER FROM WEBSITE SEERAT, JIMMY & TARAS */
router.post('/submit-cart-website', function (req, res, next) {
	// code that stores form responses in JSON format
	var customerID = "5c967e32d2e79f4afc43fdef"; //hardcoded customer id 
	var d = new Date();
	var timestamp = d.toDateString() + d.toTimeString();
	db.getUserCart(customerID, function (userCart) {
		var cartItems = userCart[0].dishes;
		var orderedItems = [];
		for (var i = 0; i < cartItems.length; i++) {
			var temp = {
				dish: cartItems[i]._id,
				title: cartItems[i].title,
				details: ""
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
});

module.exports = router;