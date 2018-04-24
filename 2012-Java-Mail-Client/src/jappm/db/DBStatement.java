/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jappm.db;

import jappm.Controller;
import jappm.JAPPAccountSettings;
import jappm.JAPPAddress;
import jappm.JAPPMessage;
import jappm.TripleDESEncrypter;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Ivan
 */
public class DBStatement extends DB {

    TripleDESEncrypter desEncrypter;

    public DBStatement(String userN, String passW, String dBlocation) throws SQLException, ClassNotFoundException {


        File file = new File(dBlocation + ".h2.db");

        if (file.exists()) {
            this.initDbObject(userN, passW, dBlocation);
        } else {
            this.initDbObject(userN, passW, dBlocation);
            recreateDatabase();
        }

        desEncrypter = new TripleDESEncrypter();

        //this.execApplyStatement("drop TRIGGER delete_hier_tr ");
        // this.execApplyStatement("CREATE TRIGGER delete_hier_tr BEFORE DELETE ON JAPPM_FOLDERS_TREE  FOR EACH ROW CALL \"jappm.db.MyTrigger\"");

    }

    public final void recreateDatabase() throws SQLException {
        try {


            this.execApplyStatement(" DROP TABLE JAPPM_SERVER_TYPES ");
            this.execApplyStatement(" DROP TABLE JAPPM_ACCOUNTS ");
            this.execApplyStatement(" DROP TABLE JAPPM_FOLDERS ");
            this.execApplyStatement(" DROP TABLE JAPPM_FOLDERS_TREE ");
            this.execApplyStatement(" DROP TABLE JAPPM_ACCOUNT_SETTINGS ");
            this.execApplyStatement(" DROP TABLE JAPPM_MESSAGES ");
            this.execApplyStatement(" DROP TABLE JAPPM_PRIORITIES ");
            this.execApplyStatement(" DROP TABLE JAPPM_ADDRESSBOOK ");


        } catch (SQLException ex) {
            // Logger.getLogger(DBStatement.class.getName()).log(Level.SEVERE, null, ex);
        }





        this.execApplyStatement(" CREATE CACHED TABLE JAPPM_SERVER_TYPES (              "
                + "   SERVER_TYPE_ID INTEGER NOT NULL AUTO_INCREMENT UNIQUE,                         "
                + "   SERVER_TYPE_NAME VARCHAR(20) NOT NULL UNIQUE,                                  "
                + "   SERVER_TYPE_PROTOCOL VARCHAR(20) NOT NULL,                                     "
                + "   CONSTRAINT JAPPM_SERVER_TYPES_PK                                               "
                + "   PRIMARY KEY (SERVER_TYPE_ID)  )                                                 ");


        this.execApplyStatement(" CREATE CACHED TABLE JAPPM_PRIORITIES(               "
                + "   PRIORITY_ID INTEGER NOT NULL AUTO_INCREMENT UNIQUE,                            "
                + "   PRIORITY_NAME VARCHAR(20) NOT NULL UNIQUE,                                     "
                + "   CONSTRAINT JAPPM_PRIORITIES_PK                                                 "
                + "   PRIMARY KEY (PRIORITY_ID)                                                     )");

        this.execApplyStatement(" CREATE CACHED TABLE JAPPM_ACCOUNTS (                 "
                + "   ACCOUNT_ID INTEGER NOT NULL AUTO_INCREMENT UNIQUE,                             "
                + "   ACCOUNT_NAME VARCHAR(50) NOT NULL UNIQUE,                                      "
                + "   CONSTRAINT JAPPM_ACCOUNTS_PK                                                   "
                + "   PRIMARY KEY (ACCOUNT_ID)    ) ");

        this.execApplyStatement(" CREATE CACHED TABLE JAPPM_FOLDERS (                   "
                + "   FOLDER_ID INTEGER NOT NULL AUTO_INCREMENT UNIQUE,                              "
                + "   ACCOUNT_ID INTEGER,                                                            "
                + "   FOLDER_NAME VARCHAR(50) NOT NULL,                                              "
                + "   IMAP_UIDVALIDITY BIGINT NOT NULL DEFAULT 0,                                    "
                + "   VISIBLE BOOLEAN NOT NULL DEFAULT TRUE,                                         "
                + "   EDITABLE BOOLEAN NOT NULL DEFAULT TRUE,                                        "
                + "   CONSTRAINT JAPPM_FOLDERS_PK                                                    "
                + "   PRIMARY KEY (FOLDER_ID),                                                       "
                + "   CONSTRAINT JAPPM_FOLDERS_ACCOUNT_ID_FK                                         "
                + "   FOREIGN KEY (ACCOUNT_ID) REFERENCES JAPPM_ACCOUNTS (ACCOUNT_ID)                "
                + "   ON DELETE CASCADE ON UPDATE CASCADE                                            "
                + "   )");


        this.execApplyStatement(" CREATE CACHED TABLE JAPPM_FOLDERS_TREE (              "
                + "  LINK_ID INTEGER NOT NULL AUTO_INCREMENT UNIQUE,                                 "
                + "  FOLDER_ID INTEGER,                                                              "
                + "  PARENT_ID INTEGER,                                                              "
                + "  CONSTRAINT JAPPM_FOLDERS_TREE_PK                                                "
                + "  PRIMARY KEY (LINK_ID),                                                          "
                + "  CONSTRAINT JAPPM_FOLDERS_TREE_FOLDER_ID_FK                                      "
                + "   FOREIGN KEY (FOLDER_ID) REFERENCES JAPPM_FOLDERS (FOLDER_ID)                   "
                + "   ON DELETE CASCADE ON UPDATE CASCADE,                                           "
                + "  CONSTRAINT JAPPM_FOLDERS_TREE_PARENT_ID_FK                                      "
                + "   FOREIGN KEY (PARENT_ID) REFERENCES JAPPM_FOLDERS (FOLDER_ID)                   "
                + "   ON DELETE CASCADE ON UPDATE CASCADE,                                           "
                + "  CONSTRAINT JAPPM_FOLDERS_TREE_UQ1 UNIQUE(FOLDER_ID , PARENT_ID)                 "
                + "   )");

        this.execApplyStatement(" CREATE CACHED TABLE JAPPM_ACCOUNT_SETTINGS (         "
                + "   SETTING_ID INTEGER NOT NULL AUTO_INCREMENT UNIQUE,                             "
                + "   ACCOUNT_ID INTEGER NOT NULL UNIQUE,                                            "
                + "   NAME VARCHAR(70),                                                              "
                + "   EMAIL VARCHAR(70),                                                             "
                + "   SERVER_IN VARCHAR(50),                                                         "
                + "   SERVER_OUT VARCHAR(50),                                                        "
                + "   USER_IN VARCHAR(50),                                                           "
                + "   USER_OUT VARCHAR(50),                                                          "
                + "   SERVER_IN_TYPE_ID INTEGER,                                                     "
                + "   SERVER_IN_IS_SSL BOOLEAN NOT NULL DEFAULT FALSE,                               "
                + "   SERVER_OUT_IS_SSL BOOLEAN NOT NULL DEFAULT FALSE,                              "
                + "   SERVER_IN_PORT INTEGER,                                                        "
                + "   SERVER_OUT_PORT INTEGER,                                                       "
                + "   PASS_IN VARCHAR(100),                                                          "
                + "   PASS_OUT VARCHAR(100),                                                         "
                + "   CONSTRAINT JAPPM_ACCCOUNT_SETTINGS_PK                                          "
                + "   PRIMARY KEY (SETTING_ID),                                                      "
                + "   CONSTRAINT JAPPM_ACCCOUNT_SETTINGS_ACCOUNT_ID_FK                               "
                + "   FOREIGN KEY (ACCOUNT_ID) REFERENCES JAPPM_ACCOUNTS (ACCOUNT_ID)                "
                + "   ON DELETE CASCADE ON UPDATE CASCADE,                                           "
                + "   CONSTRAINT JAPPM_ACCCOUNT_SETTINGS_SERVER_IN_TYPE_ID_FK                        "
                + "   FOREIGN KEY (SERVER_IN_TYPE_ID) REFERENCES JAPPM_SERVER_TYPES (SERVER_TYPE_ID) "
                + "   ON DELETE CASCADE ON UPDATE CASCADE                                            "
                + "  )");




        this.execApplyStatement(" CREATE CACHED TABLE JAPPM_MESSAGES (                  "
                + "  MESSAGE_ID INTEGER NOT NULL AUTO_INCREMENT UNIQUE,                              "
                + "  ACCOUNT_ID INTEGER DEFAULT 1,                                                   "
                + "  FOLDER_ID INTEGER,                                                              "
                + "  IMAP_UID BIGINT NOT NULL DEFAULT 0,                                             "
                + "  F_SEEN BOOLEAN NOT NULL DEFAULT FALSE,                                          "
                + "  H_SUBJECT VARCHAR(255),                                                         "
                + "  H_DT_SENT DATETIME,                                                             "
                + "  H_FROM VARCHAR(255),                                                            "
                + "  H_TO CLOB,                                                                      "
                + "  H_CC CLOB,                                                                      "
                + "  H_BCC VARCHAR(255),                                                             "
                + "  H_PRIORITY_ID INTEGER,                                                          "
                + "  M_ATTACHMENTS BOOLEAN NOT NULL DEFAULT FALSE,                                   "
                + "  M_PLAINTEXTBODY CLOB,                                                           "
                + "  M_RAW LONGBLOB,                                                                 "
                + "  DT_LASTMODIF TIMESTAMP,                                                         "
                + "   CONSTRAINT JAPPM_MESSAGES_PK                                                   "
                + "   PRIMARY KEY (MESSAGE_ID),                                                      "
                + "   CONSTRAINT JAPPM_MESSAGES_FOLDER_ID_FK                                         "
                + "   FOREIGN KEY (FOLDER_ID) REFERENCES JAPPM_FOLDERS (FOLDER_ID)                   "
                + "   ON DELETE CASCADE ON UPDATE CASCADE,                                           "
                + "   CONSTRAINT JAPPM_MESSAGES_ACCOUNT_ID_FK                                        "
                + "   FOREIGN KEY (ACCOUNT_ID) REFERENCES JAPPM_ACCOUNTS (ACCOUNT_ID)                "
                + "   ON DELETE CASCADE ON UPDATE CASCADE                                            "
                + "  )");

        this.execApplyStatement(" CREATE CACHED TABLE JAPPM_ADDRESSBOOK (               "
                + "  ADDRESS_ID INTEGER NOT NULL AUTO_INCREMENT UNIQUE,                              "
                + "  ACCOUNT_ID INTEGER DEFAULT 1,                                                   "
                + "  NAME_FIRST VARCHAR(255),                                                        "
                + "  NAME_LAST VARCHAR(255),                                                         "
                + "  EMAIL VARCHAR(255) NOT NULL,                                                    "
                + "  DT_LASTMODIF TIMESTAMP,                                                         "
                + "   CONSTRAINT JAPPM_ADDRESSBOOK_PK                                                "
                + "   PRIMARY KEY (ADDRESS_ID),                                                      "
                + "   CONSTRAINT JAPPM_ADDRESSBOOK_ACCOUNT_ID_FK                                     "
                + "   FOREIGN KEY (ACCOUNT_ID) REFERENCES JAPPM_ACCOUNTS (ACCOUNT_ID)                "
                + "   ON DELETE CASCADE ON UPDATE CASCADE                                            "
                + "  )");




        // DML INITILIZATION


        this.execApplyStatement(" INSERT INTO JAPPM_SERVER_TYPES ( SERVER_TYPE_PROTOCOL, SERVER_TYPE_NAME ) VALUES ('imap','IMAP') ");
        this.execApplyStatement(" INSERT INTO JAPPM_SERVER_TYPES ( SERVER_TYPE_PROTOCOL, SERVER_TYPE_NAME ) VALUES ('pop3','POP3') ");

        this.execApplyStatement(" INSERT INTO JAPPM_ACCOUNTS ( ACCOUNT_ID , ACCOUNT_NAME ) VALUES ( 1, 'JAPPM') ");

        this.execApplyStatement(" INSERT INTO JAPPM_FOLDERS ( FOLDER_ID , FOLDER_NAME , ACCOUNT_ID , EDITABLE ) VALUES (1,'JAPPM',1 ,FALSE) ");
        this.execApplyStatement(" INSERT INTO JAPPM_FOLDERS ( FOLDER_ID , FOLDER_NAME , ACCOUNT_ID , EDITABLE ) VALUES (2,'Outbox',1,FALSE) ");
        this.execApplyStatement(" INSERT INTO JAPPM_FOLDERS ( FOLDER_ID , FOLDER_NAME , ACCOUNT_ID , EDITABLE ) VALUES (3,'Drafts',1,FALSE) ");

        this.execApplyStatement(" INSERT INTO JAPPM_FOLDERS_TREE ( FOLDER_ID , PARENT_ID ) VALUES (1,1) ");
        this.execApplyStatement(" INSERT INTO JAPPM_FOLDERS_TREE ( FOLDER_ID , PARENT_ID ) VALUES (2,1) ");
        this.execApplyStatement(" INSERT INTO JAPPM_FOLDERS_TREE ( FOLDER_ID , PARENT_ID ) VALUES (3,1) ");

        this.execApplyStatement(" INSERT INTO JAPPM_PRIORITIES ( PRIORITY_ID , PRIORITY_NAME ) VALUES (1,'Low') ");
        this.execApplyStatement(" INSERT INTO JAPPM_PRIORITIES ( PRIORITY_ID , PRIORITY_NAME ) VALUES (2,'Normal') ");
        this.execApplyStatement(" INSERT INTO JAPPM_PRIORITIES ( PRIORITY_ID , PRIORITY_NAME ) VALUES (3,'High') ");


    }

