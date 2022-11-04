package com.cpg12.findingfresh.objects

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint

data class Market(var name: String? = "",
                  var address: String? = "",
                  var phone: String? = "",
                  var category: String? = "",
                  var image: DocumentReference? = null,
                  var imageUrl: String? = null,
                  var location: GeoPoint? = null
    )