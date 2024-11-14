package tn.pfe.CnotConnectV1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.pfe.CnotConnectV1.dto.ResultDTO;
import tn.pfe.CnotConnectV1.entities.Game;
import tn.pfe.CnotConnectV1.entities.Result;
import tn.pfe.CnotConnectV1.repository.GameRepository;
import tn.pfe.CnotConnectV1.repository.ResultRepository;
import tn.pfe.CnotConnectV1.services.interfaces.IResultService;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ResultService implements IResultService {
    
    private final ResultRepository resultRepository;
    @Autowired
    private  GameRepository gameRepository;

    
    @Autowired
    public ResultService(ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    @Override
    public Result saveResult(ResultDTO resultDTO) {
        Result result = new Result();
        
        // Set properties from DTO
        result.setWinner(resultDTO.getWinner());
        result.setRunnerUp(resultDTO.getRunnerUp());
        result.setThirdPlace(resultDTO.getThirdPlace());
        result.setScores(resultDTO.getScores());
        result.setHighlights(resultDTO.getHighlights());
        result.setStatus(resultDTO.getStatus());
        result.setResultDate(resultDTO.getResultDate() != null ? resultDTO.getResultDate() : LocalDateTime.now());
        result.setComments(resultDTO.getComments());

        // Fetch and set the Game entity if needed
        if (resultDTO.getGameId() != null) {
            Game game = gameRepository.findById(resultDTO.getGameId())
                    .orElseThrow(() -> new RuntimeException("Game not found"));
            result.setGame(game);
        }

        String generateResultNumber = generateResultNumber(); 
        result.setResultNumber(generateResultNumber);

        return resultRepository.save(result);
    }
    private static final String ALPHANUMERIC_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int LENGTH = 6; 

    private String generateResultNumber() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(LENGTH);
        
        for (int i = 0; i < LENGTH; i++) {
            int index = random.nextInt(ALPHANUMERIC_CHARACTERS.length());
            sb.append(ALPHANUMERIC_CHARACTERS.charAt(index));
        }
        return sb.insert(LENGTH / 2, '-').toString();
    }
    @Override
    public Optional<Result> getResultById(Long resultId) {
        return resultRepository.findById(resultId);
    }

    @Override
    public List<Result> getAllResults() {
        return resultRepository.findAll();
    }

    @Override
    public void deleteResult(Long resultId) {
        resultRepository.deleteById(resultId);
    }
}