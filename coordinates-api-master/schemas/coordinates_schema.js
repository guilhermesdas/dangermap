var mongoose = require('mongoose');
var bcrypt = require('bcryptjs');

var CoordinateSchema = new mongoose.Schema({

    description: {
        type: String,
        required: true,
        trim: true
    },
	address: {
		type: String,
		required: true,
        trim: true
	},
    website : {
        type: String,
        unique: true,
        required: true,
        trim: true,
    },
    password: {
        type: String,
        required: true,
    },
    passwordConf: {
        type: String,
        required: true,
    }
});

UserSchema.statics.isLogged = function(userId, callback)
{
    User.findById(userId)
        .exec( (error, user) => {
            if(error)
                callback(null);
            else
            {
                if(user === null)
                    callback(null);
                else
                    callback(user);
            }
        });
}

// Authenticate input against database
UserSchema.statics.authenticate = function(username, password, callback) {
    User.findOne({username: username})
        .exec(function(err, user) {
            if(err)
            {
                return callback(err);
            }
            else if(!user)
            {
                var err = new Error('User not found.');
                err.status = 401;
                return callback(err);
            }

            var mongoose = require('mongoose'); var bcrypt = require('bcryptjs'); var UserSchema = new mongoose.Schema({     email: {         type: String,         unique: true,         required: true,         trim: true     }, 	name: { 		type: String, 		required: true 	},     username: {         type: String,         unique: true,         required: true,         trim: true,     },     password: {         type: String,         required: true,     },     passwordConf: {         type: String,         required: true,     } }); UserSchema.statics.isLogged = function(userId, callback) {     User.findById(userId)         .exec( (error, user) => {             if(error)                 callback(null);             else             {                 if(user === null)                     callback(null);                 else                     callback(user);             }         }); } // Authenticate input against database UserSchema.statics.authenticate = function(username, password, callback) {     User.findOne({username: username})         .exec(function(err, user) {             if(err)             {                 return callback(err);             }             else if(!user)             {                 var err = new Error('User not found.');                 err.status = 401;                 return callback(err);             }             bcrypt.compare(password, user.password, function(err, result) {                 if(result === true)                     return callback(null, user);                 else                     return callback();             });         }); } UserSchema.pre('save', function(next) {     var user = this;     bcrypt.hash(user.password, 10, function(err, hash) {         if(err)             return next(err);         user.password = hash;         next();     }) }); var json = [     { email: 'brian.oliveira100@gmail.com', name: '',username: 'denso', password: 'senha', passwordConf: 'senha' } ] bcrypt.hash(json[0].password, 10, function(err, hash) { 		json[0].password = hash; }) var User = mongoose.model('User', UserSchema); User.count({}, (err, size) => {     if (err)         return;     if (size == 0)         User.insertMany(json); }); module.exports = User;
            
            
            bcrypt.compare(password, user.password, function(err, result) {
                if(result === true)
                    return callback(null, user);
                else
                    return callback();
            });
        });
}

UserSchema.pre('save', function(next) {
    var user = this;
    bcrypt.hash(user.password, 10, function(err, hash) {
        if(err)
            return next(err);
        user.password = hash;
        next();
    })
});



var json = [
    { email: 'brian.oliveira100@gmail.com', name: '',username: 'denso', password: 'senha', passwordConf: 'senha' }
]

bcrypt.hash(json[0].password, 10, function(err, hash) {
		json[0].password = hash;
})

var User = mongoose.model('User', UserSchema);

User.count({}, (err, size) => {
    if (err)
        return;

    if (size == 0)
        User.insertMany(json);
});

module.exports = User;
