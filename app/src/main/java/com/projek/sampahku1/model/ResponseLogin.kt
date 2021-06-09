package com.projek.sampahku1.model

import android.os.Parcel
import android.os.Parcelable

data class ResponseLogin(
    var username: String?,
    var id: String?,
    var fullname:String?,
    var email:String?
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(username)
        parcel.writeString(id)
        parcel.writeString(fullname)
        parcel.writeString(email)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ResponseLogin> {
        override fun createFromParcel(parcel: Parcel): ResponseLogin {
            return ResponseLogin(parcel)
        }

        override fun newArray(size: Int): Array<ResponseLogin?> {
            return arrayOfNulls(size)
        }
    }
}