package com.coolshop.demo.View;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.coolshop.demo.Model.LoginApiResponse;
import com.coolshop.demo.Model.LoginUser;
import com.coolshop.demo.R;
import com.coolshop.demo.Util.UserSharedPreferences;
import com.coolshop.demo.databinding.ActivityMainBinding;
import com.coolshop.demo.vm.LoginViewModel;

import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityMainBinding binding;
    private UserSharedPreferences Preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setLoginViewModel(loginViewModel);
        Preference = new UserSharedPreferences(this);

        if (!Preference.getAccessToken().isEmpty() && !Preference.getUserId().isEmpty()) {
            startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
            finish();
        }


        loginViewModel.getUser().observe(this, new Observer<LoginUser>() {
            @Override
            public void onChanged(@Nullable LoginUser loginUser) {
                if (TextUtils.isEmpty(Objects.requireNonNull(loginUser).getStrEmailAddress())) {
                    binding.txtPassword.setError(getString(R.string.validemail));
                    binding.txtPassword.requestFocus();
                }

                if (!loginUser.isEmailValid()) {
                    binding.txtEmailAddress.setError(getString(R.string.validemail));
                    binding.txtEmailAddress.requestFocus();
                } else if (TextUtils.isEmpty(Objects.requireNonNull(loginUser).getStrPassword())) {
                    binding.txtPassword.setError(getString(R.string.enter_password));
                    binding.txtPassword.requestFocus();
                } else if (!loginUser.isPasswordLengthGreaterThan4()) {
                    binding.txtPassword.setError(getString(R.string.validpass));
                    binding.txtPassword.requestFocus();
                }

            }
        });
        loginViewModel.loginResponse().observe(this, new Observer<LoginApiResponse>() {
            @Override
            public void onChanged(@Nullable LoginApiResponse response) {
                if (response!=null && response.getUserid() != null && response.getToken() != null) {
                    Preference.setAccessToken(response.getToken());
                    Preference.setUserId(response.getUserid());
                    Preference.setEmailId(binding.txtEmailAddress.toString());
                    startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                    finish();
                } else
                    Toast.makeText(LoginActivity.this, getString(R.string.api_error), Toast.LENGTH_LONG).show();

            }
        });
    }

}
