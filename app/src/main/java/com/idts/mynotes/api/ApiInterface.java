package com.idts.mynotes.api;

import com.idts.mynotes.model.Note;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("save.php")
    Call<Note> saveNote(
            @Field("title") String title,
            @Field("note") String note,
            @Field("color") int color
    );

    @GET("notes.php")
    Call<List<Note>> getNotes();

    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("update.php")
    Call<Note> updateNote(
            @Field("id") int id,
            @Field("title") String title,
            @Field("note") String note,
            @Field("color") int color
    );

    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("delete.php")
    Call<Note> deleteNote( @Field("id") int id );
}
