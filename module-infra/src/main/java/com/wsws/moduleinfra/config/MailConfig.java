package com.wsws.moduleinfra.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class MailConfig {


    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private boolean auth;


    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private boolean startTlsEnable;


    @Bean
    public JavaMailSender javaMailService() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(host);
        javaMailSender.setPort(port);
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);

        Properties props = javaMailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", startTlsEnable);
        props.put("mail.smtp.connectiontimeout", "5000"); // 연결 타임아웃 (ms)
        props.put("mail.smtp.timeout", "5000");          // 응답 타임아웃 (ms)
        props.put("mail.smtp.writetimeout", "5000");     // 쓰기 타임아웃 (ms)
        props.put("mail.smtp.ssl.trust", host);          // SSL 신뢰 호스트

        javaMailSender.setJavaMailProperties(props);
        javaMailSender.setDefaultEncoding("UTF-8");
        return javaMailSender;
    }

}
