package model.am.util;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.apps.fnd.ext.common.EBiz;


public class EBizHelper {
    private static final Logger logger =
        Logger.getLogger(EBizHelper.class.getName());

    private static EBiz INSTANCE = null;

    public static EBiz getEBizInstance(Connection connection,String application_id) {
        if (INSTANCE == null) {
            try {
                INSTANCE =
                        new EBiz(connection,application_id);
            } catch (SQLException e) {
                logger.log(Level.SEVERE,
                           "SQLException while creating EBiz instance -->", e);
                throw new RuntimeException(e);
            } catch (Exception e) {
                logger.log(Level.SEVERE,
                           "Exception while creating EBiz instance -->", e);
                throw new RuntimeException(e);
            } 
        }
        return INSTANCE;
    }
}
