package com.coolshop.demo.api;

import com.coolshop.demo.Model.Avatar;
import com.coolshop.demo.Model.AvatarResponse;
import com.coolshop.demo.Model.LoginApiResponse;
import com.coolshop.demo.Model.LoginUser;
import com.coolshop.demo.Model.ProfileResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface  apiservice {
    @POST("api/login")
    Call<LoginApiResponse> getLoginDetails(@Body LoginUser user);

    @GET("users/{userid} ")
    Call<ProfileResponse> getAccountDetails(@Path("userid") String userid, @Header("Authorization") String token);

    @POST("users/{userid}/avatar")
    Call<AvatarResponse> updateAvatar(@Body Avatar av,@Path("userid") String userid, @Header("Authorization") String token);
}
