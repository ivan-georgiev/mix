/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NewJFrame.java
 *
 * Created on 2011-12-25, 21:40:43
 */
package jappm.ui;

import jappm.Common;
import jappm.Controller;
import jappm.JAPPMessage;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.html.HTMLEditorKit;

/**
 *
 * @author Ivan
 */
public class JF_Compose extends javax.swing.JFrame {

    Integer accountId;
    Controller controller;
    JAPPMessage originalMessage;
    //
    ArrayList<File> filesToBeAttached = new ArrayList();
    HashMap<Integer, Integer> accountIdToComboIndex = new HashMap();
    //
    public final static int NEW = 0;
    public final static int REPLY = 1;
    public final static int REPLY_ALL = 2;
    public final static int FORWARD = 3;
    public final static int FORWARD_ALL = 4;
    public final static int FORWARD_ATTACHED = 5;
    //
    private final String ICONS_LOCATION = "images";
    public final String SEND_IMAGE = ICONS_LOCATION + File.separator + "send.png";

    /** Creates new form NewJFrame */
    public JF_Compose(Controller controller, JAPPMessage originalMessage, int replyMode) {

        this.controller = controller;
        this.originalMessage = originalMessage;

        setLookAndFeel();

        initComponents();

        if (originalMessage != null) {

            switch (replyMode) {
                case REPLY:
                    this.jTextPane1.setText(originalMessage.getFrom());
                    this.jTextPane4.setText("Re: " + controller.getStripedSubject(originalMessage.getSubject()));
                    jEditorPane1.setText(controller.getHtmlReplyBody(originalMessage));
                    break;

                case REPLY_ALL:
                    this.jTextPane1.setText(originalMessage.getFrom());
                    this.jTextPane2.setText(originalMessage.getTo() + "; " + originalMessage.getCC());
                    this.jTextPane4.setText("Re: " + controller.getStripedSubject(originalMessage.getSubject()));
                    jEditorPane1.setText(controller.getHtmlReplyBody(originalMessage));
                    break;

                case FORWARD:
                    this.jTextPane4.setText("Fw: " + controller.getStripedSubject(originalMessage.getSubject()));
                    jEditorPane1.setText(controller.getHtmlReplyBody(originalMessage));

                    ArrayList<File> files = controller.getAttachments(originalMessage);

                    if (files != null) {
                        for (File f : files) {
                            this.createAttachmentButton(f);
                        }
                    }
                    break;

                case FORWARD_ATTACHED:
                    this.createAttachmentButton(controller.getMessageAsFile(originalMessage));
                    break;

            }


            jEditorPane1.setCaretPosition(0);

        }

        this.setPreferredSize(new Dimension(820, 550));

        this.pack();

        this.setTitle("JAPP Mail Compose");

        this.setLocationRelativeTo(null);

    }

    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane2 = new javax.swing.JTextPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextPane3 = new javax.swing.JTextPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextPane4 = new javax.swing.JTextPane();
        jToolBar2 = new javax.swing.JToolBar();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jComboBox2 = new javax.swing.JComboBox();
        jComboBox1 = new javax.swing.JComboBox();
        jScrollPane5 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jButton2 = new javax.swing.JButton();
        jToolBar1 = new javax.swing.JToolBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        HTMLEditorKit kit = new HTMLEditorKit();
        jEditorPane1.setEditorKit(kit);

        jButton1.setText("Send");
        jButton1.setIcon(Common.getImageAsIconWithSize(SEND_IMAGE, 30, 30));
        jButton1.setVerticalTextPosition(SwingConstants.BOTTOM);
        jButton1.setHorizontalTextPosition(SwingConstants.CENTER);
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setText("Cc:");
        jButton3.setBorderPainted(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Bcc:");
        jButton4.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel1.setText("Subject:");

        jScrollPane1.setViewportView(jTextPane1);
        jScrollPane2.setViewportView(jTextPane2);
        jScrollPane3.setViewportView(jTextPane3);
        jScrollPane4.setViewportView(jTextPane4);

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);
        jToolBar2.add(jButton5);
        jToolBar2.addSeparator();
        jToolBar2.add(jButton6);
        jToolBar2.add(jButton7);
        jToolBar2.add(jComboBox2);

