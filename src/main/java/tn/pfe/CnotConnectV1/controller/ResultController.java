package tn.pfe.CnotConnectV1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.pfe.CnotConnectV1.dto.ResultDTO;
import tn.pfe.CnotConnectV1.entities.Result;
import tn.pfe.CnotConnectV1.services.ResultService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/results")
@CrossOrigin(origins = "http://localhost:3000") 
public class ResultController {
    
    private final ResultService resultService;

    @Autowired
    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    @PostMapping("/add")
    public ResponseEntity<Result> createResult(@RequestBody ResultDTO resultDTO) {
        Result result = resultService.saveResult(resultDTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{resultId}")
    public ResponseEntity<Result> getResultById(@PathVariable Long resultId) {
        Optional<Result> resultOptional = resultService.getResultById(resultId);
        return resultOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    public List<Result> getAllResults() {
        return resultService.getAllResults();
    }

    @DeleteMapping("/{resultId}")
    public ResponseEntity<Void> deleteResult(@PathVariable Long resultId) {
        resultService.deleteResult(resultId);
        return ResponseEntity.noContent().build();
    }
}