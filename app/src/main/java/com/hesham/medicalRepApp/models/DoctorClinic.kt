package com.hesham.medicalRepApp.models

class DoctorClinic {
    var doctorId:String?=null
    var center: String? = null
    var days: List<Int>? = null
    var city: String? = null
    var area: String? = null
    var location: List<Double>? = null

    constructor()

    constructor(
        doctorId:String?,
        center: String?,
        days: List<Int>?,
        city: String?,
        area: String?,
        location: List<Double>?
    ) {
        this.doctorId=doctorId
        this.center = center
        this.days = days
        this.city = city
        this.area = area
        this.location = location
    }
}