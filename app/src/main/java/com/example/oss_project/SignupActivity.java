package com.example.oss_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity
{
    private FirebaseAuth mFirebaseAuth; //firebaseAuth 변수 선언하여 파이어베이스 인증 처리
    private DatabaseReference mDatabaseRef; //실시간 데이터베이스
    private EditText mEmail, mPwd; //회원가입 입력필드
    private Button mBtnSingup; //회원가입 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFirebaseAuth =FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Montak");

        mEmail = findViewById(R.id.email);
        mPwd = findViewById(R.id.pwd);
        mBtnSingup = findViewById(R.id.sign_button);

        mBtnSingup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //회원가입 버튼 클릭 될 때 처리를 시작하는 이벤트
                String strEmail = mEmail.getText().toString();
                String strPwd = mPwd.getText().toString();

                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);

                //Firebase Auth 진행
                //파이어베이스에서 유저 생성해준다
                mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) //인증처리 완료됐을 때
                    {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser(); //파이어베이스 유저를 가져온다

                            UserAccount account = new UserAccount();
                            account.setIdToken(firebaseUser.getUid());
                            account.setEmailId(firebaseUser.getEmail());
                            account.setPassword(strPwd);

                            //setValue() : databaset에 insert 행위
                            mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);

                            Toast.makeText(SignupActivity.this, "회원가입에 성공하셨습니다", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignupActivity.this, "회원가입에 실패하셨습니다", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}