package tn.pfe.CnotConnectV1.services.interfaces;

import java.util.List;
import java.util.Optional;

import tn.pfe.CnotConnectV1.dto.DepartmentDTO;
import tn.pfe.CnotConnectV1.entities.Department;

public interface IDepartmentService {

	Department findDepartmentByName(String name);

	Department findDepartmentById(Long departmentId);

	List<Department> getAllDepartments();


	void deleteDepartment(Long departmentId);

	Department addDepartment(Department department);


	Optional<Department> updateDepartment(Long departmentId, DepartmentDTO departmentDTO);

}
