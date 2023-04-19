package com.hesham.medicalRepApp.models

class DoctorModel {
    var id: String? = null
    var name: String? = null
    var specialty: String? = null
    var phoneNum:String?=null
    var days: List<Int>? = null
    var city: String? = null
    var gender: String? = null
    var area: String? = null
    var location: List<Double>? = null
    var center: String? = null
    var visitsByMonth: Int? = null
    var lastVisit: String? = null
    var products: List<Int>? = null
    var notes: String? = null

    constructor() {

    }

    constructor(
        id: String,
        name: String,
        specialty: String,
        phoneNum:String,
        days: List<Int>,
        gender: String,
        location: List<Double>?,
        visitsByMonth: Int,
        city: String,
        area: String,
        center: String,
        products: List<Int>,
        notes: String,
        lastVisit: String
    ) {
        this.id = id
        this.name = name
        this.days = days
        this.specialty = specialty
        this.phoneNum=phoneNum
        this.gender = gender
        this.location = location
        this.visitsByMonth = visitsByMonth
        this.city = city
        this.area = area
        this.center = center
        this.products = products
        this.notes = notes
        this.lastVisit = lastVisit
    }
}
