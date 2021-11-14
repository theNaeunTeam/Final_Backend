package com.douzone.final_backend.Common;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class S3Service {
    private String accessKey;
    private String secretKet;

    @PostConstruct
    private void setS3Client(){
        AWSCredentials credentials = new BasicAWSCredentials();

    }
}
