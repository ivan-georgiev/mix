/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jappm.mail;

/**
 *
 * @author Ivan 
 */

import com.sun.mail.smtp.SMTPTransport;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;
import org.jsoup.Jsoup;

public class Mail {

    /* GLOBAL Variables used by the methods */
    private Session session;
    private Store store;
    private Folder sfolder;
    private SMTPTransport smtpTr;

    private URLName urlIn;
    private URLName urlOut;
    private boolean debugSession;
    //
    HashMap<String, String> extensionToMIMETypeHashMap;
    //
    private int accountInType;

    //CONSTANTS
    private final int STEPSIZE = 20;
    //Static CONSTANTS
    public static final int IMAP_ACCOUNT = 1;
    public static final int POP3_ACCOUNT = 2;
    public static final String PATH_SEPARATOR = "/";
    //
    private final String validEmailRegex = "[a-zA-Z1-9._]*@[a-zA-Z1-9.]*[^.]?";
    private final String validEmailRegexFull = ".* <" + validEmailRegex + ">";
    //
    private int serverTimeout = 10000;

    //Constructors
    public Mail() {
        initExtensionsHashMap();
    }

    public Mail(
            String userIn,
            String userOut,
            String passIn,
            String passOut,
            String protocolIn,
            String protocolOut,
            String serverIn,
            String serverOut,
            int portIn,
            int portOut,
            boolean DebugSession) throws NoSuchProviderException {

        //Debug flag
        this.debugSession = DebugSession;

        //Connection URLs
        urlIn = new URLName(protocolIn, serverIn, portIn, "", userIn, passIn);
        urlOut = new URLName(protocolOut, serverOut, portOut, "", userOut, passOut);

        //System.setProperty("mail.mime.decodetext.strict", "false");
        Properties props = new Properties();

        //General properties
        props.put("mail.mime.decodetext.strict", "false");
        props.put("mail.smtp.sendpartial", "true");
        //timeouts
        props.put("mail.smtp.connectiontimeout", serverTimeout);
        props.put("mail.smtp.timeout", serverTimeout);
        props.put("mail.imap.connectiontimeout", serverTimeout);
        props.put("mail.imap.timeout", serverTimeout);
        props.put("mail.smtp.connectiontimeout", serverTimeout);
        props.put("mail.smtp.timeout", serverTimeout);

        //Session config
        session = Session.getInstance(props, null);
        session.setDebug(debugSession);

        //Store config
        store = session.getStore(urlIn);

        //SMTP session object config
        smtpTr = (SMTPTransport) session.getTransport(urlOut);



        if (protocolIn.contains("pop")) {
            this.accountInType = Mail.POP3_ACCOUNT;
        } else if (protocolIn.contains("imap")) {
            this.accountInType = Mail.IMAP_ACCOUNT;
        } else {
            //ERROR
            this.accountInType = 0;
        }

        initExtensionsHashMap();

    }

