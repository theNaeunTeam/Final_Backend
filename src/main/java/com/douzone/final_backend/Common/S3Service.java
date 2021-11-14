package com.douzone.final_backend.Common;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class S3Service {
    private AmazonS3 s3Client;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    private String bucket="thebetterteam-image";

    @Value("${cloud.aws.region.static}")
    private String region;

    @PostConstruct
    private void setS3Client(){
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey,this.secretKey);

        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
    }

    public String upload(MultipartFile file) throws IOException{
        String fileName = file.getOriginalFilename();

        s3Client.putObject(new PutObjectRequest(bucket,fileName,file.getInputStream(),null)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return s3Client.getUrl(bucket,fileName).toString();
    }
}
