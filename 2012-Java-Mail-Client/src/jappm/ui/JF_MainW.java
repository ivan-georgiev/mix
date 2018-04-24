/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jappm.ui;

import jappm.Controller;
import jappm.JAPPMessage;
import jappm.ThreadsCreator;
import jappm.Common;
import jappm.mail.Mail;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
//import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.swing.*;
import javax.swing.ImageIcon;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author Ivan
 */
public class JF_MainW extends JFrame {

    //SWING Objects Declaration
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    public javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextPane jTextPane1;
    public javax.swing.JTree jTree1;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTree jTree12;
    private javax.swing.JCheckBox jCheckBox1;
    //
    private final String ICONS_LOCATION = "images";
    public final String ICON_ATTACHMENT_IMAGE = ICONS_LOCATION + File.separator + "attachmentIcon.png";
    public final String SYSTEM_ATTACHMENT_IMAGE = ICONS_LOCATION + File.separator + "system.jpg";
    public final String ACCOUNT_ATTACHMENT_IMAGE = ICONS_LOCATION + File.separator + "account.jpg";
    public final String FOLDER_ATTACHMENT_IMAGE = ICONS_LOCATION + File.separator + "folder.png";
    public final String FOLDER_ATTACHMENT_IMAGE2 = ICONS_LOCATION + File.separator + "folder_l.png";
    public final String OUTBOX_IMAGE = ICONS_LOCATION + File.separator + "outbox.png";
    public final String SETTINGS_IMAGE = ICONS_LOCATION + File.separator + "settings.png";
    public final String DRAFTS_IMAGE = ICONS_LOCATION + File.separator + "drafts.png";
    public final String START_IMAGE = ICONS_LOCATION + File.separator + "startup.jpg";
    public final String ABOUT_IMAGE = ICONS_LOCATION + File.separator + "about.jpg";
    //
    Controller controller;
    //Other objects declaration
    private String columns[] = {"!", "A", "From", "To", "Subject", "Date", "JAPPMESSAGE_HIDDEN", "SEEN_HIDDEN"};
    DefaultTableModel tableModel;
    boolean horizontalMailContentPreview = false;
    boolean resizeTableDisabled = true;
    //TODO Make private and create get methods
    //Threads 
    public Thread previewMailsFromTheDb = new Thread();
    public Thread syncIMAPMessages = new Thread();
    public Thread syncPOP3Messages = new Thread();
    public Thread previewMailContent = new Thread();
    //
    ThreadsCreator tc;

    public JF_MainW(Controller controller) {

        this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        this.controller = controller;

        setLookAndFeel();

        initComponents();

        this.setMinimumSize(new Dimension(800, 530));
        this.setPreferredSize(new Dimension(950, 600));

        this.pack();

        this.setTitle("JAPP Mail");

        jTree1.setSelectionRow(0);
        this.jSplitPane1.setRightComponent(this.jPanel2);
        this.jSplitPane1.setDividerLocation(jSplitPane1.getDividerLocation());

        this.setLocationRelativeTo(null);

        //this.setSize(900, 600);
        //this.setResizable(false);

    }

    public JTree getJTree1() {
        return this.jTree1;
    }

    public JEditorPane getJEditorPane1() {
        return this.jEditorPane1;
    }

    public JTable getJTable1() {
        return this.jTable1;
    }

    public JButton getJButtonAttachments() {

        return this.jButton9;
    }

    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        // jTree1 = new javax.swing.JTree(controller.getTreeFromNode(1));
        jTree1 = this.createMyJTree();
        jSplitPane2 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jSplitPane3 = new javax.swing.JSplitPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jToolBar1 = new javax.swing.JToolBar();
        jToolBar2 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        jPanel2 = new ImagePanel(this.START_IMAGE);
        //jPanel2 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        //
        jPanel1 = new javax.swing.JPanel();
        jButton11 = new javax.swing.JButton();
        jScrollPane12 = new javax.swing.JScrollPane();
        // jTree12 = new javax.swing.JTree();
        jTree12 = this.createMyJTree();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jTextField11 = new javax.swing.JTextField();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();

        jCheckBox1.setText("Go to my Inbox on startup");
        jCheckBox1.setBackground(Color.white);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addComponent(jCheckBox1).addContainerGap(203, Short.MAX_VALUE)));
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup().addContainerGap(210, Short.MAX_VALUE).addComponent(jCheckBox1).addContainerGap()));



        jButton11.setText("Get Foldes List");
        jButton11.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jTree12.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {

            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTree12ValueChanged(evt);
            }
        });
        jScrollPane12.setViewportView(jTree12);

        jButton12.setText("New Folder");
        jButton12.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton13.setText("Delete Folder");
        jButton13.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jTextField11.setText("");

        jButton14.setText("Rename");
        jButton14.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton15.setText("Show/Hide");
        jButton15.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE).addGap(18, 18, 18).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jButton12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE).addComponent(jButton11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jButton15, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE).addComponent(jButton13, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE).addComponent(jTextField11, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE).addComponent(jButton14, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)).addContainerGap()));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(33, 33, 33).addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(25, 25, 25).addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(59, Short.MAX_VALUE)).addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE));


        //Layout
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE, Short.MAX_VALUE).addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(4, 4, 4).addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)));



        HTMLEditorKit kit = new HTMLEditorKit();
        jEditorPane1.setEditorKit(kit);
        jEditorPane1.setEditable(false);
        // JEditorPaneMailBody.setText("This is the body <b>HTML</b>");


        DefaultTreeModel model = (DefaultTreeModel) jTree1.getModel();
        model.setRoot(controller.getTreeFromNode(1));

