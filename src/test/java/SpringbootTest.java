import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.junit.jupiter.api.Test;
import javax.annotation.Resource;

public class SpringbootTest {
    @Autowired
    JavaMailSenderImpl javaMailSender;

    @Test
    public void sendmailtest(){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject("测试springboot发送邮件");//主题
        mailMessage.setText("这是springboot发送邮件的内容，你看看怎么样呢？是不是成功了？");//内容        mailMessage.setTo("xxxx@163.com");
        mailMessage.setFrom("1317542534@qq.com");
        javaMailSender.send(mailMessage);
    }

}
