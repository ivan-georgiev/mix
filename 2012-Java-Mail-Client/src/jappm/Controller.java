/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jappm;

import jappm.db.DBStatement;
import jappm.mail.ExtendedMessage;
import jappm.mail.Mail;
import jappm.ui.JF_MainW;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Address;
import javax.mail.Flags.Flag;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.internet.AddressException;
import javax.mail.internet.ParseException;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Ivan
 */
public class Controller {

    //Constructor variables
    private DBStatement db;
    private HashMap<Integer, Mail> mailObjectsHashMap;
    //Other variables
    private HashMap<DefaultMutableTreeNode, Integer> treeNodesObjectsHashMap = new HashMap();
    private String exceptionInfo;
    private ArrayList<String> pop3Folders = new ArrayList();
    //
    private final String TEMP_FILES_DIRECTORY = "cache";
    public final static Integer OUTBOX_ID = 2;
    public final static Integer DRAFTS_ID = 3;
    //
    public final int FOLDER_DELETED = 1;
    public final int FOLDER_CLEANED = 2;
    public final int FOLDER_ERROR = 0;
    //
    private String sentItemsFolder = "Sent Items";
    //TODO ???
    private JF_MainW mainWindow;

    public Controller(DBStatement db, HashMap<Integer, Mail> mailObjects) {

        this.db = db;
        this.mailObjectsHashMap = mailObjects;

        pop3Folders.add("Inbox");
        pop3Folders.add("Sent Items");
        pop3Folders.add("Spam");

    }

    public void setMainWindow(JF_MainW mainWindow) {
        this.mainWindow = mainWindow;
    }

    private void addJAPPMessageToPreviewTable(JAPPMessage jpm) {

        mainWindow.addRowToMailsTable(new Object[]{
                    " ",
                    mainWindow.getAttachmentIcon(jpm),
                    jpm.getFrom(),
                    jpm.getTo(),
                    jpm.getSubject(),
                    jpm.getSentDate().toString().substring(0, 16),
                    jpm,
                    jpm.getSeen()
                });

    }

    private void setExceptionInfo(Throwable e) {
        this.exceptionInfo = Common.stackTraceToString(e);
    }

    private void clearExceptionInfo(String text) {
        this.exceptionInfo = null;
    }

    public String getExceptionInfo() {
        return this.exceptionInfo;
    }

    private Mail connectMail(Integer accountId, boolean closeFirstIfOpen) {

        Mail mail = mailObjectsHashMap.get(accountId);

        if (mail == null) {
            return null;
        }

        if (closeFirstIfOpen) {
            if (mail.isConnected()) {
                mail.disconnect();
            }
        }
        try {

            mail.connect();

        } catch (MessagingException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);

            this.setExceptionInfo(ex);
            mail = null;
        }

