package com.misnadqasim.prayertime.network

import com.misnadqasim.prayertime.models.TimingByCity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {

//    http://api.aladhan.com/v1/timingsByCity?city=Dubai&country=United Arab Emirates&method=8

    @GET("timingsByCity")
    suspend fun getTimingByCity(
        @Query("city") city: String,
        @Query("country") country: String,
        @Query("method") method: Int
    ): Response<TimingByCity>

    @GET("timingsByCity/{date}")
    suspend fun getTimingByCity(
        @Path("date") date: String,
        @Query("city") city: String,
        @Query("country") country: String,
        @Query("method") method: Int
    ): Response<TimingByCity>


//    http://api.aladhan.com/v1/timings/17-07-2007?latitude=51.508515&longitude=-0.1254872&method=2

    @GET("timings")
    suspend fun getTimingByLocation(
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("method") method: Int
    )

    @GET("timings/{date}")
    suspend fun getTimingByLocation(
        @Path("date") date: String,
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("method") method: Int
    )

}
