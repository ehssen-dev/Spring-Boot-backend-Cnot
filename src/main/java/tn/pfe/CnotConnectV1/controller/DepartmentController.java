package tn.pfe.CnotConnectV1.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.pfe.CnotConnectV1.dto.DepartmentDTO;
import tn.pfe.CnotConnectV1.entities.Department;
import tn.pfe.CnotConnectV1.services.interfaces.IDepartmentService;

@RestController
@RequestMapping("/api/departments")
@CrossOrigin(origins = "http://localhost:3000")
public class DepartmentController {
    
    @Autowired
    private IDepartmentService departmentService;

    @GetMapping("/{departmentId}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long departmentId) {
        Department department = departmentService.findDepartmentById(departmentId);
        return ResponseEntity.ok(department);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Department>> getAllDepartments() {
        List<Department> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }
/*
    @PostMapping("/add")
    public ResponseEntity<Department> createDepartment(@RequestBody Department department) {
        try {
            Department savedDepartment = departmentService.addDepartment(department);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDepartment);
        } catch (Exception e) {
            // Log the exception and return a meaningful error response
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(null);
        }
    }*/
    @PostMapping("/add")
    public ResponseEntity<Department> createDepartment(@RequestBody DepartmentDTO departmentDTO) {
        // Convert DepartmentDTO to Department entity
        Department department = new Department();
        department.setName(departmentDTO.getName());
        department.setEmail(departmentDTO.getEmail());
        department.setContactInformation(departmentDTO.getContactInformation());
        department.setResponsibilities(departmentDTO.getResponsibilities());

        // Save the Department entity
        Department savedDepartment = departmentService.addDepartment(department);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDepartment);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Department> updateDepartment(
            @PathVariable("id") Long departmentId,
            @RequestBody DepartmentDTO departmentDTO) {
        Optional<Department> updatedDepartment = departmentService.updateDepartment(departmentId, departmentDTO);

        if (updatedDepartment.isPresent()) {
            return ResponseEntity.ok(updatedDepartment.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @DeleteMapping("/{departmentId}")
    public ResponseEntity<String> deleteDepartment(@PathVariable Long departmentId) {
        departmentService.deleteDepartment(departmentId);
        return ResponseEntity.ok("Department with ID " + departmentId + " has been successfully deleted.");
    }

}