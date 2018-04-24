/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jappm;


import jappm.db.DBStatement;
import jappm.mail.Mail;
import jappm.ui.JF_MainW;
import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;

/**
 *
 * @author Ivan
 */
public class Main {

    private static final String DATABASE_NAME = "jappm";
    private static final String DATABASE_FULLPATH = "."
            + File.separator + "config"
            + File.separator + DATABASE_NAME;
    private static final String DATABASE_USER = "jappm";
    private static final String DATABASE_PASS = "jappm";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws NoSuchProviderException, SQLException, ClassNotFoundException, MessagingException {


        System.out.println("JAPP Mail");
        /* TODOs
         * MAIN WINDOW:
         * delte of multiple selected files
         * save all attachments dialog
         * Controller:
         * syncFolders method to be fixed
         * move mail between 2 imap accounts
         * Compose:
         * html edit toolbar
         * 
         * 
         */
        
        
      // NewJFrame d = new NewJFrame(null,null,null);
       //d.setVisible(true);

        DBStatement db;
        HashMap<Integer, Mail> mailAccounts = new HashMap();



        db = new DBStatement(DATABASE_USER, DATABASE_PASS, DATABASE_FULLPATH);
        db.open();

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

            mailAccounts.put(dms.getAccountId(), new Mail(
                    dms.userIn, dms.userOut,
                    dms.passwordIn, dms.passwordOut,
                    dms.serverInType + suffixIn, "smtp" + suffixOut,
                    dms.serverIn, dms.serverOut,
                    dms.serverInPort, dms.serverOutPort,
                    false));

        }



        //        mailAccounts.put(2, new Mail(
        //                "thesis.testmail@gmail.com", "thesis.testmail@gmail.com",
        //                "12qwzxas", "12qwzxas",
        //                "imaps", "smtps",
        //                "imap.gmail.com", "smtp.gmail.com",
        //                993, 465,//993,465 143,25
        //                false));
        //
        //        mailAccounts.put(3, new Mail(
        //                "thesis.testmail@gmail.com", "thesis.testmail@gmail.com",
        //                "12qwzxas", "12qwzxas",
        //                "pop3s", "smtps",
        //                "pop.gmail.com", "smtp.gmail.com",
        //                995, 465,
        //                false));


        Controller controller = new Controller(db, mailAccounts);

        JF_MainW mainWindow = new JF_MainW(controller);
        mainWindow.setVisible(true);

        controller.setMainWindow(mainWindow);
    }
}
