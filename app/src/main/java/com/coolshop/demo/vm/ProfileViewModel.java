package com.coolshop.demo.vm;

import android.app.Application;

import com.coolshop.demo.Model.AvatarResponse;
import com.coolshop.demo.Model.ProfileResponse;
import com.coolshop.demo.repo.Repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class ProfileViewModel extends AndroidViewModel {

    public MutableLiveData<ProfileResponse> profiledata= new MutableLiveData<>();
    public MutableLiveData<AvatarResponse> avatarresp= new MutableLiveData<>();


    public ProfileViewModel(@NonNull Application application) {
        super(application);
    }


    public void getProdiledetails(String userId, String accessToken){
      Repository.getInstance().getAccountDetails(userId,accessToken,profiledata);
    }

    public void updateAvatar(String avatarbase64,String userId, String accessToken){
        Repository.getInstance().updateAvatarImg(avatarbase64,userId,accessToken,avatarresp);
    }

    public MutableLiveData<ProfileResponse> ProfileResponse() {
        return profiledata;
    }

    public MutableLiveData<AvatarResponse> updateAvatar() {
        return avatarresp;
    }
}
