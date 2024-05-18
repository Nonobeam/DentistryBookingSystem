package com.example.DentistryManagement.controller;


import com.example.DentistryManagement.core.mail.Mail;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.service.MailService;
import com.example.DentistryManagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/player")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "User API")
public class UserController {

    private final UserService userService;

    private final MailService mailService;
    @Operation(summary = "All users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @GetMapping("/all")
    public ResponseEntity<List<Client>> findAllUsers(){
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @PostMapping("/sendMail/{mail}")
    public String sendMail(@PathVariable String mail, @RequestBody Mail mailStructure){
        mailService.sendMail(mail, mailStructure);

        return "Successfully";
    }
}
