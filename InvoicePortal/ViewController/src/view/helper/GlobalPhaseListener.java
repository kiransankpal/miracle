package view.helper;

import java.sql.Connection;

import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import javax.el.ELContext;
import javax.el.ExpressionFactory;

import javax.el.ValueExpression;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.am.CommonAppModuleImpl;

import oracle.adf.controller.v2.lifecycle.Lifecycle;
import oracle.adf.controller.v2.lifecycle.PagePhaseEvent;
import oracle.adf.controller.v2.lifecycle.PagePhaseListener;
import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCDataControl;
import oracle.adf.share.ADFContext;

import oracle.apps.fnd.ext.common.AppsRequestWrapper;
import oracle.apps.fnd.ext.common.CookieStatus;
import oracle.apps.fnd.ext.common.Session;

import oracle.jbo.JboException;


public class GlobalPhaseListener implements PagePhaseListener {
    private static final Logger logger =
        Logger.getLogger(GlobalPhaseListener.class.getName());


    public GlobalPhaseListener() {
        super();
    }

    /**
     * Before Phase Listener
     * Calling two methods checkEBSSession and EBSLocale
     * @param pagePhaseEvent
     */
    public void beforePhase(PagePhaseEvent pagePhaseEvent) {
        logger.info("In before Phase, " +
                    Lifecycle.getPhaseName(pagePhaseEvent.getPhaseId()));
    }

    /**
     * Method To Check EBS Session and Extarct EBS Cookie
     * Calling two methods checkEBSSession and EBSLocale
     */
    private Session checkEBSSession() {
        Session sessionEBS = null;
        AppsRequestWrapper wrappedRequest = null;
        Connection conn = null;
        FacesContext fctx = FacesContext.getCurrentInstance();
        HttpServletRequest request =
            (HttpServletRequest)fctx.getExternalContext().getRequest();
        HttpServletResponse response =
            (HttpServletResponse)fctx.getExternalContext().getResponse();
        try {
            conn = getCommonAppModuleImpl().getConnFromDS();
            wrappedRequest =
                    new AppsRequestWrapper(request, response, conn, getCommonAppModuleImpl().getEBiz());
            sessionEBS = wrappedRequest.getAppsSession(true);
            if (!isAuthenticated(sessionEBS)) {
                logger.info("No valid FND user ICX session exists" +
                            " currently. Redirecting to EBS AppsLogin page");
                String ebsUrl =
                    wrappedRequest.getEbizInstance().getAppsServletAgent();
                ebsUrl =
                        ebsUrl + "AppsLogin"; // This url will redirect to SSO Url.
                logger.info("ebsUrl = " + ebsUrl);
                response.sendRedirect(ebsUrl);
                fctx.responseComplete();
                return null;
            } else {
                logger.info("We got a valid ICX session. Proceeding.");
                String currentUser = sessionEBS.getUserName();
                logger.info("currentUser:" + currentUser);
                String currentUserId = sessionEBS.getUserId();
                logger.info("currentUserId:" + currentUserId);
                Map<String, String> sessionInfo = sessionEBS.getInfo();
                Integer respId =
                    Integer.parseInt(sessionInfo.get("RESPONSIBILITY_ID"));
                logger.info("respId:" + respId);
                Integer respApplId =
                    Integer.parseInt(sessionInfo.get("RESPONSIBILITY_APPLICATION_ID"));
                logger.info("respApplId:" + respApplId);

                Map sessionScope = ADFContext.getCurrent().getSessionScope();
                sessionScope.put("AppsRequestWrapper", wrappedRequest);
                FndUserBean fndUser =
                    (FndUserBean)sessionScope.get("fndUserBean");
                if (fndUser == null) {
                    fndUser = new FndUserBean();
                    fndUser.setUserName(currentUser);
                    fndUser.setUserId(Integer.parseInt(currentUserId));
                    fndUser.setRespId(respId);
                    fndUser.setRespApplId(respApplId);
                    fndUser.setUserInfo(sessionInfo);
                    logger.info("fndUserBean is put into session bean");
                    sessionScope.put("fndUserBean", fndUser);
                } else {
                    if (fndUser.getRespId() != respId) {
                        fndUser.setRespId(respId);
                        fndUser.setRespApplId(respApplId);
                        logger.info("Responsibility is changed in fndUserBean");
                    }
                }
            }
            return sessionEBS;
        } catch (Exception ex) {
            logger.severe(ex.getMessage());
            throw (new JboException(ex));
        }
    }

    /**
     * After Phase Listener
     * Calling two methods checkEBSSession and EBSLocale
     * @param pagePhaseEvent
     */
    public void afterPhase(PagePhaseEvent pagePhaseEvent) {
        logger.info("afterPhase-->" +
                    Lifecycle.getPhaseName(pagePhaseEvent.getPhaseId()));
        if (pagePhaseEvent.getPhaseId() == Lifecycle.INIT_CONTEXT_ID) {
            logger.info("Checking for EBS Session in afterPhase");
             Session ebsSession = checkEBSSession();
             setEBSLocale(ebsSession);
        }
    }

    /**
     * Method to Check Cookie Status
     * @param sessionEBS
     * @return
     */
    private boolean isAuthenticated(Session sessionEBS) {
        CookieStatus icxCookieStatus = null;
        icxCookieStatus = sessionEBS.getCurrentState().getIcxCookieStatus();
        {
            if (!icxCookieStatus.equals(CookieStatus.VALID)) {
                logger.info("Login Required");
                return false;
            }
        }
        return true;
    }


    /**
     * Method to Get Application Module
     * @return
     */
    public CommonAppModuleImpl getCommonAppModuleImpl() {
        BindingContext bctx = BindingContext.getCurrent();
        DCDataControl dc = bctx.findDataControl("CommonAppModuleDataControl");
        CommonAppModuleImpl commonAM = null;
        if (dc != null) {
            commonAM = (CommonAppModuleImpl)dc.getDataProvider();
        } else {
            commonAM =
                    (CommonAppModuleImpl)resolvElDC("CommonAppModuleDataControl");
        }
        if (commonAM != null) {
            return commonAM;
        }
        return null;
    }

    /**
     * Method for Setting EBS Locale
     * @param ebsSession
     */
    private void setEBSLocale(Session ebsSession) {
        if (ebsSession != null) {
            Locale locale = ebsSession.getLocale();
            if (FacesContext.getCurrentInstance().getViewRoot() != null)
                FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
        }

    }

    private Object resolvElDC(String data) {
        FacesContext fc = FacesContext.getCurrentInstance();
        Application app = fc.getApplication();
        ExpressionFactory elFactory = app.getExpressionFactory();
        ELContext elContext = fc.getELContext();
        ValueExpression valueExp =
            elFactory.createValueExpression(elContext, "#{data." + data +
                                            ".dataProvider}", Object.class);
        return valueExp.getValue(elContext);
    }

}

