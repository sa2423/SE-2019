var express = require('express');
var router = express.Router();
var db = require('../database.js');
//var FileReader = require('filereader')
//var filereader = new FileReader();

/* GET home page. */
router.get('/', function(req, res, next) {
	res.render('index');
});

/* MENU AND SUBPAGES*/
router.get('/menu', function(req, res, next){
	var pop_dishes = [{title:"Spicy Chicken Rigatoni", price:18.75, description:"Chicken breast, garlic, crushed red pepper and peas in spicy rosa sauce", filename:"chicken_rigatoni.jpg"},
	{title:"Stuffed Shells", price:14.95, description:"Pasta shells filled with spicy Italian sausage, spinach, ricotta and Parmesan", filename:"stuffed_shells.jpg"},
	{title:"Arrabbiata Pizza", price:25.95, description:"Spicy Italian sausage, pepperoni, caramelized red onions, Gorgonzola, mozzarella, provolone", filename:"arrabbiata.jpg"}];
	var appetizers = [{title:"Stuffed Mushrooms",price:11.5,description:"Mushrooms stuffed with spinach and feta",filename:"stuffed_mushrooms.jpg"},
	{title:"Sausage & Peppers",price:9.95,description:"Organic sausages stuffed with caramelized peppers",filename:"sausage_peppers.jpg"},
	{title:"Spinach & Artichoke al Forno",price:11.5,description:"Spinach & artichoke dip, piping hot and fresh from the oven!",filename:"spinach_artichoke.jpg"}];
	var entrees = [{title:"Prime Ribeye", price:38.95, description:"16 oz. Bone in, Crispy Vesuvio Potatoes, Chianti Onions", filename:"prime_rib.jpg"},
	{title:"Lighter Take Veal Marsala", price:29.5, description:"Mushrooms & Marsala Sauce with Hand-Cut Fettuccine", filename:"veal_marsala.jpg"},
	{title:"Eggplant Parmesan", price:15.95, description:"Mozzarella & Marinara Sauce with Spaghetti Pomodoro", filename:"eggplant_parmesan.jpg"}];
	var drinks = [{title:"Cappuccino",price:3.00,description:"Made from Double espresso and hot milk. Topped with foamed milk",filename:"cappuccino.jpg"},
	{title:"Hot Tea",price:3.00,description:"English Breakfast Tea",filename:"hot_tea.jpg"},
	{title:"Flavored Iced Tea",price:3.00,description:"Comes in raspberry and peach flavors",filename:"flavored_iced_tea.jpg"}];
	var desserts = [{title:"Double Chocolate Brownie",price:8.25,description:"Vanilla Bean Ice Cream, Fresh Strawberries, Hot Fudge",filename:"double_choc_brownie.jpg"},
	{title:"Tiramisu",price:8.25,description:"Ladyfingers soaked in Espresso with Mascarpone Cheese",filename:"tiramisu.jpg"},
	{title:"Creme Brulee",price:8.25,description:"Hint of Citrus, Caramelized Sugar, Fresh Berries",filename:"creme_brulee.jpg"}];
	res.render('menu', {
		pop_dishes,
		appetizers,
		entrees,
		drinks,
		desserts
	}); 
});

router.get('/popular-dishes', function(req, res, next){
	var menuItems = [{title:"Stuffed Mushrooms",price:11.5,description:"Mushrooms stuffed with spinach and feta",filename:"stuffed_mushrooms.jpg"},
	{title:"Sausage & Peppers",price:9.95,description:"Organic sausages stuffed with caramelized peppers",filename:"sausage_peppers.jpg"},
	{title:"Zucchini and Squash Noodles", price:15.95, description:"Zucchini noodles and summer squash sauce", filename:"zucchini_squash.jpg"},
	{title:"Spicy Chicken Rigatoni", price:18.75, description:"Chicken breast, garlic, crushed red pepper and peas in spicy rosa sauce", filename:"chicken_rigatoni.jpg"},
	{title:"Stuffed Shells", price:14.95, description:"Pasta shells filled with spicy Italian sausage, spinach, ricotta and Parmesan", filename:"stuffed_shells.jpg"},
	{title:"Arrabbiata Pizza", price:25.95, description:"Spicy Italian sausage, pepperoni, caramelized red onions, Gorgonzola, mozzarella, provolone", filename:"arrabbiata.jpg"},
	{title:"Vegetable Pizza", price:19.95, description:"Eggplant, broccoli, red and green bell peppers, mushrooms, yellow onions, mozzarella, provolone", filename:"veggie_pizza.jpg"},
	{title:"Double Chocolate Brownie",price:8.25,description:"Vanilla Bean Ice Cream, Fresh Strawberries, Hot Fudge",filename:"double_choc_brownie.jpg"},
	{title:"Tiramisu",price:8.25,description:"Ladyfingers soaked in Espresso with Mascarpone Cheese",filename:"tiramisu.jpg"}]
	res.render('popular_dishes', {
		menuItems
	});
});