    public ArrayList<JAPPAccountSettings> getMailAccountsSettings() throws SQLException {

        ArrayList<JAPPAccountSettings> result = new ArrayList();

        Integer settingsId;
        Integer accountId;
        String accountName;
        String name;
        String email;
        String userIn;
        String userOut;
        String serverIn;
        String serverOut;
        Integer serverInTypeId;
        String serverInType;
        boolean serverInIsSSL;
        boolean serverOutIsSSL;
        Integer serverInPort;
        Integer serverOutPort;
        String passwordIn;
        String passwordOut;



        ResultSet rs = null;

        //TODO Replace * with colum names
        rs = this.execSelectStatement(
                " SELECT *, JST.SERVER_TYPE_PROTOCOL as STP_IN "
                + " FROM JAPPM_ACCOUNT_SETTINGS  JAS                                                      "
                + " INNER JOIN JAPPM_ACCOUNTS  JA  ON ( JA.ACCOUNT_ID  = JAS.ACCOUNT_ID  )                       "
                + " INNER JOIN JAPPM_SERVER_TYPES  JST ON ( JAS.SERVER_IN_TYPE_ID   = JST.SERVER_TYPE_ID  )      ");
        //  + " INNER JOIN JAPPM_SERVER_TYPES  JST2 ON ( JAS.SERVER_OUT_TYPE_ID   = JST2.SERVER_TYPE_ID  )   ");

        while (rs.next()) {

            settingsId = rs.getInt("SETTING_ID");
            accountId = rs.getInt("ACCOUNT_ID");
            accountName = rs.getString("ACCOUNT_NAME");
            name = rs.getString("NAME");
            email = rs.getString("EMAIL");
            userIn = rs.getString("USER_IN");
            userOut = rs.getString("USER_OUT");
            serverIn = rs.getString("SERVER_IN");
            serverOut = rs.getString("SERVER_OUT");
            serverInTypeId = rs.getInt("SERVER_IN_TYPE_ID");
            serverInType = rs.getString("STP_IN");
            serverInIsSSL = rs.getBoolean("SERVER_IN_IS_SSL");
            serverOutIsSSL = rs.getBoolean("SERVER_OUT_IS_SSL");
            serverInPort = rs.getInt("SERVER_IN_PORT");
            serverOutPort = rs.getInt("SERVER_OUT_PORT");
            passwordIn = rs.getString("PASS_IN");
            passwordOut = rs.getString("PASS_OUT");


            result.add(new JAPPAccountSettings(settingsId,
                    accountId,
                    accountName,
                    name,
                    email,
                    userIn,
                    userOut,
                    serverIn,
                    serverOut,
                    serverInTypeId,
                    serverInType,
                    serverInIsSSL,
                    serverOutIsSSL,
                    serverInPort,
                    serverOutPort,
                    this.desEncrypter.decrypt(passwordIn),
                    this.desEncrypter.decrypt(passwordOut)));
        }


        return result;
    }

