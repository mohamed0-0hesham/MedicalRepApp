package com.hesham0_0.medicalRepApp.models

class UserModel {
    var id: String? = null
    var photoUrl: String? = null
    var name: String? = null
    var email: String? = null
    var phone: String? = null
    var jobTitle:String?=null
    var cities:List<String>?=null
    var teamId:String?=null
    var companyId:String?=null

    constructor(){

    }

    constructor(
        id: String?,
        photoUrl: String?,
        name: String?,
        email: String?,
        phone: String?,
        jobTitle: String?,
        cities: List<String>?,
        teamId: String?,
        companyId: String?
    ) {
        this.id = id
        this.photoUrl = photoUrl
        this.name = name
        this.email = email
        this.phone = phone
        this.jobTitle = jobTitle
        this.cities = cities
        this.teamId = teamId
        this.companyId = companyId
    }

}