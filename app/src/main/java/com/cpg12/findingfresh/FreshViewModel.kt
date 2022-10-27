package com.cpg12.findingfresh

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cpg12.findingfresh.objects.Market

class FreshViewModel(): ViewModel() {

    private var _id = MutableLiveData<Long>()
    val id: LiveData<Long> = _id
    fun setIdData(passId: Long) {
        _id.value = passId
    }

    private var _name = MutableLiveData<String>()
    val name: LiveData<String> = _name
    fun setNameData(passName: String) {
        _name.value = passName
    }

    private var _market = MutableLiveData<Market>()
    val market: LiveData<Market> = _market
    fun setMarketData(passMarket: Market) {
        _market.value = passMarket
    }
}