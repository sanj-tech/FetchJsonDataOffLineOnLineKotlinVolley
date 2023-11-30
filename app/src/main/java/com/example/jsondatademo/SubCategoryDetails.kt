package com.example.jsondatademo

import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity()
data class SubCategoryDetails(
    @PrimaryKey val id: Int,
    @SerializedName("name")
    val name: String,

    @SerializedName("banner_image")
    val bannerImage: String

)
