var mongoose = require('mongoose');

var KeywordsSchema = new mongoose.Schema({

    keyword: {
        type: String,
        unique: true,
        required: true
    },
    blacklist: {
        type: Boolean,
        required: true
    }
});

var Keywords = mongoose.model('Keywords', KeywordsSchema);

module.exports = Keywords;
