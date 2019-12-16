package com.huynhtrongman.masoi;

import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Data {
    public static FirebaseUser user;
    public static FirebaseAuth mAuth;
    public static String passAuth;
    public static SharedPreferences sharedPreferences;
    public static  SharedPreferences.Editor editSharedPrefereces;
}
