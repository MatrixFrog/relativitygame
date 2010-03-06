package phcs.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

import phcs.PhysicalObject;
import phcs.objects.Tunnel;
import util.swingutils.RecursiveEnablePanel;
import util.swingutils.SwingUtils;

/**
 * A GUI panel that allows the user to control a Tunnel by stating at what times each
 * gate should be toggled. All times are given in the tunnel's rest frame and if the
 * tunnel is moving they are automatically converted to whatever frame the animation
 * is being viewed in.
 */
public class TunnelController extends RecursiveEnablePanel implements ActionListener {

  private Tunnel tunnel;
  private DefaultListModel listModel = new DefaultListModel();
  private List<GateEvent> events = new ArrayList<GateEvent>();
  private Map<Double, GateEvent> eventMap;

  private ButtonGroup gateButtons = new ButtonGroup();
  private JRadioButton leftGateButton = new JRadioButton("left gate", true);
  private JRadioButton rightGateButton = new JRadioButton("right gate");
  private JTextField timeField = new JTextField(10);
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

  class GateEvent {
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
    if (eventMap == null) {
      buildEventMap();
    }
    // Any events that happen between now and (now + addedTime) are triggered.
    for (Entry<Double, GateEvent> e : eventMap.entrySet()) {
      // TODO figure out a way to do this so that we're not looping over the whole map every single timestep
      double eventTime = e.getKey();
      GateEvent event = e.getValue();
      if (eventTime >= time && eventTime < (time+addedTime)) {
        event.doEvent();
      }
    }
    time += addedTime;
  }

  public void reset() {
    time = 0;
    eventMap = null;
    tunnel.reset();
  }

  public void buildEventMap() {
    eventMap = new HashMap<Double, GateEvent>();
    for (GateEvent event : events) {
      double time = event.time/PhysicalObject.gamma(tunnel.getSpeed());
      if (!event.gate) {
        time += tunnel.getSpeed()*tunnel.getWidth();
      }
      eventMap.put(time,event);
    }
  }

  public void actionPerformed(ActionEvent ae) {
    try {
      double eventTime = Double.parseDouble(timeField.getText());
      GateEvent gateEvent = new GateEvent(leftGateButton.isSelected(), eventTime);
      addGateEvent(gateEvent);
      timeField.setText("");
    } catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(SwingUtils.getActiveFrame(), "Invalid number: " + timeField.getText());
    }
  }

  void addGateEvent(GateEvent e) {
    listModel.addElement(e);
    events.add(e);
  }


}
