package com.example.DentistryManagement.controller;

import com.example.DentistryManagement.core.dentistry.Service;
import com.example.DentistryManagement.repository.ServiceRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/boss")
@RestController
@RequiredArgsConstructor
@Tag(name = "Boss API")
public class BossController {

    private final ServiceRepository serviceRepository;

    @Operation(summary = "Add new service")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @PostMapping("/service/add")
    public ResponseEntity<Service> addNewService(@RequestBody Service service) {
        return ResponseEntity.ok(serviceRepository.save(service));
    }
}
