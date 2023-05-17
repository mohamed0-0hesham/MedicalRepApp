package com.hesham.medicalRepApp.models

import com.google.firebase.firestore.GeoPoint

class ReportModel {
    var id:String?=null
    var userId:String?=null
    var startTime:Long?=null
    var date:String?=null
    var startLocation:GeoPoint?=null
    var visits:List<String>?=null
    var endLocation:GeoPoint?=null
    var endTime:Long?=null

    constructor(){}
    constructor(
        id: String?,
        userId: String?,
        startTime: Long?,
        date: String?,
        startLocation: GeoPoint?,
        visits: List<String>?,
        endLocation: GeoPoint?,
        endTime: Long?
    ) {
        this.id = id
        this.userId = userId
        this.startTime = startTime
        this.date = date
        this.startLocation = startLocation
        this.visits = visits
        this.endLocation = endLocation
        this.endTime = endTime
    }


}