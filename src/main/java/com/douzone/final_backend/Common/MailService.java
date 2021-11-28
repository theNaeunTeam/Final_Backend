package com.douzone.final_backend.Common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
@Slf4j
public class MailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMail(String email,String id, String pw){ //아이디, 암호화된 비밀번호, 링크에 연결해서 보내기
        MimeMessage message = javaMailSender.createMimeMessage();

        try{
            message.setSubject("판다오더에서 보내는 비밀번호 재설정 메일");

            String html = "<h1>비밀번호 재설정 메일</h1>" + "<a href=\"http://localhost:3000/findpw/"+id+"/"+pw+"\">비밀번호 재설정</a>";
            message.setText(html, "utf-8", "html");

            message.addRecipient(Message.RecipientType.TO,new InternetAddress(email));
            javaMailSender.send(message);

        }catch (Exception e){
            log.info("mail 전송 에러");
            log.info(e.getMessage());
        }


    }
}
