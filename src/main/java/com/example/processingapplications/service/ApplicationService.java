package com.example.processingapplications.service;

import com.example.processingapplications.dto.ApplicationDto;
import com.example.processingapplications.dto.CreateOrUpdateApplicationDto;
import com.example.processingapplications.entity.Application;
import com.example.processingapplications.entity.User;
import com.example.processingapplications.enums.ApplicationStatus;
import com.example.processingapplications.mapper.ApplicationMapper;
import com.example.processingapplications.repository.ApplicationRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationService {

    public static final int PAGE_SIZE = 5;
    private final ApplicationRepository applicationRepository;
    private final ApplicationMapper applicationMapper;
    private final UserService userService;
    private final RoleService roleService;

    public ApplicationService(ApplicationRepository applicationRepository,
                              ApplicationMapper applicationMapper,
                              UserService userService,
                              RoleService roleService) {
        this.applicationRepository = applicationRepository;
        this.applicationMapper = applicationMapper;
        this.userService = userService;
        this.roleService = roleService;
    }

    public ApplicationDto createApplication(CreateOrUpdateApplicationDto createOrUpdateApplicationDto,
                                            ApplicationStatus applicationStatus,
                                            Authentication authentication) {
        Application application = applicationMapper.toEntity(createOrUpdateApplicationDto);
        application.setUser(userService.loadUserByUsername(authentication.getName()));
        application.setStatus(applicationStatus.name());
        return applicationMapper.toDto(applicationRepository.save(application));
    }

    public Application findById(Long id) {
        return applicationRepository.findById(id).orElseThrow(); // TODO: exception add
    }

    public ApplicationDto getSentApplication(Long id) {
        Application application = findById(id);
        if (application.getStatus().equals(ApplicationStatus.SENT.name())) {
            return applicationMapper.toDto(application);
        }
        throw new RuntimeException(); // TODO: add exception
    }

    public ApplicationDto sendDraftApplication(Long id) {
        Application application = findById(id);
        ApplicationStatus applicationStatus = ApplicationStatus.valueOf(application.getStatus());
        if (applicationStatus == ApplicationStatus.DRAFT) {
            application.setStatus(ApplicationStatus.SENT.name());
            return applicationMapper.toDto(application);
        }
        throw new RuntimeException(); // TODO: add exception
    }

    public List<ApplicationDto> getPersonalApplications(Authentication authentication, Integer page, String sortType) {
        PageRequest pageRequest = getPageRequest(page, sortType);
        User user = userService.loadUserByUsername(authentication.getName());
        List<Application> applications = applicationRepository.findAllByUserId(user.getId(), pageRequest);
        return applications.stream()
                .map(applicationMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<ApplicationDto> getApplications(Authentication authentication, String status, String searchByName, Integer page, String sortType) {
        User user = userService.loadUserByUsername(authentication.getName());
        boolean isOperator = user.getAuthorities().contains(roleService.getOperatorRole());
        status = status == null ? "sent" : status;
        status = isOperator ? ApplicationStatus.SENT.name() : status.toUpperCase();
        PageRequest pageRequest = getPageRequest(page, sortType);
        List<Application> applications = null;

        if (searchByName == null || searchByName.isBlank()) {
            applications = applicationRepository.findAllByStatus(status, pageRequest);
        } else {
            applications = applicationRepository.findAllByStatusAndName(status, searchByName, pageRequest);
        }

        return applications.stream()
                .map(applicationMapper::toDto)
                .collect(Collectors.toList());
    }

    private PageRequest getPageRequest(Integer page, String sortType) {
        sortType = sortType == null ? "asc" : sortType;
        Sort sort = switch (sortType) {
            case "asc" -> Sort.by("createdAt").ascending();
            case "desc" -> Sort.by("createdAt").descending();
            default -> Sort.unsorted();
        };
        return PageRequest.of(page, PAGE_SIZE, sort);
    }

    public ApplicationDto updateApplication(Long id, CreateOrUpdateApplicationDto createOrUpdateApplicationDto) {
        Application application = findById(id);
        if (application.getStatus().equals(ApplicationStatus.DRAFT.name())) {
            application.setText(createOrUpdateApplicationDto.getText());
            return applicationMapper.toDto(applicationRepository.save(application));
        }
        return null; // TODO: add exception
    }

    public ApplicationDto decide(Long id, ApplicationStatus applicationStatus) {
        Application application = findById(id);
        if (application.getStatus().equals(ApplicationStatus.SENT.name())) {
            application.setStatus(applicationStatus.name());
            return applicationMapper.toDto(applicationRepository.save(application));
        }
        return null; // TODO: add exception
    }
}
