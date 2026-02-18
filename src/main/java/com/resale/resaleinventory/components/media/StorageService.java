package com.resale.resaleinventory.components.media;

import com.ibm.cloud.objectstorage.services.s3.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3;



import java.util.ArrayList;
import java.util.List;

@Service
public class StorageService {

    @Autowired
    private AmazonS3 s3Client;


        @Value("${ibm.cos.bucket-name}")
        private String bucketName;

        @Value("${ibm.cos.endpoint-url}")
        private String endpointUrl;

        @Value("${ibm.cos.location}")
        private String location;


        public List<String> listObjectsByPrefix(String prefix) {

            List<String> keys = new ArrayList<>();

        ListObjectsV2Request request = new ListObjectsV2Request()
                .withBucketName(bucketName)
                .withPrefix(prefix)
                .withMaxKeys(1000);

        ListObjectsV2Result result;

        do {
            result = s3Client.listObjectsV2(request);

            for (S3ObjectSummary summary : result.getObjectSummaries()) {
                // Skip "folders"
                if (!summary.getKey().endsWith("/")) {
                    keys.add(summary.getKey());
                }
            }

            request.setContinuationToken(result.getNextContinuationToken());

        } while (result.isTruncated());

        return keys;
    }
    public String buildPublicUrl(String objectKey) {
        return endpointUrl + "/" + bucketName + "/" + objectKey;
    }


    public List<String> listCommonPrefixes(String prefix) {

        List<String> folders = new ArrayList<>();

        ListObjectsV2Request request = new ListObjectsV2Request()
                .withBucketName(bucketName)
                .withPrefix(prefix)
                .withDelimiter("/");

        ListObjectsV2Result result;

        do {
            result = s3Client.listObjectsV2(request);
            folders.addAll(result.getCommonPrefixes());
            request.setContinuationToken(result.getNextContinuationToken());
        } while (result.isTruncated());

        return folders;
    }
}


