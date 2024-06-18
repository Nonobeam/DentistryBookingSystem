package com.example.DentistryManagement.service;

import com.example.DentistryManagement.core.dentistry.Services;
import com.example.DentistryManagement.repository.DentistRepository;
import com.example.DentistryManagement.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceService {
    private static final Logger LOGGER = LogManager.getLogger(ServiceService.class);
    private final ServiceRepository serviceRepository;
    private final DentistRepository dentistRepository;

//    public List<Services> findAllServices() {
//        List<Services> services;
//        try {
//            services = serviceRepository.findAllByServiceIDNotNull();
//            return services;
//        } catch (Error error) {
//            throw new Error("Error while fetch data from JPA");
//        }
//    }


    public Services findServiceByID(String servicesID) {
        Services service;
        try {
            service = serviceRepository.findByServiceID(servicesID);
            return service;
        } catch (Error error) {
            throw new Error("Error while fetch data from JPA");
        }
    }
}