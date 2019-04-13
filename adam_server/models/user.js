var mongoose = require('mongoose');
var bcrypt = require('bcrypt-nodejs');


var userSchema = mongoose.Schema({

    local: {
        username: String,
        password: String,
        email: String,
        role: String,
        first_name: String,
        last_name: String,
    }

});

//Generate a password hash
userSchema.methods.generateHash = function(password){
    return bcrypt.hashSync(password, bcrypt.genSaltSync(8), null);
}

//Verify a password has
userSchema.methods.validatePassword = function(password){
    return bcrypt.compareSync(password, this.local.password);
}

module.exports = mongoose.model('User', userSchema);
