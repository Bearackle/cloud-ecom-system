package com.dinhhuan.controller;


import com.dinhhuan.dto.ProductSimpleDto;
import com.dinhhuan.dto.VariantCreation;
import com.dinhhuan.dto.VariantDto;
import com.dinhhuan.service.VariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/variants")
@RequiredArgsConstructor
public class VariantController {
    private final VariantService variantService;
    @RequestMapping(value = "",method = RequestMethod.POST)
    public ResponseEntity<Map<String,String>> create(@RequestBody VariantCreation variantCreation) {
        Long id = variantService.createVariant(variantCreation);
        if(id == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(Map.of("id", String.valueOf(id)));
    }
    @RequestMapping(value = "/product/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<VariantDto>> getByProduct(@PathVariable Long id) {
        List<VariantDto> variants = variantService.getVariantOfProductId(id);
        if(variants == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(variants);
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<VariantDto> get(@PathVariable Long id) {
        var variant = variantService.findVariantById(id);
        if(variant == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(variant);
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<VariantDto> update(@PathVariable Long id, @RequestBody VariantDto variantDto) {
        var variant = variantService.updateVariant(id, variantDto);
        if(variant == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(variant);
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        variantService.deleteVariant(id);
        return ResponseEntity.ok(Map.of("status", "deleted"));
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<VariantDto>> getListVariants(
            @RequestParam(name = "_page", defaultValue = "1") int page,
            @RequestParam(name = "_perPage", defaultValue = "10") int perPage,
            @RequestParam(name = "_sort", defaultValue = "id") String sort,
            @RequestParam(name = "_order", defaultValue = "ASC") String order
    )
    {
        Pageable pageable = PageRequest.of(
                page - 1,
                perPage,
                order.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC,
                sort
        );
        var variantList = variantService.getVariantList(pageable);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Range", String.format("brands %d-%d/%d",
                (page - 1) * perPage,
                Math.min(page * perPage - 1, (int) variantList.getTotalElements() - 1),
                variantList.getTotalElements()
        ));
        return ResponseEntity.ok()
                .headers(headers)
                .body(variantList.getContent());
    }
}
