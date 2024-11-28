package com.wsws.moduleinfra.storage;

import com.wsws.modulecommon.service.FileStorageService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class S3FileStorageService implements FileStorageService {

    @Override
    public String saveFile(MultipartFile file) {
        return ""; // 여기에 https://s3.amazonaws.com/bucket이름/~~~ 이런식으로
    }
}
