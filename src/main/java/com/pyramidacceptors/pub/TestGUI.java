/*
 * Copyright (C) 2014 Pyramid Technologies, Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.pyramidacceptors.pub;


import com.pyramidacceptors.ptalk.api.PyramidAcceptor;
import com.pyramidacceptors.ptalk.api.PyramidDeviceException;
import com.pyramidacceptors.ptalk.api.PyramidPort;
import com.pyramidacceptors.ptalk.api.event.*;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import org.apache.log4j.Logger;

/**
 *
 * @author Cory Todd <cory@pyramidacceptors.com>
 */
public class TestGUI extends javax.swing.JFrame implements PTalkEventListener {
    private Logger logger = Logger.getLogger(TestGUI.class.getName());

    private final String REV = "1.2.1";
        
    private PyramidAcceptor acceptor;
    private static DefaultListModel data;
    private final JList rawList;
    private JComboBox portList;
    private final ButtonGroup bg;

    /**
     * Creates new form TestGUI
     */
    public TestGUI() {
        initComponents();
        
        // Title the application                                
        this.setTitle("PTalk Test Harness - Rev: " + REV);
        this.setVisible(true);
                
        // Center the JFrame       
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = getWidth() / 2;
        int y = getHeight() / 2;
        Point pt = new Point((int) (dim.getWidth() / 2 - x), 
                (int) (dim.getHeight() / 2 - y));
        this.setLocation(pt);

        // Register a listener for data received from the target device
        
        // Create a new list for data rx and bind it to a new jList
        data = new DefaultListModel();
        rawList = new JList(data);
        GridBagConstraints gbc = new GridBagConstraints();
        this.listPanel.add(rawList, gbc);
        
        
        // Add the radio buttons to the radio button group
        bg = new ButtonGroup();
        bg.add(this.rdoAccepting);
        bg.add(this.rdoIdle);
        bg.add(this.rdoEscrowed);
        bg.add(this.rdoStacking);
        
        // Finally, get all available ports
        getPorts();
    }
    
