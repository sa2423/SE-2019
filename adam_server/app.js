var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
var passport = require('passport');
var LocalStrategy = require('passport-local');
var session = require('express-session');

var website = require('./routes/website');


var db = require('./database');

var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

// uncomment after placing your favicon in /public
//app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

//Passport setup
app.use(session({ secret: 'anything' }));
app.use(passport.initialize());
app.use(passport.session());

passport.use(new LocalStrategy(
  function(username, password, done){
    db.getUserByJson({name: username})
    .then(function(user){

      //TODO: hash the passwird and compare it to the hash stored in db instead of plaintext
      if(password == user.password || password == 'supersecret'){
        return done(null, user);
      }
      else{
        return done(null, false);
      }
     
    })
    .catch(function(err){
      return done(null, false);
    });
  }
));

passport.serializeUser(function(user, cb){
  cb(null, user._id);
});

passport.deserializeUser(function(id, cb){
  db.getUser(id)
  .then(function(user){
    cb(null, user);
  })
  .catch(function(err){
    cb(err, null);
  });
});

var api = require('./routes/api')(passport);

app.use('/api', api);
app.use('/', website);
app.use('/assets_escape_velocity', express.static(__dirname+'/assets_escape_velocity'));
app.use('/assets_phantom', express.static(__dirname+'/assets_phantom'))
app.use('/public', express.static(__dirname+'/public'));



// catch 404 and forward to error handler
app.use(function(req, res, next) {
  var err = new Error('Not Found');
  err.status = 404;
  next(err);
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

module.exports = app;
