package com.example.mymovies.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.function.DoubleBinaryOperator

@Entity(tableName = "movies")
data class MovieItem(
    @PrimaryKey
    @ColumnInfo(name = "id")
    @Expose
    var id : Int,

    @ColumnInfo(name = "title")
    @Expose
    var title: String,

    @ColumnInfo(name = "plot")
    @SerializedName("overview")
    @Expose
    var plot : String,

    @ColumnInfo(name = "year")
    @SerializedName("release_date")
    @Expose
    var year : String,

    @ColumnInfo(name = "movie length")
    @SerializedName("runtime")
    @Expose
    var length: Int,

    @ColumnInfo(name = "photo URL")
    @SerializedName("backdrop_path")
    @Expose
    var photo: String?,

    @ColumnInfo(name = "rating")
    @SerializedName("vote_average")
    @Expose
    var rating: Double,

    @ColumnInfo(name = "notes")
    var notes: String?,

    @ColumnInfo(name = "favorite")
    var isFav : Boolean = false)
{
}