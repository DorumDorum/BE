package com.project.dorumdorum.global.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "aws.s3")
public class S3Properties {

    /**
     * 업로드/다운로드에 사용할 S3 버킷 이름
     */
    private String bucket;

    /**
     * 버킷이 위치한 리전(e.g. ap-northeast-2)
     */
    private String region;
}

