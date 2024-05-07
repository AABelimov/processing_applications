package com.example.processingapplications.controller;

import com.example.processingapplications.dto.ApplicationDto;
import com.example.processingapplications.dto.CreateOrUpdateApplicationDto;
import com.example.processingapplications.enums.ApplicationStatus;
import com.example.processingapplications.service.ApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApplicationDto> createAndSendApplication(@RequestBody CreateOrUpdateApplicationDto createOrUpdateApplicationDto,
                                                                   Authentication authentication) {
        return ResponseEntity.ok(applicationService.createApplication(createOrUpdateApplicationDto, ApplicationStatus.SENT, authentication));
    }

    @PostMapping("/draft")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApplicationDto> createDraftApplication(@RequestBody CreateOrUpdateApplicationDto createOrUpdateApplicationDto,
                                                                 Authentication authentication) {
        return ResponseEntity.ok(applicationService.createApplication(createOrUpdateApplicationDto, ApplicationStatus.DRAFT, authentication));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    public ResponseEntity<ApplicationDto> getApplication(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.getSentApplication(id));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<ApplicationDto>> getPersonalApplications(@RequestParam Integer page,
                                                                        @RequestParam(required = false) String sort,
                                                                        Authentication authentication) {
        return ResponseEntity.ok(applicationService.getPersonalApplications(authentication, page, sort));
    }

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_OPERATOR') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ApplicationDto>> getApplications(@RequestParam Integer page,
                                                                @RequestParam(required = false) String status,
                                                                @RequestParam(required = false, name = "search-by-name") String searchByName,
                                                                @RequestParam(required = false) String sort,
                                                                Authentication authentication) {
        return ResponseEntity.ok(applicationService.getApplications(authentication, status, searchByName, page, sort));
    }

    @PatchMapping("/{id}/send")
    @PreAuthorize("hasRole('ROLE_USER') and @applicationService.findById(#id).user.username == authentication.name")
    public ResponseEntity<ApplicationDto> sendDraftApplication(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.sendDraftApplication(id));
    }

    @PatchMapping("/{id}/update")
    @PreAuthorize("hasRole('ROLE_USER') and @applicationService.findById(#id).user.username == authentication.name")
    public ResponseEntity<ApplicationDto> updateApplication(@PathVariable Long id,
                                                            @RequestBody CreateOrUpdateApplicationDto createOrUpdateApplicationDto) {
        return ResponseEntity.ok(applicationService.updateApplication(id, createOrUpdateApplicationDto));
    }

    @PatchMapping("/{id}/accept")
    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    public ResponseEntity<ApplicationDto> acceptApplication(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.decide(id, ApplicationStatus.ACCEPTED));
    }

    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    public ResponseEntity<ApplicationDto> rejectApplication(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.decide(id, ApplicationStatus.REJECTED));
    }
}