        jButton5.setText("Attach File");
        jButton5.setFocusable(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton5.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });



        jButton6.setText("B");
        jButton6.setFocusable(false);
        jButton6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton6.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);


        jButton7.setText("U");
        jButton7.setFocusable(false);
        jButton7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);



        for (String[] cur : controller.getAccountsList()) {

            jComboBox1.addItem(new UserAccountItem(Integer.parseInt(cur[0]), cur[1]));

            accountIdToComboIndex.put(Integer.getInteger(cur[0]), jComboBox1.getItemCount() - 1);
        }


        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"8", "10", "12", "16"}));
        jComboBox2.setSelectedIndex(1);
        jComboBox2.setMaximumSize(new java.awt.Dimension(40, 20));


        jScrollPane5.setViewportView(jEditorPane1);

        jButton2.setText("To:");
        jButton2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setVisible(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jToolBar1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 621, Short.MAX_VALUE).addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, 621, Short.MAX_VALUE).addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 621, Short.MAX_VALUE).addComponent(jComboBox1, javax.swing.GroupLayout.Alignment.LEADING, 0, 621, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addGroup(layout.createSequentialGroup().addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE).addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))).addComponent(jLabel1)).addGap(6, 6, 6).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE).addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE).addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE).addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)))).addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jButton2)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jButton3)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jButton4).addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel1).addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE).addContainerGap()));


    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {

        controller.constructAndInsertNewMessageInDb(
                ((UserAccountItem) jComboBox1.getSelectedItem()).getId(),
                ((UserAccountItem) jComboBox1.getSelectedItem()).toString(),
                this.jTextPane1.getText(),
                this.jTextPane2.getText(),
                this.jTextPane3.getText(),
                this.jTextPane4.getText(),
                this.jEditorPane1.getText(),
                filesToBeAttached,
                originalMessage,
                controller.OUTBOX_ID,
                false);


        //TODO temp
        boolean success = controller.sendMessages();

        if (!success) {

            JOptionPane.showMessageDialog(this, controller.getExceptionInfo(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        this.dispose();
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {

        GJD_SearchAddresses newJDialog = new GJD_SearchAddresses(this, true);
        newJDialog.setVisible(true);
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {

        GJD_SearchAddresses newJDialog = new GJD_SearchAddresses(this, true);
        newJDialog.setVisible(true);
    }

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {

        GJD_SearchAddresses newJDialog = new GJD_SearchAddresses(this, true);
        newJDialog.setVisible(true);
    }

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {

        JFileChooser fc = new JFileChooser();
        fc.setDialogType(JFileChooser.CUSTOM_DIALOG);
        fc.setDialogTitle("Choose a file to attach");
        fc.setApproveButtonToolTipText("Click to attach");
        fc.showOpenDialog(this);

        this.createAttachmentButton(fc.getSelectedFile());

    }

    private void createAttachmentButton(File file) {

        final File f = file;

        if (f == null) {
            return;
        }

        if (!controller.fileIsValidAttachmentType(f)) {
            //not valid file type
            return;
        }

        filesToBeAttached.add(f);

        final JPopupMenu jPopupMenuAttachmentControl = new JPopupMenu();
        JMenuItem menuItem;
        final JButton jButtonAttachmentControl = new JButton();

        jButtonAttachmentControl.setText(f.getName());
        jButtonAttachmentControl.setComponentPopupMenu(jPopupMenuAttachmentControl);
        jButtonAttachmentControl.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                jPopupMenuAttachmentControl.show(e.getComponent(), e.getX(), e.getY());
            }
        });

        this.jToolBar1.add(jButtonAttachmentControl);


        menuItem = new JMenuItem("Open");

        menuItem.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // throw new UnsupportedOperationException("Not supported yet.");

                    Desktop.getDesktop().open(f);

                } catch (IOException ex) {
                    Logger.getLogger(JF_MainW.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

        jPopupMenuAttachmentControl.add(menuItem);


        menuItem = new JMenuItem("Remove");

        menuItem.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                filesToBeAttached.remove(f);
                jButtonAttachmentControl.setVisible(false);
                if (filesToBeAttached.isEmpty()) {
                    jToolBar1.setVisible(false);
                }
            }
        });
        jPopupMenuAttachmentControl.add(menuItem);

        jToolBar1.setVisible(true);


    }

    private static void setLookAndFeel() {
        try {

            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JF_MainW.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(JF_MainW.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(JF_MainW.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(JF_MainW.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public JTextPane getJTextPane1() {
        return jTextPane1;
    }

    public JTextPane getJTextPane2() {
        return jTextPane2;
    }

    public JTextPane getJTextPane3() {
        return jTextPane3;
    }

    public void setJTextPane1(String text) {
        jTextPane1.setText(text);
    }

    public void setJTextPane2(String text) {
        jTextPane2.setText(text);
    }

    public void setJTextPane3(String text) {
        jTextPane3.setText(text);
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {

        int n = JOptionPane.showConfirmDialog(
                this,
                "Would you to save the message to drafts?",
                "Windows is closing",
                JOptionPane.YES_NO_OPTION);
        
        boolean deleteOriginal=false;

        if (n == 0) {
            //save draft
            
            if(originalMessage!= null && originalMessage.getFolderId().intValue()==controller.DRAFTS_ID.intValue()){
               deleteOriginal=true;
            }
           
                    controller.constructAndInsertNewMessageInDb(
                ((UserAccountItem) jComboBox1.getSelectedItem()).getId(),
                ((UserAccountItem) jComboBox1.getSelectedItem()).toString(),
                this.jTextPane1.getText(),
                this.jTextPane2.getText(),
                this.jTextPane3.getText(),
                this.jTextPane4.getText(),
                this.jEditorPane1.getText(),
                filesToBeAttached,
                originalMessage,
                controller.DRAFTS_ID,
                deleteOriginal);

        }


    }
    // Variables declaration - do not modify                     
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JTextPane jTextPane2;
    private javax.swing.JTextPane jTextPane3;
    private javax.swing.JTextPane jTextPane4;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    // End of variables declaration    

    class UserAccountItem {

        Integer accountId;
        String preview;

        UserAccountItem(Integer accountId, String preview) {
            this.accountId = accountId;
            this.preview = preview;
        }

        public Integer getId() {
            return this.accountId;
        }

        @Override
        public String toString() {
            return this.preview;
        }
    }
}
