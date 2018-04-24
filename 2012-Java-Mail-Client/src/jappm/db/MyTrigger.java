/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jappm.db;

import java.sql.Connection;
import java.sql.SQLException;
import org.h2.api.Trigger;

/**
 *
 * @author Ivan
 */
public class MyTrigger implements Trigger {

    DBStatement db;

    @Override
    public void init(Connection conn, String schemaName, String triggerName, String tableName, boolean before, int type) {
        // Initializing trigger
    }

    public void fire(Connection conn,
            Object[] oldRow, Object[] newRow)
            throws SQLException {


        Integer diff = null;

        if (newRow != null) {
            diff = (Integer) newRow[1];
        }
        if (oldRow != null) {
            diff = (Integer) oldRow[1];
           
        }

        try{
        db.execSelectStatement(
                " delete from  JAPPM_FOLDERS where FOLDER_ID= ?",
                diff);
        }catch(Exception e){
            
        }

    }

    @Override
    public void remove() throws SQLException {
      //  throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void close() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
