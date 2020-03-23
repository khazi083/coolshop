package com.coolshop.demo.vm;

import android.app.Application;
import android.view.View;

import com.coolshop.demo.Model.LoginApiResponse;
import com.coolshop.demo.Model.LoginUser;
import com.coolshop.demo.repo.Repository;


import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;


public class LoginViewModel extends AndroidViewModel {

    public MutableLiveData<String> EmailAddress = new MutableLiveData<>();
    public MutableLiveData<String> Password = new MutableLiveData<>();
    public Integer isLoading=View.GONE;
    private MutableLiveData<LoginUser> userMutableLiveData=new MutableLiveData<>();
    public MutableLiveData<LoginApiResponse> loginresp= new MutableLiveData<>();

    public MutableLiveData<LoginUser> getUser() {

        if (userMutableLiveData == null) {
            userMutableLiveData = new MutableLiveData<>();
        }
        return userMutableLiveData;

    }

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public void onClick(View view) {

        isLoading=View.VISIBLE;
        LoginUser loginUser = new LoginUser(EmailAddress.getValue(),Password.getValue() );
        userMutableLiveData.setValue(loginUser);
        if(loginUser.isvalid())
            Repository.getInstance().getLoginDetails(loginUser,loginresp);
  }


    public MutableLiveData<LoginApiResponse> loginResponse() {
        return loginresp;
    }

}
