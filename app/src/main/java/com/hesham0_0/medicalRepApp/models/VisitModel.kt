package com.hesham0_0.medicalRepApp.models

class VisitModel {
    var id: String? = null
    var to: String? = null
    var time: String? = null
    var abstract:String?=null
    var order: List<String>? = null
    var notes: String? = null

    constructor(){

    }

    constructor(
        id: String?,
        to: String?,
        time: String?,
        abstract: String?,
        order: List<String>?,
        notes: String?
    ) {
        this.id = id
        this.to = to
        this.time = time
        this.abstract = abstract
        this.order = order
        this.notes = notes
    }

}