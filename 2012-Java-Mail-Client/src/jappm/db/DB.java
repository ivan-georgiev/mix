/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jappm.db;

/**
 *
 * @author Ivan
 */
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.h2.jdbcx.JdbcDataSource;

public class DB {

    private static Connection connection = null;
    private JdbcDataSource dataSource = new JdbcDataSource();
    //private static String driverName = "org.h2.Driver";

    /**
     * Constructor
     */
    protected DB() throws SQLException, ClassNotFoundException {
    }

    /**
     * 
     * @param userN
     * @param passW
     * @param dBlocation 
     */
    protected void initDbObject(String userN, String passW, String dBlocation) {

        dataSource.setURL("jdbc:h2:file:" + dBlocation);
        dataSource.setUser(userN);
        dataSource.setPassword(passW);
    }

    public void close() throws SQLException {
        if (connection.isValid(0)) {
            connection.close();
        }
    }

    public void open() throws SQLException, ClassNotFoundException {

        if (connection == null || connection.isClosed()) {

            // Class.forName(driverName);
            connection = dataSource.getConnection();
        }
    }

    /**
     * Match java data types
     */
    public enum classType {

        STRING,
        INTEGER,
        INT,
        DOUBLE,
        FLOAT,
        DATE,
        LONG,
        BYTES,
        BOOLEAN,
        NOVALUE;

        public static classType toType(String str) {
            try {
                //String lastWord = str.substring(str.lastIndexOf('.') + 1);
                //return valueOf(lastWord.toUpperCase());
                String type = str.replace("[]", "s");
                return valueOf(type.toUpperCase());
            } catch (Exception ex) {
                return NOVALUE;
            }
        }
    }

    /**
     * Add all parameters in PreparedStatement
     */
    private int InitPreparedStatement(PreparedStatement prepStat, Object... Params) throws SQLException {

        String typeParam = null;
        Integer numParam = 0;
        java.util.Date dt = null;
        long t = 0;
        Object empty = null;

        for (Object param : Params) {
            numParam++;

            if (param == null) {
                // parameter is empty
                prepStat.setInt(numParam, (Integer) empty);
                continue;
            }
            // typeParam = param.getClass().toString();
            // typeParam = param.getClass().getCanonicalName();

            typeParam = param.getClass().getSimpleName();

            switch (classType.toType(typeParam)) {
                case STRING:
                    prepStat.setString(numParam, (String) param);
                    break;
                case INTEGER:
                    prepStat.setInt(numParam, (Integer) param);
                    break;
                case INT:
                    prepStat.setInt(numParam, (Integer) param);
                    break;
                case DOUBLE:
                    prepStat.setDouble(numParam, (Double) param);
                    break;
                case FLOAT:
                    prepStat.setFloat(numParam, (Float) param);
                    break;
                case LONG:
                    prepStat.setLong(numParam, (Long) param);
                    break;
                case BYTES:
                    prepStat.setBytes(numParam, (byte[]) param);
                    break;
                case BOOLEAN:
                    prepStat.setBoolean(numParam, (Boolean) param);
                    break;
                case DATE: {

                    dt = (java.util.Date) param;
                    t = dt.getTime();

                    java.sql.Timestamp dt_sql = new java.sql.Timestamp(t);
                    prepStat.setTimestamp(numParam, dt_sql);
                    break;

                }
            }

        }

        return (0);

    }

    protected ResultSet execSelectStatement(String aStatement, Object... Params) throws SQLException {

        try {
            this.open();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }

        ResultSet rs = null;

        PreparedStatement prepStat = connection.prepareStatement(aStatement);

        InitPreparedStatement(prepStat, Params);

        rs = prepStat.executeQuery();

        prepStat = null;

        return (rs);
    }

    /**
     * Execute Insert/Update/Delete statement
     */
    protected Integer execApplyStatement(String aStatement, Object... Params) throws SQLException {

        try {
            this.open();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }

        Integer rs = null;

        PreparedStatement prepStat = connection.prepareStatement(aStatement);

        InitPreparedStatement(prepStat, Params);

        rs = prepStat.executeUpdate();

        prepStat = null;


        return (rs);
    }

    public Integer getLastInsertedId() throws SQLException {

        try {
            this.open();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }

        ResultSet rs = null;
        Integer ID = 0;

        String queryStr = "select LAST_INSERT_ID()";
        Statement prepStat = connection.createStatement();

        rs = prepStat.executeQuery(queryStr);

        prepStat = null;
        if (rs.next()) {
            ID = rs.getInt(1);
        }

        return (ID);
    }
}
