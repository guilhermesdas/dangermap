var mongoose = require('mongoose');
var links = require('links_schema.js');

var RepositorySchema = new mongoose.Schema({

    
    neighborhood_id: {
        type: Number,
        
    }

});

var Repository = mongoose.model('Repository', RepositorySchema);

module.exports = Repository;