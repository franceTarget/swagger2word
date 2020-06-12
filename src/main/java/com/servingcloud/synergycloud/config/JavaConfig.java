package com.servingcloud.synergycloud.config;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by XiuYin.Cui on 2018/6/21.
 */
@Configuration
@ServletComponentScan("com.servingcloud.synergycloud.controller")
public class JavaConfig {

    @Bean
    public RestTemplate restTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }
}
