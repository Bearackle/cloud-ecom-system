package com.dinhhuan.service;

import com.dinhhuan.dto.OssImageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface OssBlobService {
    Long uploadFile(MultipartFile file);
    boolean deletefile(Long id);
    Page<OssImageDto> getImages(Pageable pageable);
    OssImageDto getImageById(Long id);
}
