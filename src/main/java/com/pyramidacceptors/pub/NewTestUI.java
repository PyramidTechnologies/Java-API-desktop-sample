package com.pyramidacceptors.pub;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.pyramidacceptors.ptalk.api.*;
import com.pyramidacceptors.ptalk.api.event.*;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cory on 4/6/2016.
 */
public class NewTestUI implements PTalkEventListener {

    private static final Logger logger = Logger.getLogger(NewTestUI.class.getName());

    private JPanel panel1;
    private JComboBox cmbPorts;
    private JButton btnConnect;
    private JList lstTx;
    private JButton btnReject;
    private JButton btnReset;
    private JList lstRx;
    private JButton btnAccept;
    private JLabel lblConnected;
    private JButton btnPause;
    private JButton btnClear;
    private JRadioButton rdoStacked;
    private JRadioButton rdoStacking;
    private JRadioButton rdoAccepting;
    private JRadioButton rdoEscrowed;
    private JRadioButton rdoStackerFull;
    private JRadioButton rdoCheated;
    private JRadioButton rdoReturned;
    private JRadioButton rdoReturning;
    private JRadioButton rdoIdle;
    private JRadioButton rdoBillJammed;
    private JRadioButton rdoRejected;
    private JRadioButton rdoFailure;
    private JRadioButton rdoCashbox;
    private JLabel lblModel;
    private JLabel lblRevision;
    private JLabel lblSerialnumber;
    private JButton btnReadInfo;
    private JCheckBox chkEnableLogging;

    private DefaultListModel rxListModel;
    private DefaultListModel txListModel;

    private PyramidAcceptor mAcceptor;
    private final SimpleDateFormat sdf = new SimpleDateFormat("h:mm:ss.SSS");


    public NewTestUI() {

        JFrame frame = new JFrame("jRS232 Sample");
        frame.add(panel1);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setupListeners();

        // Populate available OS serial ports
        for (String port : PyramidPort.getPortList())
            cmbPorts.addItem(port);


        // Bind our debugging view to models that are filled when
        // we receive the serialdataevent
        rxListModel = new DefaultListModel();
        txListModel = new DefaultListModel();
        lstTx.setModel(txListModel);
        lstRx.setModel(rxListModel);
        lstRx.setCellRenderer(new AlternatingRowColors());
        lstTx.setCellRenderer(new AlternatingRowColors());

        // Disable everything that is a an acceptor command
        setConnected(false);

        // Start with logging enabled
        chkEnableLogging.setSelected(true);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        logger.info("Sample app successfully launched");

    }

