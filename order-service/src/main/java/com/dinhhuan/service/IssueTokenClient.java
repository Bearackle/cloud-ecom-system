package com.dinhhuan.service;


import com.dinhhuan.dto.PaymentUrlResponse;
import com.dinhhuan.dto.request.IssueTokensRequest;
import com.dinhhuan.dto.request.PaymentRequest;
import com.dinhhuan.dto.response.IssueTokensResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(name = "loyalty-service", url = "http://localhost:8308")
public interface IssueTokenClient {
    @PostMapping("/customers/0xa51ee91013e1acd77f838b2720ddb3391a1fdfea/tokens/issue")
    IssueTokensResponse giveToken(@RequestBody IssueTokensRequest request);
}
