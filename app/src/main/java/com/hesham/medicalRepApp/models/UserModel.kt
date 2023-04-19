package com.hesham.medicalRepApp.models

class UserModel {
    var id: String? = null
    var photoUrl: String? = null
    var name: String? = null
    var email: String? = null
    var phone: String? = null

    constructor() {

    }

    constructor(
        id: String,
        photoUrl: String?,
        name: String?,
        email: String,
        phone: String?
    ) {
        this.id = id
        this.photoUrl = photoUrl
        this.name = name
        this.email = email
        this.phone = phone
    }
}