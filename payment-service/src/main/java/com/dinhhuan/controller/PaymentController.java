package com.dinhhuan.controller;

import com.dinhhuan.dto.PayUrlRequest;
import com.dinhhuan.dto.PaymentDto;
import com.dinhhuan.dto.enums.PaymentStatus;
import com.dinhhuan.producer.PaymentFailedProducer;
import com.dinhhuan.producer.PaymentSuccessProducer;
import com.dinhhuan.service.PaymentService;
import com.dinhhuan.service.impl.MoMoService;
import com.dinhhuan.util.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final MoMoService moMoService;
    private final PaymentSuccessProducer paymentSuccessProducer;
    private final PaymentFailedProducer paymentFailedProducer;
    @Value("${vnpay.hashSecret}")
    private String vnp_HashSecret;
    @Value("${frontend.redirect}")
    private String frontendRedirect;
    @RequestMapping(value = "/generate-url", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> getPayUrl(@RequestBody PayUrlRequest request) {
        var url = paymentService.getPaymentUrl(request.getOrderId(), request.getAmount(), request.getPaymentMethod());
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
    public RedirectView getReturnPayments(HttpServletRequest request){
        Map<String, String> fields = new TreeMap<>();
        request.getParameterMap().forEach((key, value) -> {
            if (value.length > 0) fields.put(key, value[0]);
        });
        String secureHash = fields.remove("vnp_SecureHash");
        String signValue = VNPayUtil.hashAllFields(fields, vnp_HashSecret);
        if (!secureHash.equals(signValue)) {
            return new RedirectView(frontendRedirect);
        }
        String responseCode = fields.get("vnp_ResponseCode");
        String orderId = fields.get("vnp_OrderInfo");
        if ("00".equals(responseCode)) {
            var p = paymentService.getPayment(Long.parseLong(orderId));
            if(p == null){
                return new RedirectView(frontendRedirect);
            }
            p.setStatus(PaymentStatus.PAID);
            paymentService.updateStatus(p.getId(),p);
            System.out.println("thanh toan thanh cong");
            //event success
            paymentSuccessProducer.sendMessage(Long.parseLong(orderId));
            return new RedirectView(frontendRedirect);
        } else {
            var p = paymentService.getPayment(Long.parseLong(orderId));
            if(p == null){
                return new RedirectView(frontendRedirect);
            }
            p.setStatus(PaymentStatus.CANCELLED);
            paymentService.updateStatus(p.getId(),p);
            //event
            paymentFailedProducer.sendMessage(Long.parseLong(orderId));
            return new RedirectView(frontendRedirect);
        }
    }

    @PostMapping("/momo-callback")
    public ResponseEntity<Map<String, Object>> momoCallback(@RequestBody Map<String, String> payload) {
        String signature = payload.get("signature");

        // Verify signature
        if (!moMoService.verifyCallback(payload, signature)) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("resultCode", 99);
            errorResponse.put("message", "Invalid signature");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        String resultCode = payload.get("resultCode");
        String orderId = payload.get("orderId");

        Map<String, Object> successResponse = new HashMap<>();
        successResponse.put("resultCode", 0);
        successResponse.put("message", "Success");

        if ("0".equals(resultCode)) { // 0 = Success for momo
            var p = paymentService.getPayment(Long.parseLong(orderId));
            if (p == null) {
                successResponse.put("message", "Order not found");
                return new ResponseEntity<>(successResponse, HttpStatus.NOT_FOUND);
            }
            p.setStatus(PaymentStatus.PAID);
            paymentService.updateStatus(p.getId(), p);

            // Send success event
            paymentSuccessProducer.sendMessage(Long.parseLong(orderId));
        } else {
            var p = paymentService.getPayment(Long.parseLong(orderId));
            if (p == null) {
                successResponse.put("message", "Order not found");
                return new ResponseEntity<>(successResponse, HttpStatus.NOT_FOUND);
            }
            p.setStatus(PaymentStatus.CANCELLED);
            paymentService.updateStatus(p.getId(), p);

            // Send failed event
            paymentFailedProducer.sendMessage(Long.parseLong(orderId));
        }

        return ResponseEntity.ok(successResponse);
    }
}
