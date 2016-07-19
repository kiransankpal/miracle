package view.helper;

import java.util.Map;

import javax.faces.context.FacesContext;

import oracle.adf.view.rich.context.AdfFacesContext;

public class InvoicePortalHelper {
    public InvoicePortalHelper() {
    }

    public void readRequestParameters() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String pageID = params.get("pageID");
        if(pageID.contains("?")){
            pageID= (pageID.split("\\?"))[0];
        }
        if(pageID!=null)
            AdfFacesContext.getCurrentInstance().getPageFlowScope().put("pageID",pageID);
    }
}
