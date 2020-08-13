package com.example.runner;

import android.content.Context;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

public class SendMail extends AppCompatActivity {
    String user="junhyok153@gmail.com"; // 보내는 계정의 id
    String password="son3j22550!"; // 보내는 계정의 pw


    //계속 인터넷 연결이 되지 않았다. 원인은 google smtp 설정을 안해놔서 그렇다. 앱 허용을 하지 않았다.
    public void sendSecurityCode(Context context, String sendTo) {
        try {
            GMailSender gMailSender = new GMailSender(user, password);
            gMailSender.sendMail("<런닝앱 Runner> 임시 비밀번호","임시 비밀번호 :" + gMailSender.getEmailCode()+"\n임시 비밀번호로 로그인 후 설정에서 비밀번호를 수정하시기 바랍니다. ", sendTo);
            Toast.makeText(context, "이메일을 성공적으로 보냈습니다.", Toast.LENGTH_SHORT).show();
        } catch (SendFailedException e) {
            Toast.makeText(context, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
        } catch (MessagingException e) {
            Toast.makeText(context, "인터넷 연결을 확인해주십시오", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