        return mail;

    }

    private void disconnectMail(Integer accountId) {

        Mail mail = mailObjectsHashMap.get(accountId);

        mail.disconnect();

    }

    public boolean connectDb() {
        try {

            db.open();
            return true;

        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public void disconnectDb() {
        try {

            db.close();

        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Mail getMailObjectForAccount(Integer accountId) {

        if (accountId == null) {
            return new Mail();
        }

        return mailObjectsHashMap.get(accountId);

    }

    public boolean loadAndInsertFoldersTreeToDB(int mainFolderId) {
        try {

            int accountId = db.getAccountId(mainFolderId);

            //@n
            Mail mail = this.getMailObjectForAccount(accountId);
            Integer folderId = db.getMainFolderId(accountId);

            if (mail.getAccountInType() == Mail.IMAP_ACCOUNT) {

                mail = this.connectMail(accountId, false);

                if (mail == null) {
                    return false;
                }

                HashMap<String, Integer> hm = new HashMap();

                String folderName;
                Integer parentId;
                String accountName;
                String accountPath;

                accountName = db.getFolderName(folderId);

                hm.put(accountName, folderId);

                for (String mailfolderPath : mail.getFoldersList()) {

                    accountPath = accountName.concat(Mail.PATH_SEPARATOR).concat(mailfolderPath);
                    folderName = getFolderName(accountPath);
                    parentId = hm.get(getFolderParentFullName(accountPath));


                    folderId = db.putFolderName(folderName, accountId, true);

                    db.putFolderUIDValidity(folderId, mail.getFolderUIDValidity(mailfolderPath));

                    db.putTreeLink(parentId, folderId);

                    hm.put(accountPath, folderId);
                }

                this.disconnectMail(accountId);

            } else if (mail.getAccountInType() == Mail.POP3_ACCOUNT) {

                for (String folderName : this.pop3Folders) {

                    db.putTreeLink(folderId, db.putFolderName(folderName, accountId, false));

                }
            }

            return true;
        } catch (MessagingException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    public DefaultMutableTreeNode getTreeFromNode(Integer folderId) {
        try {
            DefaultMutableTreeNode result = new DefaultMutableTreeNode(db.getFolderName(folderId));

            this.treeNodesObjectsHashMap.put(result, folderId);

            ArrayList<Integer> folderIds = db.getSubfoldersIds(folderId);

            if (folderIds == null) {
                return result;
            }

            for (Integer fid : folderIds) {
                result.add(getTreeFromNode(fid));

            }


            return result;
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private String getFolderName(String folderPath) {

        if (folderPath == null) {
            return null;
        }

        int separatorIndex = folderPath.lastIndexOf(Mail.PATH_SEPARATOR);

        if (separatorIndex == -1) {
            return folderPath;
        } else {
            return folderPath.substring(separatorIndex + 1, folderPath.length());
        }


    }

    private String getFolderParentFullName(String folderPath) {

        if (folderPath == null) {
            return null;
        }

        int separatorIndex = folderPath.lastIndexOf(Mail.PATH_SEPARATOR);

        if (separatorIndex == -1) {
            return null;
        } else {

            return folderPath.substring(0, separatorIndex);
        }

    }

    private String getAccountFullPath(String treeFullPath) {

        if (treeFullPath == null) {
            return null;
        }

        int separatorIndex = treeFullPath.indexOf(Mail.PATH_SEPARATOR);

        if (separatorIndex == -1) {
            return null;
        } else {

            return treeFullPath.substring(separatorIndex + 1);
        }

    }

    private String getFolderFullPath(String accountFullPath) {

        if (accountFullPath == null) {
            return null;
        }

        int separatorIndex = accountFullPath.indexOf(Mail.PATH_SEPARATOR);

        if (separatorIndex == -1) {
            return null;
        } else {

            return accountFullPath.substring(separatorIndex + 1);
        }

    }

    public Integer getFolderId(DefaultMutableTreeNode treeElement) {

        return this.treeNodesObjectsHashMap.get(treeElement);
    }

    private ArrayList<JAPPMessage> castExtMessagesToJAPPMessages(ArrayList<ExtendedMessage> messages, Integer folderId, Boolean cacheWhole) throws MessagingException, ParseException, UnsupportedEncodingException {

        ArrayList<JAPPMessage> result = null;
        JAPPMessage jpm;

        if (messages == null) {
            return result;
        }

        result = new ArrayList();

        for (ExtendedMessage m : messages) {

            jpm = castExtMessageToJAPPMessage(m, folderId, cacheWhole);

            if (jpm != null) {
                result.add(jpm);
            }

        }

        return result;
    }

    private JAPPMessage castExtMessageToJAPPMessage(ExtendedMessage message, Integer folderId, Boolean cacheWhole) throws MessagingException, ParseException, UnsupportedEncodingException {


        JAPPMessage japMessage;

        if (message == null) {
            return null;
        }

        Mail mail = this.getMailObjectForAccount(null);

        Address[] sender;
        Address[] recepients;
        Address[] ccrecepients;


        String from = "";
        String to = "";
        String cc = "";
        String subject;
        Date date;

        //String body = null;

        sender = message.getMessage().getFrom();
        recepients = message.getMessage().getRecipients(Message.RecipientType.TO);
        ccrecepients = message.getMessage().getRecipients(Message.RecipientType.CC);



        subject = mail.fixHeaderSubject(message.getMessage().getSubject());


        if (sender != null) {
            from = mail.castAddressToString(sender[0]);
        }

        //process mail.TO
        if (recepients != null) {

            for (int j = 0; j < recepients.length; j++) {
                //recepients list
                //to = to + mail.getEmailAddress(recepients[j]) + "; ";
                to = to + mail.castAddressToString(recepients[j]) + "; ";
            }
            to = to.substring(0, to.length() - 2);
        }

        //process mail.CC
        if (ccrecepients != null) {

            for (int j = 0; j < ccrecepients.length; j++) {
                //recepients list
                cc = cc + mail.castAddressToString(ccrecepients[j]) + "; ";

            }
            cc = cc.substring(0, cc.length() - 2);
        }

        date = message.getMessage().getSentDate();

        japMessage = new JAPPMessage(folderId,
                message.getUID(),
                subject,
                from,
                to,
                cc,
                date);


        if (message.getMessage().isSet(Flag.SEEN)) {
            japMessage.setSeen();
        }

        if (cacheWhole) {
            try {

                japMessage.setRawMessage(mail.getByteArrayFromMessage(message.getMessage()));

                if (getAttachments(japMessage) != null) {
                    japMessage.setAttachmentsAvaliable();
                }


            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return japMessage;
    }

    private Integer castExtMessageToJAPPMessageAndPutInDb(ExtendedMessage message, Integer folderId, Boolean cacheWhole) throws MessagingException, ParseException, UnsupportedEncodingException, SQLException {


        JAPPMessage japMessage;

        if (message == null) {
            return null;
        }

        Mail mail = this.getMailObjectForAccount(null);

        Address[] sender;
        Address[] recepients;
        Address[] ccrecepients;


        String from = "";
        String to = "";
        String cc = "";
        String subject;
        Date date;

        //String body = null;

        sender = message.getMessage().getFrom();
        recepients = message.getMessage().getRecipients(Message.RecipientType.TO);
        ccrecepients = message.getMessage().getRecipients(Message.RecipientType.CC);



        subject = mail.fixHeaderSubject(message.getMessage().getSubject());


        if (sender != null) {
            from = mail.castAddressToString(sender[0]);
        }

        //process mail.TO
        if (recepients != null) {

            for (int j = 0; j < recepients.length; j++) {
                //recepients list
                //to = to + mail.getEmailAddress(recepients[j]) + "; ";
                to = to + mail.castAddressToString(recepients[j]) + "; ";
            }
            to = to.substring(0, to.length() - 2);
        }

        //process mail.CC
        if (ccrecepients != null) {

            for (int j = 0; j < ccrecepients.length; j++) {
                //recepients list
                cc = cc + mail.castAddressToString(ccrecepients[j]) + "; ";

            }
            cc = cc.substring(0, cc.length() - 2);
        }

        date = message.getMessage().getSentDate();

        japMessage = new JAPPMessage(folderId,
                message.getUID(),
                subject,
                from,
                to,
                cc,
                date);


        if (message.getMessage().isSet(Flag.SEEN)) {
            japMessage.setSeen();
        }

        if (cacheWhole) {
            try {

                japMessage.setRawMessage(mail.getByteArrayFromMessage(message.getMessage()));

                if (getAttachments(japMessage) != null) {
                    japMessage.setAttachmentsAvaliable();
                }


            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return db.putMessage(japMessage);
    }

    private ExtendedMessage castJAPPMessageToExtMessage(JAPPMessage jappMessage) throws MessagingException {

        if (jappMessage == null || jappMessage.getRawMessage() == null || jappMessage.getUID() == null) {
            return null;
        }

        Mail mail = this.getMailObjectForAccount(null);

        return new ExtendedMessage(mail.createMimeMessageFromByteArray(jappMessage.getRawMessage()), jappMessage.getUID());


    }

    public ArrayList<JAPPMessage> getMessagesFromDb(Integer folderId) {
        try {
            ArrayList<JAPPMessage> result;

            result = db.getMessages(folderId);

            return result;
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public void previewMessages(ArrayList<JAPPMessage> jappMessages) {
        for (JAPPMessage jpm : jappMessages) {
            this.addJAPPMessageToPreviewTable(jpm);
        }
    }

    public ArrayList<JAPPMessage> getMessagesFromDb(ArrayList<Integer> messageIDs) {
        try {
            ArrayList<JAPPMessage> result;

            result = db.getMessages(messageIDs);

            return result;
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public String getMessageBodyFromDb(JAPPMessage m) {
        try {

            String result = null;

            Mail mail = this.getMailObjectForAccount(null);

            if (mail == null) {
                return null;
            }

            m = db.getMessage(m.getMessageId());

            if (m.getRawMessage() != null) {

                System.out.println("Available in the database");

                result = mail.getBodyHTML(
                        mail.createMimeMessageFromByteArray(
                        m.getRawMessage()));

                if (result == null) {
                    //No html body, try to get paintext                   
                    result = mail.text2html(mail.getBodyPlaintext(
                            mail.createMimeMessageFromByteArray(
                            m.getRawMessage())));
                }


            }
            return result;
        } catch (MessagingException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public Integer updateMessageContentToFull(JAPPMessage m) {
        //@n
        try {

            ExtendedMessage eMessage;
            Integer result;

            Integer accountId = db.getAccountId(m.getFolderId());
            Integer messageId = m.getMessageId();
            Long messageUID = m.getUID();
            String folderName = this.getFolderFullPath(this.getAccountFullPath(db.getFolderFullTreePath(m.getFolderId())));

            Mail mail = this.connectMail(accountId, true);

            if (mail == null) {
                return null;
            }

            eMessage = mail.getMessageByUID(folderName, messageUID);

            result = db.putMessageRawContent(messageId, mail.getByteArrayFromMessage(eMessage.getMessage()));

            this.disconnectMail(accountId);

            return result;

        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (MessagingException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public void updateMessageContainsAttachments(Integer MessageId) {
        try {

            db.putMessageConstainsAttachments(MessageId);

        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void upadteMessageIsSeen(Integer messageId) {
        try {

            db.putMessageIsSeen(messageId);

        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<Integer> syncMessagesWithPOP3(Integer folderId) {
        //@n
        try {

            ArrayList<Integer> result = new ArrayList();
            ArrayList<ExtendedMessage> eMessages;

            Integer accountId = db.getAccountId(folderId);

            Mail mail = this.getMailObjectForAccount(accountId);

            if (mail == null) {
                return null;
            }

            if (folderId != db.getInboxFolderId(accountId)
                    || mail.getAccountInType() == Mail.IMAP_ACCOUNT
                    || folderId == db.getMainFolderId(accountId)) {
                return result;
            }

            mail = this.connectMail(accountId, true);

            if (mail == null) {
                return null;
            }

            eMessages = mail.getMessages(null);



            Integer res;
            JAPPMessage m;

            for (ExtendedMessage em : eMessages) {
                res = castExtMessageToJAPPMessageAndPutInDb(em, folderId, true);
                m = db.getMessage(res);

                if (result != null) {
                    result.add(res);

                    mainWindow.addRowToMailsTable(new Object[]{
                                " ",
                                mainWindow.getAttachmentIcon(m),
                                m.getFrom(),
                                m.getTo(),
                                m.getSubject(),
                                m.getSentDate().toString().substring(0, 16),
                                m,
                                m.getSeen()
                            });
                }
            }


            // result = db.putMessages(this.castExtMessagesToJAPPMessages(eMessages, folderId, true));

            //mail.deleteExtendedMessages(eMessages);

            this.disconnectMail(accountId);

            return result;

        } catch (ParseException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (MessagingException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public ArrayList<Integer> syncMessagesWithIMAP(Integer folderId) {
        //@n
        try {

            ArrayList<Integer> newMessages = new ArrayList();
            //ArrayList<Integer> deletedMessages = new ArrayList();
            ArrayList<ExtendedMessage> eMessages;
            JAPPMessage m;


            Integer accountId = db.getAccountId(folderId);
            Long folderUIDValidity = db.getFolderUIDValidity(folderId);
            Mail mail = this.getMailObjectForAccount(accountId);

            if (mail == null || folderUIDValidity == null) {
                return null;
            }

            if (mail.getAccountInType() == Mail.POP3_ACCOUNT
                    || db.getMainFolderId(accountId) == folderId
                    || folderUIDValidity.longValue() == -1) {
                return newMessages;
            }



            ArrayList<Long> serverUIDs;
            ArrayList<Long> databaseUIDs;
            ArrayList<Long> differentUIDs = new ArrayList();
            String folderName = this.getFolderFullPath(this.getAccountFullPath(db.getFolderFullTreePath(folderId)));


            mail = this.connectMail(accountId, true);

            if (mail == null) {
                return null;
            }


            if (folderUIDValidity.longValue() != mail.getFolderUIDValidity(folderName).longValue()) {

                db.deleteMessages(folderId);

                eMessages = mail.getMessages(folderName);

                if (eMessages == null) {
                    return newMessages;
                }

            } else {

                serverUIDs = mail.getMessagesUIDs(folderName);

                databaseUIDs = db.getFolderUIDs(folderId);

                if (serverUIDs == null) {
                    return newMessages;
                }

                for (Long uid : serverUIDs) {

                    if (databaseUIDs.contains(uid)) {
                        continue;
                    }

                    differentUIDs.add(uid);

                }

                System.out.println(differentUIDs.size() + " new messages");

                eMessages = mail.getMessagesByUIDs(folderName, convertArrayListLongtoArraylong(differentUIDs));

                for (Long uid : databaseUIDs) {

                    if (serverUIDs.contains(uid)) {
                        continue;
                    }

                    db.deleteMessage(uid, folderId);

                }

            }


            Integer result;
            for (ExtendedMessage em : eMessages) {
                result = castExtMessageToJAPPMessageAndPutInDb(em, folderId, false);
                m = db.getMessage(result);

                if (result != null) {
                    newMessages.add(result);

                    mainWindow.addRowToMailsTable(new Object[]{
                                "",
                                mainWindow.getAttachmentIcon(m),
                                m.getFrom(),
                                m.getTo(),
                                m.getSubject(),
                                m.getSentDate().toString().substring(0, 16),
                                m,
                                m.getSeen()
                            });
                }
            }




            // newMessages = db.putMessages(this.castExtMessagesToJAPPMessages(eMessages, folderId, false));

            this.disconnectMail(accountId);

            return newMessages;

        } catch (ParseException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            this.setExceptionInfo(ex);
            return null;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            this.setExceptionInfo(ex);
            return null;
        } catch (MessagingException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            this.setExceptionInfo(ex);
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            this.setExceptionInfo(ex);
            return null;
        }

    }

    private long[] convertArrayListLongtoArraylong(ArrayList<Long> arrList) {

        if (arrList == null) {
            return null;
        }

        long[] result = new long[arrList.size()];
        int i = 0;

        for (Long l : arrList) {

            result[i] = l.longValue();
            i++;
        }

        return result;
        //return arrList.toArray(new Long[arrList.size()]);

    }

    public int getAccountType(Integer folderId) {
        try {
            int accId = db.getAccountId(folderId);

            if (accId == 1) {
                //System folder selected, return valid value, methods are protected
                // return Mail.IMAP_ACCOUNT;
                return -1;
            }

            return this.getMailObjectForAccount(accId).getAccountInType();

        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    public boolean isAccountFolder(Integer folderId) {
        try {
            if (db.getMainFolderId(db.getAccountId(folderId)) == folderId) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public ArrayList<File> getAttachments(JAPPMessage jappMessage) {
        try {

            ArrayList<File> result;
            Mail mail = this.getMailObjectForAccount(null);
            JAPPMessage m;
            if (jappMessage.getMessageId() != null) {
                m = db.getMessage(jappMessage.getMessageId());
            } else {
                m = jappMessage;
            }

            byte[] rawMessage = m.getRawMessage();

            if (rawMessage == null) {
                return null;
            }

            result = mail.getAttachments(mail.createMimeMessageFromByteArray(rawMessage), TEMP_FILES_DIRECTORY);

            if (result == null) {
                return null;
            }
            // Desktop.getDesktop().open(f);


            return result;
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (MessagingException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public File getMessageAsFile(JAPPMessage jappMessage) {

        //TODO File should be passed as object without being saved to HD

        try {
            Mail mail = this.getMailObjectForAccount(null);

            JAPPMessage m;

            if (jappMessage.getMessageId() != null) {
                m = db.getMessage(jappMessage.getMessageId());
            } else {
                m = jappMessage;
            }

            byte[] rawMessage = m.getRawMessage();

            if (rawMessage == null) {
                return null;
            }

            mail.saveMailEml(mail.createMimeMessageFromByteArray(rawMessage),
                    TEMP_FILES_DIRECTORY,
                    "Original_Message");

            return new File(TEMP_FILES_DIRECTORY + File.separator + "Original_Message.eml");

        } catch (MessagingException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public boolean messageIsInDatabase(Integer messageId, Integer folderId) {
        try {

            JAPPMessage jpm = db.getMessage(messageId);

            if (jpm == null) {
                return false;
            } else {

                if (folderId == null || jpm.getFolderId() == folderId) {
                    return true;
                }

                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public JAPPMessage getLastVersionOfJAPPMessage(JAPPMessage jappMessage) {
        try {

            return db.getMessage(jappMessage.getMessageId());
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            return jappMessage;
        }

    }

    public void killAllDBSessions() {
        try {
            db.close();
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void killAllMailSessions() {

        for (Mail mail : this.mailObjectsHashMap.values()) {

            //    if (mail.getAccountInType()==Mail.IMAP_ACCOUNT){
            mail.disconnect();
            //  }

        }
    }

    public void constructAndInsertNewMessageInDb(
            Integer accountId,
            String from,
            String to,
            String cc,
            String bcc,
            String subject,
            String text,
            ArrayList<File> filesToBeAttached,
            JAPPMessage originalMessage,
            Integer folderId,
            boolean deleteOriginal) {
        try {

            JAPPMessage jappMessage;

            Mail mail = this.getMailObjectForAccount(accountId);
            Message original = null;

            if (originalMessage != null) {
                originalMessage = db.getMessage(originalMessage.getMessageId());
                original = mail.createMimeMessageFromByteArray(originalMessage.getRawMessage());
            }


            Message m = mail.createMimeMessage(constructAddressesFromString(from),
                    constructAddressesFromString(to),
                    constructAddressesFromString(cc),
                    constructAddressesFromString(bcc),
                    subject,
                    text,
                    filesToBeAttached,
                    original);


            jappMessage = this.castExtMessageToJAPPMessage(new ExtendedMessage(m, null), folderId, true);
            jappMessage.setAccountId(accountId);

            db.putMessage(jappMessage);

            if (deleteOriginal) {
                db.deleteMessage(originalMessage.getMessageId());
            }

            // mail.sendMessage(m);


        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean sendMessages() {

        ArrayList<JAPPMessage> messages;
        Mail mail;

        try {
            messages = db.getMessages(OUTBOX_ID);
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            this.setExceptionInfo(ex);
            return false;
        }


        for (JAPPMessage jpm : messages) {
            try {
                mail = this.getMailObjectForAccount(jpm.getAccountId());

                mail.sendMessage(mail.createMimeMessageFromByteArray(jpm.getRawMessage()));

                if (mail.getAccountInType() == Mail.POP3_ACCOUNT) {

                    db.moveMessage(jpm.getMessageId(), db.getSentItemsFolderId(jpm.getAccountId()));

                } else {

                    mail.appendMessageToFolder(mail.createMimeMessageFromByteArray(jpm.getRawMessage()), sentItemsFolder);
                    db.deleteMessage(jpm.getMessageId());

                }


            } catch (MessagingException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                this.setExceptionInfo(ex);
                return false;
            } catch (SQLException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                this.setExceptionInfo(ex);
                return false;
            }
        }


        return true;

    }

    private Address[] constructAddressesFromString(String addreses) throws UnsupportedEncodingException, AddressException {

        Mail mail = this.getMailObjectForAccount(null);

        String row = addreses.replace(";", ",");
        ArrayList<Address> result = new ArrayList();
        Address current;
        String curEl;

        Scanner scanner = new Scanner(row).useDelimiter(",");

        while (scanner.hasNext()) {

            curEl = scanner.next().toString().trim();

            current = mail.createAddress(curEl);

            if (current != null) {
                result.add(current);
            }
        }

        if (result.isEmpty()) {
            return null;
        }

        return (Address[]) result.toArray(new Address[result.size()]);
    }

    public boolean fileIsValidAttachmentType(File f) {

        Mail mail = this.getMailObjectForAccount(null);

        if (mail.getMimeExtensionOfFile(f) == null) {
            return false;
        }

        return true;

    }

    public String getHtmlReplyBody(JAPPMessage m) {
        try {
            Mail mail = this.getMailObjectForAccount(null);

            m = db.getMessage(m.getMessageId());

            Message message = mail.createMimeMessageFromByteArray(m.getRawMessage());

            return mail.getReplyMessageBody(message);



        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);


            return null;
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);


            return null;
        } catch (MessagingException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);


            return null;
        }

    }

    public ArrayList<String[]> getAccountsList() {
        try {

            return db.getAccountsList();


        } catch (Exception ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);


            return null;
        }
    }

    public boolean deleteMessage(JAPPMessage jpm) {
        try {

            Mail mail;
            String folderName;

            if (db.getFolderUIDValidity(jpm.getFolderId()).longValue() != 0) {

                folderName = this.getFolderFullPath(this.getAccountFullPath(db.getFolderFullTreePath(jpm.getFolderId())));
                mail = this.getMailObjectForAccount(db.getAccountId(jpm.getFolderId()));
                mail.deleteMessages(new long[]{jpm.getUID()}, folderName);

            }

            db.deleteMessage(jpm.getMessageId());

            return true;

        } catch (MessagingException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public String getStripedSubject(String subject) {

        String result = null;

        Mail mail = this.getMailObjectForAccount(null);

        result = mail.fixForwardSubject(subject, "");

        return result;

    }

    public boolean moveMessage(Integer messageId, Integer srcFolderId, Integer dstFolderId) {
        try {

            String dstFolderName;
            String srcFolderName;
            JAPPMessage jpm;
            Mail mail;


            if (db.getFolderUIDValidity(srcFolderId).longValue() == 0) {


                if (db.getFolderUIDValidity(dstFolderId).longValue() == 0) {

                    db.moveMessage(messageId, dstFolderId);
                    System.out.println("Message moved");
                    return true;

                } else {

                    jpm = db.getMessage(messageId);

                    dstFolderName = this.getFolderFullPath(this.getAccountFullPath(db.getFolderFullTreePath(dstFolderId)));

                    mail = this.getMailObjectForAccount(db.getAccountId(dstFolderId));

                    mail.appendMessageToFolder(mail.createMimeMessageFromByteArray(jpm.getRawMessage()),
                            dstFolderName);

                    db.deleteMessage(messageId);

                    System.out.println("Message moved");
                    return true;

                }

            } else {


                //IMAP FOLDER AS SRC


                if (db.getFolderUIDValidity(dstFolderId).longValue() == 0) {

                    jpm = db.getMessage(messageId);
                    srcFolderName = this.getFolderFullPath(this.getAccountFullPath(db.getFolderFullTreePath(srcFolderId)));


                    if (jpm.getRawMessage() == null) {
                        this.updateMessageContentToFull(jpm);
                        jpm = db.getMessage(messageId);
                    }

                    db.putMessageUIDValidityZero(messageId);
                    db.moveMessage(messageId, dstFolderId);

                    mail = this.connectMail(db.getAccountId(srcFolderId), false);
                    mail.deleteMessages(new long[]{jpm.getUID()}, srcFolderName);

                    return true;

                } else {

                    if (db.getAccountId(dstFolderId).intValue() == db.getAccountId(srcFolderId)) {
                        //same imap account, no need of second mail object

                        jpm = db.getMessage(messageId);

                        srcFolderName = this.getFolderFullPath(this.getAccountFullPath(db.getFolderFullTreePath(srcFolderId)));
                        dstFolderName = this.getFolderFullPath(this.getAccountFullPath(db.getFolderFullTreePath(dstFolderId)));

                        mail = this.connectMail(db.getAccountId(srcFolderId), false);

                        boolean success = mail.moveMessages(new long[]{jpm.getUID().longValue()}, srcFolderName, dstFolderName);


                        if (success) {

                            db.deleteMessage(messageId);

                        }

                    } else {
                        //TODO DIFFERENT IMAPS ACOUNTS MOVE SUPPORT
                    }

                }

            }

            return true;


        } catch (MessagingException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            this.setExceptionInfo(ex);
            this.mainWindow.showError();
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            this.setExceptionInfo(ex);
            this.mainWindow.showError();
            return false;
        }
    }

    public boolean moveMessages(ArrayList<Integer> messageIds, Integer srcFolderId, Integer dstFolderId) {
        try {

            String dstFolderName;
            String srcFolderName;
            JAPPMessage jpm;
            ArrayList<Message> messages = new ArrayList();
            ArrayList<Long> uids = new ArrayList();
            Mail mail;


            if (db.getFolderUIDValidity(srcFolderId).longValue() == 0) {


                if (db.getFolderUIDValidity(dstFolderId).longValue() == 0) {

                    for (Integer messageId : messageIds) {
                        db.moveMessage(messageId, dstFolderId);
                    }
                    System.out.println("Messages moved");
                    return true;

                } else {
                    mail = this.getMailObjectForAccount(db.getAccountId(dstFolderId));

                    for (Integer messageId : messageIds) {
                        jpm = db.getMessage(messageId);
                        messages.add(mail.createMimeMessageFromByteArray(jpm.getRawMessage()));
                    }

                    dstFolderName = this.getFolderFullPath(this.getAccountFullPath(db.getFolderFullTreePath(dstFolderId)));



                    mail.appendMessagesToFolder((Message[]) messages.toArray(new Message[messages.size()]),
                            dstFolderName);

                    for (Integer messageId : messageIds) {
                        db.deleteMessage(messageId);
                    }
                    System.out.println("Messages moved");
                    return true;

                }

            } else {


                //IMAP FOLDER AS SRC


                if (db.getFolderUIDValidity(dstFolderId).longValue() == 0) {

                    for (Integer messageId : messageIds) {
                        jpm = db.getMessage(messageId);


                        if (jpm.getRawMessage() == null) {
                            this.updateMessageContentToFull(jpm);
                            jpm = db.getMessage(messageId);
                        }

                        db.putMessageUIDValidityZero(messageId);
                        db.moveMessage(messageId, dstFolderId);

                        uids.add(jpm.getUID());

                    }

                    srcFolderName = this.getFolderFullPath(this.getAccountFullPath(db.getFolderFullTreePath(srcFolderId)));

                    mail = this.connectMail(db.getAccountId(srcFolderId), false);
                    mail.deleteMessages(Common.convertArrayListLongToPrimitive(uids), srcFolderName);
                    return true;

                } else {

                    if (db.getAccountId(dstFolderId).intValue() == db.getAccountId(srcFolderId)) {
                        //same imap account, no need of second mail object

                        for (Integer messageId : messageIds) {
                            jpm = db.getMessage(messageId);
                            uids.add(jpm.getUID());
                        }


                        srcFolderName = this.getFolderFullPath(this.getAccountFullPath(db.getFolderFullTreePath(srcFolderId)));
                        dstFolderName = this.getFolderFullPath(this.getAccountFullPath(db.getFolderFullTreePath(dstFolderId)));

                        mail = this.connectMail(db.getAccountId(srcFolderId), false);

                        boolean success = mail.moveMessages(Common.convertArrayListLongToPrimitive(uids), srcFolderName, dstFolderName);


                        if (success) {

                              for (Integer messageId : messageIds) {
                            db.deleteMessage(messageId);
                              }
                        }

                    } else {
                        //TODO DIFFERENT IMAPS ACOUNTS MOVE SUPPORT
                    }

                }

            }

            return true;


        } catch (MessagingException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            this.setExceptionInfo(ex);
            this.mainWindow.showError();
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            this.setExceptionInfo(ex);
            this.mainWindow.showError();
            return false;
        }
    }

    public ArrayList<JAPPAccountSettings> getJAPPSettings() {
        try {

            return db.getMailAccountsSettings();


        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public void setJAPPSettings(ArrayList<JAPPAccountSettings> mailAccountsSettings) {
        try {

            db.putMailAccountsSettings(mailAccountsSettings);

            mailObjectsHashMap = new HashMap();

            String suffixIn;
            String suffixOut;

            for (JAPPAccountSettings dms : db.getMailAccountsSettings()) {

                suffixIn = "";
                suffixOut = "";

                if (dms.serverInIsSSL) {
                    suffixIn = "s";
                }

                if (dms.serverOutIsSSL) {
                    suffixOut = "s";
                }

                mailObjectsHashMap.put(dms.getAccountId(), new Mail(
                        dms.userIn, dms.userOut,
                        dms.passwordIn, dms.passwordOut,
                        dms.serverInType + suffixIn, "smtp" + suffixOut,
                        dms.serverIn, dms.serverOut,
                        dms.serverInPort, dms.serverOutPort,
                        false));

            }

        } catch (NoSuchProviderException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean folderCanBeModified(Integer folderId) {
        try {
            return db.folderCanBeModified(folderId);

        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            return true;
        }
    }

    public void deleteAccount(Integer accountId) {
        try {

            db.deleteAccount(accountId);

        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<JAPPAddress> getAddresses() {
        try {

            return db.getAddresses();

        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public void putAddresses(ArrayList<JAPPAddress> addresses) {
        try {

            db.putAddresses(addresses);

        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Integer getInboxId(Integer folderId) {
        try {
            return db.getInboxFolderId(db.getAccountId(folderId));
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public DefaultMutableTreeNode createNewFolder(Integer parentId, String folderName) {
        try {

            Integer accountId = db.getAccountId(parentId);
            Integer folderId;
            String parentPath;
            DefaultMutableTreeNode result;
            Long folderUID;
            Mail mail;


            if (this.getAccountType(parentId) == Mail.POP3_ACCOUNT) {

                folderId = db.putFolderName(folderName, accountId, true);

                db.putTreeLink(parentId, folderId);

                result = new DefaultMutableTreeNode(folderName);

                this.treeNodesObjectsHashMap.put(result, folderId);

                return result;
            } else if (this.getAccountType(parentId) == Mail.IMAP_ACCOUNT) {

                mail = this.getMailObjectForAccount(accountId);

                parentPath = this.getFolderFullPath(this.getAccountFullPath(db.getFolderFullTreePath(parentId)));

                if (parentPath == null) {
                    folderUID = mail.createFolder(folderName);
                } else {
                    folderUID = mail.createFolder(parentPath + Mail.PATH_SEPARATOR + folderName);
                }


                if (folderUID != null) {


                    folderId = db.putFolderName(folderName, accountId, true);

                    db.putFolderUIDValidity(folderId, folderUID);

                    db.putTreeLink(parentId, folderId);

                    result = new DefaultMutableTreeNode(folderName);

                    this.treeNodesObjectsHashMap.put(result, folderId);

                    return result;


                }

            }

            return null;

        } catch (MessagingException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            this.setExceptionInfo(ex);
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            this.setExceptionInfo(ex);
            return null;
        }
    }

    public int deleteFolder(Integer folderId) {
        try {


            Integer accountId = db.getAccountId(folderId);
            String folderName = this.getFolderFullPath(this.getAccountFullPath(db.getFolderFullTreePath(folderId)));
            Mail mail = this.getMailObjectForAccount(accountId);


            if (db.getSubfoldersIds(folderId) == null) {
                if (this.getAccountType(folderId) == Mail.IMAP_ACCOUNT) {

                    mail.deleteFolder(folderName);
                }
                db.deleteFolder(folderId);
                return this.FOLDER_DELETED;
            } else {
                if (this.getAccountType(folderId) == Mail.IMAP_ACCOUNT) {

                    mail.deleteMessagesInAFolder(folderName);
                }
                db.deleteMessages(folderId);
                return this.FOLDER_CLEANED;
            }



        } catch (MessagingException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            this.setExceptionInfo(ex);
            return this.FOLDER_ERROR;
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            this.setExceptionInfo(ex);
            return this.FOLDER_ERROR;
        }

    }

    public boolean renameFolder(Integer folderId, String folderNewName) {
        try {


            Integer accountId = db.getAccountId(folderId);
            String folderName;
            boolean result;
            String folderParent;
            Mail mail;

            if (this.getAccountType(folderId) == Mail.POP3_ACCOUNT) {

                db.renameFolder(folderId, folderNewName);
                return true;
            } else if (this.getAccountType(folderId) == Mail.IMAP_ACCOUNT) {

                mail = this.getMailObjectForAccount(accountId);

                folderParent = this.getFolderParentFullName(this.getFolderFullPath(this.getAccountFullPath(db.getFolderFullTreePath(folderId))));
                folderName = this.getFolderName(this.getAccountFullPath(db.getFolderFullTreePath(folderId)));


                if (folderParent == null) {
                    result = mail.renameFolder(folderName, folderNewName);
                } else {
                    result = mail.renameFolder(folderParent + Mail.PATH_SEPARATOR + folderName, folderParent + Mail.PATH_SEPARATOR + folderNewName);
                }

                if (result) {

                    db.renameFolder(folderId, folderNewName);
                    this.treeNodesObjectsHashMap.put(new DefaultMutableTreeNode(folderNewName), folderId);

                    return true;

                }


            }


            return false;
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (MessagingException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }
}