    public void putMailAccountsSettings(ArrayList<JAPPAccountSettings> mailAccountsSettings) throws SQLException {

        this.execApplyStatement("  DELETE FROM JAPPM_ACCOUNT_SETTINGS ");

        //TODO in progress

        for (JAPPAccountSettings jps : mailAccountsSettings) {


            if (jps.accountId == -1) {

                this.execApplyStatement(" INSERT INTO JAPPM_ACCOUNTS ( ACCOUNT_NAME ) values ( ? ) ",
                        jps.accountName);

                jps.accountId = this.getLastInsertedId();


                this.execApplyStatement(" INSERT INTO JAPPM_FOLDERS ( FOLDER_NAME, ACCOUNT_ID , EDITABLE ) VALUES ( ? , ? , FALSE ) ",
                        jps.accountName,
                        jps.accountId);

                int folderId = this.getLastInsertedId();

                this.execApplyStatement("INSERT INTO JAPPM_FOLDERS_TREE ( FOLDER_ID , PARENT_ID ) VALUES ( ? , 1 ); ",
                        folderId);

            }


            this.execApplyStatement("  INSERT INTO JAPPM_ACCOUNT_SETTINGS "
                    + " ( ACCOUNT_ID , NAME , EMAIL , SERVER_IN , SERVER_OUT , USER_IN , USER_OUT , "
                    + " SERVER_IN_TYPE_ID , SERVER_IN_IS_SSL , SERVER_OUT_IS_SSL , SERVER_IN_PORT , "
                    + " SERVER_OUT_PORT , PASS_IN , PASS_OUT  ) "
                    + " values (? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? )",
                    jps.accountId,
                    jps.name,
                    jps.email,
                    jps.serverIn,
                    jps.serverOut,
                    jps.userIn,
                    jps.userOut,
                    jps.serverInTypeId,
                    jps.serverInIsSSL,
                    jps.serverOutIsSSL,
                    jps.serverInPort,
                    jps.serverOutPort,
                    this.desEncrypter.encrypt(jps.passwordIn),
                    this.desEncrypter.encrypt(jps.passwordOut));

            int fodlerId = this.getMainFolderId(jps.accountId);

            this.execApplyStatement(" UPDATE JAPPM_ACCOUNTS SET ACCOUNT_NAME=? where ACCOUNT_ID=?",
                    jps.accountName,
                    jps.accountId);

            this.execApplyStatement(" UPDATE JAPPM_FOLDERS SET FOLDER_NAME=? where FOLDER_ID=? ",
                    jps.accountName,
                    fodlerId);

        }

    }