    private void setupListeners() {
        btnConnect.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                btnConnectMouseClicked(evt);
            }
        });

        btnClear.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                btnClearMouseClicked(evt);
            }
        });

        btnPause.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                btnPausedMouseClicked(evt);
            }
        });

        btnReset.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                btnResetMouseClicked(evt);
            }
        });

        btnReset.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                btnResetMouseClicked(evt);
            }
        });

        btnReadInfo.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                btnReadInfoMouseClicked(evt);
            }
        });

        chkEnableLogging.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                chkDebugMonitorChanged(e);
            }
        });
    }

    private void setConnected(boolean connected) {

        // Cannot change port when connect
        this.cmbPorts.setEnabled(!connected);
        this.btnPause.setEnabled(connected);
        this.btnReset.setEnabled(connected);
        this.btnReadInfo.setEnabled(connected);
        this.btnConnect.setText(connected ? "Disconnect" : "Connect");
        this.lblConnected.setText(connected ? "Connected" : "Not Connected");

        if (connected) {
            this.lstRx.clearSelection();
            this.lstTx.clearSelection();
        }

    }

    private void chkDebugMonitorChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            RS232Configuration.INSTANCE.setEventMask(RS232Configuration.DEFAULT_EVENT_MASK | Events.SerialData.getIntId());
        } else {
            RS232Configuration.INSTANCE.setEventMask(RS232Configuration.DEFAULT_EVENT_MASK & ~(Events.SerialData.getIntId()));
        }
    }

    private void btnReadInfoMouseClicked(MouseEvent evt) {
        lblModel.setText(mAcceptor.getAcceptorModel());
        lblRevision.setText(mAcceptor.getFirmwareRevision());

        mAcceptor.requestSerialNumber();
        lblSerialnumber.setText(mAcceptor.getSerialNumber());
    }

    static boolean isPaused = false;

    private void btnPausedMouseClicked(MouseEvent evt) {

        // mAcceptor will never be null
        if (isPaused) {
            mAcceptor.unpause();
            btnPause.setText("Pause");
        } else {
            mAcceptor.pause();
            btnPause.setText("Un pause");
        }

        isPaused = !isPaused;

    }

    private void btnResetMouseClicked(MouseEvent evt) {
        mAcceptor.requestReset();
    }

    private void btnClearMouseClicked(MouseEvent evt) {
        txListModel.clear();
        rxListModel.clear();

        lblRevision.setText("");
        lblSerialnumber.setText("");
        lblModel.setText("");
    }

    private void btnConnectMouseClicked(MouseEvent evt) {

        // We have never connected during this execution
        if (mAcceptor == null) {

            boolean failed = false;

            try {

                mAcceptor = PyramidAcceptor.valueOfRS232(cmbPorts.getSelectedItem().toString());

                mAcceptor.connect();

                if (!mAcceptor.isConnected())
                    failed = true;
                else
                    mAcceptor.addChangeListener(this);

            } catch (PyramidDeviceException e) {
                logger.info("Failed to connect to acceptor");
                failed = true;
            }

            if (failed) {
                JOptionPane.showMessageDialog(panel1, "Failed to connect to the acceptor." +
                        "Check that you selected the correct port");
                setConnected(false);
            } else {

                setConnected(true);

                lblModel.setText(mAcceptor.getAcceptorModel().toString());
                lblRevision.setText(mAcceptor.getFirmwareRevision());
                lblSerialnumber.setText(mAcceptor.getSerialNumber());
            }

        } else {

            mAcceptor.disconnect();

            mAcceptor.removeChangeListener(this);

            setConnected(false);

            mAcceptor = null;

        }
    }

    @Override
    public void changeEventReceived(PTalkEvent evt) {

        if (evt.getId() == Events.SerialData) {
            debuggerViewSafeAdd((SerialDataEvent) evt);
        } else if (evt.getId() == Events.CommunicationFailure) {
            ConnectionFailureEvent c = (ConnectionFailureEvent) evt;

            Object[] options = {"Yes", "No"};
            int selectedValue = JOptionPane.showOptionDialog(
                    panel1,
                    String.format("Acceptor is no longer responding!\nDisconnect?", c.getFailureCount()),
                    "Warning",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
            if (selectedValue == 0) {
                // yes, ugly I know
                btnConnectMouseClicked(null);
            }


            logger.error(String.format("Acceptor is no longer responding! Count: %d", c.getFailureCount()));
        } else if (evt.getId() == Events.Credit) {
            JOptionPane.showMessageDialog(panel1, "Credit received! " +
                    ((CreditEvent) evt).getBillName().name());
        } else {
            setEventState(evt);
        }

    }

    private void debuggerViewSafeAdd(SerialDataEvent e) {
        String message = String.format("%s    %s",
                sdf.format(new Date()),
                e.message);

        // Add this message to the debug viewer. Also track
        // the size of this debug list and clear as needed

        if (e.messageType == MessageType.Slave) {
            this.rxListModel.add(0, message);
            if (this.rxListModel.size() > 499)
                this.rxListModel.remove(498);
        } else {
            this.txListModel.add(0, message);
            if (this.txListModel.size() > 499)
                this.txListModel.remove(498);
        }
    }

    private void setEventState(PTalkEvent e) {
        switch (e.getId()) {
            case Idling:
                rdoIdle.setSelected(true);
                break;
            case Accepting:
                rdoAccepting.setSelected(true);
                break;
            case Escrowed:
                rdoEscrowed.setSelected(true);
                break;
            case Stacking:
                rdoStacking.setSelected(true);
                break;
            case Stacked:
                rdoStacked.setSelected(true);
                break;
        }

        rdoReturning.setSelected(e.getId() == Events.Returning);
        rdoReturned.setSelected(e.getId() == Events.Returned);
        rdoFailure.setSelected(e.getId() == Events.Failure);
        rdoRejected.setSelected(e.getId() == Events.BillRejected);
        rdoStackerFull.setSelected(e.getId() == Events.StackerFull);
        rdoBillJammed.setSelected(e.getId() == Events.BillJammed);
        rdoCheated.setSelected(e.getId() == Events.Cheated);
        rdoCashbox.setSelected(e.getId() == Events.BillCasetteRemoved);

    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(5, 8, new Insets(5, 5, 5, 5), -1, -1));
        cmbPorts = new JComboBox();
        panel1.add(cmbPorts, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("State");
        panel1.add(label1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        rdoIdle = new JRadioButton();
        rdoIdle.setEnabled(false);
        rdoIdle.setText("Idle");
        panel2.add(rdoIdle, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rdoAccepting = new JRadioButton();
        rdoAccepting.setEnabled(false);
        rdoAccepting.setText("Accepting");
        panel2.add(rdoAccepting, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rdoEscrowed = new JRadioButton();
        rdoEscrowed.setEnabled(false);
        rdoEscrowed.setText("Escrowed");
        panel2.add(rdoEscrowed, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rdoStacking = new JRadioButton();
        rdoStacking.setEnabled(false);
        rdoStacking.setText("Stacking");
        panel2.add(rdoStacking, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rdoStacked = new JRadioButton();
        rdoStacked.setEnabled(false);
        rdoStacked.setText("Stacked");
        panel2.add(rdoStacked, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(5, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new GridConstraints(3, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        rdoFailure = new JRadioButton();
        rdoFailure.setEnabled(false);
        rdoFailure.setText("Failure");
        panel3.add(rdoFailure, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rdoRejected = new JRadioButton();
        rdoRejected.setEnabled(false);
        rdoRejected.setText("Rejected");
        panel3.add(rdoRejected, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rdoBillJammed = new JRadioButton();
        rdoBillJammed.setEnabled(false);
        rdoBillJammed.setText("Bill Jammed");
        panel3.add(rdoBillJammed, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rdoCashbox = new JRadioButton();
        rdoCashbox.setEnabled(false);
        rdoCashbox.setText("Cashbox");
        panel3.add(rdoCashbox, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rdoStackerFull = new JRadioButton();
        rdoStackerFull.setEnabled(false);
        rdoStackerFull.setText("Stacker full");
        panel3.add(rdoStackerFull, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rdoCheated = new JRadioButton();
        rdoCheated.setEnabled(false);
        rdoCheated.setText("Cheated");
        panel3.add(rdoCheated, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rdoReturning = new JRadioButton();
        rdoReturning.setEnabled(false);
        rdoReturning.setText("Returning");
        panel3.add(rdoReturning, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rdoReturned = new JRadioButton();
        rdoReturned.setEnabled(false);
        rdoReturned.setText("Returned");
        panel3.add(rdoReturned, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnAccept = new JButton();
        btnAccept.setEnabled(false);
        btnAccept.setText("Accept");
        panel1.add(btnAccept, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnReject = new JButton();
        btnReject.setEnabled(false);
        btnReject.setText("Reject");
        panel1.add(btnReject, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Events");
        panel1.add(label2, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnConnect = new JButton();
        btnConnect.setText("Connect");
        panel1.add(btnConnect, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnPause = new JButton();
        btnPause.setText("Pause");
        panel1.add(btnPause, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 6, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel4, new GridConstraints(4, 0, 1, 8, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Model:");
        panel4.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblModel = new JLabel();
        lblModel.setText("");
        panel4.add(lblModel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Revision:");
        panel4.add(label4, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblRevision = new JLabel();
        lblRevision.setText("");
        panel4.add(lblRevision, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Serial Number:");
        panel4.add(label5, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblSerialnumber = new JLabel();
        lblSerialnumber.setText("");
        panel4.add(lblSerialnumber, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        chkEnableLogging = new JCheckBox();
        chkEnableLogging.setText("Debug Monitor");
        panel1.add(chkEnableLogging, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lstTx = new JList();
        panel1.add(lstTx, new GridConstraints(3, 3, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(300, 500), new Dimension(300, 500), null, 0, false));
        btnReset = new JButton();
        btnReset.setText("Reset Bill Acceptor");
        panel1.add(btnReset, new GridConstraints(0, 3, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lstRx = new JList();
        panel1.add(lstRx, new GridConstraints(3, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(300, 500), new Dimension(300, 500), null, 0, false));
        btnReadInfo = new JButton();
        btnReadInfo.setText("Read Info");
        panel1.add(btnReadInfo, new GridConstraints(1, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnClear = new JButton();
        btnClear.setText("Clear Screen");
        panel1.add(btnClear, new GridConstraints(1, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblConnected = new JLabel();
        lblConnected.setText("Not Connected");
        panel1.add(lblConnected, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Slave");
        panel1.add(label6, new GridConstraints(2, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Master");
        panel1.add(label7, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(rdoEscrowed);
        buttonGroup.add(rdoEscrowed);
        buttonGroup.add(rdoIdle);
        buttonGroup.add(rdoAccepting);
        buttonGroup.add(rdoStacked);
        buttonGroup.add(rdoStacking);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }
}