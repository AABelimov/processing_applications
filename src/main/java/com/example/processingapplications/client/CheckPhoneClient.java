package com.example.processingapplications.client;

import com.example.processingapplications.client.model.CheckPhoneResponse;
import com.example.processingapplications.config.CheckPhoneClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "checkPhone", configuration = CheckPhoneClientConfiguration.class)
public interface CheckPhoneClient {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    CheckPhoneResponse[] getInfo(@RequestBody String[] phone);
}
