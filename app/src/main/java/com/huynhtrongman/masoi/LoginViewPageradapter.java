package com.huynhtrongman.masoi;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.time.chrono.HijrahChronology;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class LoginViewPageradapter extends PagerAdapter {
    Context context;
    Login login;
    int mLayouts[];
    final String TAG = "Adapter_Login";
    EditText edt_user_sign_up;
    EditText edt_pass_sign_up;
    EditText edt_user_sign_in;
    EditText edt_pass_sign_in;
    RelativeLayout sms__veritify_group;
    RelativeLayout sign_up_group;
    Button but_sign_up;
    Button but_vertify_code;
    Button but_resend;
    CountDownTimer count_down_sms;
    int count_resend=60;
    EditText edt_sms1;
    EditText edt_sms2;
    EditText edt_sms3;
    EditText edt_sms4;
    EditText edt_sms5;
    EditText edt_sms6;
    String phone_valid;
    private PhoneAuthCredential credentialSMS;
    TextView cation_notify;
    ImageView but_back_sign_up;
    ImageView hide_or_visible;
    ImageView hide_or_visible_sign_in;
    private boolean isPassword=true;
    private boolean isPassword_sign_in=true;


    public LoginViewPageradapter(Context context, int[] mLayouts, Login login) {
        this.context = context;
        this.mLayouts = mLayouts;
        this.login = login;


    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(mLayouts[position], container, false);
        if(mLayouts[position]==(R.layout.sign_in))
        {
            // Đăng Nhập
            edt_user_sign_in = view.findViewById(R.id.user_sign_in);
           edt_pass_sign_in = view.findViewById(R.id.pass_sign_in);
            Button but_sign_in = view.findViewById(R.id.but_sign_in);
            hide_or_visible_sign_in = view.findViewById(R.id.hide_or_visible_sign_in);
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
              edt_user_sign_in.setText(Data.sharedPreferences.getString("Phone",""));
               edt_pass_sign_in.setText(Data.sharedPreferences.getString("Pass",""));
            } else {

            }
            hide_or_visible_sign_in.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isPassword_sign_in)
                    {
                        isPassword_sign_in = false;
                        hide_or_visible_sign_in.setImageResource(R.drawable.visible);
                        edt_pass_sign_in.setTransformationMethod(null);

                    }else
                    {
                        hide_or_visible_sign_in.setImageResource(R.drawable.hide);
                        isPassword_sign_in = true;
                        edt_pass_sign_in.setTransformationMethod(new PasswordTransformationMethod());
                    }
                }
            });


        }else
        {

            // Đăng Ký
            edt_user_sign_up = view.findViewById(R.id.user_sign_up);
            edt_pass_sign_up = view.findViewById(R.id.pass_sign_up);
            but_sign_up = view.findViewById(R.id.but_sign_up);
            sms__veritify_group = view.findViewById(R.id.group_veritify_sms);
            sign_up_group = view.findViewById(R.id.group_sign_up);
            sms__veritify_group.setVisibility(View.INVISIBLE);
            but_resend = view.findViewById(R.id.time_resend);
            but_vertify_code = view.findViewById(R.id.but_sign_up_verification);
            hide_or_visible = view.findViewById(R.id.hide_or_visible);

            edt_sms1 = view.findViewById(R.id.edt_sms1);
            edt_sms2 = view.findViewById(R.id.edt_sms2);
            edt_sms3 = view.findViewById(R.id.edt_sms3);
            edt_sms4 = view.findViewById(R.id.edt_sms4);
            edt_sms5 = view.findViewById(R.id.edt_sms5);
            edt_sms6 = view.findViewById(R.id.edt_sms6);
            cation_notify = view.findViewById(R.id.cation_notify);
            cation_notify.setVisibility(View.INVISIBLE);
            but_back_sign_up = view.findViewById(R.id.but_back_sign_up);

            // OnClick
            but_vertify_code.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(credentialSMS != null)
                    {
                        count_down_sms.cancel();
                        signInWithPhoneAuthCredential(credentialSMS);
                    }

                }
            });
            but_sign_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Data.passAuth = edt_pass_sign_up.getText().toString();
                            count_down_sms = new CountDownTimer(60000,1000) {
                        @Override
                        public void onTick(long l) {
                            count_resend--;
                            but_resend.setText("("+count_resend+")");
                        }

                        @Override
                        public void onFinish() {
                            but_resend.setText("Gửi Lại");
                        }
                    };
                    count_down_sms.start();
                    sign_up_group.setVisibility(View.INVISIBLE);
                    sms__veritify_group.setVisibility(View.VISIBLE);
                    Check_Login();


                }
            });
            but_back_sign_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sign_up_group.setVisibility(View.VISIBLE);
                    sms__veritify_group.setVisibility(View.INVISIBLE);
                }
            });

            hide_or_visible.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(isPassword)
                    {
                        isPassword = false;
                        hide_or_visible.setImageResource(R.drawable.visible);
                        edt_pass_sign_up.setTransformationMethod(null);
                    }else
                    {
                        isPassword = true;
                        edt_pass_sign_up.setTransformationMethod(new PasswordTransformationMethod());
                        hide_or_visible.setImageResource(R.drawable.hide);
                    }
                }
            });


        }
        container.addView(view);
        return view;
    }

    private void Check_Login() {
        // Kiểm Tra
        if(edt_pass_sign_up.getText().toString().equals("") || edt_user_sign_up.getText().toString().equals(""))
        {
            Toast.makeText(context,"Vui Lòng Điền Đầy Đủ",Toast.LENGTH_SHORT).show();
            return;
        }
        if(edt_pass_sign_up.getText().toString().length()<8)
        {
            Toast.makeText(context,"Mật Khẩu Phải Từ 8 Kí Tự Trở Lên",Toast.LENGTH_SHORT).show();
            edt_pass_sign_up.setText("");
            return;
        }
        char first_number = edt_user_sign_up.getText().toString().charAt(0);
        if(first_number!= '0' || edt_user_sign_up.getText().toString().length()<9 || edt_user_sign_up.getText().toString().length()>12)
        {
            Toast.makeText(context,"Mật Khẩu Không Hợp Lệ",Toast.LENGTH_SHORT).show();
            edt_user_sign_up.setText("");
            return;
        }
       phone_valid = "+84"+(edt_user_sign_up.getText().toString().substring(1,edt_user_sign_up.getText().length()));
        Send_Phone_Firebase( phone_valid);
        Toast.makeText(context,edt_user_sign_up.getText().toString(),Toast.LENGTH_SHORT).show();
    }

    private void Send_Phone_Firebase(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                login,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                        Log.d(TAG, "onVerificationCompleted:" + credential);
                        credentialSMS = credential;
                        if(credential.getSmsCode().length()>5)
                        {
                            edt_sms1.setText(credential.getSmsCode().charAt(0)+"");
                            edt_sms2.setText(credential.getSmsCode().charAt(1)+"");
                            edt_sms3.setText(credential.getSmsCode().charAt(2)+"");
                            edt_sms4.setText(credential.getSmsCode().charAt(3)+"");
                            edt_sms5.setText(credential.getSmsCode().charAt(4)+"");
                            edt_sms6.setText(credential.getSmsCode().charAt(5)+"");

                        }
                        signInWithPhoneAuthCredential(credential);
                        but_resend.setVisibility(View.INVISIBLE);



                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Log.d(TAG, "onVerificationFailed:" + e.toString());
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        Log.d(TAG, "onCodeSent:" + verificationId);
                        cation_notify.setVisibility(View.VISIBLE);
//                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, "123456");
//                        signInWithPhoneAuthCredential(credential);
                    }
                });
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        Data.mAuth.signInWithCredential(credential)
                .addOnCompleteListener(login, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(context,"Đăng Ký Thành Công",Toast.LENGTH_SHORT).show();
                            Data.user = task.getResult().getUser();
                            Data.editSharedPrefereces.putString("Phone", phone_valid);
                            Data.editSharedPrefereces.putString("Pass",edt_pass_sign_up.getText().toString());

                            login.startActivity(new Intent(login,Fill_In_Profile.class));
                            login.finish();
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.d(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(context,"Code Xác Nhận Không Đúng !",Toast.LENGTH_SHORT).show();
                                edt_sms1.setText("");
                                edt_sms2.setText("");
                                edt_sms3.setText("");
                                edt_sms4.setText("");
                                edt_sms5.setText("");
                                edt_sms6.setText("");
                            }
                        }
                    }
                });
    }

    @Override
    public int getCount() {
        return mLayouts.length;

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
