var mongoose = require('mongoose');

var LinkSchema = new mongoose.Schema({

    link: {
        type: String,
        required: true,
        unique: true,
        trim: true
    },
    isBaseURL: {
        type: Boolean,
        required: true
    },
    visitedOn: {
        type: Date,
        required: false
    }
});

var Link = mongoose.model('Links', LinkSchema);

module.exports = Link;