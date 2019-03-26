var expect  = require('chai').expect;
var request = require('request');

var base_url = 'http://52.39.140.122/api/';

describe('Endpoints status tests', function(){
    
    it('/api/users', function(done){
        request(base_url+'users', function(err, response, body){
            expect(response.statusCode).to.equal(200);
            done();
        });
    });
});

describe('Database tests', function(){
    
});