package com.coolshop.demo.View;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.coolshop.demo.Model.AvatarResponse;
import com.coolshop.demo.Model.ProfileResponse;
import com.coolshop.demo.R;
import com.coolshop.demo.Util.MD5Util;
import com.coolshop.demo.Util.UserSharedPreferences;
import com.coolshop.demo.databinding.ProfileBinding;
import com.coolshop.demo.vm.ProfileViewModel;

import java.io.ByteArrayOutputStream;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class ProfileActivity extends AppCompatActivity {

    private ProfileViewModel profileViewModel;
    private ProfileBinding binding;
    private UserSharedPreferences preference;
    RequestOptions options;
    final int camerapic = 1;
    final int gallerypic = 2;
    final int mb = 1000000;
    private String profilepicBase64string = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.profile);
        binding.setProfileViewModel(profileViewModel);
        preference = new UserSharedPreferences(this);
        profileViewModel.getProdiledetails(preference.getUserId(), preference.getAccessToken());
        binding.emailid.setText(preference.getEmailId());
        binding.password.setText(preference.getPassword());
        options = new RequestOptions()
                .placeholder(R.drawable.blankimage)
                .centerCrop()
                .error(R.drawable.blankimage);
        binding.profileimgLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    callPermission();
                else if (ActivityCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    callPermission();
                } else
                    selectImage(ProfileActivity.this);
            }
        });

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preference.clearSession();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                finish();
            }
        });

        profileViewModel.ProfileResponse().observe(this, new Observer<ProfileResponse>() {
            @Override
            public void onChanged(@Nullable ProfileResponse response) {
                if (response != null) {
                    if (response.getEmail() != null && !response.getEmail().isEmpty())
                        binding.emailid.setText(response.getEmail());
                    if (response.getAvatar_url() != null && !response.getAvatar_url().isEmpty()) {
                        Glide.with(ProfileActivity.this).load(response.getAvatar_url())
                                .apply(options)
                                .skipMemoryCache(true) //2
                                .override(1600, 1600)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .into(binding.profileImage);
                    } else {

                        Glide.with(ProfileActivity.this).load("https://www.gravatar.com/avatar/" + MD5Util.md5Hex(response.getEmail())+ "?s=150" + "&d=404")
                                .apply(options)
                                .skipMemoryCache(true) //2
                                .override(500, 500)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .into(binding.profileImage);
                    }
                }
            }
        });


        profileViewModel.updateAvatar().observe(this, new Observer<AvatarResponse>() {
            @Override
            public void onChanged(@Nullable AvatarResponse response) {
                if (response != null) {
                    if (response.getAvatar_url() != null && !response.getAvatar_url().isEmpty())
                        Glide.with(ProfileActivity.this).load(response.getAvatar_url())
                                .apply(options)
                                .skipMemoryCache(true) //2
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .into(binding.profileImage);
                }
            }
        });

    }

    private void selectImage(Context context) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, camerapic);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, gallerypic);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void callPermission() {
        ActivityCompat.requestPermissions(ProfileActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                4);
    }


    private String getbase64string(Bitmap bm) {

        String result = "";
        int bytesize = 0;
        int qty = 100;
        do {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, qty, baos);
            byte[] imageBytes = baos.toByteArray();
            bytesize = imageBytes.length;
            result = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            Log.d("coolshop", "bytesize" + String.valueOf(bytesize));
            Log.d("coolshop", "Base64size" + result.length());
            qty = qty - 10;
        }
        while (result.length() > mb);
        return result;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case camerapic:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        profilepicBase64string = getbase64string(selectedImage);
                        profileViewModel.updateAvatar(profilepicBase64string, preference.getUserId(), preference.getAccessToken());
//                      binding.profileImage.setImageBitmap(selectedImage);
                    }

                    break;
                case gallerypic:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                //                               binding.profileImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                profilepicBase64string = getbase64string(BitmapFactory.decodeFile(picturePath));
                                profileViewModel.updateAvatar(profilepicBase64string, preference.getUserId(), preference.getAccessToken());
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }
}
