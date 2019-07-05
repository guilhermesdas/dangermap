var mongoose = require('mongoose');
var bcrypt = require('bcryptjs');

var UserSchema = new mongoose.Schema({
	name: {
		type: String,
        required: true
	},
	email: {
		type: String,
        required: true,
        trim: true
	},
    username: {
        type: String,
        unique: true,
        required: true,
        trim: true,
    },
    password: {
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
            bcrypt.compare(password, user.password, function(err, result) {
                if(result === true)
                    return callback(null, user);
                else {
                    var err = new Error('Incorrect password.');
                    err.status = 402;
                    err.message = 'Incorrect password.';
                    return callback(err);
                }
            });
        });
}

// Delete user against database
UserSchema.statics.delete = function(userjs, callback) {
    //var state = 0;
    User.deleteOne({username: userjs.username})
        .exec(function(err, user) {
            if(err) {
                return callback(err);
            } else {
                return callback(null);
            }
            /*else if( !user ) {
                state = -1;
            } else{
                bcrypt.compare(userjs.password, user.password, function(err, result) {
                    if (err){
                        return callback(err);
                    } else if(result){
                        state = 1;
                    }
                    else {
                        state = -2;
                    }
                });
            }*/
        });
        /*if ( state == -1 ){
            var err = new Error('User not found.');
            err.status = 401;
            return callback(err); 
        } else if ( state == -2 ) {
            var err = new Error('Incorrect password.');
            err.status = 402;
            return callback(err);
        } else {
            User.deleteOne({username: userjs.username});
            return callback(null);
        }*/
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

var User = mongoose.model('User', UserSchema);

module.exports = User;