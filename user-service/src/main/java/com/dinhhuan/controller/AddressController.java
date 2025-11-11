package com.dinhhuan.controller;

import com.dinhhuan.dto.request.AddressCreationDto;
import com.dinhhuan.dto.request.AddressDto;
import com.dinhhuan.service.AddressService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/address")
public class AddressController {
    private final AddressService addressService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Map<String,String>> create(@RequestHeader("X-User-Id") String userId, @RequestBody AddressCreationDto creation){
        var id = addressService.create(creation, Long.parseLong(userId));
        if(id == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(Map.of("id", String.valueOf(id)));
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Map<String,String>> update(@PathVariable Long id, @RequestBody AddressDto address){
        addressService.update(id, address);
        return ResponseEntity.ok(Map.of("status", "address updated"));
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String,String>> delete(@PathVariable Long id){
        addressService.delete(id);
        return ResponseEntity.ok(Map.of("status", "address deleted"));
    }
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<AddressDto>> getList(@RequestHeader("X-User-Id") String userId){
        var userAddresses = addressService.getList(Long.parseLong(userId));
        if(userAddresses == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(userAddresses);
    }
}
