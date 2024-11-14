package tn.pfe.CnotConnectV1.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import tn.pfe.CnotConnectV1.entities.BudgetAllocation;
import tn.pfe.CnotConnectV1.entities.Invoice;
import tn.pfe.CnotConnectV1.entities.Project;
import tn.pfe.CnotConnectV1.entities.PurchaseOrder;
import tn.pfe.CnotConnectV1.entities.User;
import tn.pfe.CnotConnectV1.enums.BudgetStatus;
import tn.pfe.CnotConnectV1.repository.BudgetAllocationRepository;
import tn.pfe.CnotConnectV1.repository.InvoiceRepository;
import tn.pfe.CnotConnectV1.repository.PurchaseRepository;
import tn.pfe.CnotConnectV1.services.interfaces.ContinuousMonitoringServiceI;

public class ContinuousMonitoringService implements ContinuousMonitoringServiceI{
	
	  @Autowired
	    private BudgetAllocationRepository budgetRepository;
	    @Autowired
	    private MailNotificationService mailNotificationService;
	    @Autowired
	    private InvoiceRepository invoiceRepository;
	    @Autowired
	    private UserService userService;
	    @Autowired
	    private PurchaseRepository purchaseOrderRepository;
	
	@Override
    public void performContinuousMonitoring() {
        monitorBudgets();
        monitorInvoices();
        monitorPurchaseOrders();

    }

    private void monitorBudgets() {
        List<BudgetAllocation> budgets = budgetRepository.findAll();
        for (BudgetAllocation budget : budgets) {
            performRealTimeEvaluations(budget);
            updateMonitoringIndicators(budget);
        }
        budgetRepository.saveAll(budgets);
    }
    private void performRealTimeEvaluations(BudgetAllocation budget) {
        // Calculate the ratio of spent amount to total amount
        BigDecimal spentToTotalRatio = calculateSpentToTotalRatio(budget);
        //  Check if the spent amount exceeds a certain threshold
        boolean isExceedingThreshold = checkExceedingThreshold(budget);
        //  Determine if the budget is in danger based on custom criteria
        boolean isBudgetInDanger = determineBudgetStatus(spentToTotalRatio, isExceedingThreshold);
        // Update the budget fields or perform additional actions based on the evaluations
        updateBudgetFields(budget, spentToTotalRatio, isExceedingThreshold, isBudgetInDanger);
    }

    private BigDecimal calculateSpentToTotalRatio(BudgetAllocation budget) {
        if (budget.getAllocatedAmount().compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;  // Avoid division by zero
        }
        return budget.getUsedBudget().divide(budget.getAllocatedAmount(), 2, RoundingMode.HALF_UP);
    }

    private boolean checkExceedingThreshold(BudgetAllocation budget) {
        //  Set a threshold, e.g., 80% of the total amount
        BigDecimal threshold = budget.getAllocatedAmount().multiply(new BigDecimal("0.8"));
        return budget.getSpentAmount().compareTo(threshold) > 0;
    }

    private boolean determineBudgetStatus(BigDecimal spentToTotalRatio, boolean isExceedingThreshold) {
        //  Determine if the budget is in danger based on your custom criteria
        return spentToTotalRatio.compareTo(new BigDecimal("0.5")) < 0 || isExceedingThreshold;
    }

