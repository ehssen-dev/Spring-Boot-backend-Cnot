package tn.pfe.CnotConnectV1.services.interfaces;

import java.util.List;
import java.util.Optional;

import tn.pfe.CnotConnectV1.dto.InvoiceDTO;
import tn.pfe.CnotConnectV1.entities.Invoice;

public interface IInvoiceService {

	Invoice markInvoiceAsPaid(Long invoiceId);

	void processInvoices();

	boolean deleteInvoice(Long invoiceId);



	List<Invoice> getAllInvoices();

	Invoice saveInvoice(Invoice invoice);

	Invoice updateInvoice(Long invoiceId, InvoiceDTO invoiceDTO);

	Invoice createInvoice(InvoiceDTO invoiceDTO);

	InvoiceDTO getInvoiceDTOById(Long invoiceId);

}
