package com.cpg12.findingfresh.objects

import com.google.firebase.firestore.DocumentReference

data class Market(var name: String? = "",
             var address: String? = "",
             var phone: String? = "",
             var category: String? = "",
             var image: String? = ""
             //,var fbImage: DocumentReference? = null
    )