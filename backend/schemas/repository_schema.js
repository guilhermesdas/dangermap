var mongoose = require('mongoose');
//var links_schema = require('./links_schema.js');
//var keywords_schema = require('./keywords_schema.js')
const ObjectId = mongoose.Schema.Types.ObjectId;

var RepositorySchema = new mongoose.Schema({

    links_id : {
        type: Number,
        required: true,
        unique: true
    },
    neighborhood_id: {
        type: ObjectId,
        required: true,
        ref:'Keywords'
    },
    foundedKeywords_ids:[{
        type: ObjectId,
        ref: 'Keywords'
    }] 
});

var Repository = mongoose.model('Repository', RepositorySchema);

module.exports = Repository;