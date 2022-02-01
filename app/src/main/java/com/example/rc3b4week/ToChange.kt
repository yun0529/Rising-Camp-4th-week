package com.example.rc3b4week

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class ToChange{
    @PrimaryKey(autoGenerate = true) var id: Int = 0
    var image: Int = 0
    var bulletImage : Int = 0

    constructor(image: Int, bulletImage: Int) {
        this.image = image
        this.bulletImage = bulletImage
    }

    @JvmName("getId1")
    fun getId(): Int {
        return id
    }
    @JvmName("setId1")
    fun setId(id:Int){
        this.id = id
    }

    @JvmName("getImage1")
    fun getImage(): Int {
        return image
    }

    @JvmName("setImage1")
    fun setImage(image: Int){
        this.image
    }

    @JvmName("getBulletImage1")
    fun getBulletImage(): Int {
        return bulletImage
    }

    @JvmName("setBulletImage1")
    fun setBulletImage(bulletImage: Int){
        this.bulletImage
    }

    override fun toString(): String {
        return "ToChange(id=$id, image=$image, bulletImage=$bulletImage)"
    }


}