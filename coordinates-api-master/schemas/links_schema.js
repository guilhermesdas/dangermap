var mongoose = require('mongoose');
var keywords = require('keywords_schema.js');
var neighborhood = require('neighborhood_schema.js')

var LinkSchema = new mongoose.Schema({

    link: {
        type: String,
        required: true,
        trim: true
    },
    isBaseURL: {
        type: Boolean,
        required: true
    },
    foundedKeywords_id: {
        type: [Number],
        required: true
    },
    visitedOn: {
        type: Date,
        required: true
    }
});

var Link = mongoose.model('Link', LinkSchema);

module.exports = Link;