package com.projek.sampahku1.model

import android.os.Parcel
import android.os.Parcelable

class RegistrationResponse(
    var username: String?,
    var fullname: String?,
    var email: String?,
    var password: String?,
    var _id: String?
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(username)
        parcel.writeString(fullname)
        parcel.writeString(email)
        parcel.writeString(password)
        parcel.writeString(_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RegistrationResponse> {
        override fun createFromParcel(parcel: Parcel): RegistrationResponse {
            return RegistrationResponse(parcel)
        }

        override fun newArray(size: Int): Array<RegistrationResponse?> {
            return arrayOfNulls(size)
        }
    }
}