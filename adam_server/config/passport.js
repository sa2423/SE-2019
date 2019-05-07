var LocalStrategy = require('passport-local').Strategy;
var User = require('../models/user');

module.exports = function(passport){
    
    passport.serializeUser(function(user, done){
        done(null, user.id);
    });

    passport.deserializeUser(function(id, done){
        User.findById(id, function(err, user){
            done(err, user);
        });
    });


    //Local signup
    passport.use('local-signup', new LocalStrategy({
        usernameField: 'username',
        passwordField: 'password',
        passReqToCallback: true
    },
    function(req, username, password, done){
        process.nextTick(function(){
            User.findOne({'local.username': username}, function(err, user){
                if(err) return done(err);

                if(user){
                    return done(null, false, res.json({error: 'Username already taken'}));
                }
                else{
                    var newUser = new User();

                    newUser.local.username = username;
                    newUser.local.password = newUser.generateHash(password);

                    newUser.save(function(err){
                        if (err) throw err;

                        return done(null, newUser);
                    });
                }

            });
        });
    }));


    //Local signin
    passport.use('local-login', new LocalStrategy({
        usernameField: 'username',
        passwordField: 'password',
        passReqToCallback: true
    },
    function(req, username, password, done){
        User.findOne({'local.username': username}, function(err, user){
            if(err) return done(err);

            if(!user){
                return done(null, false, res.json({error: "User not found"}));
            }
            if(!user,validPassword(password)){
                return done(null, false, res.json({error: "Wrong password"}));
            }
            return done(null, user);
        });
    }));

}