package com.huynhtrongman.masoi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.huynhtrongman.masoi.model.DiaLog;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Login extends AppCompatActivity {
    ViewPager viewPager;
    TextView txt_sign_in,txt_sign_up;
    int Layouts []= {R.layout.sign_in,R.layout.sign_up};
    LinearLayout but_google;
    com.facebook.login.widget.LoginButton but_facebook;
    LinearLayout but_sms;
    RelativeLayout group_sign_in;
    View view_animation;
    View view_sms;
    LinearLayout container_login;
    CallbackManager mCallbackManager;
    GoogleSignInClient mGoogleSignInClient;
    final static int RC_SIGN_IN = 123;
    final static String TAG = "Login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Init
        viewPager = findViewById(R.id.view_pager);
        txt_sign_in = findViewById(R.id.txt_sign_in);
        txt_sign_up = findViewById(R.id.txt_sign_up);
        but_google = findViewById(R.id.but_google);
        but_facebook = findViewById(R.id.but_facebook);
        but_sms = findViewById(R.id.but_sms);
        view_animation = findViewById(R.id.view_animation);
        container_login = findViewById(R.id.container_login);
        view_sms = findViewById(R.id.view_sms);
        // Init Auth
        Data.mAuth  = FirebaseAuth.getInstance();
        Data.mAuth.useAppLanguage();

        // Setting
        LoginViewPageradapter loginViewPageradapter  = new LoginViewPageradapter(getApplicationContext(),Layouts, Login.this);
        viewPager.setAdapter(loginViewPageradapter);
        viewPager.setClickable(false);
        viewPager.setVisibility(View.GONE);
        container_login.setVisibility(View.INVISIBLE);
        Event_Click();

        // Init Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Init Facebook
        mCallbackManager = CallbackManager.Factory.create();

     //   but_facebook.setReadPermissions("email", "public_profile");
        but_facebook.setLoginText("Đăng Nhập Với Facebook");

        but_facebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });



        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.huynhtrongman.masoi",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        Data.mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = Data.mAuth.getCurrentUser();
                            startActivity(new Intent(Login.this,Fill_In_Profile.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Lỗi Đăng Nhập",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }


    private void Event_Click() {
        txt_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
            }
        });
        txt_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(0);
            }
        });

        but_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
        but_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setY(-100f);
                view_sms.setVisibility(View.INVISIBLE);
                view_animation.setScaleX(0f);
                view_animation.setScaleY(0f);
                viewPager.setVisibility(View.VISIBLE);
                view_animation.setVisibility(View.VISIBLE);
                ViewCompat.animate(viewPager).translationY(100f).setDuration(1000).start();
                ViewCompat.animate(view_animation).scaleX(20f).setDuration(1500).start();
                ViewCompat.animate(view_animation).scaleY(40f).setDuration(1500).start();
            }
        });
        but_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_sms.setScaleX(0f);
                view_sms.setScaleY(0f);
                viewPager.setVisibility(View.VISIBLE);
                view_sms.setVisibility(View.VISIBLE);
                container_login.setVisibility(View.VISIBLE);
                ViewCompat.animate(viewPager).translationY(100f).setDuration(1000).start();
                ViewCompat.animate(view_sms).scaleX(20f).setDuration(1500).start();
                ViewCompat.animate(view_sms).scaleY(40f).setDuration(1500).start();
            }
        });
    }

    @Override
    public void onBackPressed() {
        new DiaLog(getApplicationContext(),"Bạn Có Muốn Đăng Xuất Không ?");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // FaceBook
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        //Google
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check Người Dùng Đã Đăng Nhập Lần Nào Chưa .
        FirebaseUser currentUser = Data.mAuth.getCurrentUser();
        if(currentUser!=null)
        {
           Data.user = currentUser;
           startActivity(new Intent(Login.this,Fill_In_Profile.class));
           finish();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        Data.mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = Data.mAuth.getCurrentUser();
                            startActivity(new Intent(Login.this,Fill_In_Profile.class));
                            finish();
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.but_google), "Đăng Nhập Lỗi.", Snackbar.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }
}
