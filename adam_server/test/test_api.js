var expect  = require('chai').expect;
var request = require('request');

var base_url = 'http://52.39.140.122/api/';

describe('Endpoints Status Tests', function(){
    
    it('should return status code 200 from /users endpoint', function(done){
        request.get(base_url+'users', function(err, response, body){
            expect(response.statusCode).to.equal(200);
            done();
        });
    });

    it('should return status code 200 from /menu endpoint', function(done){
        request.get(base_url + 'menu', function(err, response, body){
            expect(response.statusCode).to.equal(200);
            done();
        });
    });

    it('should return status code 200 from /orders endpoint', function(done){
        request.get(base_url + 'orders', function(err, response, body){
            expect(response.statusCode).to.equal(200);
            done();
        });
    });

	it('should return status code 200 from /assistanceRequests endpoint', function(done){
        request.get(base_url + 'assistanceRequests', function(err, response, body){
            expect(response.statusCode).to.equal(200);
            done();
        });
    });
});

var MongoClient = require('mongodb').MongoClient,
    ObjectID = require('mongodb').ObjectID,
    ISODate = require('mongodb').ISODate;

var dbURL = "mongodb://localhost:27017/adam";
var userCollection = 'users',
    dishesCollection = 'menu',
    ordersCollection = 'orders',
    assistanceRequestsCollection = "assistanceRequests";

describe('Orders Database Tests', function(){

	beforeEach(() => { //Before each test we empty the database
        return new Promise(function(resolve){
            MongoClient.connect(dbURL)
            .then(function(db){
                var collection = db.collection(ordersCollection);
    
                collection.remove({})
                resolve()
            })
        })});

	it('should return get no orders from empty orders collection', function(done){
        request.get(base_url + 'orders', function(err, response, body){
            expect(response.body).to.equal('[]');
            done();
        });
    });
    
    /*it('should successfully place order', function(done){
		var d = new Date();
		var timestamp = d.toDateString() + d.toTimeString();

    	var formData = {
    		userId: "5c967e32d2e79f4afc43fdef",
    		orderItems: ["Sprite"],
    		date_placed: timestamp,
    		paid: "false",
    		completed: "false",
    		served: "false"
    	};

        request.post({url: base_url + 'placeOrder', formData: formData}, function(err, response, body){
            expect(response.statusCode).to.equal(200);
            done();
        });
    });*/

    it('should successfully place order', function(done){
		var d = new Date();
		var timestamp = d.toDateString() + d.toTimeString();

    	var formData = {
    		userId: "5c967e32d2e79f4afc43fdef",
    		orderItems: ["Sprite"],
    		date_placed: timestamp,
    		paid: "false",
    		completed: "false",
    		served: "false"
    	};

        request.post('http://52.39.140.122/submit-cart-website', function(err, response, body){
            expect(response.statusCode).to.equal(302);
            done();
        });
    });

    it('should successfully get order from /orders after making order', function(done){
        request.post('http://52.39.140.122/submit-cart-website', function(err, response, body){
            expect(response.statusCode).to.equal(302);
            request.get(base_url + 'orders', function(err, response, body){
	            expect(response.body).to.not.equal('[]');
	            done();
	        });
        });
    });

    it('should successfully get order from orders/id after making order', function(done){
        request.post('http://52.39.140.122/submit-cart-website', function(err, response, body){
            expect(response.statusCode).to.equal(302);
            request.get(base_url + 'orders', function(err, response, body){
	            expect(response.body).to.not.equal('[]');
	            var body = JSON.parse(response.body);
	            request.get(base_url + 'orders/' + body[0]._id, function(err, response, body){
		            expect(response.statusCode).to.equal(200);
		            var body = JSON.parse(response.body);
		            done()
		        });
	        });
        });
    });

    it('should successfully set order complete', function(done){
        request.post('http://52.39.140.122/submit-cart-website', function(err, response, body){
            expect(response.statusCode).to.equal(302);
            request.get(base_url + 'orders', function(err, response, body){
	            expect(response.body).to.not.equal('[]');
	            var body = JSON.parse(response.body);
	            var id = body[0]._id
	            request.get(base_url + 'orders/' + id + "/complete", function(err, response, body){
		            expect(response.statusCode).to.equal(200);
		            var body = JSON.parse(response.body);
		            expect(body.success).to.be.true;
		            done()
		        });
	        });
        });
    });

    it('should successfully set order served', function(done){
        request.post('http://52.39.140.122/submit-cart-website', function(err, response, body){
            expect(response.statusCode).to.equal(302);
            request.get(base_url + 'orders', function(err, response, body){
	            expect(response.body).to.not.equal('[]');
	            var body = JSON.parse(response.body);
	            var id = body[0]._id
	            request.get(base_url + 'orders/' + id + "/serve", function(err, response, body){
		            expect(response.statusCode).to.equal(200);
		            var body = JSON.parse(response.body);
		            expect(body.success).to.be.true;
		            done()
		        });
	        });
        });
    });
});

describe('Assistance Requests Tests', function(){

	beforeEach(() => { //Before each test we empty the database
        return new Promise(function(resolve){
            MongoClient.connect(dbURL)
            .then(function(db){
                var collection = db.collection(assistanceRequestsCollection);
    
                collection.remove({})
                resolve()
            })
        })});

	it('should return get no orders from empty assistanceRequests collection', function(done){
        request.get(base_url + 'assistanceRequests', function(err, response, body){
            expect(response.body).to.equal('[]');
            done();
        });
    });

    it('should successfully create assistance request', function(done){

    	var options = {
    		method: 'POST',
    		uri: base_url + 'assistance',
    		body: {
    			user_id: "5c967e32d2e79f4afc43fdef",
    			timestamp: "..."
    		},
    		json: true
    	};

        request(options, function(err, response, body){
            expect(response.statusCode).to.equal(200);
            done();
        });
    });

    /*it('should successfully get order from /assistanceRequests after creating request', function(done){
            
        var options = {
    		method: 'POST',
    		uri: base_url + 'assistance',
    		body: {
    			user_id: "5c967e32d2e79f4afc43fdef",
    			timestamp: "..."
    		},
    		json: true
    	};

        request(options, function(err, response, body){
            expect(response.statusCode).to.equal(200);
            request.get(base_url + 'assistanceRequests', function(err, response, body){
	            expect(response.body).to.not.equal('[]');
	            done();
	        });
        });
    });

    it('should successfully get order from assistanceRequests/id after making order', function(done){
		
		var options = {
    		method: 'POST',
    		uri: base_url + 'assistance',
    		body: {
    			user_id: "5c967e32d2e79f4afc43fdef",
    			timestamp: "..."
    		},
    		json: true
    	};

    	request(options, function(err, response, body){
            expect(response.statusCode).to.equal(200);
            request.get(base_url + 'assistanceRequests', function(err, response, body){
	            expect(response.body).to.not.equal('[]');
	            var body = JSON.parse(response.body);
	            request.get(base_url + 'assistanceRequests/' + body[0]._id, function(err, response, body){
		            expect(response.statusCode).to.equal(200);
		            var body = JSON.parse(response.body);
		            done()
		        });
	        });
        });
    });*/
});