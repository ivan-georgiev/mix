/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jappm;

import jappm.ui.JF_MainW;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Ivan
 */
public class ThreadsCreator implements Runnable {

    public static final int LOAD_MESSAGES_FROM_DB = 1;
    public static final int SYNC_WITH_IMAP_AND_PREVIEW = 3;
    public static final int SYNC_WITH_POP3_AND_PREVIEW = 2;
    public static final int PREVIEW_MAIL_CONTENT = 4;
    //
    private boolean previewErrors = true;
    int mode;
    Controller controller;
    JF_MainW mainWindow;
    Integer folderId;
    JAPPMessage jappMessage;

    public ThreadsCreator(Controller controller, JF_MainW main, Integer folderId, JAPPMessage message,
            Integer mode) {

        this.controller = controller;
        this.mainWindow = main;
        this.folderId = folderId;
        this.jappMessage = message;
        this.mode = mode;
    }

    public ThreadsCreator(JF_MainW main, Integer folderId) {

        this.mainWindow = main;
        this.folderId = folderId;
    }

    public void disableErrors() {
        this.previewErrors = false;
    }

    @Override
    public void run() {

        // Thread t = Thread.currentThread();
        //  System.out.println("Start: " + mode + " " + t.getName());

        switch (mode) {
            case LOAD_MESSAGES_FROM_DB:
                Common.sleep(500);
                previewDbMails();
                break;
            case SYNC_WITH_IMAP_AND_PREVIEW:
                Common.sleep(2000);
                syncMailWithIMAP();
                removeMissingMessages();
                break;
            case SYNC_WITH_POP3_AND_PREVIEW:
                Common.sleep(2000);
                syncMailWithPOP3();
                break;
            case PREVIEW_MAIL_CONTENT:
                Common.sleep(500);
                if (downloadCompleteMessage() != null) {
                    previewMail();
                    processAttachments();
                    updateMessageAsSeen();

                }
                break;
            default:
                return;
        }

        //System.out.println("End: " + mode + " " + t.getName());
    }

    private void previewDbMails() {

        controller.previewMessages(controller.getMessagesFromDb(folderId));

        int rowItmesCount = mainWindow.getJTable1().getRowCount();

        if (rowItmesCount == 0) {
            mainWindow.setTitle("JAPP Mail: No items in this view");
        } else {
            mainWindow.setTitle("JAPP Mail: " + rowItmesCount + " items in this view");
        }
    }

    private void syncMailWithIMAP() {

        ArrayList<Integer> messagesIDs;

        while (mainWindow.previewMailsFromTheDb.isAlive()) {
            Common.sleep(500);
        }

        mainWindow.setTitle("JAPP Mail: Synchronizing with the server...");


        messagesIDs = controller.syncMessagesWithIMAP(folderId);

        if (messagesIDs != null) {
            mainWindow.setTitle("JAPP Mail: " + messagesIDs.size() + " new messages.");
        } else {
            if (previewErrors) {
                JOptionPane.showMessageDialog(mainWindow, controller.getExceptionInfo(), "Error", JOptionPane.ERROR_MESSAGE);
            }

        }


        int rowItmesCount = mainWindow.getJTable1().getRowCount();
        if (rowItmesCount == 0) {
            mainWindow.setTitle("JAPP Mail: No items in this view");
        } else {
            mainWindow.setTitle("JAPP Mail: " + rowItmesCount + " items in this view");
        }


    }

    private void syncMailWithPOP3() {

        while (mainWindow.previewMailsFromTheDb.isAlive()) {
            Common.sleep(500);
        }

        ArrayList<Integer> messagesIDs;

        mainWindow.setTitle("JAPP Mail: Synchronizing with the server...");

        messagesIDs = controller.syncMessagesWithPOP3(folderId);

        if (messagesIDs != null) {
            mainWindow.setTitle("JAPP Mail: " + messagesIDs.size() + " new messages.");
        } else {
            if (previewErrors) {
                JOptionPane.showMessageDialog(mainWindow, controller.getExceptionInfo(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        int rowItmesCount = mainWindow.getJTable1().getRowCount();
        if (rowItmesCount == 0) {
            mainWindow.setTitle("JAPP Mail: No items in this view");
        } else {
            mainWindow.setTitle("JAPP Mail: " + rowItmesCount + " items");
        }

    }

    private Integer downloadCompleteMessage() {

        Integer result = null;

        while (mainWindow.syncPOP3Messages.isAlive() || mainWindow.syncIMAPMessages.isAlive()) {
            Common.sleep(500);
        }

        // String fullPathFromTreeNode = mainWindow.getFullPathFromTreeNode(mainWindow.getJTree1().getSelectionPath());

        mainWindow.setTitle("JAPP Mail: Downloading message...");

        result = controller.updateMessageContentToFull(jappMessage);

        mainWindow.setTitle("JAPP Mail");

        return result;
    }

    private void previewMail() {

        String text = controller.getMessageBodyFromDb(jappMessage);

        if (text != null) {
            mainWindow.getJEditorPane1().setText(text);
        }

    }

    private void updateMessageAsSeen() {
        mainWindow.updateMessageAsSeen(jappMessage);
    }

    private void processAttachments() {

        mainWindow.updateMessageAttachments(jappMessage);

    }

    private void removeMissingMessages() {


        mainWindow.removeMissingMessages();

    }
}
