router.post('/editUser', function(req, res){
        var user = req.body.user;
        var set = req.body.set;

        console.log(user);

        try {
            user = JSON.parse(user);
            set = JSON.parse(set);

            db.updateUser(user, set)
            .then(function(result){
                console.log('user updated');
        
            })
            .catch(function(error){
                console.log('error updating user: ' + error);
                res.json(error);
            });
        } catch (e) {
            return console.error(e);
        }

    });



exports.updateUser = function(user, set){
    return new Promise(function(resolve, reject){
        try{
            MongoClient.connect(dbURL)
            .then(function(db){
                var collection = db.collection(usersCollection);
    
                collection.updateOne(user, set, function(err, result){
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
