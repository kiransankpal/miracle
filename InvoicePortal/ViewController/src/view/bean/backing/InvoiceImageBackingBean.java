package view.bean.backing;

import java.io.IOException;

import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.layout.RichPanelHeader;

import oracle.adf.view.rich.model.AutoSuggestUIHints;

import oracle.apps.fnd.ext.common.AppsRequestWrapper;
import oracle.apps.fnd.ext.common.AppsSessionHelper;
import oracle.apps.fnd.ext.common.Session;

import view.helper.FndUserBean;

import view.utils.ADFUtils;


public class InvoiceImageBackingBean {
    private RichPanelHeader panelHeaderBinding;
    private String invoiceId;

    public InvoiceImageBackingBean() {
    }

    public void setPanelHeaderBinding(RichPanelHeader panelHeaderBinding) {
        this.panelHeaderBinding = panelHeaderBinding;
    }

    public RichPanelHeader getPanelHeaderBinding() {
        return panelHeaderBinding;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceId() {
        invoiceId= ADFUtils.getBoundAttributeValue("InvoiceId").toString();
        if(invoiceId == null){
            invoiceId = "1484867";
        }
        System.out.println(invoiceId);
        return invoiceId;
    }
    
    private String userName;
    private String userInfo;

   
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {

        Map sessionScope = ADFContext.getCurrent().getSessionScope();
        FndUserBean fndUser = (FndUserBean)sessionScope.get("fndUserBean");
        if (fndUser != null) {
            return fndUser.getUserName();
        }
        return userName;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public String getUserInfo() {
        Map sessionScope = ADFContext.getCurrent().getSessionScope();
        FndUserBean fndUser = (FndUserBean)sessionScope.get("fndUserBean");
        if (fndUser != null) {
            Map userInfoMap = fndUser.getUserInfo();
            StringBuffer temp = new StringBuffer();
            temp.append("<table>");
            for (Object key : userInfoMap.keySet()) {
                temp.append("<tr>");
                temp.append("<td>");
                temp.append(key);
                temp.append("</td>");
                temp.append("<td>");
                temp.append(userInfoMap.get(key));
                temp.append("</td>");
                temp.append("</tr>");
            }
            temp.append("</table>");
            return temp.toString();
        }
        return userInfo;
    }


    public String logOutSession() {
        FacesContext fctx = FacesContext.getCurrentInstance();
        HttpServletRequest request =
            (HttpServletRequest)fctx.getExternalContext().getRequest();
        HttpServletResponse response =
            (HttpServletResponse)fctx.getExternalContext().getResponse();
        //invalidate ICX session & HTTP session
        
        Map sessionScope = ADFContext.getCurrent().getSessionScope();
        AppsRequestWrapper wrappedRequest = (AppsRequestWrapper)sessionScope.get("AppsRequestWrapper");
        
        Session session = wrappedRequest.getAppsSession();
        //logout only if it is present
        if (session != null) {
            AppsSessionHelper helper =
                new AppsSessionHelper(wrappedRequest.getEbizInstance());
            helper.destroyAppsSession(session,
                                      wrappedRequest, response);
        }
        request.getSession(true).invalidate();
        
        String ebsUrl =
            wrappedRequest.getEbizInstance().getAppsServletAgent();
        ebsUrl =
                ebsUrl + "AppsLogin"; // This url will redirect to SSO Url.
        System.out.println("ebsUrl = " + ebsUrl);
        try {
            response.sendRedirect(ebsUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        fctx.responseComplete();

        return null;
    }

  
}