    public boolean folderCanBeModified(Integer folderId) throws SQLException {

        boolean result = true;

        ResultSet rs = null;

        rs = this.execSelectStatement(
                " SELECT "
                + " EDITABLE "
                + " FROM JAPPM_FOLDERS "
                + " WHERE FOLDER_ID=? ",
                folderId);

        if (rs.next()) {

            result = rs.getBoolean("EDITABLE");
        }

        return result;

    }

    public ArrayList<JAPPAddress> getAddresses() throws SQLException {

        ArrayList<JAPPAddress> result = new ArrayList();

        String nameFirst;
        String nameLast;
        String email;


        ResultSet rs = null;

        rs = this.execSelectStatement(
                " SELECT NAME_FIRST, NAME_LAST, EMAIL "
                + " FROM JAPPM_ADDRESSBOOK ");

        while (rs.next()) {


            nameLast = rs.getString("NAME_LAST");
            nameFirst = rs.getString("NAME_FIRST");
            email = rs.getString("EMAIL");


            result.add(new JAPPAddress(
                    nameFirst,
                    nameLast,
                    email));
        }


        return result;
    }

    public void putAddresses(ArrayList<JAPPAddress> addresses) throws SQLException {

        this.execApplyStatement("  DELETE FROM JAPPM_ADDRESSBOOK ");


        for (JAPPAddress jps : addresses) {

            this.execApplyStatement("  INSERT INTO JAPPM_ADDRESSBOOK "
                    + " ( NAME_FIRST , NAME_LAST , EMAIL )"
                    + " values (? , ? , ? )",
                    jps.nameFirst,
                    jps.nameLast,
                    jps.email);

        }

    }

