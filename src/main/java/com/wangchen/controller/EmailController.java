package com.wangchen.controller;

import com.wangchen.annotations.Log;
import com.wangchen.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/cart")
public class EmailController {

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/send")
    @Log
    public Result sendEmailAndBuy(@RequestBody Map<String, Object> requestData) {
        try {
            String email = (String) requestData.get("email");
            String address = (String) requestData.get("address");
            Map<String, Integer> cartItems = (Map<String, Integer>) requestData.get("cartItems");

            SimpleMailMessage message = new SimpleMailMessage();
            // 发件人
            message.setFrom("1278868754@qq.com");
            // 收件人
            message.setTo(email);
            // 邮件标题
            message.setSubject("订单确认邮件");

            // 构造邮件内容
            StringBuilder content = new StringBuilder("您购买了以下商品：\n");
            for (Map.Entry<String, Integer> entry : cartItems.entrySet()) {
                content.append(entry.getKey()).append(" 数量 ").append(entry.getValue()).append("\n");
            }
            content.append("总价是：").append(requestData.get("totalPrice")).append("\n");
            content.append("将配送至：").append(address);
            message.setText(content.toString());

            mailSender.send(message);
            return Result.success("成功发送确认邮件");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("发送邮件失败：" + e.getMessage());
        }
    }
}


//@RestController
//@RequestMapping("/cart")
//public class EmailController {
//
//    @Autowired
//    private JavaMailSender mailSender;
//    @PostMapping("/send")
//    public Result send(){
//
//        SimpleMailMessage message = new SimpleMailMessage();
//        // 发件人
//        message.setFrom("1278868754@qq.com");
//        // 收件人
//        message.setTo("1278868754@qq.com");
//        // 邮件标题
//        message.setSubject("Java发送邮件");
//        // 邮件内容
//        message.setText("你好，这是一条用于测试Spring Boot邮件发送功能的邮件！哈哈哈~~~");
//        // 抄送人
////        message.setCc("1278868754@qq.com");
//        mailSender.send(message);
//        return Result.success("成功发送确认邮件");
//    }
//}

