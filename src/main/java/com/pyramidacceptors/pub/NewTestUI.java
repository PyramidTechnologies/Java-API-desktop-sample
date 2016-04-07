package com.pyramidacceptors.pub;

import com.pyramidacceptors.ptalk.api.MessageType;
import com.pyramidacceptors.ptalk.api.PyramidAcceptor;
import com.pyramidacceptors.ptalk.api.PyramidDeviceException;
import com.pyramidacceptors.ptalk.api.PyramidPort;
import com.pyramidacceptors.ptalk.api.event.Events;
import com.pyramidacceptors.ptalk.api.event.PTalkEvent;
import com.pyramidacceptors.ptalk.api.event.PTalkEventListener;
import com.pyramidacceptors.ptalk.api.event.SerialDataEvent;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
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
        for(String port : PyramidPort.getPortList())
            cmbPorts.addItem(port);


        // Bind our debugging view to models that are filled when
        // we receive the serialdataevent
        rxListModel = new DefaultListModel();
        txListModel = new DefaultListModel();
        lstTx.setModel(txListModel);
        lstRx.setModel(rxListModel);
        lstRx.setCellRenderer(new AlternatingRowColors());
        lstTx.setCellRenderer(new AlternatingRowColors());


        frame.pack();

        // Center the JFrame
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = frame.getWidth();
        int y = frame.getHeight() / 2;
        Point pt = new Point((int) (dim.getWidth() / 2 - x),
                (int) (dim.getHeight() / 2 - y));
        frame.setLocation(pt);

        // Disable everything that is a an acceptor command
        setConnected(false);

        frame.setVisible(true);
        logger.info("Sample app successfully launched");

    }

    private void setupListeners() {
        btnConnect.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnConnectMouseClicked(evt);
            }
        });

        btnClear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnClearMouseClicked(evt);
            }
        });

        btnPause.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPausedMouseClicked(evt);
            }
        });

        btnReset.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnResetMouseClicked(evt);
            }
        });

        btnReset.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnResetMouseClicked(evt);
            }
        });

        btnReadInfo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnReadInfoMouseClicked(evt);
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

        if(connected) {
            this.lstRx.clearSelection();
            this.lstTx.clearSelection();
        }

    }

    private void btnReadInfoMouseClicked(java.awt.event.MouseEvent evt) {
        lblModel.setText(mAcceptor.getAcceptorModel());
        lblRevision.setText(mAcceptor.getFirmwareRevision());

        mAcceptor.requestSerialNumber();
        lblSerialnumber.setText(mAcceptor.getSerialNumber());
    }

    static boolean isPaused = false;
    private void btnPausedMouseClicked(java.awt.event.MouseEvent evt) {

        // mAcceptor will never be null
        if(isPaused) {
            mAcceptor.unpause();
            btnPause.setText("Pause");
        }
        else {
            mAcceptor.pause();
            btnPause.setText("Un pause");
        }

        isPaused = !isPaused;

    }

    private void btnResetMouseClicked(java.awt.event.MouseEvent evt) {
        mAcceptor.requestReset();
    }

    private void btnClearMouseClicked(java.awt.event.MouseEvent evt) {
        txListModel.clear();
        rxListModel.clear();
    }

    private void btnConnectMouseClicked(java.awt.event.MouseEvent evt) {

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

        if(evt.getId() == Events.SerialData) {
            debuggerViewSafeAdd((SerialDataEvent)evt);
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

        if(e.messageType == MessageType.Slave) {
            this.rxListModel.add(0, message);
            if(this.rxListModel.size() > 500)
                this.rxListModel.remove(499);
        } else {
            this.txListModel.add(0, message);
            if(this.txListModel.size() > 500)
                this.txListModel.remove(499);
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

}