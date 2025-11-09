package com.dinhhuan.service.impl;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.baidu.fsg.uid.impl.DefaultUidGenerator;
import com.dinhhuan.dto.OssImageDto;
import com.dinhhuan.model.ImageBlob;
import com.dinhhuan.repository.ImageRepository;
import com.dinhhuan.service.OssBlobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OssBlobServiceImpl implements OssBlobService {
    private final ImageRepository imageRepository;
    private final DefaultUidGenerator uid;
    @Value("${azure.storage.account-name}")
    private String accountName;
    @Value("${azure.storage.account-key}")
    private String accountKey;
    @Value("${azure.storage.container-name}")
    private String containerName;
    private BlobContainerClient getContainerClient() {
        String connectionString = String.format(
                "DefaultEndpointsProtocol=https;AccountName=%s;AccountKey=%s;EndpointSuffix=core.windows.net",
                accountName, accountKey
        );

        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();

        return blobServiceClient.getBlobContainerClient(containerName);
    }

    @Override
    public Long uploadFile(MultipartFile file) {
        BlobContainerClient containerClient = getContainerClient();
        if (!containerClient.exists()) {
            return null;
        }
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
        try {
            BlobClient blobClient = containerClient.getBlobClient(fileName);
            blobClient.upload(file.getInputStream(), file.getSize(), true);
            ImageBlob image = new ImageBlob();
            image.setId(uid.getUID());
            image.setUrl(blobClient.getBlobUrl());
            image.setFileName(fileName);
            imageRepository.save(image);
            return image.getId();
        } catch (IOException ex){
            log.error(ex.getMessage());
        }
        return null;
    }

    @Override
    public boolean deletefile(Long id) {
        try {
            BlobContainerClient containerClient = getContainerClient();
            String blobName = imageRepository.findById(id).orElseThrow().getFileName();
            if(blobName == null){
                return false;
            }
            BlobClient blobClient = containerClient.getBlobClient(blobName);
            if (blobClient.exists()) {
                blobClient.delete();
                imageRepository.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error("Error deleting blob: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public Page<OssImageDto> getImages(Pageable pageable) {
        return imageRepository.findAll(pageable)
                .map(im -> OssImageDto.builder()
                        .id(im.getId())
                        .url(im.getUrl())
                        .build());
    }

    @Override
    public OssImageDto getImageById(Long id) {
        return imageRepository.findById(id)
                .map(i -> OssImageDto.builder()
                        .id(i.getId())
                        .url(i.getUrl())
                        .build())
                .orElse(null);
    }

}
