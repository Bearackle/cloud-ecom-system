package com.dinhhuan.controller;

import com.dinhhuan.common.Data;
import com.dinhhuan.dto.request.AddressRequest;
import com.dinhhuan.dto.response.AddressResponse;
import com.dinhhuan.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/addresses")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;
    private final RestClient.Builder builder;

    @PostMapping
    public ResponseEntity<Data<Map<String, Long>>> create(@RequestBody AddressRequest addressRequest) {
        Long id = addressService.createAddress(addressRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Data.<Map<String, Long>>builder()
                        .code(HttpStatus.CREATED.value())
                        .message("Address created successfully")
                        .data(Map.of("id", id))
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Data<AddressResponse>> get(@PathVariable Long id) {
        AddressResponse addressResponse = addressService.getAddressById(id);
        return ResponseEntity.ok(Data.<AddressResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Success")
                .data(addressResponse)
                .build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Data<List<AddressResponse>>> getByUser(@PathVariable Long userId) {
        List<AddressResponse> response = addressService.getAddressesByUserId(userId);
        return ResponseEntity.ok(Data.<List<AddressResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Success")
                .data(response)
                .build());
    }

    @GetMapping
    public ResponseEntity<Data<List<AddressResponse>>> getAll() {
        return ResponseEntity.ok(Data.<List<AddressResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Success")
                .data((List<AddressResponse>) addressService.getAllAddresses())
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Data<AddressResponse>> update(@PathVariable Long id, @RequestBody AddressRequest request) {
        return ResponseEntity.ok(Data.<AddressResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Address updated successfully")
                .data(addressService.updateAddress(id, request))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Data<Map<String, String>>> delete(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return ResponseEntity.ok(Data.<Map<String, String>>builder()
                .code(HttpStatus.OK.value())
                .message("Address deleted successfully")
                .data(Map.of("id", String.valueOf(id)))
                .build());
    }
}
