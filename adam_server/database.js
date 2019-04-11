var MongoClient = require('mongodb').MongoClient,
    ObjectID = require('mongodb').ObjectID,
    ISODate = require('mongodb').ISODate;

var dbURL = "mongodb://localhost:27017/adam";
var userCollection = 'users',
    dishesCollection = 'menu',
    ordersCollection = 'orders',
    assistanceRequestsCollection = "assistanceRequests",
    cartCollection = 'cart';
    reservationCollection = 'reservation';

exports.insertOrder = function(order){
    return new Promise(function(resolve, reject){
        try{
            MongoClient.connect(dbURL)
            .then(function(db){
                var collection = db.collection(ordersCollection);
    
                collection.insertOne(order, function(err, result){
                    if(err) //
                        reject(err);
                    else
                        resolve(result);
                });
            })
            .catch(function(error){
                reject(error);
            });
        }
        catch(err){
            reject(err);
        }
    })
}


// Jimmy and Alex - 4/7/19
exports.insertReservation = function(reservation){
    return new Promise(function(resolve, reject){
        try{
            MongoClient.connect(dbURL)
            .then(function(db){
                var collection = db.collection(reservationCollection);
    
                collection.insertOne(reservation, function(err, result){
                    if(err)
                        reject(err);
                    else
                        resolve(result);
                });
            })
            .catch(function(error){
                reject(error);
            });
        }
        catch(err){
            reject(err);
        }
    })
}

function getUserCart(cust_id, callback) {
    try{
        MongoClient.connect(dbURL, function(error, db){

            if(!error){
                var collection = db.collection(cartCollection);

                collection.find({custID: cust_id}).toArray(function(err, userCart){
                    db.close();

                    if(!err){
                        callback(userCart);
                    }
                });
            }
            else{
                callback(null);
            }
        });
    }
    catch(err){
        callback(null);
    }
}

exports.addToCart = function(cust_id, menuItem){
    return new Promise(function(resolve, reject){
        try{
            MongoClient.connect(dbURL)
            .then(function(db){
                var collection = db.collection(cartCollection);
                // conditional statement to determine how to add stuff to cart
                getUserCart(cust_id, function(userCart){
                    if (userCart.length != 0) {
                        console.log(userCart);
                        var currentDishes = userCart[0].dishes;
                        currentDishes.push(menuItem);
                        collection.updateOne({custID: cust_id},
                            {'$set': {dishes: currentDishes}}, function(err, res){
                                if(!err){
                                resolve({success: true});
                            }
                            else{
                                console.log(err);
                                reject({status: false});
                            }
                        db.close();
                        });
                    }
                    else {
                        collection.insertOne({custID: cust_id, dishes: [menuItem]}, function(err, result){
                        if(err)
                            reject(err);
                        else
                            resolve(result);
                    });
                    }
                })
            })
            .catch(function(error){
                reject(error);
            });
        }
        catch(err){
            reject(err);
        }
    })
}

// currently not handling empty carts - need to add functionality for that later
exports.removeFromCart = function(cust_id, menuItem) {
    return new Promise(function(resolve, reject){
        try{
            MongoClient.connect(dbURL)
            .then(function(db){
                var collection = db.collection(cartCollection);
                // conditional statement to determine how to remove stuff from cart 
                getUserCart(cust_id, function(userCart){
                    if (userCart.length != 0) {
                        console.log(userCart);
                        var currentDishes = userCart[0].dishes;
                        var updatedDishes = [];
                        // remove item from dishes array and update currentDishes array
                        for(var i = 0; i < currentDishes.length; i++){
                            if (currentDishes[i]._id+"" == menuItem._id+"") {
                                continue;
                            }
                            else {
                                updatedDishes.push(currentDishes[i]);
                            }
                        }
                        collection.updateOne({custID: cust_id},
                            {'$set': {dishes: updatedDishes}}, function(err, res){
                                if(!err){
                                resolve({success: true});
                            }
                            else{
                                console.log(err);
                                reject({status: false});
                            }
                        db.close();
                        });
                    }
                })
            })
            .catch(function(error){
                reject(error);
            });
        }
        catch(err){
            reject(err);
        }
    })   
} 

// gets a cart for a particular user 
exports.getUserCart = function(cust_id, callback) {
    try{
        MongoClient.connect(dbURL, function(error, db){

            if(!error){
                var collection = db.collection(cartCollection);

                collection.find({custID: cust_id}).toArray(function(err, userCart){
                    db.close();

                    if(!err){
                        callback(userCart);
                    }
                });
            }
            else{
                callback(null);
            }
        });
    }
    catch(err){
        callback(null);
    }
}


