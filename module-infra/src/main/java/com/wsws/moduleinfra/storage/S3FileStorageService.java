package com.wsws.moduleinfra.storage;

import com.wsws.modulecommon.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3FileStorageService implements FileStorageService {

    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${spring.cloud.aws.region.static}")
    private String region;



    @Override
    public String saveFile(MultipartFile file) {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename(); // 고유 파일 이름 생성

        try {
            // S3에 파일 업로드
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName) // S3 버킷 이름
                            .key(fileName) // S3에 저장될 파일 경로
                            .build(),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()) // 파일 내용
            );

            // 업로드된 파일의 S3 URL 반환
            return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, fileName);
        } catch (IOException e) {
            throw FileUploadFailException.EXCEPTION;
        }
    }

    @Override
    public String saveFile(MultipartFile file, String type){
        switch (type) {
            case "image":
                return saveToS3(file, "images");
            case "audio":
                return saveToS3(file, "audios");
            default:
                throw new IllegalArgumentException("지원하지 않는 파일 타입입니다.");
        }
    }

    private String saveToS3(MultipartFile file, String folder) {
//        String fileName = folder + "/" + file.getOriginalFilename();
//        try (InputStream inputStream = file.getInputStream()) {
//            // S3에 파일을 업로드
//            amazonS3.putObject(new PutObjectRequest(bucketName, fileName, inputStream, null));
//
//            // S3에 업로드한 파일의 URL 반환
//            return "https://s3.amazonaws.com/" + bucketName + "/" + fileName;
//        } catch (AmazonServiceException e) {
//            throw new RuntimeException("파일을 S3에 업로드하는 중 오류가 발생했습니다.", e);
//        } catch (IOException e) {
//            throw new RuntimeException("파일을 읽는 중 오류가 발생했습니다.", e);
//        }
        return "";
    }
}
