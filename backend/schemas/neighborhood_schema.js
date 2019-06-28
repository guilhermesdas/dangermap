var mongoose = require('mongoose');

var NeighborhoodSchema = new mongoose.Schema({

    name: {
        type: String,
        required: true,
        unique: true
    },
    lat: {
        type: String,
        required: true
    },
    lng: {
        type: String,
        required: true
    }

});

var Neighborhood = mongoose.model('Neighborhood', NeighborhoodSchema);

module.exports = Neighborhood;
