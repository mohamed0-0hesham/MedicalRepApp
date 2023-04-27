package com.hesham.medicalRepApp.models

class ProductsModel {
    var id:String?=null
    var name:String?=null
    var normalPrice:Int?=null
    var minPrice:Int?=null
    var category:String?=null
    var description:String?=null

    constructor(){

    }

    constructor(
        id: String?,
        name: String?,
        normalPrice: Int?,
        minPrice: Int?,
        category: String?,
        description: String?
    ) {
        this.id = id
        this.name = name
        this.normalPrice = normalPrice
        this.minPrice = minPrice
        this.category = category
        this.description = description
    }


}