    public ArrayList<JAPPMessage> getMessages(Integer folderId) throws SQLException {

        //TODO in progress

        ArrayList<JAPPMessage> result = new ArrayList();
        JAPPMessage m;

        ResultSet rs = null;

        //TODO Replace * with colum names
        rs = this.execSelectStatement(
                " SELECT "
                + " * "
                + " FROM JAPPM_MESSAGES "
                + " WHERE FOLDER_ID=? ",
                folderId);

        while (rs.next()) {

            m = this.createJappMessageFromResultSet(rs);

            result.add(m);
        }

        return result;
    }

    public JAPPMessage getMessage(Integer messageId) throws SQLException {


        JAPPMessage m = null;

        ResultSet rs = null;

        //TODO Replace * with colum names
        rs = this.execSelectStatement(
                " SELECT "
                + " * "
                + " FROM JAPPM_MESSAGES "
                + " WHERE MESSAGE_ID=? ",
                messageId);

        if (rs.next()) {

            m = this.createJappMessageFromResultSet(rs);

        }

        return m;
    }

    private JAPPMessage createJappMessageFromResultSet(ResultSet rs) throws SQLException {

        JAPPMessage m = null;

        Integer messageId;
        Long uid;
        String subject;
        String from;
        String to;
        String cc;
        Date dateSent;
        byte[] rawMessage;
        Boolean attachments;
        Integer folderId;
        Integer accountId;
        Boolean seen;
        //Not implemented yet
        Integer priority;
        String plaintextBody;
        String bcc;

        messageId = rs.getInt("MESSAGE_ID");
        subject = rs.getString("H_SUBJECT");
        from = rs.getString("H_FROM");
        to = rs.getString("H_TO");
        cc = rs.getString("H_CC");
        dateSent = rs.getTimestamp("H_DT_SENT");
        folderId = rs.getInt("FOLDER_ID");
        accountId = rs.getInt("ACCOUNT_ID");
        uid = rs.getLong("IMAP_UID");
        rawMessage = rs.getBytes("M_RAW");
        attachments = rs.getBoolean("M_ATTACHMENTS");
        seen = rs.getBoolean("F_SEEN");

        m = new JAPPMessage(folderId,
                uid,
                subject,
                from,
                to,
                cc,
                dateSent);

        m.setMessageId(messageId);

        m.setRawMessage(rawMessage);

        m.setAccountId(accountId);

        if (seen) {
            m.setSeen();
        }

        if (attachments) {
            m.setAttachmentsAvaliable();
        }

        return m;

    }

    public ArrayList<JAPPMessage> getMessages(ArrayList<Integer> messageIDs) throws SQLException {

        ArrayList<JAPPMessage> result = new ArrayList();

        if (messageIDs == null) {
            return result;
        }

        for (Integer id : messageIDs) {

            result.add(this.getMessage(id));

        }

        return result;
    }

    public ArrayList<Long> getFolderUIDs(Integer folderId) throws SQLException {


        //TODO in progress

        ArrayList<Long> result = new ArrayList();


        ResultSet rs = null;
        Long uid;

        //TODO Replace * with colum names
        rs = this.execSelectStatement(
                " SELECT "
                + " IMAP_UID "
                + " FROM JAPPM_MESSAGES "
                + " WHERE FOLDER_ID=? ",
                folderId);

        while (rs.next()) {


            uid = rs.getLong("IMAP_UID");

            if (uid != null) {
                result.add(uid);
            } else {
                return null;
            }

        }

        return result;
    }

