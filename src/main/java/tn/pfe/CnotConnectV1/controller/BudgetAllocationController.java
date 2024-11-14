 package tn.pfe.CnotConnectV1.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.pfe.CnotConnectV1.dto.BudgetAllocationDTO;
import tn.pfe.CnotConnectV1.dto.ProcurementRequestDTO;
import tn.pfe.CnotConnectV1.entities.BudgetAllocation;
import tn.pfe.CnotConnectV1.entities.ProcurementRequest;
import tn.pfe.CnotConnectV1.enums.BudgetStatus;
import tn.pfe.CnotConnectV1.exeptions.BudgetAllocationNotFoundException;
import tn.pfe.CnotConnectV1.exeptions.ProjectNotFoundException;
import tn.pfe.CnotConnectV1.services.interfaces.IBudgetAllocationService;

@CrossOrigin(origins = "http://localhost:3000") 
@RestController
@RequestMapping("/api/budgetAllocations")
public class BudgetAllocationController {

    @Autowired
    private IBudgetAllocationService budgetAllocationService;

   
    @PostMapping("/add")
    public ResponseEntity<BudgetAllocationDTO> addBudgetAllocation(@Valid @RequestBody BudgetAllocationDTO dto) {
        try {
            BudgetAllocationDTO createdDTO = budgetAllocationService.addBudgetAllocation(dto);
            return new ResponseEntity<>(createdDTO, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/{budgetId}")
    public ResponseEntity<BudgetAllocationDTO> updateBudgetAllocation(@PathVariable Long budgetId, @RequestBody BudgetAllocationDTO dto) {
        try {
            BudgetAllocationDTO updatedBudgetAllocationDTO = budgetAllocationService.updateBudgetAllocation(budgetId, dto);
            return ResponseEntity.ok(updatedBudgetAllocationDTO);
        } catch (BudgetAllocationNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // Bad request if IDs are invalid
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Internal server error for unexpected issues
        }
    }

    @PutMapping("/{budgetId}/status")
    public ResponseEntity<Void> updateBudgetStatus(@PathVariable Long budgetId, @RequestBody BudgetStatus newStatus) {
        try {
            BudgetAllocation budgetAllocation = budgetAllocationService.getBudgetAllocationById(budgetId);
            budgetAllocationService.updateBudgetStatus(budgetAllocation, newStatus);
            return ResponseEntity.ok().build();
        } catch (BudgetAllocationNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{budgetId}")
    public ResponseEntity<Void> deleteBudgetAllocation(@PathVariable Long budgetId) {
        try {
            budgetAllocationService.deleteBudgetAllocation(budgetId);
            return ResponseEntity.noContent().build();
        } catch (BudgetAllocationNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    
    @GetMapping("/all")
    public ResponseEntity<List<BudgetAllocationDTO>> getAllBudgetAllocations() {
        List<BudgetAllocationDTO> budgetAllocations = budgetAllocationService.getAllBudgetAllocations();
        return ResponseEntity.ok(budgetAllocations);
    }
    
    @GetMapping("/{budgetId}")
    public ResponseEntity<?> getBudgetAllocationById(@PathVariable Long budgetId) {
        try {
            BudgetAllocation budgetAllocation = budgetAllocationService.getBudgetAllocationById(budgetId);
            return ResponseEntity.ok(budgetAllocation);
        } catch (BudgetAllocationNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Budget Allocation not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }
    

    @GetMapping("/project/{projectId}/total")
    public ResponseEntity<Double> calculateTotalBudgetForProject(@PathVariable Long projectId) {
        try {
            Double totalBudget = budgetAllocationService.calculateTotalBudgetForProject(projectId);
            return ResponseEntity.ok(totalBudget);
        } catch (ProjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/allocateB")
    public ResponseEntity<BudgetAllocation> allocateBudget(@RequestBody BudgetAllocationDTO dto) {
        try {
            BudgetAllocation allocatedBudget = budgetAllocationService.allocateBudgetB(dto);
            return ResponseEntity.ok(allocatedBudget);
        } catch (Exception e) {
            // Handle exceptions as appropriate
            return ResponseEntity.badRequest().body(null);
        }
    }
   
    @PostMapping("/allocate/{budgetId}")
    public ResponseEntity<BudgetAllocation> allocateBudget(
            @PathVariable Long budgetId, 
            @RequestBody BudgetAllocationDTO dto) {
        try {
            BudgetAllocation updatedBudgetAllocation = budgetAllocationService.allocateBudget(budgetId, dto);
            return ResponseEntity.ok(updatedBudgetAllocation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null); // or handle the exception appropriately
        }
    }
    
    @PostMapping("/{budgetId}/add-expense")
    public ResponseEntity<String> addExpenseToBudget(
            @PathVariable Long budgetId,
            @RequestParam Double expenseAmount) {
        try {
        	budgetAllocationService.addExpenseToBudget(budgetId, expenseAmount);
            return ResponseEntity.ok("Expense added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
    
    
}