package com.example.oss_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class LogoutActivity extends AppCompatActivity
{
    private FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        mFirebaseAuth = FirebaseAuth.getInstance();

        Button btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //로그아웃
                mFirebaseAuth.signOut();

                //로그아웃시 로그인창으로 이동
                Intent intent = new Intent(LogoutActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        //탈퇴 처리
        mFirebaseAuth.getCurrentUser().delete();

    }
}