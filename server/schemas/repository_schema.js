var mongoose = require('mongoose');
const ObjectId = mongoose.Schema.Types.ObjectId;

var RepositorySchema = new mongoose.Schema({

    link : {
        type: ObjectId,
        required: true,
        unique: true,
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
    ],
    brief: {
        type: String,
        required: false
    }
});

var Repository = mongoose.model('Repository', RepositorySchema);

module.exports = Repository;