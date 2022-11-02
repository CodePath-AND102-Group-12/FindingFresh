package com.cpg12.findingfresh.database

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint
import java.lang.ref.Reference

data class Markets(
    // these are the current ones used to move to Realtime database
    var marketName : String ? = null,
    var marketLocation : String ? = null,
    var marketEmail : String ? = null,
    var marketDescription: String ? = null,
    //TODO: using a datetime object instead of string?
    var marketOpenTime: String ? = null,
    var marketCloseTime: String ? = null,
//    var image: DocumentReference ? = null,
    var sunday: Boolean ? = null,
    var monday: Boolean ? = null,
    var tuesday: Boolean ? = null,
    var wednesday: Boolean ? = null,
    var thursday: Boolean ? = null,
    var friday: Boolean ? = null,
    var saturday: Boolean ? = null,

    // these are all that is currently kept in Firestore
//    var address: String ? = null,
//    var category: String ? = null,
//    var image: Reference<String> ? = null,
//    var location: GeoPoint ? = null,
//    var name: String ? = null,
//    var phone: String ? = null
)