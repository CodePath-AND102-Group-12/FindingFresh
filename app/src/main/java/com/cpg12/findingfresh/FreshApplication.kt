package com.cpg12.findingfresh

import android.app.Application
import com.cpg12.findingfresh.database.FreshDatabase

class FreshApplication : Application() {
    val db by lazy { FreshDatabase.getInstance(this) }
}