    private void updateBudgetFields(BudgetAllocation budget, BigDecimal spentToTotalRatio, boolean isExceedingThreshold, boolean isBudgetInDanger) {
        //  Update the financial performance indicator based on the evaluations
        if (isBudgetInDanger) {
            budget.setFinancialPerformanceIndicator("Critical");
        } else if (isExceedingThreshold) {
            budget.setFinancialPerformanceIndicator("Warning");
        } else {
            budget.setFinancialPerformanceIndicator("Good");
        }

        // Additional updates based on your requirements
        // You can log events, send notifications, or take other actions as needed
    }
    private void updateMonitoringIndicators(BudgetAllocation budget) {
        String financialPerformanceIndicator = budget.getFinancialPerformanceIndicator();
        switch (financialPerformanceIndicator) {
            case "Critical" -> handleCriticalBudget(budget);
            case "Warning" -> handleWarningBudget(budget);
            case "Good" -> handleGoodBudget(budget);
            default -> handleDefaultCase(budget);
        }
        budgetRepository.save(budget);
    }
    private void handleCriticalBudget(BudgetAllocation budget) {
        sendCriticalAlerts(budget);
        escalateCriticalIssue(budget);

    }
    private void sendCriticalAlerts(BudgetAllocation budget) {
        User projectManager = budget.getProject().getProjectManager();
//        List<User> financeTeam = getFinanceTeamMembers(budget);
        mailNotificationService.sendEmailNotification(projectManager, "Critical Budget Alert", "The budget requires immediate attention.");

//        for (User financeTeamMember : financeTeam) {
//            sendSMSAlert(financeTeamMember.getPhoneNumber(), "Critical Budget Alert: Immediate action needed.");
//        }
    }
//    private List<User> getFinanceTeamMembers(Budget budget) {
//        return userService.getFinanceTeamMembers(budget.getProject());
//    }

//    private void sendSMSAlert(String recipientPhoneNumber, String message) {
//
//    }
    private void escalateCriticalIssue(BudgetAllocation budget) {
        updateBudgetStatus(budget, BudgetStatus.ESCALATED);
        initiateEmergencyProcedures(budget);
    }
    private void initiateEmergencyProcedures(BudgetAllocation budget) {
        recordEmergencyDetails(budget);
    }
    private void recordEmergencyDetails(BudgetAllocation budget) {
        String emergencyDescription = "Critical budget issue detected. Emergency procedures initiated.";
        logEmergencyDetails(budget, emergencyDescription);

    }
    private void logEmergencyDetails(BudgetAllocation budget, String emergencyDescription) {
        Logger logger = LoggerFactory.getLogger(getClass());
        String logMessage = String.format("Emergency Details for Budget ID %d: %s", budget.getId(), emergencyDescription);
        logger.info(logMessage);

    }
    private void updateBudgetStatus(BudgetAllocation budget, BudgetStatus newStatus) {
        budget.setBudgetStatus(newStatus);
        budgetRepository.save(budget);
    }
    private void handleWarningBudget(BudgetAllocation budget) {

        if (isBudgetAmountWarning(budget)) {
            sendWarningAlertsBudget(budget);
        } else {
            logEventbudget(budget);
        }
        budgetRepository.save(budget);
    }
    private void logEventbudget(BudgetAllocation budget) {
        System.out.println("Log Event for " + budget.getId() + ": " + "Unexpected financial performance indicator. No action taken for warning.");
    }
    private boolean isBudgetAmountWarning(BudgetAllocation budget) {
        BigDecimal totalAmount = budget.getAllocatedAmount();
        BigDecimal spentAmount = budget.getSpentAmount();

        double warningThresholdPercentage = 80.0;
        BigDecimal thresholdAmount = totalAmount.multiply(BigDecimal.valueOf(warningThresholdPercentage / 100.0));

        return spentAmount.compareTo(thresholdAmount) >= 0;
    }
    private void sendWarningAlertsBudget(BudgetAllocation budget) {
        User projectManager = budget.getProject().getProjectManager();
        mailNotificationService.sendEmailNotification(projectManager , "Budget Warning Alert", "The budget is approaching the warning threshold.");
    }

    private void handleGoodBudget(BudgetAllocation budget) {
        logEvent("Handling good budget for project: " + budget.getProject().getProjectName());
        budget.setBudgetStatus(BudgetStatus.GOOD);
        budgetRepository.save(budget);
    }
    private void logEvent(String message) {
        Logger logger = LoggerFactory.getLogger(getClass());
        logger.info(message);
    }


    private void handleDefaultCase(BudgetAllocation budget) {
        logEventDefault("Handling default case for project: " + budget.getProject().getProjectName());
        budget.setBudgetStatus(BudgetStatus.PENDING);
        budgetRepository.save(budget);
    }

    private void logEventDefault(String message) {
        Logger logger = LoggerFactory.getLogger(getClass());
        logger.info(message);
    }

    //////  method to monitor invoices
    private void monitorInvoices() {
        List<Invoice> invoices = invoiceRepository.findAll();
        for (Invoice invoice : invoices) {
            // Check for critical conditions in each invoice
            if (isInvoiceAmountCritical(invoice)) {
                handleCriticalInvoice(invoice);
            } else if (isInvoiceAmountWarning(invoice)) {
                handleWarningInvoice(invoice);
            } else {
                handleDefaultCaseInvoice(invoice);
            }
        }
    }
    private boolean isInvoiceAmountCritical(Invoice invoice) {
        BigDecimal criticalThreshold = new BigDecimal("10000.00");

        return invoice.getTotalAmount().compareTo(criticalThreshold) >= 0;
    }
    private boolean isInvoiceAmountWarning(Invoice invoice) {
        BigDecimal warningThreshold = new BigDecimal("5000.00");

        return invoice.getTotalAmount().compareTo(warningThreshold) >= 0;
    }
    private void handleCriticalInvoice(Invoice invoice) {
        // Implement actions for handling a critical invoice
        sendCriticalAlertsInvoice(invoice);
        escalateCriticalIssueInvoice(invoice);
        logCriticalEventInvoice(invoice);
    }
    private void handleWarningInvoice(Invoice invoice) {
        sendWarningAlertsInvoice(invoice);
        logWarningEventInvoice(invoice);
    }
    private void sendWarningAlertsInvoice(Invoice invoice) {
        User projectManager = invoice.getProject().getProjectManager();
        mailNotificationService.sendEmailNotification(projectManager, "Warning: Invoice Alert", "The invoice requires attention.");

    }
    private void logWarningEventInvoice(Invoice invoice) {
        Project project = invoice.getProject();
        String logMessage = "Warning event for invoice " + invoice.getId() + ": " ;
        logEvent(project, logMessage);
    }
    private void logEvent(Project project, String logMessage) {
        System.out.println("Log Event for " + project.getProjectName() + ": " + logMessage);
    }

