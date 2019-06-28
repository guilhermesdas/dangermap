var mongoose = require('mongoose');

var NeighborhoodSchema = new mongoose.Schema({

    name: {
        type: String,
        required: true,
        unique: true
    },
    lat: {
        type: String,
        required: false
    },
    lng: {
        type: String,
        required: false
    }

});

var Neighborhood = mongoose.model('Neighborhood', NeighborhoodSchema);

module.exports = Neighborhood;