router.get('/appetizers', function(req, res, next){
	var menuItems = [{title:"Stuffed Mushrooms",price:11.5,description:"Mushrooms stuffed with spinach and feta",filename:"stuffed_mushrooms.jpg"},
	{title:"Sausage & Peppers",price:9.95,description:"Organic sausages stuffed with caramelized peppers",filename:"sausage_peppers.jpg"},
	{title:"Spinach & Artichoke al Forno",price:11.5,description:"Spinach & artichoke dip, piping hot and fresh from the oven!",filename:"spinach_artichoke.jpg"},
	{title:"Chicken & Roasted Pepper Flatbread",price:13.25,description:"Organic chicken with roasted peppers on focaccia",filename:"chicken_roasted_red_pepper_flatbread.jpg"},
	{title:"Tomato Caprese",price:9.95,description:"Freshly sliced organic tomato layered with fresh mozzarrella and topped with olive oil",filename:"tomato_caprese.jpg"},
	{title:"Calamari Fritte",price: 14.25, description:"Fresh calamari, deep friend",filename:"calamari_fritte.jpg"}];
	res.render('appetizers', {
		menuItems
	});
});

router.get('/entrees', function(req, res, next){
	var menuItems = [{title:"Prime Ribeye", price:38.95, description:"16 oz. Bone in, Crispy Vesuvio Potatoes, Chianti Onions", filename:"prime_rib.jpg"},
	{title:"Lighter Take Veal Marsala", price:29.5, description:"Mushrooms & Marsala Sauce with Hand-Cut Fettuccine", filename:"veal_marsala.jpg"},
	{title:"Eggplant Parmesan", price:15.95, description:"Mozzarella & Marinara Sauce with Spaghetti Pomodoro", filename:"eggplant_parmesan.jpg"},
	{title:"Jumbo Lump Crab Cakes", price:31.95, description:"Lemon Aioli with Arugula & Tomatoes", filename:"jumbo_lump_crab_cakes.jpg"},
	{title:"Shrimp Fra Diavolo", price:23.95, description:"Pan-Seared Shrimp, Garlic, Tomatoes, Diavolo Sauce with Hand-Cut Fettuccine", filename:"shrimp_fra_diavolo.jpg"},
	{title:"Linguine di Mare", price:25.95, description:"Lobster, Shrimp, Mussels, Clams, Spicy Tomato Lobster Sauce", filename:"linguine_di_mare.jpg"},
	{title:"Parmesan-Crusted Cod", price:19.95, description:"Fresh Cod covered with Parmesan and Garlic Bread Crumbs", filename:"parmesan_crusted_cod.jpg"},
	{title:"Zucchini and Squash Noodles", price:15.95, description:"Zucchini noodles and summer squash sauce", filename:"zucchini_squash.jpg"},
	{title:"Spicy Chicken Rigatoni", price:18.75, description:"Chicken breast, garlic, crushed red pepper and peas in spicy rosa sauce", filename:"chicken_rigatoni.jpg"},
	{title:"Stuffed Shells", price:14.95, description:"Pasta shells filled with spicy Italian sausage, spinach, ricotta and Parmesan", filename:"stuffed_shells.jpg"},
	{title:"Arrabbiata Pizza", price:25.95, description:"Spicy Italian sausage, pepperoni, caramelized red onions, Gorgonzola, mozzarella, provolone", filename:"arrabbiata.jpg"},
	{title:"Vegetable Pizza", price:19.95, description:"Eggplant, broccoli, red and green bell peppers, mushrooms, yellow onions, mozzarella, provolone", filename:"veggie_pizza.jpg"}];
	res.render('entrees', {
		menuItems 
	});
});

router.get('/desserts', function(req, res, next){
	var menuItems = [{title:"Double Chocolate Brownie",price:8.25,description:"Vanilla Bean Ice Cream, Fresh Strawberries, Hot Fudge",filename:"double_choc_brownie.jpg"},
	{title:"Tiramisu",price:8.25,description:"Ladyfingers soaked in Espresso with Mascarpone Cheese",filename:"tiramisu.jpg"},
	{title:"Creme Brulee",price:8.25,description:"Hint of Citrus, Caramelized Sugar, Fresh Berries",filename:"creme_brulee.jpg"},
	{title:"Spiced Zinfandel Granita with Grapes and Cream",price:8.25,description:"Sicilian icy Granita served with Grapes and Cream",filename:"spiced_granita.jpg"},
	{title:"Strawberry Granita",price:8.25,description:"Icy Granita, with more than a hint of strawberry",filename:"strawberry_granita.jpg"},
	{title:"Amaretti Cookies",price:8.25,description:"The Italian Macaron, made with crunchy almonds and amaretto (32 cal)",filename:"amaretti_cookies.jpg"}];
	res.render('desserts', {
		menuItems
	});
});

router.get('/drinks', function(req, res, next){
	var menuItems = [{title:"Sprite",price:1.00,description:"",filename:"sprite.jpg"},
	{title:"Coca Cola",price:1.00,description:"",filename:"coke.jpg"},
	{title:"Cappuccino",price:3.00,description:"Made from Double espresso and hot milk. Topped with foamed milk",filename:"cappuccino.jpg"},
	{title:"Hot Tea",price:3.00,description:"English Breakfast Tea",filename:"hot_tea.jpg"},
	{title:"Flavored Iced Tea",price:3.00,description:"Comes in raspberry and peach flavors",filename:"flavored_iced_tea.jpg"},
	{title:"San Pellegrino Water",price:2.00,description:"Sparkling Mineral Water",filename:"san_pellegrino.jpg"}];
	res.render('drinks', {
		menuItems
	});
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
