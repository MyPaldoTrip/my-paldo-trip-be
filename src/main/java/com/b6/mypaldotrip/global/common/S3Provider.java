package com.b6.mypaldotrip.global.common;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.b6.mypaldotrip.global.exception.GlobalException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class S3Provider {

    private final AmazonS3 amazonS3;
    private final String SEPARATOR = "/";

    @Value("${cloud.aws.s3.bucket.name}")
    public String bucket;

    @Value("${cloud.aws.s3.bucket.url}")
    private String url;

    private static ObjectMetadata setObjectMetadata(MultipartFile multipartFile) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());
        return metadata;
    }

    public String saveFile(MultipartFile multipartFile, String folderName) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }
        String originalFilename = multipartFile.getOriginalFilename();
        originalFilename =
                folderName
                        + SEPARATOR
                        + UUID.randomUUID()
                        + originalFilename.substring(originalFilename.lastIndexOf("."));
        createFolder(folderName);

        ObjectMetadata metadata = setObjectMetadata(multipartFile);

        amazonS3.putObject(bucket, originalFilename, multipartFile.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, originalFilename).toString();
    }

    private void createFolder(String folderName) {
        if (!amazonS3.doesObjectExist(bucket, folderName)) {
            amazonS3.putObject(
                    bucket,
                    folderName + SEPARATOR,
                    new ByteArrayInputStream(new byte[0]),
                    new ObjectMetadata());
        }
    }

    public <T extends BaseEntity> void deleteFile(T entity) {
        String originalFilename = getOriginalFilename(entity);

        validate(amazonS3, bucket, originalFilename);
        amazonS3.deleteObject(bucket, originalFilename);
    }

    public <T extends BaseEntity> String updateFile(T entity, MultipartFile multipartFile)
            throws IOException {
        String originalFilename = getOriginalFilename(entity);

        validate(amazonS3, bucket, originalFilename);
        ObjectMetadata metadata = setObjectMetadata(multipartFile);

        amazonS3.putObject(bucket, originalFilename, multipartFile.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, originalFilename).toString();
    }

    private <T extends BaseEntity> String getOriginalFilename(T entity) {
        if (entity.getFileURL() == null) {
            return null;
        }
        return entity.getFileURL().replace(url, "");
    }

    private void validate(AmazonS3 amazonS3, String bucket, String filename) {
        if (filename == null || !amazonS3.doesObjectExist(bucket, filename)) {
            throw new GlobalException(GlobalResultCode.NOT_FOUND_FILE);
        }
    }
}
