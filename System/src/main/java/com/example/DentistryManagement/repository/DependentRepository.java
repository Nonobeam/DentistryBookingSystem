package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.user.Dependent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DependentRepository extends JpaRepository<Dependent, Integer> {

    Dependent findByDependentID(String dependentID);
}
