var mongoose = require('mongoose');

var NeighborhoodSchema = new mongoose.Schema({

    name: {
        type: String,
        required: true,
        unique: true
    }

});

var Neighborhood = mongoose.model('Neighborhood', NeighborhoodSchema);

module.exports = Neighborhood;
