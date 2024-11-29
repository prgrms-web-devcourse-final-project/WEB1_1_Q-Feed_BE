package com.wsws.moduleinfra.storage;

import com.wsws.modulecommon.service.FileStorageService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Component
public class S3FileStorageService implements FileStorageService {

    @Override
    public String saveFile(MultipartFile file) {
        return ""; // 여기에 https://s3.amazonaws.com/bucket이름/~~~ 이런식으로
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