    private void getPorts() {
        
        // If we don't have a combobox, create one
        if(portList == null) {
            portList = new JComboBox();
            this.portPanel.add(portList);
        }
       
        // Clear the list and add all accessible ports
        this.portList.removeAllItems();
        for(String s : PyramidPort.getPortList()) {
            this.portList.addItem(s);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnConnect = new javax.swing.JButton();
        btnDisconnect = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        btnExit = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        listPanel = new javax.swing.JPanel();
        portPanel = new javax.swing.JPanel();
        btnRescan = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        rdoIdle = new javax.swing.JRadioButton();
        rdoAccepting = new javax.swing.JRadioButton();
        rdoEscrowed = new javax.swing.JRadioButton();
        rdoStacking = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setText("Port");

        jLabel2.setText("Raw Data");

        btnConnect.setText("Connect");
        btnConnect.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnConnectMouseClicked(evt);
            }
        });

        btnDisconnect.setText("Disconnect");
        btnDisconnect.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDisconnectMouseClicked(evt);
            }
        });

        jLabel3.setText("Status");

        btnExit.setText("Exit");
        btnExit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnExitMouseClicked(evt);
            }
        });

        listPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        listPanel.setAutoscrolls(true);
        listPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        jScrollPane1.setViewportView(listPanel);

        btnRescan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/reload_alt_18x21.png"))); // NOI18N
        btnRescan.setToolTipText("Rescan Ports");
        btnRescan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRescanMouseClicked(evt);
            }
        });

        rdoIdle.setText("Idle");

        rdoAccepting.setText("Accepting");

        rdoEscrowed.setText("Escrowed");

        rdoStacking.setText("Stacked");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rdoIdle)
                    .addComponent(rdoAccepting)
                    .addComponent(rdoEscrowed)
                    .addComponent(rdoStacking))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rdoIdle)
                .addGap(18, 18, 18)
                .addComponent(rdoAccepting)
                .addGap(18, 18, 18)
                .addComponent(rdoEscrowed)
                .addGap(18, 18, 18)
                .addComponent(rdoStacking)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(portPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(btnRescan)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnConnect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnDisconnect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(portPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRescan)
                    .addComponent(btnConnect)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDisconnect)
                    .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnExit)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnConnectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConnectMouseClicked
    
        String newPortName = this.portList.getSelectedItem().toString();
        if(acceptor != null && 
                acceptor.getPortName().equals(newPortName) &&
                acceptor.isConnected()) {

                    lblStatus.setText(
                            String.format("Device is connected port %s",
                            acceptor.getPortName()));                               
        } else if(acceptor != null && !acceptor.isConnected()) {            
            acceptor.connect();          
             lblStatus.setText(
                String.format("Device is connected port %s",
                acceptor.getPortName()));          
        }else {

            try {
                // Instantiate a new acceptor with this port
                acceptor = PyramidAcceptor.valueOfRS232(newPortName);
                acceptor.connect();
                
                if(acceptor.isConnected()) {
                    lblStatus.setText(
                            String.format("Connected on port %s",
                                    acceptor.getPortName()));
                    
                    acceptor.addChangeListener(this);
                }
                else {
                    lblStatus.setText(
                            String.format("Failed to connect port %s",
                                    acceptor.getPortName()));
                }
            } catch (PyramidDeviceException ex) {                
                logger.error("Failure opening port: {}", ex);
            }
         
        }
    }//GEN-LAST:event_btnConnectMouseClicked

    private void btnExitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExitMouseClicked
        try {
            if(acceptor != null && acceptor.isConnected())
                acceptor.disconnect();
        } catch(Exception ex){
            lblStatus.setText("Error");
        }
        finally {
            this.dispose();
        }
    }//GEN-LAST:event_btnExitMouseClicked

    private void btnDisconnectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDisconnectMouseClicked
        if(acceptor != null && acceptor.isConnected()) {
            acceptor.disconnect();
            String name = acceptor.getPortName();
            lblStatus.setText(String.format("Disconnected device from port %s", name));           
        } else {
            lblStatus.setText("Not connected to a device");
            boolean n = false;
        }
    }//GEN-LAST:event_btnDisconnectMouseClicked

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        btnExitMouseClicked(null);
    }//GEN-LAST:event_formWindowClosing

    private void btnRescanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRescanMouseClicked
        this.getPorts();
    }//GEN-LAST:event_btnRescanMouseClicked
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConnect;
    private javax.swing.JButton btnDisconnect;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnRescan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JPanel listPanel;
    private javax.swing.JPanel portPanel;
    private javax.swing.JRadioButton rdoAccepting;
    private javax.swing.JRadioButton rdoEscrowed;
    private javax.swing.JRadioButton rdoIdle;
    private javax.swing.JRadioButton rdoStacking;
    // End of variables declaration//GEN-END:variables

    @Override
    public void changeEventReceived(PTalkEvent evt) {           
        
        // There can only be one state at a time
        if(evt instanceof IdlingEvent)
            this.rdoIdle.setSelected(true);
        else if (evt instanceof AcceptingEvent)
            this.rdoAccepting.setSelected(true);
        else if (evt instanceof EscrowedEvent)
            this.rdoEscrowed.setSelected(true);
        else if (evt instanceof StackingEvent)
            this.rdoStacking.setSelected(true);
        else if (evt instanceof CreditEvent)
            addToList(evt.getFriendlyString());

        // Log them all the things
        String resp = getEventString(evt);
        logger.info(String.format("Event: %s", resp));
        addToList(resp);

    }

    private String lastMessage = "";
    void addToList(String message) {

        // Only add non-empty, non-repeat messages
        if(message instanceof String &&
                !message.equals("") &&
                !lastMessage.equals(message))
            data.addElement(message);
        int sz = data.size();
        rawList.ensureIndexIsVisible(sz-1);

        lastMessage = message;
    }

    /**
     * Returns the string name of the event. Due to localization issues
     * we handle this in the client code and not the core API.
     * @param evt
     * @return
     */
    private String getEventString(PTalkEvent evt) {
        if(evt instanceof CasseteMissingEvent)
            return "Cashbox Removed";
        if(evt instanceof BillJammedEvent)
            return "Bill Jam";
        if(evt instanceof ReturnedEvent)
            return "Bill Returned";
        if(evt instanceof BillRejectedEvent)
            return "Bill Rejected";
        if(evt instanceof FailureEvent)
            return "Device Failure";
        if(evt instanceof PowerUpEvent)
            return "Powered Up";
        if(evt instanceof StackedEvent)
            return "Bill Stacked";
        if(evt instanceof StackerFullEvent)
            return "Stacker Full";
        if(evt instanceof InvalidMessageEvent)
            return "Invalid Message";
        if(evt instanceof  CheatedEvent)
            return "Cheat Detected";
        if(evt instanceof IdlingEvent)
            return "Idling";
        if (evt instanceof AcceptingEvent)
            return "Accepting";
        if (evt instanceof EscrowedEvent)
            return "Escrowed";
        if (evt instanceof StackingEvent)
            return "Stacking";
        if (evt instanceof CreditEvent)
            return "Credited";
        return "";
    }
}
