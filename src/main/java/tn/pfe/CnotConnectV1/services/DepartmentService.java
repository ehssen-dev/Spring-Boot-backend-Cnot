package tn.pfe.CnotConnectV1.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import tn.pfe.CnotConnectV1.dto.DepartmentDTO;
import tn.pfe.CnotConnectV1.entities.Department;
import tn.pfe.CnotConnectV1.entities.Project;
import tn.pfe.CnotConnectV1.entities.User;
import tn.pfe.CnotConnectV1.exeptions.DepartmentNotFoundException;
import tn.pfe.CnotConnectV1.repository.DepartmentRepository;
import tn.pfe.CnotConnectV1.repository.UserRepository;
import tn.pfe.CnotConnectV1.services.interfaces.IDepartmentService;


@Service
@Transactional
public class DepartmentService implements IDepartmentService {
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    public DepartmentService(DepartmentRepository departmentRepository, UserRepository userRepository) {
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Department findDepartmentByName(String name) {
        return departmentRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new DepartmentNotFoundException("Department with name " + name + " not found"));
    }

    @Override
    public Department findDepartmentById(Long departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException("Department with id " + departmentId + " not found"));
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public Department addDepartment(Department department) {
        return departmentRepository.save(department);
    }
    @Override
    public Optional<Department> updateDepartment(Long departmentId, DepartmentDTO departmentDTO) {
        return departmentRepository.findById(departmentId).map(existingDepartment -> {
            existingDepartment.setName(departmentDTO.getName());
            existingDepartment.setEmail(departmentDTO.getEmail());
            existingDepartment.setContactInformation(departmentDTO.getContactInformation());
            existingDepartment.setResponsibilities(departmentDTO.getResponsibilities());
            return departmentRepository.save(existingDepartment);
        });
    }
    @Override
    public void deleteDepartment(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException("Department with id " + departmentId + " not found"));
        
        // Optionally delete related entities here if needed
        departmentRepository.delete(department);
    }
}