package com.example.rc3b4week

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ToChangeDao {

    @Query("SELECT * FROM ToChange")
    fun getAll(): List<ToChange>

    @Insert
    fun insert(toChange: ToChange)

    @Update
    fun update(toChange: ToChange)

    @Delete
    fun delete(toChange: ToChange)

}