    public ArrayList<String> getServerTypes() throws SQLException {

        ArrayList<String> result = new ArrayList();

        ResultSet rs = null;

        rs = this.execSelectStatement(
                " SELECT "
                + " SERVER_TYPE_PROTOCOL "
                + " FROM JAPPM_SERVER_TYPES ");

        while (rs.next()) {

            result.add(rs.getString("SERVER_TYPE_PROTOCOL"));

        }

        return result;
    }

    public ArrayList<Integer> putMessages(ArrayList<JAPPMessage> messages) throws SQLException {

        ArrayList<Integer> result = new ArrayList();

        for (JAPPMessage m : messages) {

            result.add(putMessage(m));

        }

        return result;
    }

    public Integer putMessage(JAPPMessage message) throws SQLException {


        Integer messageId;

        this.execApplyStatement(
                " INSERT INTO JAPPM_MESSAGES ( "
                + " FOLDER_ID ,"
                + " ACCOUNT_ID, "
                + " H_SUBJECT , "
                + " H_FROM , "
                + " H_TO , "
                + " H_CC , "
                + " H_DT_SENT"
                + ") VALUES ( ? , ? , ? , ? , ? , ? , ?) ",
                message.getFolderId(),
                message.getAccountId(),
                message.getSubject(),
                message.getFrom(),
                message.getTo(),
                message.getCC(),
                message.getSentDate());

        messageId = this.getLastInsertedId();

        if (message.getUID() != null) {

            this.execApplyStatement(" UPDATE JAPPM_MESSAGES "
                    + " SET IMAP_UID=? "
                    + " WHERE MESSAGE_ID=? ",
                    message.getUID(),
                    messageId);
        }

        if (message.getRawMessage() != null) {
            this.putMessageRawContent(messageId, message.getRawMessage());
        }

        if (message.getSeen()) {
            this.putMessageIsSeen(messageId);
        }

        return messageId;


    }

    public String getFolderName(int folderId) throws SQLException {

        String result = null;

        ResultSet rs = null;

        rs = this.execSelectStatement(
                " SELECT "
                + " FOLDER_NAME "
                + " FROM JAPPM_FOLDERS "
                + " WHERE FOLDER_ID=? ",
                folderId);

        if (rs.next()) {

            result = rs.getString("FOLDER_NAME");
        }


        return result;
    }

    public Long getFolderUIDValidity(int folderId) throws SQLException {

        Long result = null;

        ResultSet rs = null;

        rs = this.execSelectStatement(
                " SELECT "
                + " IMAP_UIDVALIDITY "
                + " FROM JAPPM_FOLDERS "
                + " WHERE FOLDER_ID=? ",
                folderId);

        if (rs.next()) {

            result = rs.getLong("IMAP_UIDVALIDITY");
        }


        return result;
    }

    public Integer getMainFolderId(int accountId) throws SQLException {

        Integer result = null;

        ResultSet rs = null;

        rs = this.execSelectStatement(
                " SELECT JFT.FOLDER_ID as MAIN_FOLDER_ID                                                      "
                + " FROM JAPPM_FOLDERS JF                                                   "
                + " INNER JOIN JAPPM_FOLDERS_TREE JFT ON ( JF.FOLDER_ID = JFT.FOLDER_ID )   "
                + " INNER JOIN JAPPM_ACCOUNTS JA ON ( JF.FOLDER_NAME = JA.ACCOUNT_NAME )    "
                + " WHERE JFT.PARENT_ID=1                                                    "
                + " AND JA.ACCOUNT_ID=?                                                      ",
                accountId);

        if (rs.next()) {

            result = rs.getInt("MAIN_FOLDER_ID");
        }


        return result;
    }

    public Integer getInboxFolderId(int accountId) throws SQLException {

        Integer result = null;

        ResultSet rs = null;

        rs = this.execSelectStatement(
                "     SELECT JFT.FOLDER_ID as INBOX_FOLDER_ID                                    "
                + "     FROM JAPPM_FOLDERS JF                                                     "
                + "     INNER JOIN JAPPM_FOLDERS_TREE JFT ON ( JF.FOLDER_ID = JFT.FOLDER_ID )     "
                + "     WHERE  UPPER(JF.FOLDER_NAME) like 'INBOX'                                  "
                + "     AND JF.ACCOUNT_ID=?                                                        "
                + "     AND JFT.PARENT_ID=(                                                        "
                + "     SELECT JFT2.FOLDER_ID as MAIN_FOLDER_ID                                    "
                + "     FROM JAPPM_FOLDERS JF2                                                    "
                + "     INNER JOIN JAPPM_FOLDERS_TREE JFT2 ON ( JF2.FOLDER_ID = JFT2.FOLDER_ID )  "
                + "     INNER JOIN JAPPM_ACCOUNTS JA2 ON ( JF2.FOLDER_NAME = JA2.ACCOUNT_NAME )   "
                + "     WHERE JFT2.PARENT_ID=1                                                     "
                + "     AND JA2.ACCOUNT_ID=?                                                       "
                + "    )                                                                           ",
                accountId,
                accountId);

        if (rs.next()) {

            result = rs.getInt("INBOX_FOLDER_ID");
        }


        return result;
    }

