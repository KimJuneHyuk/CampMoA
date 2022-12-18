package com.example.campmoa.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Component
public class MailComponent {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Autowired
    public MailComponent(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

//    오버로딩....
    public void sendHtml (String form, String to, String subject, String viewName) throws MessagingException {
        this.sendHtml(form, to, subject, viewName, new Context());
    }
    public void sendHtml(String form, String to, String subject, String viewName, Context context) throws MessagingException {
        MimeMessage mm = this.mailSender.createMimeMessage();
        MimeMessageHelper mmh = new MimeMessageHelper(mm, true);
//        MimeMessageHelper(); >> 예외처리.(MessagingException)
        mmh.setFrom(form);
        mmh.setTo(to);
        mmh.setSubject(subject);
        mmh.setText(this.templateEngine.process(viewName, context), true);
//        메일을 보낼떄 나용을 담고 html 코드 허용.
        this.mailSender.send(mm);
    }
}
