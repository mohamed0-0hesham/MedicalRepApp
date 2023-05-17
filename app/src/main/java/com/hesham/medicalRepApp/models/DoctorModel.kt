package com.hesham.medicalRepApp.models

class DoctorModel {
    var id: String? = null
    var name: String? = null
    var specialty: String? = null
    var photoUrl:String?=null
    var phoneNum:String?=null
    var gender: String? = null
    var days: List<Int>? = null
    var city: String? = null
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
        id: String?,
        name: String?,
        specialty: String?,
        photoUrl: String?,
        phoneNum: String?,
        days: List<Int>?,
        city: String?,
        gender: String?,
        area: String?,
        location: List<Double>?,
        center: String?,
        visitsByMonth: Int?,
        lastVisit: String?,
        products: List<Int>?,
        notes: String?
    ) {
        this.id = id
        this.name = name
        this.specialty = specialty
        this.photoUrl = photoUrl
        this.phoneNum = phoneNum
        this.days = days
        this.city = city
        this.gender = gender
        this.area = area
        this.location = location
        this.center = center
        this.visitsByMonth = visitsByMonth
        this.lastVisit = lastVisit
        this.products = products
        this.notes = notes
    }

}
