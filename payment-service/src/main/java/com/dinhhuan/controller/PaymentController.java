package com.dinhhuan.controller;

import com.dinhhuan.dto.PayUrlRequest;
import com.dinhhuan.dto.PaymentDto;
import com.dinhhuan.dto.enums.PaymentStatus;
import com.dinhhuan.producer.PaymentFailedProducer;
import com.dinhhuan.producer.PaymentSuccessProducer;
import com.dinhhuan.service.PaymentService;
import com.dinhhuan.util.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final PaymentSuccessProducer paymentSuccessProducer;
    private final PaymentFailedProducer paymentFailedProducer;
    @Value("${vnpay.hashSecret}")
    private String vnp_HashSecret;
    @RequestMapping(value = "/generate-url", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> getPayUrl(@RequestBody PayUrlRequest request) {
        var url = paymentService.getPaymentUrl(request.getOrderId(), request.getAmount());
        if(url == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(Map.of("url", url), HttpStatus.OK);
    }
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<PaymentDto>> getAllPayments(){
        var ls = paymentService.getAllPayments();
        if(ls == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(ls);
    }
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ResponseEntity<List<PaymentDto>> getUserPayments(@RequestHeader("X-User-Id") String userId){
        var ls = paymentService.getAllUserPayments(Long.parseLong(userId));
        if(ls == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(ls);
    }
    @GetMapping("/return-callback")
    public ResponseEntity<String> getReturnPayments(HttpServletRequest request){
        Map<String, String> fields = new TreeMap<>();
        request.getParameterMap().forEach((key, value) -> {
            if (value.length > 0) fields.put(key, value[0]);
        });
        String secureHash = fields.remove("vnp_SecureHash");
        String signValue = VNPayUtil.hashAllFields(fields, vnp_HashSecret);
        if (!secureHash.equals(signValue)) {
            return ResponseEntity.badRequest().body("Invalid signature");
        }
        String responseCode = fields.get("vnp_ResponseCode");
        String orderId = fields.get("vnp_OrderInfo");
        if ("00".equals(responseCode)) {
            var p = paymentService.getPayment(Long.parseLong(orderId));
            if(p == null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            p.setStatus(PaymentStatus.PAID);
            paymentService.updateStatus(p.getId(),p);
            System.out.println("thanh toan thanh cong");
            //event success
            paymentSuccessProducer.sendMessage(Long.parseLong(orderId));
            return ResponseEntity.ok("Payment success");
        } else {
            var p = paymentService.getPayment(Long.parseLong(orderId));
            if(p == null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            p.setStatus(PaymentStatus.CANCELLED);
            paymentService.updateStatus(p.getId(),p);
            //event
            paymentFailedProducer.sendMessage(Long.parseLong(orderId));
            return ResponseEntity.ok("Payment failed: " + responseCode);
        }
    }
}