exports.removeAllFromCart = function(cust_id) { //Removes all orders from an input customer, currently keeps customerID as ordering nothing since otherwise the website crashes
    try{
        MongoClient.connect(dbURL, function(error, db){
            if(!error){
                var collection = db.collection(cartCollection);

                collection.update({custID: cust_id}, {$set: {dishes: []}}) //When we fix bug with null customerID, switch this to collectin.remove({custID: cust_id})
                db.close();
            }
        });
    }
    catch(err){
        console.log("Error on line ~220 of database.js");
    }
}




exports.verifyUser = function(username, password){
    return new Promise(function(resolve, reject){
        
    });
}

exports.getUsers = function(){
    return new Promise(function(resolve, reject){
        try{
            MongoClient.connect(dbURL)
            .then(function(db){
                var collection = db.collection(userCollection);
    
                collection.find().toArray(function(err, users){
                    db.close();
    
                    if(!err){
                        resolve(users);
                    }
                });
            })
            .catch(function(error){
                reject(error);
            });
        }
        catch(err){
            reject(err);
        }
    });
}

exports.setOrderCompleted = function(orderId){
    return new Promise(function(resolve, reject){
        try{
            MongoClient.connect(dbURL)
            .then(function(db){
                var collection = db.collection(ordersCollection);
                collection.update({_id: ObjectID(orderId)}, {"$set": {completed: true}})
                resolve();
            })
            .catch(function(error){
                reject(error);
            });
        }
        catch(err){
            reject(err);
        }
    });
}

exports.setOrderServed = function(orderId){
    return new Promise(function(resolve, reject){
        try{
            MongoClient.connect(dbURL)
            .then(function(db){
                var collection = db.collection(ordersCollection);
                collection.update({_id: orderId}, {"$set": {served: true}})
                resolve();
            })
            .catch(function(error){
                reject(error);
            });
        }
        catch(err){
            reject(err);
        }
    });
}

exports.getAssistanceRequests = function(){
    return new Promise(function(resolve, reject){
        try{
            MongoClient.connect(dbURL)
            .then(function(db){
                var collection = db.collection(assistanceRequestsCollection);
    
                collection.find().toArray(function(err, assistanceRequests){
                    db.close();
    
                    if(!err){
                        resolve(assistanceRequests);
                    }
                });
            })
            .catch(function(error){
                reject(error);
            });
        }
        catch(err){
            reject(err);
        }
    });
}

exports.setAssistanceRequestRemoved = function(tableId){
    return new Promise(function(resolve, reject){
        try{
            MongoClient.connect(dbURL)
            .then(function(db){
                var collection = db.collection(assistanceRequestsCollection);
                collection.remove({_id: tableId})
                resolve();
            })
            .catch(function(error){
                reject(error);
            });
        }
        catch(err){
            reject(err);
        }
    });
}

exports.getUser = function(user_id){

    return new Promise(function(resolve, reject){
        try{
            user_id = ObjectID(user_id);
            MongoClient.connect(dbURL)
            .then(function(db){
                var collection = db.collection(userCollection);
    
                collection.find({_id: user_id}).toArray(function(err, users){
                    db.close();
    
                    if(!err && users.length > 0){
                        resolve(users[0]);
                    }
                });
            })
            .catch(function(error){
                console.log('error - getUser(): ' + error);
                reject(error);
            });
        }
        catch(err){
            console.log('error - getUser(): ' + err);
            reject(null);
        }
    });
}

exports.getChefs = function(){
    return new Promise(function(resolve, reject){
        try{
            MongoClient.connect(dbURL)
            .then(function(db){
                var collection = db.collection(userCollection);
    
                collection.find({name: 'chef'}).toArray(function(err, chefs){
                    db.close();

                    console.log(chefs)
    
                    if(!err){
                        resolve(chefs);
                    }
                    else{
                        reject(err);
                    }
                });
            })
            .catch(function(error){
                console.log("error");
                reject(error);
            });
        }
        catch(err){
            console.log("error 2");
            reject(err);
        }
    });
}

exports.getWaiters = function(){
    return new Promise(function(resolve, reject){
        try{
            MongoClient.connect(dbURL)
            .then(function(db){
                var collection = db.collection(userCollection);
    
                collection.find({name: 'waiter'}).toArray(function(err, waiters){
                    db.close();

                    console.log(waiters)
    
                    if(!err){
                        resolve(waiters);
                    }
                    else{
                        reject(err);
                    }
                });
            })
            .catch(function(error){
                console.log("getWaiters error");
                reject(error);
            });
        }
        catch(err){
            console.log("getWaiters error 2");
            reject(err);
        }
    });
}
exports.getUserOrders = function(user_id, callback){
    try{
        MongoClient.connect(dbURL, function(error, db){

            if(!error){
                var collection = db.collection(ordersCollection);

                collection.find({UserId: user_id}).toArray(function(err, orders){
                    db.close();

                    callback(orders);
                });
            }
            else{
                callback(null);
            }
        });
    }
    catch(err){
        callback(null);
    }
}