    public Integer getSentItemsFolderId(int accountId) throws SQLException {

        Integer result = null;

        ResultSet rs = null;

        rs = this.execSelectStatement(
                "     SELECT JFT.FOLDER_ID as INBOX_FOLDER_ID                                    "
                + "     FROM JAPPM_FOLDERS JF                                                     "
                + "     INNER JOIN JAPPM_FOLDERS_TREE JFT ON ( JF.FOLDER_ID = JFT.FOLDER_ID )     "
                + "     WHERE  UPPER(JF.FOLDER_NAME) like 'SENT ITEMS'                             "
                + "     AND JF.ACCOUNT_ID=?                                                        "
                + "     AND JFT.PARENT_ID=(                                                        "
                + "     SELECT JFT2.FOLDER_ID as MAIN_FOLDER_ID                                    "
                + "     FROM JAPPM_FOLDERS JF2                                                    "
                + "     INNER JOIN JAPPM_FOLDERS_TREE JFT2 ON ( JF2.FOLDER_ID = JFT2.FOLDER_ID )  "
                + "     INNER JOIN JAPPM_ACCOUNTS JA2 ON ( JF2.FOLDER_NAME = JA2.ACCOUNT_NAME )   "
                + "     WHERE JFT2.PARENT_ID=1                                                     "
                + "     AND JA2.ACCOUNT_ID=?                                                       "
                + "    )                                                                           ",
                accountId,
                accountId);

        if (rs.next()) {

            result = rs.getInt("INBOX_FOLDER_ID");
        }


        return result;
    }

    public Integer putFolderName(String folderName, Integer accountId, Boolean editable) throws SQLException {

        this.execApplyStatement(
                " INSERT INTO JAPPM_FOLDERS ( FOLDER_NAME , ACCOUNT_ID , EDITABLE ) VALUES ( ? , ? ,? ) ",
                folderName,
                accountId,
                editable);

        return this.getLastInsertedId();
    }

    public Integer putFolderUIDValidity(Integer folderId, Long uidValidity) throws SQLException {

        this.execApplyStatement(
                "  UPDATE JAPPM_FOLDERS SET IMAP_UIDVALIDITY=? WHERE FOLDER_ID=? ",
                uidValidity,
                folderId);

        return this.getLastInsertedId();
    }

    public Integer putMessageRawContent(Integer messageId, byte[] byteArray) throws SQLException {

        this.execApplyStatement(
                " UPDATE JAPPM_MESSAGES SET M_RAW=? WHERE MESSAGE_ID=? ",
                byteArray,
                messageId);

        return this.getLastInsertedId();
    }

    public void putMessageConstainsAttachments(Integer messageId) throws SQLException {

        this.execApplyStatement(
                " UPDATE JAPPM_MESSAGES SET M_ATTACHMENTS='TRUE' WHERE MESSAGE_ID=? ",
                messageId);
    }

    public void putMessageIsSeen(Integer messageId) throws SQLException {

        this.execApplyStatement(
                " UPDATE JAPPM_MESSAGES SET F_SEEN='TRUE' WHERE MESSAGE_ID=? ",
                messageId);
    }

    public void deleteMessage(Long messageUID, Integer folderId) throws SQLException {
        this.execApplyStatement(
                " DELETE FROM JAPPM_MESSAGES "
                + " WHERE IMAP_UID=? "
                + " AND FOLDER_ID=?",
                messageUID,
                folderId);
    }

    public void deleteMessage(Integer messageId) throws SQLException {
        this.execApplyStatement(
                " DELETE FROM JAPPM_MESSAGES "
                + " WHERE MESSAGE_ID=? ",
                messageId);
    }

    public void deleteMessages(Integer folderId) throws SQLException {

        this.execApplyStatement(
                " DELETE FROM JAPPM_MESSAGES "
                + " WHERE FOLDER_ID=?",
                folderId);
    }

    public void deleteAccount(Integer accountId) throws SQLException {
        this.execApplyStatement(
                " DELETE FROM JAPPM_ACCOUNTS "
                + " WHERE ACCOUNT_ID=? ",
                accountId);
    }

    public void deleteFolder(Integer folderId) throws SQLException {
        this.execApplyStatement(
                " DELETE FROM JAPPM_FOLDERS "
                + " WHERE FOLDER_ID=? ",
                folderId);
    }

    public void renameFolder(Integer folderId, String folderNewName) throws SQLException {
        this.execApplyStatement(
                " UPDATE JAPPM_FOLDERS "
                + " SET FOLDER_NAME = ? "
                + " WHERE FOLDER_ID=? ",
                folderNewName,
                folderId);
    }

