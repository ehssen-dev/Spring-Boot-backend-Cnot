package tn.pfe.CnotConnectV1.dto;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SolidarityOlympicDTO implements Serializable {

    public SolidarityOlympicDTO(Long solidarityOlympicId2, String programName2) {
		// TODO Auto-generated constructor stub
	}
	private Long solidarityOlympicId;
    private String programName;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
}