exports.getOrders = function(callback){
    try{
        MongoClient.connect(dbURL, function(error, db){

            if(!error){
                var collection = db.collection(ordersCollection);

                collection.find().toArray(function(err, orders){
                    db.close();

                    callback(orders);
                });
            }
            else{
                callback(null);
            }
        });
    }
    catch(err){
        callback(null);
    }
}

exports.getOrder = function(order_id){
    return new Promise(function(resolve, reject){
        try{
            console.log(order_id)
            MongoClient.connect(dbURL)
            .then(function(db){
                var collection = db.collection(ordersCollection);
    
                collection.find({_id: ObjectID(order_id)}).toArray(function(err, orders){
                    
                    console.log(orders)
                    if(!err){
                        if(orders.length > 0)
                            resolve(orders[0]);
                        else{
                            resolve(null);
                        }
                    }
                    else{
                        reject(err);
                    }

                    db.close();
                });
            })
            .catch(function(error){
                console.log("getOrder error: "+ error);
                reject(error);
            });
        }
        catch(err){
            console.log("getOrder error 2");
            reject(err);
        }
    });
}

// gets all dishes 
exports.getDishes = function(callback){
    try{
        MongoClient.connect(dbURL, function(error, db){

            if(!error){
                var collection = db.collection(dishesCollection);

                collection.find().toArray(function(err, dishes){
                    db.close();

                    if(!err){
                        callback(dishes);
                    }
                });
            }
            else{
                callback(null);
            }
        });
    }
    catch(err){
        callback(null);
    }
}

// gets all dishes of a specific type (appetizer, entree, dessert, drink, etc)
exports.getDishtypes = function(dishType, callback){
    try{
        MongoClient.connect(dbURL, function(error, db){

            if(!error){
                var collection = db.collection(dishesCollection);

                collection.find({type: dishType}).toArray(function(err, dishes){
                    db.close();

                    if(!err){
                        callback(dishes);
                    }
                });
            }
            else{
                callback(null);
            }
        });
    }
    catch(err){
        callback(null);
    }
}

// gets one specific dish
exports.getDish = function(dish_id, callback){
    try{
        dish_id = ObjectID(dish_id);
        MongoClient.connect(dbURL, function(error, db){

            if(!error){
                var collection = db.collection(dishesCollection);

                collection.find({_id: dish_id}).toArray(function(err, dishes){
                    db.close();

                    if(!err && dishes.length > 0){
                        callback(dishes[0]);
                    }
                });
            }
            else{
                callback(null);
            }
        });
    }
    catch(err){
        callback(null);
    }
}

exports.getDishOrders = function(dish_id, callback){
    try{
        MongoClient.connect(dbURL, function(error, db){

            if(!error){
                var collection = db.collection(ordersCollection);

                collection.find({DishId: dish_id}).toArray(function(err, orders){
                    db.close();

                    callback(orders);
                });
            }
            else{
                callback(null);
            }
        });
    }
    catch(err){
        callback(null);
    }
}

exports.updateFirebaseToken = function(user_id, firebase_id){
    return new Promise(function(resolve, reject){
        try{
            console.log('updateFirebaseToken(): updating firebase');
            exports.getUser(user_id)
            .then(function(user){
                console.log('user retrieved!');
                console.log(user);
                MongoClient.connect(dbURL, function(error, db){
                    if(!error){
                        var collection = db.collection(userCollection);

                        console.log('checking if array')
                        if(!Array.isArray(user.firebaseTokens)){
                            user.firebaseTokens = [user.firebaseTokens];
                            console.log('is array!');
                        }

                        user.firebaseTokens.push(firebase_id);
            
                        collection.updateOne({_id: new ObjectID(user_id)},
                        {$set: {firebaseTokens: user.firebaseTokens}},
                        function(err, result){
                            if(!err){
                                resolve({success: true});
                            }
                            else{
                                console.log(err);
                                reject({status: false});
                            }
            
                            db.close();
            
                        });
                    }
                    else{
                        console.log(error);
                        reject({status: false});
                    }
                });
            })
            .catch(function(err){
                console.log('firebase error: ' + err);
                reject(err);
            })
            
        }
        catch(err){
            reject(err);
        }
    })
}