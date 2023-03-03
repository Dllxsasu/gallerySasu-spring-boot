package com.jeremias.dev.mail;



import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.jeremias.dev.exception.GalleryPhotosException;
import com.jeremias.dev.models.NotificationEmail;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {
	
	private final JavaMailSender mailSender;
    private final MailContentBuilder mailContentBuilder;
    //@ASYNC PARA ASINCRONIDAD
    @Async
  public  void sendMail(NotificationEmail notificationEmail) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("springreddit@email.com");
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());
            //Before messageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()));
// After
messageHelper.setText(notificationEmail.getBody());
        };
        try {
            mailSender.send(messagePreparator);
            log.info("Correo enviiado");
        } catch (MailException e) {
            throw new GalleryPhotosException("Un error ocurrio, al realizar un envio de correo" + notificationEmail.getRecipient());
        }
    }
}
