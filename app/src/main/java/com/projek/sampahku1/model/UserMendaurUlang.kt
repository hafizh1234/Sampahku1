package com.projek.sampahku1.model

/*{
    "message": "success booking",
    "transaksi": {
        "_id": "60c06158c696180004858040",
        "invoice": "6792636",
        "tglTransaksi": "2021-08-04T00:00:00.000Z",
        "idUser": "60bc316a63cca20004e619cf",
        "member": {
            "_id": "60bc316a63cca20004e619cf",
            "fullname": "Hafizh Haritsa",
            "username": "Hafizh",
            "email": "haf12h345@gmail.com"
        },
        "namaTps": "TPS Silaweh",
        "namaKategori": "Sampah Plastik",
        "berat": "1",
        "imageUrl": "images/1623220568557.png",
        "__v": 0
    }
}*/
data class UserMendaurUlang (
    var message:String?,
    var transaksi:TransaksiKirim
)