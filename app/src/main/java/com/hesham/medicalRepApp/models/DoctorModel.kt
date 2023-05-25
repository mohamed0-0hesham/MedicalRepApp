package com.hesham.medicalRepApp.models

class DoctorModel {
    var id: String? = null
    var name: String? = null
    var specialty: String? = null
    var photoUrl:String?=null
    var phoneNum:String?=null
    var gender: String? = null
    var days: List<Map<String,Int>>? = null
    var clinics:List<DoctorClinic>? = null

    constructor() {

    }

    constructor(
        id: String?,
        name: String?,
        specialty: String?,
        photoUrl: String?,
        phoneNum: String?,
        gender: String?,
        days: List<Map<String, Int>>?,
        clinics: List<DoctorClinic>?
    ) {
        this.id = id
        this.name = name
        this.specialty = specialty
        this.photoUrl = photoUrl
        this.phoneNum = phoneNum
        this.gender = gender
        this.days = days
        this.clinics = clinics
    }

}
