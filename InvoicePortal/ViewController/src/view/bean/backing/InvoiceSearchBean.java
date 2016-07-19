package view.bean.backing;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import oracle.adf.model.BindingContext;

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

import view.utils.ADFUtils;
import view.utils.JSFUtils;


public class InvoiceSearchBean {
    String vendorName;
    Integer vendorNumber;
    Long invoiceNumber;
    String invoiceCurrencyCode;
    String invoiceType;
    Date maturityStartDate;
    Date maturityEndDate;
    Date invoiceStartDate;
    Date invoiceEndDate;
    String invoiceStatus;
    Long invoiceFormAmount;
    Long invoiceToAmount;


    public InvoiceSearchBean() {
    }

    public void invoiceVendorNameValueChangeListener(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != null) {
            vendorName = (String)valueChangeEvent.getNewValue();
        }
    }

    public void invoiceVendorNumberValueChangeListener(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != null) {
            vendorNumber = (Integer)valueChangeEvent.getNewValue();
        }
    }

    public BindingContainer getBindings() {
        return BindingContext.getCurrent().getCurrentBindingsEntry();
    }

    public String searchInvoice() {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yy");
        BindingContainer bindings = getBindings();
        OperationBinding operationBinding =
            bindings.getOperationBinding("searchInvoice");
        Map operMap = operationBinding.getParamsMap();
        operMap.put("vendorName", vendorName);
        operMap.put("vendorNumber", vendorNumber);
        operMap.put("invoiceNumber", invoiceNumber);
        if (invoiceType != null) {
            operMap.put("invoiceType", invoiceType);
        } else {
            operMap.put("invoiceType", "Standard");
        }
        operMap.put("currencyCode", invoiceCurrencyCode);

        if (maturityStartDate != null) {
            operMap.put("maturityStartDate",
                        formatter.format(maturityStartDate).toString());
        }
        if (maturityEndDate != null) {
            operMap.put("maturityEndDate",
                        formatter.format(maturityEndDate).toString());
        }
        if (invoiceStartDate != null) {
            operMap.put("invoiceStartDate",
                        formatter.format(invoiceStartDate).toString());
        }
        if (invoiceEndDate != null) {
            operMap.put("invoiceEndDate",
                        formatter.format(invoiceEndDate).toString());
        }
        operMap.put("invoiceFromAmount", invoiceFormAmount);
        operMap.put("invoiceToAmount", invoiceToAmount);
        operMap.put("invoiceStatus", invoiceStatus);
        operationBinding.execute();
        if (!operationBinding.getErrors().isEmpty()) {
            return null;
        }
        return null;
    }

    public String resetSearch() {
        vendorNumber = null;
        vendorName = null;
        invoiceNumber = null;
        invoiceCurrencyCode = null;
        invoiceType = null;
        maturityEndDate = null;
        maturityStartDate = null;
        invoiceEndDate = null;
        invoiceStartDate = null;
        invoiceStatus=null;
        invoiceFormAmount=null;
        invoiceToAmount=null;
        return null;
    }

    public void setMaturityStartDate(Date maturityStartDate) {
        this.maturityStartDate = maturityStartDate;
    }

    public Date getMaturityStartDate() {
        return maturityStartDate;
    }

    public void setMaturityEndDate(Date maturityEndDate) {
        this.maturityEndDate = maturityEndDate;
    }

    public Date getMaturityEndDate() {
        return maturityEndDate;
    }

    public void setInvoiceStartDate(Date invoiceStartDate) {
        this.invoiceStartDate = invoiceStartDate;
    }

    public Date getInvoiceStartDate() {
        return invoiceStartDate;
    }

    public void setInvoiceEndDate(Date invoiceEndDate) {
        this.invoiceEndDate = invoiceEndDate;
    }

    public Date getInvoiceEndDate() {
        return invoiceEndDate;
    }


    public void invoiceCurrencyCodeValueChange(ValueChangeEvent valueChangeEvent) {
        UIComponent uiComponent = valueChangeEvent.getComponent();
        uiComponent.processUpdates(FacesContext.getCurrentInstance());
        invoiceCurrencyCode =
                (String)JSFUtils.resolveExpression("#{bindings.attrCurrencyCode.inputValue}");
    }
    
    public void invoiceStatusValueChange(ValueChangeEvent valueChangeEvent) {
        UIComponent uiComponent = valueChangeEvent.getComponent();
        uiComponent.processUpdates(FacesContext.getCurrentInstance());
        invoiceStatus =
                (String)JSFUtils.resolveExpression("#{bindings.attrStatus.inputValue}");
    }
    
    
    public void invoiceTypeValueChange(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != null) {
            invoiceType = (String)valueChangeEvent.getNewValue();
        }
    }

    public void setInvoiceNumber(Long invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Long getInvoiceNumber() {
        return invoiceNumber;
    }


    public void setInvoiceFormAmount(Long invoiceFormAmount) {
        this.invoiceFormAmount = invoiceFormAmount;
    }

    public Long getInvoiceFormAmount() {
        return invoiceFormAmount;
    }

    public void setInvoiceToAmount(Long invoiceToAmount) {
        this.invoiceToAmount = invoiceToAmount;
    }

    public Long getInvoiceToAmount() {
        return invoiceToAmount;
    }
}