    /* METHODS PART */
    private void initExtensionsHashMap() {

        extensionToMIMETypeHashMap = new HashMap();

        extensionToMIMETypeHashMap.put("doc", "application/msword");
        extensionToMIMETypeHashMap.put("docx", "application/msword");
        extensionToMIMETypeHashMap.put("pdf", "application/pdf");
        extensionToMIMETypeHashMap.put("rss", "application/rss+xml");
        extensionToMIMETypeHashMap.put("kml", "application/vnd.google-earth.kml+xml");
        extensionToMIMETypeHashMap.put("kmz", "application/vnd.google-earth.kmz");
        extensionToMIMETypeHashMap.put("xls", "application/vnd.ms-excel");
        extensionToMIMETypeHashMap.put("xlsx", "application/vnd.ms-excel");
        extensionToMIMETypeHashMap.put("pptx", "application/vnd.ms-powerpoint");
        extensionToMIMETypeHashMap.put("pps", "application/vnd.ms-powerpoint");
        extensionToMIMETypeHashMap.put("ppt", "application/vnd.ms-powerpoint");
        extensionToMIMETypeHashMap.put("odp", "application/vnd.oasis.opendocument.presentation");
        extensionToMIMETypeHashMap.put("ods", "application/vnd.oasis.opendocument.spreadsheet");
        extensionToMIMETypeHashMap.put("odt", "application/vnd.oasis.opendocument.text");
        extensionToMIMETypeHashMap.put("sxc", "application/vnd.sun.xml.calc");
        extensionToMIMETypeHashMap.put("sxw", "application/vnd.sun.xml.writer");
        extensionToMIMETypeHashMap.put("gzip", "application/x-gzip");
        extensionToMIMETypeHashMap.put("zip", "application/zip");
        extensionToMIMETypeHashMap.put("flac", "audio/flac");
        extensionToMIMETypeHashMap.put("mid", "audio/mid");
        extensionToMIMETypeHashMap.put("m4a", "audio/mp4");
        extensionToMIMETypeHashMap.put("mp3", "audio/mpeg");
        extensionToMIMETypeHashMap.put("ogg", "audio/ogg");
        extensionToMIMETypeHashMap.put("wav", "audio/x-wav");
        extensionToMIMETypeHashMap.put("gif", "image/gif");
        extensionToMIMETypeHashMap.put("jpeg", "image/jpeg");
        extensionToMIMETypeHashMap.put("jpg", "image/jpeg");
        extensionToMIMETypeHashMap.put("png", "image/png");
        extensionToMIMETypeHashMap.put("tiff", "image/tiff");
        extensionToMIMETypeHashMap.put("tif", "image/tiff");
        extensionToMIMETypeHashMap.put("wbmp", "image/vnd.wap.wbmp");
        extensionToMIMETypeHashMap.put("bmp", "image/x-ms-bmp");
        extensionToMIMETypeHashMap.put("ics", "text/calendar");
        extensionToMIMETypeHashMap.put("csv", "text/comma-separated-values");
        extensionToMIMETypeHashMap.put("css", "text/css");
        extensionToMIMETypeHashMap.put("html", "text/html");
        extensionToMIMETypeHashMap.put("htm", "text/html");
        extensionToMIMETypeHashMap.put("text", "text/plain");
        extensionToMIMETypeHashMap.put("txt", "text/plain");
        extensionToMIMETypeHashMap.put("asc", "text/plain");
        extensionToMIMETypeHashMap.put("vcf", "text/x-vcard");
        extensionToMIMETypeHashMap.put("mp4", "video/mp4");
        extensionToMIMETypeHashMap.put("mpeg", "video/mpeg");
        extensionToMIMETypeHashMap.put("mpg", "video/mpeg");
        extensionToMIMETypeHashMap.put("ogv", "video/ogg");
        extensionToMIMETypeHashMap.put("qt", "video/quicktime");
        extensionToMIMETypeHashMap.put("mov", "video/quicktime");
        extensionToMIMETypeHashMap.put("avi", "video/x-msvideo");
        extensionToMIMETypeHashMap.put("eml", "message/rfc822");
    }

    public String getMimeTypeForExtension(String extension) {

        String result = extensionToMIMETypeHashMap.get(extension);

        if (result == null) {
            result = "application/unknown";
        }

        return result;
    }

    public void setDebug() {
        this.debugSession = true;
    }

    public void unsetDebug() {
        this.debugSession = false;
    }

    public boolean connect() throws MessagingException {

        if (!store.isConnected()) {
            store.connect();
            return false;
        }

        return true;
    }

