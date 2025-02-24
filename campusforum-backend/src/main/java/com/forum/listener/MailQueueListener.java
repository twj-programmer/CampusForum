package com.forum.listener;

import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "mail")
public class MailQueueListener {

    @Resource
    JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    String username;

    @RabbitHandler
    public void sendMailMessage(Map<String, Object> data) {
        String email = (String) data.get("email");
        Integer code = (Integer) data.get("code");
        String type = (String) data.get("type");
        SimpleMailMessage message = switch (type) {
            case "register" -> createMessage("注册验证码", "您的验证码是：" + code, email);
            case "reset" -> createMessage("重置密码", "您的验证码是：" + code, email);
            case "modify" -> createMessage("您的邮件修改验证邮箱",
                    "您好，您正在绑定新的邮箱，验证码" + code + "，有效时间3分钟，如非本人操作，请无视。", email);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
        mailSender.send(message);
    }

    private SimpleMailMessage createMessage(String title, String context, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(username);
        message.setTo(email);
        message.setSubject(title);
        message.setText(context);
        return message;
    }
}
