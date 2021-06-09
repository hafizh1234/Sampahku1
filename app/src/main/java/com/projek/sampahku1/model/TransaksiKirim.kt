package com.projek.sampahku1.model

import android.os.Parcel
import android.os.Parcelable

/*
"transaksi": [
{
    "_id": "60bfaece390186577c5b7a02",
    "invoice": "25749731",
    "tglTransaksi": "2021-09-03T17:00:00.000Z",
    "idUser": "60bc316a63cca20004e619cf",
    "namaTps": "TPS Cijerah",
    "namaKategori": "Sampah Kardus",
    "berat": "3",
    "imageUrl": "images/1623174862104.jpg"
},
{
    "_id": "60bfb121ac69065c38fa2f7f",
    "invoice": "2442135",
    "tglTransaksi": "2021-09-03T17:00:00.000Z",
    "idUser": "60bc316a63cca20004e619cf",
    "namaTps": "TPS Mana Aja",
    "namaKategori": "Sampah Botol",
    "berat": "3",
    "imageUrl": "images/1623175457274.jpg"
},
{
    "_id": "60c06158c696180004858040",
    "invoice": "6792636",
    "tglTransaksi": "2021-08-04T00:00:00.000Z",
    "idUser": "60bc316a63cca20004e619cf",
    "namaTps": "TPS Silaweh",
    "namaKategori": "Sampah Plastik",
    "berat": "1",
    "imageUrl": "images/1623220568557.png"
}
]
}

 */
data class TransaksiKirim (
    var _id:String?,
    var idUser:String?,
    var tglTransaksi: String?,
    var namaTps:String?,
    var namaKategori:String?,
    var berat:String?,
    var imageUrl:String?
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeString(idUser)
        parcel.writeString(tglTransaksi)
        parcel.writeString(namaTps)
        parcel.writeString(namaKategori)
        parcel.writeString(berat)
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TransaksiKirim> {
        override fun createFromParcel(parcel: Parcel): TransaksiKirim {
            return TransaksiKirim(parcel)
        }

        override fun newArray(size: Int): Array<TransaksiKirim?> {
            return arrayOfNulls(size)
        }
    }
}