    private void handleDefaultCaseInvoice(Invoice invoice) {
        logEventinvoice(invoice);
        invoiceRepository.save(invoice);
    }
    private void logEventinvoice(Invoice invoice) {
        System.out.println("Log Event for " + invoice.getId() + ": " + "Unhandled financial performance indicator. Taking default action.");
    }
    private void sendCriticalAlertsInvoice(Invoice invoice) {
        User projectManager = invoice.getProject().getProjectManager();
//        List<User> financeTeam = getFinanceTeamMembersInvoice(invoice);
        mailNotificationService.sendEmailNotification(projectManager, "Critical Invoice Alert", "The invoice requires immediate attention.");

//        for (User financeTeamMember : financeTeam) {
//            mailNotificationService.sendEmailNotification(financeTeamMember.getEmail(), "Critical Invoice Alert", "The invoice requires immediate attention.");
//        }
    }

    private void escalateCriticalIssueInvoice(Invoice invoice) {
        initiateEmergencyProceduresInvoice(invoice);
    }
    private void initiateEmergencyProceduresInvoice(Invoice invoice) {
       /// Update the status of the invoice to reflect the emergency
        invoice.setStatus(InvoiceStatus.EMERGENCY);
//        notifyStakeholdersAboutEmergencyInvoice(invoice);
//        performAdditionalEmergencyActionsInvoice(invoice);
    }
    private void logCriticalEventInvoice(Invoice invoice) {
        //Log critical event details using a logger
        Logger logger = LoggerFactory.getLogger(getClass());

        String logMessage = String.format(
                "Critical issue in Invoice %d: Amount exceeded the threshold. Emergency procedures initiated.",
                invoice.getId()
        );
        logger.error(logMessage);
        logAdditionalDetailsInvoice(invoice);
    }
    private void logAdditionalDetailsInvoice(Invoice invoice) {
        Logger logger = LoggerFactory.getLogger(getClass());

        String additionalDetailsMessage = String.format(
                "Additional details for Invoice %d: Supplier - %s, Date Issued - %s",
                invoice.getId(),
                invoice.getSupplier().getName(),
                invoice.getDateIssued()
        );
        logger.info(additionalDetailsMessage);

    }
    ///////// method to monitor PurchaseOrders
    public void monitorPurchaseOrders() {
        List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAll();
        for (PurchaseOrder purchaseOrder : purchaseOrders) {
            if (isPurchaseOrderAmountCritical(purchaseOrder)) {
                sendCriticalAlertsPurchaseOrder(purchaseOrder);
                escalateCriticalIssuePurchaseOrder(purchaseOrder);
                logCriticalEventPurchaseOrder(purchaseOrder);

            }
        }
    }
    private boolean isPurchaseOrderAmountCritical(PurchaseOrder purchaseOrder) {
        double criticalThreshold = 100000.0;
        return purchaseOrder.getTotalPrice() > criticalThreshold;
    }
    private void sendCriticalAlertsPurchaseOrder(PurchaseOrder purchaseOrder) {
        User projectManager = purchaseOrder.getProject().getProjectManager();
        mailNotificationService.sendEmailNotification(projectManager, "Critical Purchase Order Alert", "The purchase order requires immediate attention.");

    }
    private void escalateCriticalIssuePurchaseOrder(PurchaseOrder purchaseOrder) {
        initiateEmergencyProceduresPurchaseOrder(purchaseOrder);
    }
    private void initiateEmergencyProceduresPurchaseOrder(PurchaseOrder purchaseOrder) {
        purchaseOrder.setStatus(OrderStatus.EMERGENCY);
        purchaseOrderRepository.save(purchaseOrder);
    }
    private void logCriticalEventPurchaseOrder(PurchaseOrder purchaseOrder) {
        Logger logger = LoggerFactory.getLogger(getClass());
        String logMessage = String.format("Critical issue detected in Purchase Order %d - %s. Additional Details: %s",
                purchaseOrder.getId(), purchaseOrder.getPurchaseOrderNumber(), "Custom details here");
        logger.error(logMessage);

    }

}