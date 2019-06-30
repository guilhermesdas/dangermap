var mongoose = require('mongoose');
const ObjectId = mongoose.Schema.Types.ObjectId;

var RepositorySchema = new mongoose.Schema({

    link : {
        type: ObjectId,
        required: true,
        ref:'Links'
    },
    neighborhood: {
        type: ObjectId,
        required: true,
        ref:'Neighborhood'
    },
    keywords: [
        {
        type: ObjectId,
        ref: 'Keywords'
        }
    ] 
});

var Repository = mongoose.model('Repository', RepositorySchema);

module.exports = Repository;