    public void putTreeLink(int parrentFolderId, int currentFolderId) throws SQLException {

        this.execApplyStatement(
                " INSERT INTO JAPPM_FOLDERS_TREE ( PARENT_ID , FOLDER_ID ) "
                + " SELECT ?, ? "
                + " FROM DUAL "
                + " WHERE NOT EXISTS ( SELECT * "
                + " FROM JAPPM_FOLDERS_TREE "
                + " WHERE PARENT_ID=? AND FOLDER_ID=?) ",
                parrentFolderId,
                currentFolderId,
                parrentFolderId,
                currentFolderId);

    }

    public String getAccountName(int accountId) throws SQLException {

        String result = null;

        ResultSet rs = null;

        rs = this.execSelectStatement(
                " SELECT "
                + " ACCOUNT_NAME "
                + " FROM JAPPM_ACCOUNTS "
                + " WHERE ACCOUNT_ID=? ",
                accountId);

        if (rs.next()) {

            result = rs.getString("ACCOUNT_NAME");
        }


        return result;
    }

    public Integer getAccountId(String accountName) throws SQLException {

        Integer result = null;

        ResultSet rs = null;

        rs = this.execSelectStatement(
                " SELECT "
                + " ACCOUNT_ID "
                + " FROM JAPPM_ACCOUNTS "
                + " WHERE ACCOUNT_NAME=? ",
                accountName);

        if (rs.next()) {

            result = rs.getInt("ACCOUNT_ID");
        }


        return result;
    }

    public Integer getAccountId(int folderId) throws SQLException {

        Integer result = null;

        ResultSet rs = null;

        rs = this.execSelectStatement(
                " SELECT "
                + " ACCOUNT_ID "
                + " FROM JAPPM_FOLDERS "
                + " WHERE FOLDER_ID=? ",
                folderId);

        if (rs.next()) {

            result = rs.getInt("ACCOUNT_ID");
        }


        return result;
    }

    public ArrayList<Integer> getSubfoldersIds(int folderId) throws SQLException {

        ArrayList<Integer> result = new ArrayList();

        ResultSet rs = null;

        rs = this.execSelectStatement(
                " SELECT "
                + " FOLDER_ID "
                + " FROM  JAPPM_FOLDERS_TREE "
                + " WHERE PARENT_ID=? "
                + " AND FOLDER_ID != PARENT_ID",
                folderId);

        while (rs.next()) {

            result.add(rs.getInt("FOLDER_ID"));
        }


        if (result.isEmpty()) {
            result = null;
        }

        return result;
    }

    public ArrayList<String[]> getAccountsList() throws SQLException {

        ArrayList<String[]> result = new ArrayList();

        String[] value;

        ResultSet rs = null;

        rs = this.execSelectStatement(
                " SELECT "
                + " ACCOUNT_ID, NAME, EMAIL "
                + " FROM JAPPM_ACCOUNT_SETTINGS ");

        while (rs.next()) {

            value = new String[2];
            value[0] = Integer.toString(rs.getInt("ACCOUNT_ID"));
            value[1] = rs.getString("NAME").concat(" <").concat(rs.getString("EMAIL")).concat("> ");

            result.add(value);

        }


        return result;
    }

    public void moveMessage(Integer messageId, Integer folderId) throws SQLException {


        if (folderId.intValue() == Controller.OUTBOX_ID.intValue()) {
            this.execApplyStatement(
                    " UPDATE JAPPM_MESSAGES "
                    + " SET FOLDER_ID= ? "
                    + " WHERE MESSAGE_ID=? ",
                    folderId,
                    messageId);
        } else {
            this.execApplyStatement(
                    " UPDATE JAPPM_MESSAGES "
                    + " SET (ACCOUNT_ID, FOLDER_ID) "
                    + " = ( 1 , ? ) "
                    + " WHERE MESSAGE_ID=? ",
                    folderId,
                    messageId);
        }

    }

    public String getFolderFullTreePath(Integer folderId) throws SQLException {

        int folderIdd = folderId;
        String folderName;

        String result;

        String fullPath = "";

        while (true) {

            folderName = this.getFolderName(folderIdd);

            if (folderName == null) {
                return null;
            }

            fullPath = folderName.concat("/").concat(fullPath);

            if (folderIdd == 1) {
                break;
            }


            ResultSet rs = null;

            rs = this.execSelectStatement(
                    " SELECT "
                    + " PARENT_ID "
                    + " FROM  JAPPM_FOLDERS_TREE "
                    + " WHERE FOLDER_ID=? ",
                    folderIdd);


            if (rs.next()) {

                folderIdd = rs.getInt("PARENT_ID");

            }

        }

        result = fullPath;

        return result.substring(0, result.length() - 1);
    }

    public void putMessageUIDValidityZero(Integer messageId) throws SQLException {

        this.execApplyStatement(" UPDATE JAPPM_MESSAGES "
                + " SET IMAP_UID=0 "
                + " WHERE MESSAGE_ID=? ",
                messageId);
    }
}
