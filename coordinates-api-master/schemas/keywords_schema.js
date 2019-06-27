var mongoose = require('mongoose');

var KeywordsSchema = new mongoose.Schema({

    keyword: {
        type: String,
        unique: true,
        required: true,
        trim: true
    }
});

var Keywords = mongoose.model('Keywords', KeywordsSchema);

Keywords.find( {}, (err, size) => {

    if ( err )
        return;

});

module.exports = Keywords;
