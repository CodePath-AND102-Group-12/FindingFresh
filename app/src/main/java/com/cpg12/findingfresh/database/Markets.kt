package com.cpg12.findingfresh.database

data class Markets(
    var marketName : String ? = null,
    var marketLocation : String ? = null,
    var marketEmail : String ? = null,
    var marketContactName: String ? = null,
    var marketCategory: String ? = null,
    var marketDescription: String ? = null,
    //TODO: using a datetime object instead of string?
    var marketDateTime: String ? = null
)