package com.coolshop.demo.repo;

import com.coolshop.demo.BuildConfig;
import com.coolshop.demo.Model.Avatar;
import com.coolshop.demo.Model.AvatarResponse;
import com.coolshop.demo.Model.LoginApiResponse;
import com.coolshop.demo.Model.LoginUser;
import com.coolshop.demo.Model.ProfileResponse;
import com.coolshop.demo.api.apiservice;
import androidx.lifecycle.MutableLiveData;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository {

    private apiservice service;
    private static Repository projectRepository;

    private Repository() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        service = retrofit.create(apiservice.class);
    }

    public synchronized static Repository getInstance() {
        //TODO No need to implement this singleton in Part #2 since Dagger will handle it ...
        if (projectRepository == null) {
            if (projectRepository == null) {
                projectRepository = new Repository();
            }
        }
        return projectRepository;
    }

    public MutableLiveData<LoginApiResponse> getLoginDetails(LoginUser user,final MutableLiveData<LoginApiResponse> logindata) {

        service.getLoginDetails(user).enqueue(new Callback<LoginApiResponse>() {
            @Override
            public void onResponse(Call<LoginApiResponse> call, Response<LoginApiResponse> response) {
                logindata.setValue(response.body());
              }

            @Override
            public void onFailure(Call<LoginApiResponse> call, Throwable t) {
                logindata.setValue(null);
            }
        });

        return logindata;
    }

    public MutableLiveData<ProfileResponse> getAccountDetails(String userid, String token,final MutableLiveData<ProfileResponse> profiledata) {
        service.getAccountDetails(userid,"Bearer "+token).enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                profiledata.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                profiledata.setValue(null);
            }
        });

        return profiledata;
    }


    public MutableLiveData<AvatarResponse> updateAvatarImg(String avatarbase64,String userid, String token,final MutableLiveData<AvatarResponse> avatarresp) {
        Avatar av=new Avatar();
        av.setAvatar(avatarbase64);


        service.updateAvatar(av,userid,"Bearer "+token).enqueue(new Callback<AvatarResponse>() {
            @Override
            public void onResponse(Call<AvatarResponse> call, Response<AvatarResponse> response) {
                avatarresp.setValue(response.body());
            }

            @Override
            public void onFailure(Call<AvatarResponse> call, Throwable t) {
                avatarresp.setValue(null);
            }
        });

        return avatarresp;
    }

}
