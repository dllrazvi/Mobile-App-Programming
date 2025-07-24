package com.example.journal.client
import com.example.journal.model.Story
import retrofit2.Call
import retrofit2.http.*

interface StoryService {
    @GET("/stories/{id}")
    fun retrieveStory(@Path("id") id: Int) : Call<Story>
    @GET("/stories")
    fun retrieveAllStories() : Call<List<Story>>
    @DELETE("/stories/{id}")
    fun deleteStory(@Path("id") id: Long) : Call<Story>
    @POST("/stories")
    fun createStory(@Body story: Story) : Call<Story>
    @PUT("/stories/{id}")
    fun updateStory(@Path("id") id: Long, @Body story: Story) : Call<Story>
}