    public void disconnect() {
        try {
            if (sfolder != null && sfolder.isOpen()) {
                sfolder.close(true);
            }
            if (store.isConnected()) {
                store.close();
            }
        } catch (MessagingException ex) {
            Logger.getLogger(Mail.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Mail.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isConnected() {

        if (store == null || !store.isConnected()) {
            return false;
        } else {
            return true;
        }

    }

    public Integer getAccountInType() {
        return this.accountInType;
    }

    /* SESSION DEPENDENT METHODS*/
    public ArrayList<ExtendedMessage> getMessages(String folderName) throws MessagingException {

        UIDFolder uidFolder = null;
        Long uid = null;
        ArrayList<ExtendedMessage> result = new ArrayList();

        FetchProfile profile = new FetchProfile();
        profile.add(FetchProfile.Item.ENVELOPE);

        if (folderName == null || this.accountInType == Mail.POP3_ACCOUNT) {
            System.out.println("Load INBOX");
            sfolder = store.getFolder("INBOX");

        } else {
            sfolder = store.getFolder(folderName);

            if (sfolder == null || !sfolder.exists()) {
                System.out.println("Invalid src folder: " + folderName);
                return null;
            }
        }

        if (sfolder instanceof UIDFolder) {
            uidFolder = (UIDFolder) sfolder;
            profile.add(UIDFolder.FetchProfileItem.UID);
        }

        sfolder.open(Folder.READ_WRITE);
        //sfolder.open(Folder.READ_ONLY);

        Message[] messages = null; //init
        int last = 1; //last fetched messge index
        int step = STEPSIZE; // how much messges are downloaded on call


        int count = sfolder.getMessageCount();

        if (count == 0) {
            System.out.println("No messages");
            return result;
        }


        System.out.print("Fetching " + count + " new messages...   ");

        if (step >= count) {
            step = count - 1;
        }

        while (last <= count) {
            // Get sfolder's list of messages.
            messages = sfolder.getMessages(last, last + step);

            // Retrieve message headers for each message in sfolder.
            sfolder.fetch(messages, profile);

            for (int i = 0; i < messages.length; i++) {

                if (uidFolder != null) {
                    uid = uidFolder.getUID(messages[i]);
                }

                result.add(new ExtendedMessage(messages[i], uid));

            }

            //inc last
            last = last + step + 1;

            if (last + step > count) {
                step = count - last;
            }

        }

        System.out.println(" Done!");
        return result;
    }

    public ArrayList<Long> getMessagesUIDs(String folderName) throws MessagingException {

        UIDFolder uidFolder = null;
        ArrayList<Long> result = new ArrayList();

        FetchProfile profile = new FetchProfile();

        if (folderName == null || this.accountInType == Mail.POP3_ACCOUNT) {
            return null;

        } else {
            sfolder = store.getFolder(folderName);

            if (sfolder == null || !sfolder.exists()) {
                System.out.println("Invalid src folder: " + folderName);
                return null;
            }
        }

        if (sfolder instanceof UIDFolder) {
            uidFolder = (UIDFolder) sfolder;
            profile.add(UIDFolder.FetchProfileItem.UID);
        } else {
            return null;
        }



        if (!sfolder.isOpen()) {

            sfolder.open(Folder.READ_ONLY);

        }

        Message[] messages = null; //init
        int last = 1; //last fetched messge index
        int step = STEPSIZE; // how much messges are downloaded on call

        int count = sfolder.getMessageCount();

        if (count == 0) {
            return result;
        }


        System.out.print("Fetching " + count + " UIDs...   ");

        if (step >= count) {
            step = count - 1;
        }

        while (last <= count) {
            // Get sfolder's list of messages.
            messages = sfolder.getMessages(last, last + step);

            // Retrieve message headers for each message in sfolder.
            sfolder.fetch(messages, profile);

            for (int i = 0; i < messages.length; i++) {

                result.add(uidFolder.getUID(messages[i]));

            }

            //inc last
            last = last + step + 1;

            if (last + step > count) {
                step = count - last;
            }

        }

        System.out.println(" Done!");
        return result;
    }

    public ArrayList<ExtendedMessage> getMessagesByUIDs(String folderName, long[] uids) throws MessagingException {

        UIDFolder uidFolder;
        Message[] messages;
        ArrayList<ExtendedMessage> result = new ArrayList();

        if (this.accountInType != Mail.IMAP_ACCOUNT) {
            return null;
        }

        sfolder = store.getFolder(folderName);

        if (sfolder == null || !sfolder.exists()) {
            System.out.println("Invalid src folder: " + folderName);
            return null;
        }

        if (sfolder instanceof UIDFolder) {
            uidFolder = (UIDFolder) sfolder;
        } else {
            return null;
        }


        if (!sfolder.isOpen()) {
            sfolder.open(Folder.READ_WRITE);
        }


        //take messages
        messages = uidFolder.getMessagesByUID(uids);

        for (Message m : messages) {
            result.add(new ExtendedMessage(m, uidFolder.getUID(m)));
        }


        return result;

    }

    public ExtendedMessage getMessageByUID(String folderName, Long uid) throws MessagingException {


        for (ExtendedMessage m : this.getMessagesByUIDs(folderName, new long[]{uid})) {

            return m;

        }

        return null;

    }

    public boolean moveMessages(long[] messagesUIDs, String srcFolderName, String dstFolderName) throws MessagingException {

        if (srcFolderName == null || dstFolderName == null || this.accountInType == Mail.POP3_ACCOUNT) {
            return false;
        }

        UIDFolder uidFolder;
        Message[] messages;

        boolean flagCanDisconnect = this.connect();

        Folder sfolder = store.getFolder(srcFolderName);
        Folder dfolder = store.getFolder(dstFolderName);


        if (sfolder instanceof UIDFolder) {
            uidFolder = (UIDFolder) sfolder;
        } else {

            if (flagCanDisconnect) {
                this.disconnect();
            }

            return false;
        }



        sfolder.open(Folder.READ_WRITE);
        messages = uidFolder.getMessagesByUID(messagesUIDs);


        if (!dfolder.exists()) {
            dfolder.create(Folder.HOLDS_MESSAGES);
            System.out.println("Folder " + dstFolderName + " created.");
        }


        dfolder.open(Folder.READ_WRITE);
        // dfolder.appendMessages(messages);
        sfolder.copyMessages(messages, dfolder);

        System.out.println(messages.length + " message(s) moved to " + dstFolderName + "\n");

        sfolder.setFlags(messages, new Flags(Flags.Flag.DELETED), true);

        sfolder.close(true);
        dfolder.close(true);

        if (flagCanDisconnect) {
            this.disconnect();
        }

        return true;
    }

    public boolean deleteMessages(long[] messagesUIDs, String folderName) throws MessagingException {

        if (folderName == null || this.accountInType == Mail.POP3_ACCOUNT) {
            return false;
        }

        UIDFolder uidFolder;
        Message[] messages;

        boolean flagCanDisconnect = this.connect();

        Folder sfolder = store.getFolder(folderName);

        if (sfolder instanceof UIDFolder) {
            uidFolder = (UIDFolder) sfolder;
        } else {

            if (flagCanDisconnect) {
                this.disconnect();
            }
            return false;
        }

        sfolder.open(Folder.READ_WRITE);

        messages = uidFolder.getMessagesByUID(messagesUIDs);

        if (messages == null) {


            if (flagCanDisconnect) {
                this.disconnect();
            }

            return false;
        }

        sfolder.setFlags(messages, new Flags(Flags.Flag.DELETED), true);

        sfolder.close(true);

        if (flagCanDisconnect) {
            this.disconnect();
        }

        return true;

    }

    public void deleteMessagesInAFolder(String folderName) throws MessagingException {

        if (folderName == null || this.accountInType == Mail.POP3_ACCOUNT) {
            return;
        }

        boolean flagCanDisconnect = this.connect();

        Folder sfolder = store.getFolder(folderName);
        sfolder.open(Folder.READ_WRITE);
        Message[] messages = sfolder.getMessages();

        sfolder.setFlags(messages, new Flags(Flags.Flag.DELETED), true);

        sfolder.close(true);

        if (flagCanDisconnect) {
            this.disconnect();
        }


    }

    public boolean renameFolder(String folderName, String newFolderName) throws MessagingException {


        if (folderName == null || this.accountInType == Mail.POP3_ACCOUNT) {
            return false;
        }

        boolean flagCanDisconnect = this.connect();

        Folder sfolder = store.getFolder(folderName);


        Folder dfolder = store.getFolder(newFolderName);

        if (dfolder.exists()) {
            return false;
        }

        sfolder.renameTo(dfolder);

        if (flagCanDisconnect) {
            this.disconnect();
        }

        return true;

    }

    public Long createFolder(String folderName) throws MessagingException {


        if (folderName == null || this.accountInType == Mail.POP3_ACCOUNT) {
            return null;
        }

        UIDFolder uidFolder;

        boolean flagCanDisconnect = this.connect();

        Folder sfolder = store.getFolder(folderName);


        if (!sfolder.exists()) {
            sfolder.create(Folder.HOLDS_MESSAGES);
            System.out.println("Folder created.");
        }


        if (sfolder instanceof UIDFolder) {
            uidFolder = (UIDFolder) sfolder;
        } else {

            if (flagCanDisconnect) {
                this.disconnect();
            }
            return null;
        }

        if (flagCanDisconnect) {
            this.disconnect();
        }

        return uidFolder.getUIDValidity();

    }

    public boolean deleteFolder(String folderName) throws MessagingException {


        if (folderName == null || this.accountInType == Mail.POP3_ACCOUNT) {
            return false;
        }


        boolean flagCanDisconnect = this.connect();

        Folder sfolder = store.getFolder(folderName);


        if (!sfolder.exists()) {
            return true;
        }


        sfolder.delete(false);

        if (flagCanDisconnect) {
            this.disconnect();
        }

        return true;

    }

    public byte[] getByteArrayFromMessage(Message m) throws IOException, MessagingException {

        ByteArrayOutputStream by = new ByteArrayOutputStream();
        byte[] arr;
        m.writeTo(by);
        arr = by.toByteArray();
        by.close();

        return arr;

    }

    /* END OF SESSION DEPENDENT METHODS*/
    //* METHODS WITH NEED TO BE CONNECTED FIRST *//
    public Long getFolderUIDValidity(String folderName) throws MessagingException {


        if (folderName == null || this.accountInType != Mail.IMAP_ACCOUNT) {
            return null;

        }

        Folder folder = store.getFolder(folderName);

        if (folder == null || !folder.exists()) {
            System.out.println("Invalid src folder: " + folderName);
            return null;
        }


        if (folder instanceof UIDFolder) {

            try {

                return ((UIDFolder) folder).getUIDValidity();

            } catch (MessagingException ex) {
                Logger.getLogger(Mail.class.getName()).log(Level.SEVERE, null, ex);
                return new Long(-1);
            }

        }

        return null;

    }

//    public void savetoSentItems(Message message) throws MessagingException {
//        System.out.print("Saving to Sent Items...  ");
//
//        Folder folder = store.getFolder("Sent Items");
//
//        if (!folder.exists()) {
//            folder.create(Folder.HOLDS_MESSAGES);
//            System.out.println("Folder created.");
//        }
//
//        folder.open(Folder.READ_WRITE);
//        message.setFlag(Flags.Flag.SEEN, true);
//        folder.appendMessages(new Message[]{message});
//        folder.close(true);
//        System.out.println("Done!");
//
//    }
    public void appendMessageToFolder(Message message, String folderName) throws MessagingException {


        boolean flagCanDisconnect = this.connect();

        Folder folder = store.getFolder(folderName);


        folder.open(Folder.READ_WRITE);
        //message.setFlag(Flags.Flag.SEEN, true);
        folder.appendMessages(new Message[]{message});
        folder.close(true);

        if (flagCanDisconnect) {
            this.disconnect();
        }

  
    }

        public void appendMessagesToFolder(Message[] message, String folderName) throws MessagingException {
            
        boolean flagCanDisconnect = this.connect();

        Folder folder = store.getFolder(folderName);


        folder.open(Folder.READ_WRITE);
        folder.appendMessages(message);
        folder.close(true);

        if (flagCanDisconnect) {
            this.disconnect();
        }

  
    }

    public ArrayList<String> getFoldersList() throws MessagingException {

        ArrayList<String> result = new ArrayList();

        for (Folder folder : store.getDefaultFolder().list("*")) {

            result.add(folder.getFullName());

        }

        return result;
    }

    //* END OF METHODS WITH NEED TO BE CONNECTED FIRST *//
    //* COMPOSE METHODS *//
    public void sendMessage(Message message) throws MessagingException {

        message.removeHeader("X-Mailer");
        message.addHeader("X-Mailer", "JAPP Mail");

        smtpTr.connect();

        smtpTr.sendMessage(message, message.getAllRecipients());
        System.out.println("Email sent!");

        smtpTr.close();

    }

    private String constructHtmlHeader(Message message) throws MessagingException {

        StringBuilder header = new StringBuilder();
        Address[] temp = null;
        String address = null;
        String email = null;
        String name = null;
        String date = message.getSentDate().toString();
        String subject = fixHeaderSubject(message.getSubject()).replace("<", "&lt;").replace(">", "&gt;");

        header.append("<BR><BR> </FONT></DIV><BR><BLOCKQUOTE style=\"BORDER-LEFT: #000000 2px solid; PADDING-LEFT: 5px; PADDING-RIGHT: 0px; MARGIN-LEFT: 5px; MARGIN-RIGHT: 0px\" dir=ltr><DIV style=\"FONT: 12pt arial\">----- Original Message ----- </DIV>");

        //From
        address = decodeMimeWord(message.getFrom()[0].toString());
        email = getEmailAddress(address);
        name = getEmailName(address);
        if (name == null) {
            name = email;
        }
        header.append("<DIV style=\"FONT: 12pt arial; BACKGROUND: #e4e4e4; font-color: black\"><B>From: </B> <A title=").append(email).append(" href=\"mailto:").append(email).append("\">").append(name).append("</A> </DIV>");

        //To
        temp = message.getRecipients(Message.RecipientType.TO);
        if (temp != null) {
            header.append("<DIV style=\"FONT: 12pt arial\"><B>To: </B>");
            for (int i = 0; i < temp.length; i++) {
                address = decodeMimeWord(temp[i].toString());
                email = getEmailAddress(address);
                name = getEmailName(address);
                if (name == null) {
                    name = email;
                }
                if (i > 0) {
                    header.append(" ; ");
                }

                header.append("<A title=").append(email).append(" href=\"mailto:").append(email).append("\">").append(name).append("</A>");

            }
            header.append("</DIV>");
        }

        //CC
        temp = message.getRecipients(Message.RecipientType.CC);
        if (temp != null) {
            header.append("<DIV style=\"FONT: 12pt arial\"><B>Cc: </B>");
            for (int i = 0; i < temp.length; i++) {
                address = decodeMimeWord(temp[i].toString());
                email = getEmailAddress(address);
                name = getEmailName(address);
                if (name == null) {
                    name = email;
                }
                if (i > 0) {
                    header.append(" ; ");
                }

                header.append("<A title=").append(email).append(" href=\"mailto:").append(email).append("\">").append(name).append("</A>");

            }
            header.append("</DIV>");
        }

        //Date
        header.append("<DIV style=\"FONT: 12pt arial\"><B>Sent: </B>").append(date).append("</DIV>");

        //Subject
        header.append("<DIV style=\"FONT: 12pt arial\"><B>Subject: </B>").append(subject).append("</DIV>");

        //finaly
        header.append("<DIV><BR></DIV>");

        return header.toString();
    }

    public Message createMimeMessage(Address[] from,
            Address[] to,
            Address[] cc,
            Address[] bcc,
            String subject,
            String htmlText,
            ArrayList<File> filesToBeAttached,
            Message originalMessage) throws MessagingException, UnsupportedEncodingException, IOException {


        MimeMessage result = new MimeMessage(session);

        if (from != null) {
            result.addFrom(from);
        }

        if (to != null) {
            result.addRecipients(Message.RecipientType.TO, to);
        }
        if (cc != null) {
            result.addRecipients(Message.RecipientType.CC, cc);
        }
        if (bcc != null) {
            result.addRecipients(Message.RecipientType.BCC, bcc);
        }
        if (subject != null) {
            result.addHeader("Subject", MimeUtility.encodeWord(subject, "UTF-8", "Q"));
        }

        result.setSentDate(new Date());

        Multipart mixed = null;
        Multipart related = null;
        MimeBodyPart bp;

        Multipart alternative = new MimeMultipart("alternative");


        bp = new MimeBodyPart();
        bp.setText(this.html2text(htmlText));
        bp.addHeaderLine("Content-Type: text/plain; charset=\"utf-8\"");
        bp.addHeaderLine("Content-Transfer-Encoding: quoted-printable");
        alternative.addBodyPart(bp);


        bp = new MimeBodyPart();
        bp.setText(htmlText);
        bp.addHeaderLine("Content-Type: text/html; charset=\"utf-8\"");
        bp.addHeaderLine("Content-Transfer-Encoding: quoted-printable");
        alternative.addBodyPart(bp);

        if (originalMessage != null) {
            ArrayList<BodyPart> obtainRelatedAttachmentsRecursivly = obtainRelatedAttachmentsRecursivly((originalMessage));

            if (obtainRelatedAttachmentsRecursivly != null) {

                related = new MimeMultipart("related");
                bp = new MimeBodyPart();
                bp.setContent(alternative);
                related.addBodyPart(bp);

                for (BodyPart tmp : obtainRelatedAttachmentsRecursivly) {

                    related.addBodyPart(tmp);
                }
            }

        }


        if (filesToBeAttached != null && !filesToBeAttached.isEmpty()) {

            FileDataSource fds;
            String contentType;

            mixed = new MimeMultipart("mixed");

            bp = new MimeBodyPart();

            if (related != null) {
                bp.setContent(related);
            } else {
                bp.setContent(alternative);
            }

            mixed.addBodyPart(bp);

            for (File f : filesToBeAttached) {

                contentType = this.getMimeExtensionOfFile(f);
                if (contentType == null) {
                    continue;
                }

                fds = new FileDataSource(f);
                bp = new MimeBodyPart();

                bp.setDataHandler(new DataHandler(fds));
                bp.setFileName(fds.getName());
                bp.setHeader("Content-Type", contentType);
                bp.setDisposition(Part.ATTACHMENT);
                mixed.addBodyPart(bp);

            }

            if (mixed.getCount() == 1) {
                mixed = null;
            }

        }


        if (mixed != null) {
            result.setContent(mixed);
        } else if (related != null) {
            result.setContent(related);
        } else {
            result.setContent(alternative);
        }

        return result;

    }

    //*END OF COMPOSE METHODS *//
//* METHODS WHICH COULD BE STATIC *//
    private String decodeMimeWord(String word) {

        String w = word;

        if (w.startsWith("=?")) {
            try {
                w = w.replace("?= ", "?=");
                w = MimeUtility.decodeWord(w);
            } catch (Exception notused) {
                return word;
            }
        }

        return w;
    }

    public String fixHeaderSubject(String s) {

        String subject = s;
        if (subject == null) {
            subject = " ";
        }

        subject = decodeMimeWord(subject);

        subject = subject.replaceAll("\\s", " ");

        return subject;
    }

    public String fixForwardSubject(String s, String prefix) {

        String subject = s;
        if (subject == null) {
            subject = " ";
        }

        subject = decodeMimeWord(subject);

        int tag = subject.indexOf(": ");

        if (tag < 5 && tag > -1) {
            subject = subject.substring(tag + 2, subject.length());
        }

        return prefix.concat(subject);

    }

    public String getSubjectPrefix(String s) {

        String subject = s;

        if (subject == null) {
            return "";
        }

        subject = decodeMimeWord(subject);

        int tag = subject.indexOf(": ");

        if (tag < 5 && tag > -1) {
            return subject.substring(0, tag);
        } else {
            return "";
        }

    }

    private String getEmailName(String address) {

        String res = null;
        int end = address.indexOf("<");
        if (end != -1) {
            res = address.substring(0, end).replace("\"", "").trim();
        }

        return res;
    }

    public String getEmailName(Address address) {

        if (address == null) {
            return null;
        }

        return getEmailName(decodeMimeWord(address.toString()));

    }

    private String getEmailAddress(String address) {

        String res;
        String[] tmp;

        res = address;

        if (res.contains(".MISSING-HOST-NAME.")) {
            res = res.replace(" ", "");
        }

        res = res.replaceAll("[<>'\"]", " ");

        tmp = res.split(" ");
        for (int i = 0; i < tmp.length; i++) {
            if (tmp[i].contains("@")) {
                res = tmp[i];
                break;
            }
        }

        return res.toLowerCase();
    }

    public String getEmailAddress(Address address) {

        if (address == null) {
            return null;
        }

        return getEmailAddress(decodeMimeWord(address.toString()));

    }

    public String castAddressToString(Address address) {

        if (address == null) {
            return null;
        }

        return decodeMimeWord(address.toString());

    }

    public String getBodyHTML(Part part) throws MessagingException, IOException {


        if (part.isMimeType("text/html") && (part.getDisposition() == null)) {
            String s = (String) part.getContent();
            return s;
        }

        if (part.isMimeType("multipart/*")) {

            Multipart mp = (Multipart) part.getContent();

            for (int i = 0; i < mp.getCount(); i++) {

                String s = getBodyHTML(mp.getBodyPart(i));

                if (s != null) {
                    return s;
                }
            }
        }

        return null;
    }

    private Multipart obtainAttachmentsRecursivly(Multipart mp) throws MessagingException, IOException {

        Multipart current = mp;

        Multipart previous;
        Multipart result = new MimeMultipart();


        for (int i = 0; i < current.getCount(); i++) {

            //if part is multipart call the method again
            if (current.getBodyPart(i).isMimeType("multipart/*")) {

                //recursion
                previous = obtainAttachmentsRecursivly((Multipart) current.getBodyPart(i).getContent());

                for (int j = 0; j < previous.getCount(); j++) {

                    result.addBodyPart(previous.getBodyPart(j));
                }


            } else if (!current.getBodyPart(i).isMimeType("text/*")
                    || (current.getBodyPart(i).isMimeType("text/*") && current.getBodyPart(i).getDisposition() != null)) {

                result.addBodyPart(current.getBodyPart(i));

            } else {
                continue;
            }
        }

        //return the multipart
        return result;
    }

    private ArrayList<BodyPart> obtainRelatedAttachmentsRecursivly(Part p) throws MessagingException, IOException {

        BodyPart bp;
        Multipart mp;
        ArrayList<BodyPart> result = new ArrayList();

        if (p.isMimeType("multipart/related")) {

            mp = (Multipart) p.getContent();

            for (int i = 0; i < mp.getCount(); i++) {


                bp = mp.getBodyPart(i);

                if (bp.isMimeType("image/*")) {

                    result.add(bp);
                }

            }

            return result;

        } else if (p.isMimeType("multipart/mixed")) {

            mp = (Multipart) p.getContent();

            for (int i = 0; i < mp.getCount(); i++) {

                bp = mp.getBodyPart(i);

                if (bp.isMimeType("multipart/*")) {
                    result = obtainRelatedAttachmentsRecursivly(bp);
                }

            }



        }

        if (result != null && result.isEmpty()) {
            result = null;
        }

        return result;
    }

    public ArrayList<File> getAttachments(Part p, String filesLocation) throws MessagingException, IOException {

        if (p == null || !p.isMimeType("multipart/*")) {
            return null;
        }

        ArrayList<File> result = new ArrayList();

        Multipart mp = obtainAttachmentsRecursivly((Multipart) p.getContent());

        (new File(filesLocation)).mkdir();

        MimeBodyPart mbp;
        String fileName;
        File file;

        for (int i = 0; i < mp.getCount(); i++) {

            mbp = (MimeBodyPart) mp.getBodyPart(i);

            int attachmentSize = mbp.getSize();

            if (attachmentSize != -1 && attachmentSize < 5000 && mbp.isMimeType("image/*")) {
                continue;
            }

            fileName = mbp.getFileName();
            file = new File(filesLocation + File.separator + fileName);

            mbp.saveFile(file);
            result.add(file);

        }

        if (result.isEmpty()) {
            result = null;
        }

        return result;
    }

    public String getBodyPlaintext(Part part) throws MessagingException, IOException {


        if (part.isMimeType("text/plain") && (part.getDisposition() == null)) {
            String s = (String) part.getContent();
            return this.formatBodyPlaintext(s);
        }

        if (part.isMimeType("multipart/*")) {

            Multipart mp = (Multipart) part.getContent();

            for (int i = 0; i < mp.getCount(); i++) {

                String s = getBodyHTML(mp.getBodyPart(i));

                if (s != null) {
                    return s;
                }
            }
        }

        return null;
    }

    private String formatBodyPlaintext(String plaintext) {

        if (plaintext == null) {
            return null;
        }

        String body = plaintext.replaceAll("[\t\\x0B\f\r]", " ");

        while (body.contains(" \n")) {
            body = body.replace(" \n", "\n");
        }

        while (body.contains("\n ")) {
            body = body.replace("\n ", "\n");
        }

        while (body.contains("\n\n\n")) {
            body = body.replace("\n\n\n", "\n\n");
        }

        //Plain text part bugs
        if (body.startsWith("Message")) {
            body = body.substring(7, body.length());
        }
        if (body.startsWith("Untitled Document")) {
            body = body.substring(17, body.length());
        }

        return body.trim();
    }

    public MimeMessage createMimeMessageFromByteArray(byte[] arr) throws MessagingException {
        return new MimeMessage(Session.getInstance(new Properties(), null), new ByteArrayInputStream(arr));
    }

    private Address createAddress(String email, String name) throws UnsupportedEncodingException, AddressException {

        Address result;

        if (email == null) {
            return null;

        }

        if (name == null) {
            result = new InternetAddress(email);
        } else {
            result = new InternetAddress(email, name);
        }

        return result;

    }

    public Address createAddress(String address) throws UnsupportedEncodingException, AddressException {

        Address result = null;
        String name;
        String email;

        if (address.matches(validEmailRegex)) {

            result = this.createAddress(address, null);

        } else if (address.matches(validEmailRegexFull)) {

            name = address.substring(0, address.indexOf("<")).trim();
            email = address.substring(address.indexOf("<") + 1, address.indexOf(">")).trim();

            result = this.createAddress(email, name);

        }

        return result;
    }

    private Address[] removeDuplicatedAddresses(Address[] addresses) {

        if (addresses == null) {
            return null;
        } else if (addresses.length == 0) {
            return null;
        }

        ArrayList<Address> new_addressses = new ArrayList<Address>();
        boolean dublicated;

        for (int i = 0; i < addresses.length - 1; i++) {

            dublicated = false;

            for (int j = i + 1; j < addresses.length; j++) {

                if (addresses[i].equals(addresses[j])) {
                    dublicated = true;
                    break;
                }
            }

            if (!dublicated) {
                new_addressses.add(addresses[i]);
            }

        }

        //add the last element
        new_addressses.add(addresses[addresses.length - 1]);

        return (Address[]) new_addressses.toArray(new Address[new_addressses.size()]);
    }

    public Address[] mergeAddresses(Address[] part_one, Address[] part_two) throws MessagingException {

        ArrayList<Address> result = new ArrayList();


        if (part_one != null) {
            result.addAll(Arrays.asList(part_one));
        }

        if (part_two != null) {
            result.addAll(Arrays.asList(part_two));
        }

        if (result.isEmpty()) {
            return null;
        }

        return (Address[]) result.toArray(new Address[result.size()]);

    }

    //* END OF METHODS WHICH COULD BE STATIC *//
    public String getMimeExtensionOfFile(File f) {

        String fileName = f.getName();

        int dotPosition = fileName.lastIndexOf(".");

        if (dotPosition == -1) {
            return null;
        }


        return getMimeTypeForExtension(fileName.substring(dotPosition + 1));


    }

    public void saveMailEml(Message message, String location, String name) {
        PrintStream out = null;
        PrintStream console = System.out;
        try {

            out = new PrintStream(new BufferedOutputStream(new FileOutputStream(location + File.separator + name + ".eml")));
            System.setOut(out);
            message.writeTo(out);
            out.close();
            System.setOut(console);
            System.out.println("Message saved as " + name + ".eml");
        } catch (IOException ex) {
            // ex.printStackTrace();
        } catch (MessagingException ex) {
            //  ex.printStackTrace();
        } finally {
            out.close();
            System.setOut(console);
        }
    }

    public String html2text(String html) {
        return Jsoup.parse(html).text();
    }

    public String text2html(String plaintext) {

        //TODO This should be replaced with html generator

        if (plaintext == null) {
            return " ";
        }

        return plaintext.replace("\n", "<br>");

    }

    private String addHeaderToOriginalHtml(String original_html, String html_header) {

        int bodytag_begin;
        int bodytag_end;
        String part1;
        String part2;
        String result;

        original_html = original_html.replaceFirst("content=\"text/html; charset=\\S*\"", "content=\"text/html; charset=utf-8\"");

        bodytag_begin = original_html.toUpperCase().indexOf("<BODY");
        if (bodytag_begin == -1) {

            return "<HTML><BODY>" + html_header + original_html + "</BODY></HTML>";

        }

        bodytag_end = original_html.indexOf(">", bodytag_begin);
        if (bodytag_end == -1) {
            return null;
        }

        part1 = original_html.substring(0, bodytag_end + 1);
        part2 = original_html.substring(bodytag_end + 1, original_html.length());

        result = part1.concat("\n").concat(html_header).concat("\n").concat(part2);

        return result;
    }

    public String getReplyMessageBody(Message m) throws MessagingException, IOException {

        String header = this.constructHtmlHeader(m);
        String body = this.getBodyHTML(m);

        return this.addHeaderToOriginalHtml(body, header);

    }

    private MimeBodyPart transformMessageInAttachment(Message message) throws IOException, MessagingException {

        // create  message part
        MimeBodyPart mbp = new MimeBodyPart();

        //make message accessable by the other tread
        final Message temp_message = message;

        //pipe out>in
        final PipedOutputStream out = new PipedOutputStream();
        InputStream in = new PipedInputStream(out);


        //write the message to stream out with other tread to prevent process from hanging

        new Thread(
                new Runnable() {

                    @Override
                    public void run() {
                        try {
                            temp_message.writeTo(out);
                            out.close();
                        } catch (IOException ex) {
                            Logger.getLogger(Mail.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (MessagingException ex) {
                            Logger.getLogger(Mail.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }).start();



        // Return the InputStream
        DataSource ds = new ByteArrayDataSource(in, "application/octet-stream");
        DataHandler dh = new DataHandler(ds);
        mbp.setDataHandler(dh);
        mbp.setFileName("Original_Message.eml");
        mbp.setHeader("Content-Type", "message/rfc822");
        mbp.setDisposition(Part.ATTACHMENT);
        in.close();

        return mbp;
    }
}
