router.post('/editUser', function(req, res){
		var user = req.body.user;

		console.log(user);

		try {
			user = JSON.parse(user);

			db.updateUser(user)
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



exports.updateUser = function(user){
    return new Promise(function(resolve, reject){
        try{
            MongoClient.connect(dbURL)
            .then(function(db){
                var collection = db.collection(usersCollection);
    
                collection.updateOne(user, function(err, result){
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
