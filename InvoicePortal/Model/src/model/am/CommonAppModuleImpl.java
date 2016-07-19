package model.am;

import model.am.common.CommonAppModule;

import model.view.ApInvoiceSearchVVOImpl;

import oracle.jbo.SessionData;
import oracle.jbo.server.ApplicationModuleImpl;
import oracle.jbo.server.ViewLinkImpl;
import oracle.jbo.server.ViewObjectImpl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import java.sql.Statement;
import java.sql.Types;

import javax.servlet.ServletContext;

import model.am.util.EBizHelper;

import oracle.adf.share.ADFContext;

import oracle.apps.fnd.ext.common.EBiz;

import oracle.jbo.JboException;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Wed Jun 22 11:08:48 CEST 2016
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class CommonAppModuleImpl extends ApplicationModuleImpl implements CommonAppModule {
    /**
     * This is the default constructor (do not remove).
     */
    public CommonAppModuleImpl() {
    }

    /**
     * Container's getter for ApInvoicesInterfaceVVO1.
     * @return ApInvoicesInterfaceVVO1
     */
    public ViewObjectImpl getApInvoicesInterfaceVVO() {
        return (ViewObjectImpl)findViewObject("ApInvoicesInterfaceVVO");
    }

    @Override
    public void prepareSession(SessionData sessionData) {
        String userId =
            (String)ADFContext.getCurrent().getSessionScope().get("SESSION_ATTR_USER");
        String responsibility =
            (String)ADFContext.getCurrent().getSessionScope().get("SESSION_ATTR_RESPONSIBILITY");
        System.out.println(" prepareSession " + userId + responsibility);
        super.prepareSession(sessionData);
        userId = "MIRACLE";
        responsibility = "Systemadministrator";
        setDBContext(userId, responsibility);
    }


    /**Method to call stored PL/SQl function to get Employee Name
     * @param empId
     * @return
     */
    public void setDBContext(String userId, String responsibility) {
        String plsql = "DECLARE\n" +
            "\n" +
            "  l_user_name VARCHAR2(1000) := null;                 -- Oracle eBusiness Suite user login\n" +
            "  l_resp_name VARCHAR2(1000) := null;     -- Oracle eBusiness Suite responsibility\n" +
            "\n" +
            "  l_user_id      NUMBER;\n" +
            "  l_resp_id      NUMBER;\n" +
            "  l_resp_appl_id NUMBER;\n" +
            "  \n" +
            "BEGIN\n" +
            "  l_user_name := ?; \n " + "l_resp_name := ?; \n" +
            "SELECT fu.user_id\n" +
            "     INTO l_user_id\n" +
            "     FROM fnd_user fu\n" +
            "    WHERE fu.user_name LIKE l_user_name;\n" +
            "\n" +
            "   SELECT res.application_id, \n" +
            "          res.responsibility_id\n" +
            "     INTO l_resp_appl_id, \n" +
            "          l_resp_id\n" +
            "     FROM fnd_responsibility_tl res\n" +
            "    WHERE res.responsibility_name LIKE l_resp_name\n" +
            "      AND res.language = 'DK';\n" +
            "\n" +
            "\n" +
            "   dbms_output.put_line('l_user_id=' || l_user_id);\n" +
            "   dbms_output.put_line('l_resp_id=' || l_resp_id);\n" +
            "   dbms_output.put_line('l_resp_appl_id=' || l_resp_appl_id);\n" +
            "\n" +
            "   fnd_global.apps_initialize(user_id            => l_user_id,\n" +
            "                              resp_id            => l_resp_id,\n" +
            "                              resp_appl_id       => l_resp_appl_id,\n" +
            "                              security_group_id  => 0,\n" +
            "                              server_id          => -1);\n" +
            "                              \n" +
            "   MO_GLOBAL.INIT('M');\n" +
            "                              \n" +
            "   dbms_output.put_line('initialized');\n" +
            "\n" +
            "   /*\n" +
            "   -- sessions can be found in the database using this SQL:\n" +
            "      SELECT ises.xsid, ises.creation_date, ises.last_update_date, fff.function_name\n" +
            "      FROM icx_sessions ises\n" +
            "      JOIN fnd_user fu ON (fu.user_id = ises.user_id)\n" +
            "      JOIN fnd_form_functions fff ON (fff.function_id = ises.function_id)\n" +
            "     WHERE fu.user_name = 'MIRACLE'\n" +
            "     ORDER BY ises.last_update_date DESC\n" +
            "   */\n" +
            "\n" +
            "EXCEPTION\n" +
            "   WHEN OTHERS THEN\n" +
            "       dbms_output.put_line('Error when initializing: ' ||SQLERRM);\n" +
            "END;\n";


        CallableStatement cst = null;
        try {
            //Creating sql statement
            cst = this.getDBTransaction().createCallableStatement(plsql, 0);
            cst.setString(1, userId);
            cst.setString(2, responsibility);

            cst.executeUpdate();
        } catch (SQLException e) {
            throw new JboException(e.getMessage());
        } finally {
            if (cst != null) {
                try {
                    cst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void checkAMConn() {
        System.out.println(" checkAMConn called");
    }

    /**
     * Container's getter for InvoiceCopyPrintVO.
     * @return InvoiceCopyPrintVO
     */
    public ViewObjectImpl getInvoiceCopyPrintVO() {
        return (ViewObjectImpl)findViewObject("InvoiceCopyPrintVO");
    }

    /**
     * Container's getter for ApInvoicesChildLinesVO.
     * @return ApInvoicesChildLinesVO
     */
    public ViewObjectImpl getApInvoicesChildLinesVO() {
        return (ViewObjectImpl)findViewObject("ApInvoicesChildLinesVO");
    }

    /**
     * Container's getter for ApInvoiceLinesVL1.
     * @return ApInvoiceLinesVL1
     */
    public ViewLinkImpl getApInvoiceLinesVL1() {
        return (ViewLinkImpl)findViewLink("ApInvoiceLinesVL1");
    }

    /**
     * Method To Get DB Connection Object
     * @return Connection
     */
    public Connection getConnFromDS() {
        Statement st = getDBTransaction().createStatement(0);
        Connection conn = null;
        try {
            conn = st.getConnection();
            st.close();
        } catch (SQLException ex) {
            throw (new JboException(ex));
        }
        return conn;
    }

    /**
     * Method To Retrive EBIZ Object
     * @return
     */
    public EBiz getEBiz() {
        ServletContext servContext =
            (ServletContext)ADFContext.getCurrent().getEnvironment().getContext();
        String applServerID = servContext.getInitParameter("APPL_SERVER_ID");
        return EBizHelper.getEBizInstance(getConnFromDS(), applServerID);
    }

    /**
     * Container's getter for UserAccessibleGroupsLOV.
     * @return UserAccessibleGroupsLOV
     */
    public ViewObjectImpl getUserAccessibleGroupsLOV() {
        return (ViewObjectImpl)findViewObject("UserAccessibleGroupsLOV");
    }

    /**
     * Container's getter for InvoiceAttachmentsListVO.
     * @return InvoiceAttachmentsListVO
     */
    public ViewObjectImpl getInvoiceAttachmentsListVO() {
        return (ViewObjectImpl)findViewObject("InvoiceAttachmentsListVO");
    }

    /**
     * Container's getter for InvoiceAttachmentsVL1.
     * @return InvoiceAttachmentsVL1
     */
    public ViewLinkImpl getInvoiceAttachmentsVL1() {
        return (ViewLinkImpl)findViewLink("InvoiceAttachmentsVL1");
    }

    /**
     * Container's getter for ApInvoiceSearchVVO1.
     * @return ApInvoiceSearchVVO1
     */
    public ApInvoiceSearchVVOImpl getApInvoiceSearchVVO() {
        return (ApInvoiceSearchVVOImpl)findViewObject("ApInvoiceSearchVVO");
    }

    /**
     * Container's getter for SupplierNameLOV1.
     * @return SupplierNameLOV1
     */
    public ViewObjectImpl getSupplierNameLOV() {
        return (ViewObjectImpl)findViewObject("SupplierNameLOV");
    }

    /**
     * Container's getter for SupplierNumberLOV.
     * @return SupplierNumberLOV
     */
    public ViewObjectImpl getSupplierNumberLOV() {
        return (ViewObjectImpl)findViewObject("SupplierNumberLOV");
    }

    /**
     * Container's getter for InvoiceCurrencyCodeLOV.
     * @return InvoiceCurrencyCodeLOV
     */
    public ViewObjectImpl getInvoiceCurrencyCodeLOV() {
        return (ViewObjectImpl)findViewObject("InvoiceCurrencyCodeLOV");
    }
}
