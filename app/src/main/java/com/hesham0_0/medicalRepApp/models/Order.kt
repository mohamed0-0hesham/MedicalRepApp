package com.hesham0_0.medicalRepApp.models

class Order {
    var id: String? = null
    var num: Int? = null
    var productId: String? = null
    var unitPrice:String?=null
    var discount: Int? = null

    constructor(){
    }

    constructor(id: String?, num: Int?, productId: String?, unitPrice: String?, discount: Int?) {
        this.id = id
        this.num = num
        this.productId = productId
        this.unitPrice = unitPrice
        this.discount = discount
    }

}