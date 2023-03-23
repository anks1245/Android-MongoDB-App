package com.android.mongoapp


import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId

open class Student() : RealmObject() {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var name: String = ""
    var email: String = ""
    constructor(email: String,name:String) : this() {
        this.email = email
        this.name = name
    }
}