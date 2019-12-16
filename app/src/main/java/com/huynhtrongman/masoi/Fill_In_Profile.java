package com.huynhtrongman.masoi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

public class Fill_In_Profile extends AppCompatActivity {
    ImageView but_back_welcome;
    EditText txt_name_welcome;
    LottieAnimationView avatar;
    ImageView imageView_avatar;
    TextView txt_next;
    final  String TAG = "Fill_In_Profile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill__in__profile);
        LottieAnimationView avatar = findViewById(R.id.avatar);
        txt_name_welcome = findViewById(R.id.txt_name);
        imageView_avatar = findViewById(R.id.img_avatar);
        txt_next = findViewById(R.id.txt_next);
        avatar.setAnimation("iet.json");
        avatar.playAnimation();
        but_back_welcome = findViewById(R.id.but_back_welcome);
        but_back_welcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Fill_In_Profile.this,Login.class));
            }
        });
        try {
            if(Data.user.getDisplayName()!=null)
            {
                txt_name_welcome.setText(Data.user.getDisplayName());
            }
            txt_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Fill_In_Profile.this,MainActivity.class));
                }
            });
        }catch (Exception e)
        {

        }

    }
}