//        jTree1.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
//        jTree1.setCellRenderer(new CustomIconRenderer());

        jTree1.setTransferHandler(new TransferHandler() {

            public boolean canImport(TransferHandler.TransferSupport support) {

                if (!support.isDrop()) {
                    return false;
                }

                JTree.DropLocation dropLocation =
                        (JTree.DropLocation) support.getDropLocation();


//                if (dropLocation.getPath() != null && dropLocation.getPath().getPath().length < 3) {
//                    return false;
//                }


                if (dropLocation.getPath() == null) {
                    return false;
                }
                Integer srcFolderId;

                Transferable transferable = support.getTransferable();

                DataFlavor[] dataFlavors = support.getDataFlavors();

                Object tobjects = null;
                ArrayList<Object[]> list;

                try {
                    tobjects = transferable.getTransferData(dataFlavors[0]);
                } catch (UnsupportedFlavorException ex) {
                    Logger.getLogger(JF_MainW.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(JF_MainW.class.getName()).log(Level.SEVERE, null, ex);
                }

                list = (ArrayList<Object[]>) tobjects;

                if (list.isEmpty()) {
                    srcFolderId = 0;
                } else {
                    srcFolderId = (Integer) ((Object[]) list.get(0))[1];
                }


                Integer dstFolderId = controller.getFolderId((DefaultMutableTreeNode) dropLocation.getPath().getLastPathComponent());

                if (srcFolderId.intValue() == controller.DRAFTS_ID.intValue()
                        && dstFolderId.intValue() == controller.OUTBOX_ID.intValue()) {
                    return true;
                }

                if (dropLocation.getPath() != null && dropLocation.getPath().getPath().length < 3) {
                    return false;
                }


                return dropLocation.getPath() != null;
            }

            public boolean importData(TransferHandler.TransferSupport support) {

                if (!canImport(support)) {
                    return false;
                }

                boolean copied;

                JTree.DropLocation dropLocation =
                        (JTree.DropLocation) support.getDropLocation();

                //TreePath path = dropLocation.getPath();

                Transferable transferable = support.getTransferable();

                DataFlavor[] dataFlavors = support.getDataFlavors();

                ArrayList<Integer> ids = new ArrayList();

                Object tobjects = null;
                ArrayList<Object[]> list;

                try {
                    tobjects = transferable.getTransferData(dataFlavors[0]);
                } catch (UnsupportedFlavorException ex) {
                    Logger.getLogger(JF_MainW.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(JF_MainW.class.getName()).log(Level.SEVERE, null, ex);
                }

                list = (ArrayList<Object[]>) tobjects;


                for (Object[] objects : list) {
                    ids.add((Integer) ((Object[]) objects)[0]);
                }
                Integer srcFolderId = (Integer) ((Object[]) list.get(0))[1];
                Integer dstFolderId = controller.getFolderId((DefaultMutableTreeNode) dropLocation.getPath().getLastPathComponent());

                copied = controller.moveMessages(ids, srcFolderId, dstFolderId);
                
                if (!copied) {
                    //get error and show dialog
                } else {
                    removeMissingMessages();
                }
                
//
//                for (Object[] objects : list) {
//
//                    Integer messageId = (Integer) ((Object[]) objects)[0];
//                    Integer srcFolderId = (Integer) ((Object[]) objects)[1];
//                    Integer dstFolderId = controller.getFolderId((DefaultMutableTreeNode) dropLocation.getPath().getLastPathComponent());
//
//                    copied = controller.moveMessage(messageId, srcFolderId, dstFolderId);
//
//                    if (!copied) {
//                        //get error and show dialog
//                    } else {
//                        removeMissingMessages();
//                    }
//                }
                //return false to avoid selecting the item
                return false;
            }
        });



        jTextPane1.setEditable(false);
        jTextPane1.setBackground(new Color(240, 240, 240));
        //jTextPane1.setText(""));
        jTextPane1.setPreferredSize(new Dimension(10, 60));


        jScrollPane1.setViewportView(jTree1);
        jScrollPane2.setViewportView(jTable1);
        jScrollPane3.setViewportView(jTextPane1);
        jScrollPane4.setViewportView(jEditorPane1);

        jScrollPane2.getViewport().setBackground(Color.white);


        jSplitPane1.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        jSplitPane1.setOneTouchExpandable(true);
        jSplitPane1.setDividerSize(8);
        jSplitPane1.setDividerLocation(190);
        jSplitPane1.setLeftComponent(jScrollPane1);
        jSplitPane1.setRightComponent(jSplitPane2);
        jSplitPane1.setBackground(Color.white);


        //jSplitPane2.setOneTouchExpandable(true);
        jSplitPane2.setDividerSize(4);
        jSplitPane2.setTopComponent(jScrollPane2);
        jSplitPane2.setBottomComponent(jSplitPane3);


        if (horizontalMailContentPreview) {
            jSplitPane2.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
            jSplitPane2.setDividerLocation(400);
        } else {
            jSplitPane2.setOrientation(JSplitPane.VERTICAL_SPLIT);
            jSplitPane2.setDividerLocation(200);
        }


        jSplitPane3.setOrientation(JSplitPane.VERTICAL_SPLIT);
        jSplitPane3.setOneTouchExpandable(true);
        jSplitPane3.setDividerSize(6);
        jSplitPane3.setTopComponent(this.jToolBar2);
        jSplitPane3.setBottomComponent(jScrollPane4);


        jToolBar2.setFloatable(false);
        jToolBar2.add(jScrollPane3);
        jToolBar2.add(this.jButton9);

        jButton1.setText("New");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });


        jButton2.setText("Reply");
        jButton2.setFocusable(false);
        jButton2.setEnabled(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });


        jButton3.setText("Reply All");
        jButton3.setFocusable(false);
        jButton3.setEnabled(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });


        jButton4.setText("Forward");
        jButton4.setFocusable(false);
        jButton4.setEnabled(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton4MouseClicked(evt);
            }
        });


        jButton5.setText("Send/Recieve");
        jButton5.setFocusable(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);


        jButton6.setText("Folders");
        jButton6.setFocusable(false);
        jButton6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton6.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);


        jButton7.setText("Delete");
        jButton7.setFocusable(false);
        jButton7.setEnabled(false);
        jButton7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        //jButton8.setText("Settings");
        jButton8.setIcon(Common.getImageAsIconWithSize(SETTINGS_IMAGE, 30, 30));
        jButton8.setToolTipText("Settings");
        jButton8.setFocusable(false);
        jButton8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton8.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton8.setComponentPopupMenu(jPopupMenu2);
        jButton8.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                //jPopupMenu2.show(e.getComponent(), e.getX(), e.getY());

                jPopupMenu2.show(jSplitPane1.getRightComponent(), jButton8.getX() - 283, jButton8.getY());

            }
        });


        //jButton9.setText("Attachments");
        jButton9.setFocusable(false);
        jButton9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton9.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton9.setBorder(new LineBorder(new Color(240, 240, 240), 1));
        jButton9.setVisible(false);
        jButton9.setIcon(Common.getImageAsIconWithSize(ICON_ATTACHMENT_IMAGE, 35, 40));
        jButton9.setComponentPopupMenu(jPopupMenu1);



        JMenuItem menuItem;

        menuItem = new JMenuItem("Accounts");

        menuItem.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                jSplitPane1.repaint();
                previewAccountsSettings();
                jSplitPane1.repaint();

            }
        });

        this.jPopupMenu2.add(menuItem);


        menuItem = new JMenuItem("Address Book");

        menuItem.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                jSplitPane1.repaint();
                previewAddressBook();
                jSplitPane1.repaint();


            }
        });

        this.jPopupMenu2.add(menuItem);

        menuItem = new JMenuItem("Preferences");
        menuItem.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                jSplitPane1.repaint();
                previewPreferences();
                jSplitPane1.repaint();

            }
        });
        //TODO preferencies are disabled from here
        menuItem.setEnabled(false);
        this.jPopupMenu2.add(menuItem);


        menuItem = new JMenuItem("About");
        menuItem.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                jSplitPane1.repaint();

                previewAbout();

                jSplitPane1.repaint();


            }
        });

        this.jPopupMenu2.addSeparator();
        this.jPopupMenu2.add(menuItem);


        tableModel = new DefaultTableModel(null, columns) {

            @Override
            public Class getColumnClass(int column) {
                if (column == 1) {
                    return Icon.class;
                }

                return Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };


        jTable1.setModel(tableModel);

        jTable1.getTableHeader().setPreferredSize(new Dimension(jTable1.getTableHeader().getWidth(), 20));

        RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableModel);
        jTable1.setRowSorter(sorter);

        sorter.toggleSortOrder(5);
        sorter.toggleSortOrder(5);

        if (resizeTableDisabled) {
            jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        }
        // jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
        /// jTable1.setDragEnabled(false);

        jTable1.setDragEnabled(true);
        jTable1.setTransferHandler(new TableRowTransferHandler());


        jTable1.setShowHorizontalLines(false);
        jTable1.setShowVerticalLines(false);


        int width = 22;
        int vColIndex = 0;
        TableColumn col = jTable1.getColumnModel().getColumn(vColIndex);
        vColIndex = 0;
        col.setResizable(false);
        col.setWidth(width);
        col.setPreferredWidth(width);
        col.setMaxWidth(width);
        vColIndex = 1;
        col = jTable1.getColumnModel().getColumn(vColIndex);
        col.setResizable(false);
        col.setWidth(width);
        col.setPreferredWidth(width);
        col.setMaxWidth(width);

        //hide the last2 columns
        jTable1.removeColumn(jTable1.getColumnModel().getColumn(6));
        jTable1.removeColumn(jTable1.getColumnModel().getColumn(6));


        if (!horizontalMailContentPreview) {

            if (resizeTableDisabled) {
                width = 140;
                vColIndex = 2;
                col = jTable1.getColumnModel().getColumn(vColIndex);
                col.setWidth(width);
                col.setPreferredWidth(width);
                col.setMinWidth(width);
                vColIndex = 3;
                col = jTable1.getColumnModel().getColumn(vColIndex);
                col.setWidth(width);
                col.setPreferredWidth(width);
                col.setMinWidth(width);
            }

            width = 310;
            vColIndex = 4;
            col = jTable1.getColumnModel().getColumn(vColIndex);
            col.setMinWidth(width);
            width = 95;
            vColIndex = 5;
            col = jTable1.getColumnModel().getColumn(vColIndex);
            col.setResizable(false);
            col.setMaxWidth(width);
            col.setMinWidth(width);
        } else {
            width = 120;
            vColIndex = 4;
            col = jTable1.getColumnModel().getColumn(vColIndex);
            col.setMinWidth(width);

        }


        jTable1.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

            // method to override - returns cell renderer component
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                // let the default renderer prepare the component for us
                Component comp = super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);

                Object valueAt;

                int realRow = jTable1.convertRowIndexToModel(row);
                int realColumn = jTable1.convertColumnIndexToModel(column);


                if (realColumn != 4) {
                    return comp;
                }

                valueAt = jTable1.getModel().getValueAt(realRow, 7);

                if ((valueAt instanceof Boolean) && (!((Boolean) valueAt).booleanValue())) {

                    Font font = comp.getFont();

                    comp.setFont(font.deriveFont(Font.BOLD));
                }

                //comp.setBackground(Color.WHITE);
                //comp.setFont(font.deriveFont(Font.PLAIN));

                return comp;
            }
        });


        //TODO sel
        //  jTable1.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        jToolBar1.setRollover(true);
        jToolBar1.setFloatable(false);
        jToolBar1.add(jButton1);
        jToolBar1.addSeparator();
        jToolBar1.add(jButton2);
        jToolBar1.add(jButton3);
        jToolBar1.add(jButton4);
        jToolBar1.addSeparator();
        jToolBar1.add(jButton5);
        jToolBar1.addSeparator();
        jToolBar1.add(jButton7);
        //jToolBar1.addSeparator();
        //jToolBar1.add(jButton6);
        //jToolBar1.addSeparator();
        jToolBar1.add(Box.createHorizontalGlue());
        jToolBar1.add(jButton8);


        /* INIT */

        this.cleanHeaderInfoPreview();

        /* Components listeners*/


        jTree1.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {

                if (evt.getKeyCode() == KeyEvent.VK_UP || evt.getKeyCode() == KeyEvent.VK_DOWN) {
                    jTree1ValueChanged(null);
                }
            }
        });


        jTree1.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {

                int selRow = jTree1.getRowForLocation(evt.getX(), evt.getY());


                //  jTree1MouseClicked(evt);

                if (selRow != -1) {
                    jTree1ValueChanged(null);
                }
            }
        });

        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {

                jTable1MouseClicked(e);

            }
        });

        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {

                jTable1KeyReleased(evt);

            }
        });

        jButton9.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                jPopupMenu1.show(e.getComponent(), e.getX(), e.getY());
            }
        });


        jButton7.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedMessage();
            }
        });


        jButton5.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                jButton5ActionPerformed(e);
            }
        });

    }

    private void previewAddressBook() {
        GJD_AddressBook newJDialog1 = new GJD_AddressBook(this, false);
        newJDialog1.setVisible(true);

    }

    private void previewAccountsSettings() {
        GJD_AccountsSettings jDialogAcc = new GJD_AccountsSettings(this, true);
        jDialogAcc.setVisible(true);
    }

    private void previewAbout() {
        GJD_About newJDialog1 = new GJD_About(this, true);
        newJDialog1.setVisible(true);
    }

    private void previewPreferences() {
        GJD_GeneralSettings newJDialog1 = new GJD_GeneralSettings(this, true);
        newJDialog1.setVisible(true);
    }

    private void deleteSelectedMessage() {

        boolean res = controller.deleteMessage((JAPPMessage) this.jTable1.getModel().getValueAt(jTable1.convertRowIndexToModel(jTable1.getSelectedRow()), 6));

        if (res) {

            this.removeMissingMessages();

        }

    }

    public void createAttachmentsPopupMenu(ArrayList<File> files) {
        jPopupMenu1.removeAll();

        JMenuItem menuItem;

        for (final File file : files) {

            menuItem = new JMenuItem(file.getName());

            menuItem.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        // throw new UnsupportedOperationException("Not supported yet.");

                        Desktop.getDesktop().open(file);

                    } catch (IOException ex) {
                        Logger.getLogger(JF_MainW.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            });

            jPopupMenu1.add(menuItem);
        }

        jPopupMenu1.addSeparator();
        menuItem = new JMenuItem("Save all");
        //TODO save to disk menuitem
        menuItem.setEnabled(false);
        jPopupMenu1.add(menuItem);

    }

    private void replyButtonsState(boolean active) {
        jButton2.setEnabled(active);
        jButton3.setEnabled(active);
        jButton4.setEnabled(active);
    }

    private void jTree1ValueChanged(javax.swing.event.TreeSelectionEvent evt) {

        this.cleanHeaderInfoPreview();
        this.cleanMailPreview();
        this.cleanMailsTable();
        replyButtonsState(false);
        jButton7.setEnabled(false);
        this.setTitle("JAPP Mail");


        DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree1.getLastSelectedPathComponent();

        if (node == null) {
            return;
        }

        Integer folderId = controller.getFolderId(node);



        if (folderId == 1) {
            this.jSplitPane1.setRightComponent(this.jPanel2);
            this.jSplitPane1.setDividerLocation(jSplitPane1.getDividerLocation());
            // this.jTextPane2.setText("SYSTEM VIEW");
            //TODO Put home picture instead of textpane
        } else if (controller.isAccountFolder(folderId)) {

            this.jSplitPane1.setRightComponent(this.jPanel1);
            this.jSplitPane1.setDividerLocation(jSplitPane1.getDividerLocation());

            DefaultTreeModel model = (DefaultTreeModel) jTree12.getModel();

            model.setRoot(node);
            // model.setRoot(controller.getTreeFromNode(folderId));

            for (int i = 0; i < jTree12.getRowCount(); i++) {
                jTree12.expandRow(i);
            }

            this.jButton12.setEnabled(false);
            this.jButton13.setEnabled(false);
            this.jButton14.setEnabled(false);
            this.jButton15.setEnabled(false);

            this.jTextField11.setText("");
            this.jTextField11.setEnabled(false);

        } else {
            this.jSplitPane1.setRightComponent(this.jSplitPane2);
            this.jSplitPane1.setDividerLocation(jSplitPane1.getDividerLocation());

            this.previewMessagesInMailTable(folderId, true);
        }

    }

    private void jTree1MouseClicked(java.awt.event.MouseEvent evt) {



        if (jTree1.getPathForLocation(evt.getX(), evt.getY()) != null) {

            // if (evt.isPopupTrigger()) 
            // if (evt.getButton() == MouseEvent.BUTTON3) {

            if (SwingUtilities.isRightMouseButton(evt)) {
                //jTree1.setSelectionRow(jTree1.getClosestRowForLocation(evt.getX(), evt.getY()));
                //jTree1.setComponentPopupMenu(jPopupMenu1);
                //jPopupMenu1.show(jTree1, evt.getX(), evt.getY());
                //this.jPopupMenu1.setVisible(true);
            }

        } else {
            //  jTree1.clearSelection();
        }

    }

    private void jTableValueChanged() {

        replyButtonsState(false);
        jButton7.setEnabled(true);

        this.setTitle("JAPP Mail");
        previewHeaderInfo((JAPPMessage) this.jTable1.getModel().getValueAt(jTable1.convertRowIndexToModel(jTable1.getSelectedRow()), 6));
        previewMail((JAPPMessage) this.jTable1.getModel().getValueAt(jTable1.convertRowIndexToModel(jTable1.getSelectedRow()), 6));

    }

    private void jTable1KeyReleased(java.awt.event.KeyEvent evt) {

        if (evt.getKeyCode() == KeyEvent.VK_DOWN || evt.getKeyCode() == KeyEvent.VK_UP) {

            this.cleanHeaderInfoPreview();
            this.cleanMailPreview();
            jTableValueChanged();
        }
    }

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {

        if (SwingUtilities.isRightMouseButton(evt)) {

            Point p = evt.getPoint();

            // get the row index that contains that coordinate
            int rowNumber = jTable1.rowAtPoint(p);

            // Get the ListSelectionModel of the JTable
            ListSelectionModel lmodel = jTable1.getSelectionModel();
            lmodel.setSelectionInterval(rowNumber, rowNumber);
        }

        this.cleanHeaderInfoPreview();
        this.cleanMailPreview();
        jTableValueChanged();
    }

    public void addRowToMailsTable(Object[] row) {
        tableModel.addRow(row);
        this.tableModel.fireTableRowsUpdated(tableModel.getRowCount() - 1, tableModel.getRowCount() - 1);

    }

    private void cleanMailsTable() {

        this.tableModel.getDataVector().clear();
        this.tableModel.fireTableDataChanged();


    }

    public void updateMessageAsSeen(JAPPMessage m) {

        controller.upadteMessageIsSeen(m.getMessageId());
        m.setSeen();

        int rowSelected = jTable1.getSelectedRow();
        jTable1.getModel().setValueAt(m.getSeen(), jTable1.convertRowIndexToModel(jTable1.getSelectedRow()), 7);

        ((DefaultTableModel) jTable1.getModel()).fireTableRowsUpdated(rowSelected, rowSelected);

        //((DefaultTableModel) jTable1.getModel()).fireTableDataChanged();
        //jTable1.setRowSelectionInterval(rowSelected, rowSelected);

        //set reply buttons visible
        replyButtonsState(true);
    }

    public void updateMessageAttachments(JAPPMessage m) {

        ArrayList<File> filesList = controller.getAttachments(m);

        if (filesList == null) {
            this.jButton9.setVisible(false);
        } else {
            controller.updateMessageContainsAttachments(m.getMessageId());

            m.setAttachmentsAvaliable();
            int rowSelected = jTable1.getSelectedRow();

            jTable1.getModel().setValueAt((this.getAttachmentIcon(m)),
                    jTable1.convertRowIndexToModel(jTable1.getSelectedRow()), 1);
            ((DefaultTableModel) jTable1.getModel()).fireTableRowsUpdated(rowSelected, rowSelected);


            // ((DefaultTableModel) jTable1.getModel()).fireTableDataChanged();
            // jTable1.setRowSelectionInterval(rowSelected, rowSelected);

            createAttachmentsPopupMenu(filesList);
            this.jButton9.setVisible(true);
        }
    }

    public void previewMessagesInMailTable(Integer folderId, boolean syncWithTheServer) {

        //TODO This contains unsynced threads

        this.cleanMailsTable();

        if (previewMailsFromTheDb != null && previewMailsFromTheDb.getState() != Thread.State.TERMINATED) {
            previewMailsFromTheDb.stop();
        }

        previewMailsFromTheDb = new Thread(new ThreadsCreator(controller, this, folderId, null,
                ThreadsCreator.LOAD_MESSAGES_FROM_DB));
        previewMailsFromTheDb.start();


        if (syncWithTheServer) {

            syncWithTheServer(folderId);
        }
    }

    private void syncWithTheServer(Integer folderId) {

        //TODO This contains unsynced threads

        if (controller.getAccountType(folderId) == Mail.POP3_ACCOUNT) {

            if (syncPOP3Messages != null && syncPOP3Messages.getState() != Thread.State.TERMINATED) {

                if (tc != null) {
                    tc.disableErrors();
                }

                controller.killAllMailSessions();
                syncPOP3Messages.stop();
            }
            if (syncIMAPMessages != null && syncIMAPMessages.getState() != Thread.State.TERMINATED) {

                if (tc != null) {
                    tc.disableErrors();
                }
                controller.killAllMailSessions();
                syncIMAPMessages.stop();
            }
            tc = new ThreadsCreator(controller, this, folderId, null,
                    ThreadsCreator.SYNC_WITH_POP3_AND_PREVIEW);

            this.syncPOP3Messages = new Thread(tc);
            syncPOP3Messages.start();


        } else if (controller.getAccountType(folderId) == Mail.IMAP_ACCOUNT) {

            if (syncIMAPMessages != null && syncIMAPMessages.getState() != Thread.State.TERMINATED) {

                if (tc != null) {
                    tc.disableErrors();
                }

                controller.killAllMailSessions();
                syncIMAPMessages.stop();
                //Common.sleep(200);
            }
            if (syncPOP3Messages != null && syncPOP3Messages.getState() != Thread.State.TERMINATED) {

                if (tc != null) {
                    tc.disableErrors();
                }

                controller.killAllMailSessions();
                syncPOP3Messages.stop();
            }

            tc = new ThreadsCreator(controller, this, folderId, null,
                    ThreadsCreator.SYNC_WITH_IMAP_AND_PREVIEW);

            syncIMAPMessages = new Thread(tc);

            syncIMAPMessages.start();

        } else {
            //ERROR
            //  System.out.println("Unknown type account");
        }

    }

    private void previewHeaderInfo(JAPPMessage m) {

        int subjectEnd;
        int fromEnd;
        int toEnd = -1;

        StyledDocument doc = jTextPane1.getStyledDocument();
        Style style = jTextPane1.addStyle("Bold", null);
        StyleConstants.setBold(style, true);
        StyleConstants.setFontSize(style, 12);

        StringBuilder header = new StringBuilder();

        header.append("Subject: ").append(m.getSubject());
        subjectEnd = header.length();
        header.append("\tDate: ").append(m.getSentDate().toString().substring(0, 16));
        header.append("\nFrom: ").append(m.getFrom());
        fromEnd = header.length();
        header.append("\nTo: ").append(m.getTo());
        if (!m.getCC().isEmpty()) {
            toEnd = header.length();
            header.append("\nCc: ").append(m.getCC());
        }

        this.jTextPane1.setText(header.toString());

        this.jTextPane1.setCaretPosition(0);

        doc.setCharacterAttributes(0, subjectEnd, jTextPane1.getStyle("Bold"), true);
        doc.setCharacterAttributes(subjectEnd + 1, 5, jTextPane1.getStyle("Bold"), true);
        doc.setCharacterAttributes(subjectEnd + 23, 6, jTextPane1.getStyle("Bold"), true);
        doc.setCharacterAttributes(fromEnd, 4, jTextPane1.getStyle("Bold"), true);

        if (toEnd != -1) {
            doc.setCharacterAttributes(toEnd, 4, jTextPane1.getStyle("Bold"), true);
        }

        this.jButton9.setVisible(m.attachmentsAvaliable());


        if (jSplitPane3.getDividerLocation() == 1) {
            return;
        }

        if (horizontalMailContentPreview) {
            jSplitPane3.setDividerLocation(110);
        } else {
            if (toEnd == -1) {
                jSplitPane3.setDividerLocation(54);

            } else {
                jSplitPane3.setDividerLocation(70);
            }
        }


    }

    public void previewMail(JAPPMessage m) {

        //TODO This contains unsynced threads

        String text = controller.getMessageBodyFromDb(m);

        if (text != null) {

            this.jEditorPane1.setText(text);
            this.jEditorPane1.setCaretPosition(0);

            this.updateMessageAttachments(m);

            this.updateMessageAsSeen(m);

        } else {
            //The jappMessage is now fully downloaded yet, proceed

            if (controller.getAccountType(m.getFolderId()) == Mail.POP3_ACCOUNT) {
                //
                this.jEditorPane1.setText("Error obtaining mail.");
                return;
            }

            if (previewMailContent != null && previewMailContent.getState() != Thread.State.TERMINATED) {
                //controller.killAllMailSessions();
                previewMailContent.stop();
                // Common.sleep(200);
            }

            previewMailContent = new Thread(new ThreadsCreator(controller, this, null, m,
                    ThreadsCreator.PREVIEW_MAIL_CONTENT));
            previewMailContent.start();
        }




    }

    public void cleanMailPreview() {

        this.jEditorPane1.setText("");

        // JEditorPaneMailBody.repaint();
    }

    public String getFullPathFromTreeNode(TreePath path) {


        if (path == null) {
            return null;
        }

        StringBuilder result = new StringBuilder();

        for (Object p : path.getPath()) {

            result.append(p.toString()).append(Mail.PATH_SEPARATOR);

        }

        return result.substring(0, result.length() - 1).toString();

    }

    public void cleanHeaderInfoPreview() {

//        StyledDocument doc = jTextPane1.getStyledDocument();
//        Style style = jTextPane1.addStyle("Bold", null);
//        StyleConstants.setBold(style, true);
//        StyleConstants.setFontSize(style, 12);


        StringBuilder header = new StringBuilder();

        header.append("Subject: ");
        header.append("\tDate: ");
        header.append("\nFrom: ");
        header.append("\nTo: ");

        this.jButton9.setVisible(false);

        this.jTextPane1.setText(header.toString());

//        doc.setCharacterAttributes(0, header.length(), jTextPane1.getStyle("Bold"), true);


        if (jSplitPane3.getDividerLocation() != 1) {
            jSplitPane3.setDividerLocation(50);
        }


    }

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {


        //get folders list

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree1.getLastSelectedPathComponent();
        DefaultTreeModel model;


        if (node == null) {
            return;
        }


        if (node.getChildCount() > 0) {

            JOptionPane.showMessageDialog(this, "Sync folders functionality will be avaliable in future releases."
                    + "\n"
                    + "Sorry for the inconvenience!", "Folders list is already obtained!", JOptionPane.INFORMATION_MESSAGE);

            return;
        }


        Integer folderId = controller.getFolderId(node);

        boolean loadAndInsertFoldersTreeToDB = controller.loadAndInsertFoldersTreeToDB(folderId);

        if (loadAndInsertFoldersTreeToDB) {
            model = (DefaultTreeModel) jTree1.getModel();
            model.setRoot(controller.getTreeFromNode(1));
            model = (DefaultTreeModel) jTree12.getModel();
            model.setRoot(controller.getTreeFromNode(folderId));
            jTree12.repaint();
            this.repaint();

        } else {
            JOptionPane.showMessageDialog(this, controller.getExceptionInfo());
        }
    }

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {

        //new folder

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree12.getLastSelectedPathComponent();
        DefaultTreeModel model = (DefaultTreeModel) jTree12.getModel();

        if (node == null) {
            return;
        }

        String s = (String) JOptionPane.showInputDialog(
                this,
                "Folder name:",
                "Type folder name",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                null);


        if (s == null || s.isEmpty()) {
            return;
        }


        Integer folderId = controller.getFolderId(node);

        DefaultMutableTreeNode createNewFolder = controller.createNewFolder(folderId, s);

        if (createNewFolder != null) {
            node.add(createNewFolder);

            model.reload(node);
            model = (DefaultTreeModel) jTree1.getModel();
            model.reload(node);


            for (int i = 0; i < jTree12.getRowCount(); i++) {
                jTree12.expandRow(i);
            }
        } else {
            JOptionPane.showMessageDialog(this, controller.getExceptionInfo(), "Error", JOptionPane.ERROR_MESSAGE);
        }



        this.jTree1.repaint();
        this.jTree12.repaint();

    }

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {

        //delete folder

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree12.getLastSelectedPathComponent();
        DefaultTreeModel model = (DefaultTreeModel) jTree1.getModel();


        if (node == null) {
            return;
        }

        Integer folderId = controller.getFolderId(node);

        int deleteFolder = controller.deleteFolder(folderId);


        if (deleteFolder == controller.FOLDER_DELETED) {

            model.removeNodeFromParent(node);

            model = (DefaultTreeModel) jTree12.getModel();
            model.reload();

            for (int i = 0; i < jTree12.getRowCount(); i++) {
                jTree12.expandRow(i);
            }

            JOptionPane.showMessageDialog(this, "Done!", "Info", JOptionPane.INFORMATION_MESSAGE);


        } else if (deleteFolder == controller.FOLDER_CLEANED) {
            JOptionPane.showMessageDialog(this, "Folder contains subfolders and can not be deleted. All messages in it are removed.", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else if (deleteFolder == controller.FOLDER_ERROR) {
            JOptionPane.showMessageDialog(this, controller.getExceptionInfo(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        this.jTree1.repaint();
        this.jTree12.repaint();
    }

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {
        //rename



        DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree12.getLastSelectedPathComponent();
        DefaultTreeModel model = (DefaultTreeModel) jTree12.getModel();
        String folderNewName;


        if (node == null) {
            return;
        }

        Integer folderId = controller.getFolderId(node);
        folderNewName = this.jTextField11.getText();

        boolean renamedFolder = controller.renameFolder(folderId, folderNewName);


        if (renamedFolder) {

            node.setUserObject(new DefaultMutableTreeNode(folderNewName));
        }

        model.nodeChanged(node);

        model = (DefaultTreeModel) jTree1.getModel();

        model.nodeChanged(node);

        this.jTree1.repaint();
        this.jTree12.repaint();


    }

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void jTree12ValueChanged(javax.swing.event.TreeSelectionEvent evt) {

        //TODO indev

        if (evt.getNewLeadSelectionPath() != null) {


            DefaultMutableTreeNode node = (DefaultMutableTreeNode) evt.getNewLeadSelectionPath().getLastPathComponent();

            this.jTextField11.setText(
                    node.toString());

            this.jButton12.setEnabled(true);

            if (controller.folderCanBeModified(
                    controller.getFolderId(node))) {

                this.jButton13.setEnabled(true);
                this.jButton14.setEnabled(true);
                this.jButton15.setEnabled(true);
                this.jTextField11.setEnabled(true);
            } else {
                //this.jButton12.setEnabled(false);
                this.jButton13.setEnabled(false);
                this.jButton14.setEnabled(false);
                this.jButton15.setEnabled(false);
                this.jTextField11.setEnabled(false);
            }

        }
    }

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {
        // send/recieve 
        //TODO indev


        boolean success = controller.sendMessages();

        if (!success) {

            JOptionPane.showMessageDialog(this, controller.getExceptionInfo(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree1.getLastSelectedPathComponent();

        if (node == null) {
            return;
        }

        //  Integer folderId = controller.getInboxId(controller.getFolderId(node));

        Integer folderId = controller.getFolderId(node);

        if (folderId == null) {
            return;
        }

        syncWithTheServer(folderId);


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

    class CustomIconRenderer extends DefaultTreeCellRenderer {

        ImageIcon folderIcon;
        ImageIcon folderIcon2;
        ImageIcon accountIcon;
        ImageIcon systemIcon;
        ImageIcon outboxIcon;
        ImageIcon draftsIcon;

        public CustomIconRenderer() {
            folderIcon = Common.getImageAsIconWithSize(FOLDER_ATTACHMENT_IMAGE, 16, 16);
            accountIcon = Common.getImageAsIconWithSize(ACCOUNT_ATTACHMENT_IMAGE, 20, 20);
            systemIcon = Common.getImageAsIconWithSize(SYSTEM_ATTACHMENT_IMAGE, 20, 18);
            folderIcon2 = Common.getImageAsIconWithSize(FOLDER_ATTACHMENT_IMAGE2, 16, 16);
            outboxIcon = Common.getImageAsIconWithSize(OUTBOX_IMAGE, 18, 14);
            draftsIcon = Common.getImageAsIconWithSize(DRAFTS_IMAGE, 16, 14);
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree,
                Object value, boolean sel, boolean expanded, boolean leaf,
                int row, boolean hasFocus) {

            super.getTreeCellRendererComponent(tree, value, sel,
                    expanded, leaf, row, hasFocus);

            Integer folderId;
            // Object nodeObj = ((DefaultMutableTreeNode) value).getUserObject();

            folderId = controller.getFolderId((DefaultMutableTreeNode) value);

            if (folderId == 1) {
                setIcon(systemIcon);
            } else if (folderId == 2) {
                setIcon(outboxIcon);
            } else if (folderId == 3) {
                setIcon(draftsIcon);
            } else if (controller.isAccountFolder(folderId)) {
                setIcon(accountIcon);
            } else if (controller.getAccountType(folderId) == Mail.IMAP_ACCOUNT) {
                setIcon(folderIcon);
            } else {
                setIcon(folderIcon2);
            }

            return this;
        }
    }

    public ImageIcon getAttachmentIcon(JAPPMessage m) {

        ImageIcon attachmentIcon;
        if (m.attachmentsAvaliable()) {
            attachmentIcon = Common.getImageAsIconWithSize(ICON_ATTACHMENT_IMAGE, 13, 13);
        } else {
            attachmentIcon = new ImageIcon();
        }

        return attachmentIcon;

    }

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {
        new JF_Compose(controller, null, JF_Compose.NEW).setVisible(true);

    }

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {
        new JF_Compose(controller,
                ((JAPPMessage) this.jTable1.getModel().getValueAt(jTable1.convertRowIndexToModel(jTable1.getSelectedRow()), 6)),
                JF_Compose.REPLY).setVisible(true);

    }

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {
        new JF_Compose(controller,
                ((JAPPMessage) this.jTable1.getModel().getValueAt(jTable1.convertRowIndexToModel(jTable1.getSelectedRow()), 6)),
                JF_Compose.REPLY_ALL).setVisible(true);

    }

    private void jButton4MouseClicked(java.awt.event.MouseEvent evt) {
        new JF_Compose(controller,
                ((JAPPMessage) this.jTable1.getModel().getValueAt(jTable1.convertRowIndexToModel(jTable1.getSelectedRow()), 6)),
                JF_Compose.FORWARD).setVisible(true);

    }

    public void removeMissingMessages() {

        int rowId;
        int selectedRowID;
        JAPPMessage m;
        for (int i = 0; i < this.jTable1.getRowCount();) {

            rowId = this.jTable1.convertRowIndexToModel(i);

            m = (JAPPMessage) this.jTable1.getModel().getValueAt(rowId, 6);

            if (!controller.messageIsInDatabase(m.getMessageId(), m.getFolderId())) {

                try {
                    selectedRowID = this.jTable1.convertRowIndexToModel(this.jTable1.getSelectedRow());
                } catch (Exception x) {
                    selectedRowID = -1;
                }


                if (selectedRowID == rowId) {
                    this.cleanHeaderInfoPreview();
                    this.cleanMailPreview();
                }

                ((DefaultTableModel) this.jTable1.getModel()).removeRow(rowId);


                continue;
            }

            i++;


        }
    }

    class TableRowTransferHandler extends TransferHandler {

        // private Object[] transferedObjects = null;
        public TableRowTransferHandler() {
        }

        @Override
        protected Transferable createTransferable(JComponent c) {

            ArrayList<Object[]> res = new ArrayList();
            for (int i : jTable1.getSelectedRows()) {
                Object[] values = new Object[2];

                //  values[0] = ((JAPPMessage) jTable1.getModel().getValueAt(jTable1.convertRowIndexToModel(jTable1.getSelectedRow()), 6)).getMessageId();
                //  values[1] = controller.getFolderId((DefaultMutableTreeNode) jTree1.getSelectionPath().getLastPathComponent());

                values[0] = ((JAPPMessage) jTable1.getModel().getValueAt(jTable1.convertRowIndexToModel(i), 6)).getMessageId();
                values[1] = controller.getFolderId((DefaultMutableTreeNode) jTree1.getSelectionPath().getLastPathComponent());


                res.add(values);

                //getFullPathFromTreeNode(jTree1.getSelectionPath());
            }
            return new DataHandler(res, DataFlavor.javaJVMLocalObjectMimeType);
            // return new DataHandler(values, DataFlavor.javaJVMLocalObjectMimeType);

//            return new DataHandler(((JAPPMessage) jTable1.getModel().getValueAt(jTable1.convertRowIndexToModel(jTable1.getSelectedRow()), 6)).getMessageId(),
//                    DataFlavor.javaJVMLocalObjectMimeType);

        }

//        @Override
//        public boolean canImport(TransferHandler.TransferSupport info) {
//            return false;
//            JTable t = (JTable) info.getComponent();
//            boolean b = info.isDrop() && info.isDataFlavorSupported(localObjectFlavor);
//            t.setCursor(b ? DragSource.DefaultMoveDrop : DragSource.DefaultMoveNoDrop);
//            return b;
//        }
//
//        @Override
        @Override
        public boolean canImport(TransferHandler.TransferSupport info) {
            return false;
        }

        public int getSourceActions(JComponent c) {
            return TransferHandler.MOVE;
        }
    }

    class ImagePanel extends JPanel {

        private Image img;

        public ImagePanel(String img) {
            this(new ImageIcon(img).getImage());
            //this(Common.getImageAsIconWithSize(img, 700, 500).getImage());

        }

        public ImagePanel(Image img) {
            this.img = img;
            Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
            setPreferredSize(size);
            setMinimumSize(size);
            setMaximumSize(size);
            setSize(size);
            setLayout(null);
        }

        @Override
        public void paintComponent(Graphics g) {
            g.drawImage(img, 0, 0, null);

        }
    }

    private JTree createMyJTree() {

        JTree jt = new JTree();

        jt.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        jt.setCellRenderer(new CustomIconRenderer());

        return jt;
    }

    public void showError() {
        JOptionPane.showMessageDialog(this, controller.getExceptionInfo(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
