package com.api.casadoconstrutor.sght.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ArquivoConfig {

    @Value("${file.upload-dir}")
    private String uploadDir;

}
