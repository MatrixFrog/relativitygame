package phcs.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import phcs.objects.Tunnel;
import util.swingutils.RecursiveEnablePanel;
import util.swingutils.SwingUtils;

public class TunnelController extends RecursiveEnablePanel implements ActionListener {

  private Tunnel tunnel;
  private DefaultListModel listModel = new DefaultListModel();
  private Map<Double, GateEvent> events = new HashMap<Double, GateEvent>();
  private ButtonGroup gateButtons = new ButtonGroup();
  private JRadioButton leftGateButton = new JRadioButton("left gate", true);
  private JRadioButton rightGateButton = new JRadioButton("right gate");
  private JTextField timeField = new JTextField(20);
  private JButton addButton = new JButton("Add event");

  private double time = 0;

  public TunnelController(Tunnel tunnel) {
    this.tunnel = tunnel;
    gateButtons.add(leftGateButton);
    gateButtons.add(rightGateButton);

    this.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = gbc.gridy = 0;
    gbc.weightx = gbc.weighty = 0;

    gbc.gridheight = 2;
    this.add(new JList(listModel), gbc);
    gbc.gridx++;

    this.add(new JLabel("Toggle"), gbc);
    gbc.gridx++;

    gbc.gridheight = 1;
    gbc.gridy = 1;
    this.add(rightGateButton, gbc);
    gbc.gridy = 0;
    this.add(leftGateButton, gbc);
    gbc.gridheight = 2;
    gbc.gridx++;

    this.add(new JLabel("at t="), gbc);
    gbc.gridx++;

    this.add(timeField, gbc);
    gbc.gridx++;

    this.add(addButton, gbc);
    gbc.gridx++;

    this.setBorder(BorderFactory.createTitledBorder(tunnel.getName()));

    addButton.addActionListener(this);
  }

  private class GateEvent {
    /** true = left, false = right */
    private boolean gate;
    private double time;

    public GateEvent(boolean gate, double time) {
      this.gate = gate;
      this.time = time;
    }

    @Override
    public String toString() {
      String gateString = gate ? "left" : "right";
      return "Toggle " + gateString + " gate at t=" + time;
    }

    public void doEvent() {
      if (gate) {
        tunnel.toggleLeftGate();
      }
      else {
        tunnel.toggleRightGate();
      }
    }
  }

  public void incrementTime(double addedTime) {
    // Any events that happen between now and (now + addedTime) are triggered.
    for (Entry<Double, GateEvent> e : events.entrySet()) {
      double eventTime = e.getKey();
      GateEvent evt = e.getValue();
      if (eventTime >= time && eventTime < (time+addedTime)) {
        evt.doEvent();
      }
    }
    time += addedTime;
  }

  public void reset() {
    time = 0;
    tunnel.reset();
  }

  public void actionPerformed(ActionEvent ae) {
    if (ae.getSource() == addButton) {
      try {
        double eventTime = Double.parseDouble(timeField.getText());
        GateEvent gateEvent = new GateEvent(leftGateButton.isSelected(), eventTime);
        listModel.addElement(gateEvent);
        events.put(gateEvent.time, gateEvent);
        timeField.setText("");
      } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(SwingUtils.getActiveFrame(), "Invalid number: " + timeField.getText());
      }
    }
  }


}
