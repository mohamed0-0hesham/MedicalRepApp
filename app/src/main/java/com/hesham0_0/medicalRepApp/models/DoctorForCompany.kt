package com.hesham0_0.medicalRepApp.models

class DoctorForCompany {
    var doctorId: String? = null
    var days: List<Map<String,Int>>? = null
    var doctorData:DoctorModel?=null
    var visitsByMonth: Int? = null
    var lastVisit: Long? = null
    var products: List<String>? = null
    var notes: String? = null
    var visitsList:List<String>?=null
    var orderList:List<String>?=null
    var clinics:List<DoctorClinic>? = null
    var lastState:Int?=null
    var totalYearOrders:Int?=null

    constructor()
    constructor(
        doctorId: String?,
        days: List<Map<String, Int>>?,
        doctorData: DoctorModel?,
        visitsByMonth: Int?,
        lastVisit: Long?,
        products: List<String>?,
        notes: String?,
        visitsList: List<String>?,
        orderList: List<String>?,
        clinics: List<DoctorClinic>?,
        lastState: Int?,
        totalYearOrders: Int?
    ) {
        this.doctorId = doctorId
        this.days = days
        this.doctorData = doctorData
        this.visitsByMonth = visitsByMonth
        this.lastVisit = lastVisit
        this.products = products
        this.notes = notes
        this.visitsList = visitsList
        this.orderList = orderList
        this.clinics = clinics
        this.lastState = lastState
        this.totalYearOrders = totalYearOrders
    }
}