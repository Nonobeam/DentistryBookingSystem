
package com.example.DentistryManagement.controller;


import com.example.DentistryManagement.core.dentistry.Schedule;
import com.example.DentistryManagement.core.mail.Notification;
import com.example.DentistryManagement.core.user.Client;
import com.example.DentistryManagement.core.user.Role;
<<<<<<< HEAD
=======
import com.example.DentistryManagement.service.DentistService;
>>>>>>> main
import com.example.DentistryManagement.service.NotificationService;
import com.example.DentistryManagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RequestMapping("/api/v1/player")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "User API")
public class UserController {

    private final UserService userService;
<<<<<<< HEAD
=======
    private final DentistService dentistService;
>>>>>>> main
    private final NotificationService notificationService;

    @Operation(summary = "All users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully"),
            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @GetMapping("/all")
    public ResponseEntity<List<Client>> findAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

//    @Operation(summary = "All dentists follow status")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successfully"),
//            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
//            @ApiResponse(responseCode = "404", description = "Not found")
//    })
//    @GetMapping("/allDentist/{status}")
//    public ResponseEntity<List<Client>> findAllDentistsByStatus(@PathVariable int status, @PathVariable Role role) {
//        return ResponseEntity.ok(dentistService.findAllDentistsByStatus(status, role));
//    }

//    @Operation(summary = "Get schedule for a dentist")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successfully"),
//            @ApiResponse(responseCode = "403", description = "Don't have permission to do this"),
//            @ApiResponse(responseCode = "404", description = "Not found")
//    })
//    @GetMapping("/dentist/schedule/{dentistID}")
//    public ResponseEntity<Schedule> findAllDentistsByStatus(@PathVariable UUID dentistID, @Param("date")LocalDate date) {
//        return ResponseEntity.ok(dentistService.getDentistSchedule(dentistID, date));
//    }

    @PostMapping("/sendMail/{mail}")
    public String sendMail(@PathVariable String mail, @RequestBody Notification notificationStructure) {
        notificationService.sendMail(mail, notificationStructure);
        return "Successfully";
    }

<<<<<<< HEAD
}
=======
}
>>>>>>> main
