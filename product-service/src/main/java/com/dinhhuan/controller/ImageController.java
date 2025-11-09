package com.dinhhuan.controller;

import com.dinhhuan.dto.OssImageDto;
import com.dinhhuan.dto.ProductSimpleDto;
import com.dinhhuan.service.OssBlobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {
    private final OssBlobService ossBlobService;
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> create(@RequestParam("file") MultipartFile file){
        Long id = ossBlobService.uploadFile(file);
        return ResponseEntity.ok(Map.of("id", String.valueOf(id)));
    }
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<OssImageDto>> getImages(
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
        Page<OssImageDto> imageList = ossBlobService.getImages(pageable);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Range", String.format("brands %d-%d/%d",
                (page - 1) * perPage,
                Math.min(page * perPage - 1, (int) imageList.getTotalElements() - 1),
                imageList.getTotalElements()
        ));
        return ResponseEntity.ok()
                .headers(headers)
                .body(imageList.getContent());
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, String>> deleteImages(@PathVariable Long id){
        ossBlobService.deletefile(id);
        return ResponseEntity.ok(Map.of("message", "success"));
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<OssImageDto> getImage(@PathVariable Long id){
        var img = ossBlobService.getImageById(id);
        if(img == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(img);
    }
}
