package com.tets.manhwa.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TetsApiService {
    @GET("/health")
    suspend fun health(): ApiEnvelope<Map<String, Any>>

    @GET("/api/sources")
    suspend fun sources(): ApiEnvelope<List<String>>

    @GET("/api/home")
    suspend fun home(
        @Query("source") source: String = "all",
        @Query("refresh") refresh: Boolean = false
    ): ApiEnvelope<HomeResponse>

    @GET("/api/list")
    suspend fun list(
        @Query("source") source: String = "all",
        @Query("page") page: Int = 1
    ): ApiEnvelope<List<ComicDto>>

    @GET("/api/search")
    suspend fun search(
        @Query("q") query: String,
        @Query("source") source: String = "all",
        @Query("page") page: Int = 1
    ): ApiEnvelope<List<ComicDto>>

    @GET("/api/genres")
    suspend fun genres(): ApiEnvelope<List<String>>

    @GET("/api/genre/{genre}")
    suspend fun genre(
        @Path("genre") genre: String,
        @Query("source") source: String = "all",
        @Query("page") page: Int = 1
    ): ApiEnvelope<List<ComicDto>>

    @GET("/api/type/{type}")
    suspend fun type(
        @Path("type") type: String,
        @Query("source") source: String = "all",
        @Query("page") page: Int = 1
    ): ApiEnvelope<List<ComicDto>>

    @GET("/api/comic/{source}/{id}")
    suspend fun comicDetail(
        @Path("source") source: String,
        @Path("id") id: String
    ): ApiEnvelope<ComicDetailDto>

    @GET("/api/comic/{source}/{id}/chapters")
    suspend fun chapters(
        @Path("source") source: String,
        @Path("id") id: String
    ): ApiEnvelope<List<ChapterDto>>

    @GET("/api/comic/{source}/{id}/chapter/{chapter}")
    suspend fun chapter(
        @Path("source") source: String,
        @Path("id") id: String,
        @Path("chapter") chapter: String
    ): ApiEnvelope<ChapterDto>
}
