var mongoose = require('mongoose');
var LinkSchema = require('links_schema')

var FrontierSchema = new mongoose.Schema({

    baseurl: ObjectIds,
    urls: [ObjectIds]

});

var Frontier = mongoose.model('Frontier', FrontierSchema);

module.exports = Frontier;