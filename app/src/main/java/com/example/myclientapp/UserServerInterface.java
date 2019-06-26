package com.example.myclientapp;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserServerInterface {
    @GET("/users/{user_name}/token/")
    Call<TokenResponse> connectUser(@Path("user_name") String user_name);

    @GET("/user/")
    Call<UserResponse> getUserInfo(@Header("Authorization") String token);

    @Headers("Content-Type: application/json")
    @POST("/user/edit/")
    Call<UserResponse> EditUserName(@Header("Authorization") String token, @Body setUserPreNameRequest request);


}
