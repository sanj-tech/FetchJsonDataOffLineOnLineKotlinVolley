package com.example.jsondatademo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SubCategoryDetailsDao {
    @Query("SELECT * FROM SubCategoryDetails")
    fun getAllSubCategories(): List<SubCategoryDetails>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(subCategories: List<SubCategoryDetails>)
}