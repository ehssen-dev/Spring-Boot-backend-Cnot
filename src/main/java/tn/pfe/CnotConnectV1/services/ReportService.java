package tn.pfe.CnotConnectV1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.pfe.CnotConnectV1.entities.FinancialReport;
import tn.pfe.CnotConnectV1.entities.Project;
import tn.pfe.CnotConnectV1.repository.FinancialReportRepository;
import tn.pfe.CnotConnectV1.repository.ProjectRepository;

@Service
public class ReportService {
    
    @Autowired
    private FinancialReportRepository financialReportRepository;
    
    @Autowired
    private ProjectRepository projectRepository;
    
}