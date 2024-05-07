package com.example.processingapplications.mapper;

import com.example.processingapplications.dto.ApplicationDto;
import com.example.processingapplications.dto.CreateOrUpdateApplicationDto;
import com.example.processingapplications.entity.Application;
import org.springframework.stereotype.Component;

@Component
public class ApplicationMapper {

    private final UserMapper userMapper;

    public ApplicationMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Application toEntity(CreateOrUpdateApplicationDto createOrUpdateApplicationDto) {
        Application application = new Application();
        application.setText(createOrUpdateApplicationDto.getText());
        application.setCreatedAt(System.currentTimeMillis());
        return application;
    }

    public ApplicationDto toDto(Application application) {
        ApplicationDto applicationDto = new ApplicationDto();

        applicationDto.setId(application.getId());
        applicationDto.setText(application.getText());
        applicationDto.setStatus(application.getStatus());
        applicationDto.setUser(userMapper.toDto(application.getUser()));

        return applicationDto;
    }
}
