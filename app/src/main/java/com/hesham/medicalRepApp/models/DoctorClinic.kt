package com.hesham.medicalRepApp.models

class DoctorClinic {
    var center: String? = null
    var days: List<Int>? = null
    var city: String? = null
    var area: String? = null
    var location: List<Double>? = null

    constructor()

    constructor(
        center: String?,
        days: List<Int>?,
        city: String?,
        area: String?,
        location: List<Double>?
    ) {
        this.center = center
        this.days = days
        this.city = city
        this.area = area
        this.location = location
    }
}