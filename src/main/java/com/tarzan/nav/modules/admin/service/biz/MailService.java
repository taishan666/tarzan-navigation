package com.tarzan.nav.modules.admin.service.biz;

import lombok.extern.slf4j.Slf4j;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


/**
 * @author tarzan
 */
@Component("mailService")
@Slf4j
public class MailService {

	public static StringTemplateGroup templateGroup;
	
	static{
		String classpath = MailService.class.getClassLoader().getResource("").getPath();
		templateGroup = new StringTemplateGroup("mailTemplates", classpath + "/mailTemplates");
	}
	
	public static String IMG_BASE_URL;
	public static String ACTIVATE_CONTEXT="http:";
	public static String RESET_PWD_CONTEXT;

	@Value("${spring.mail.username}")
	private String username;
    @Resource
    private JavaMailSender mailSender;


    private void sendMail(String to, String subject, String body) {
    	MimeMessage mail = mailSender.createMimeMessage();	
    	try {
    		MimeMessageHelper helper = new MimeMessageHelper(mail, true, "utf-8");
			helper.setFrom(username);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(body, true);
			mailSender.send(mail);
		} catch (MessagingException e) {
			log.error(e.getMessage());
		}
    }
    
    /**
     * send activation mail to
     * @param to
     */
    public void sendAccountActivationEmail(String to, String key){
    	StringTemplate activation_temp = templateGroup.getInstanceOf("activation");
    	activation_temp.setAttribute("img_base_url", IMG_BASE_URL);
    	activation_temp.setAttribute("email", to);
    	activation_temp.setAttribute("href", ACTIVATE_CONTEXT+key+"?email="+to);
    	activation_temp.setAttribute("link", ACTIVATE_CONTEXT+key+"?email="+to);
    	sendMail(to, "OSF账户激活", activation_temp.toString());
    }

	@Async
	public void sendEmailCode(String to, String code){
		StringTemplate activation_temp = templateGroup.getInstanceOf("activation");
		activation_temp.setAttribute("img_base_url", IMG_BASE_URL);
		activation_temp.setAttribute("email", to);
		activation_temp.setAttribute("href", code);
		activation_temp.setAttribute("link", code);
		sendMail(to, "OSF账户激活", activation_temp.toString());
	}
    
    /**
     * send change password link to
     * @param to
     */
    public void sendResetPwdEmail(String to, String key){
    	StringTemplate activation_temp = templateGroup.getInstanceOf("resetpwd");
    	activation_temp.setAttribute("img_base_url", IMG_BASE_URL);
    	activation_temp.setAttribute("href", RESET_PWD_CONTEXT+"?key="+key+"&email="+to);
    	activation_temp.setAttribute("link", RESET_PWD_CONTEXT+"?key="+key+"&email="+to);
    	sendMail(to, "OSF账户密码重置", activation_temp.toString());
    }
}
