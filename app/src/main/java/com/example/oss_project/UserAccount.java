package com.example.oss_project;


/**
 * 사용자 계정 정보 모델 클라스
 */
public class UserAccount 
{
    private String idToken; //Firebase Uid
    private String emailId; //이메일 아이디
    private String password; //비밀번호
    
    public UserAccount(){ } //가장 먼저 호출, 파이어베이스는 여기 필수

    public String getIdToken() {
        return idToken;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
