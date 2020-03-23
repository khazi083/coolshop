package com.coolshop.demo.Model;

import android.util.Patterns;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class LoginUser {

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("password")
    @Expose
    private String password;

    public LoginUser(String EmailAddress, String Password) {
        email = EmailAddress;
        password = Password;
    }

    public String getStrEmailAddress() {
        return email;
    }

    public String getStrPassword() {
        return password;
    }

    public boolean isEmailValid() {
        return Patterns.EMAIL_ADDRESS.matcher(getStrEmailAddress()).matches();
    }

    public boolean isPasswordLengthGreaterThan4() {
        return getStrPassword().length() >=4;
    }


    public boolean isvalid(){
        if(!email.isEmpty() && !password.isEmpty()){
            if(Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length()>=4)
                return  true;
        }
        return false;
    }
}
