var mongoose = require('mongoose');
const ObjectId = mongoose.Schema.Types.ObjectId;

var RepositorySchema = new mongoose.Schema({

    link : {
        type: String,
        required: true,
        unique: true,
        ref:'Links'
    },
    neighborhood: {
        type: String,
        required: true,
        ref:'Neighborhood'
    },
    keywords: [
        {
        type: String,
        ref: 'Keywords'
        }
    ] 
});

var Repository = mongoose.model('Repository', RepositorySchema);

module.exports = Repository;