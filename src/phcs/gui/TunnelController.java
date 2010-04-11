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

import phcs.PhysicalObject;
import phcs.Relativity;
import phcs.objects.Tunnel;
import util.swingutils.RecursiveEnablePanel;
import util.swingutils.SwingUtils;

/**
 * A GUI panel that allows the user to control a Tunnel by stating at what times each
 * gate should be toggled. All times are given in the tunnel's rest frame and if the
 * tunnel is moving they are automatically converted to whatever frame the animation
 * is being viewed in.
 */
// TODO use an enum for left/right gate instead of a boolean.
public class TunnelController extends RecursiveEnablePanel implements ActionListener {

  private Tunnel tunnel;
  private Map<Double, GateEvent> eventMap;

  private DefaultListModel listModel = new DefaultListModel();
  private JList guiEventList = new JList(listModel);
  private ButtonGroup gateButtons = new ButtonGroup();
  private JRadioButton leftGateButton = new JRadioButton("left gate", true);
  private JRadioButton rightGateButton = new JRadioButton("right gate");
  private JTextField timeField = new JTextField(6);
  private JButton addButton = new JButton("Add event");
  private JButton removeButton = new JButton("Remove event");

  private double time = 0;

  public TunnelController() {
    gateButtons.add(leftGateButton);
    gateButtons.add(rightGateButton);

    addButton.addActionListener(this);
    removeButton.addActionListener(this);

    layoutGUI();
  }

  public TunnelController(Tunnel tunnel) {
    this();
    setTunnel(tunnel);
  }

  public void setTunnel(Tunnel tunnel) {
    this.tunnel = tunnel;

    this.setBorder(BorderFactory.createTitledBorder(tunnel.getName()));
  }

  private void layoutGUI() {
    this.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = gbc.gridy = 0;
    gbc.weightx = gbc.weighty = 0;

    this.add(guiEventList, gbc);
    gbc.gridy++;

    this.add(removeButton, gbc);
    gbc.gridx++;
    gbc.gridy = 0;

    gbc.gridheight = 2;
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

  public void timeIncrement(double addedTime) {
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
  }

  public void buildEventMap() {
    if (Relativity.DEBUG) {
      System.out.println("TunnelController: building event map. tunnel.getVX() = " + tunnel.getVX());
    }
    eventMap = new HashMap<Double, GateEvent>();
    for (Object eventObj : listModel.toArray()) {
      GateEvent event = (GateEvent) eventObj;
      double eventTime = event.time/PhysicalObject.gamma(tunnel.getSpeed());
      if (!event.gate) {
        eventTime += tunnel.getVX()*tunnel.getWidth();
      }
      eventMap.put(eventTime,event);
    }
  }

  void addEvent(GateEvent e) {
    listModel.addElement(e);
  }

  private void addEventFromGUI() {
    try {
      double eventTime = Double.parseDouble(timeField.getText());
      GateEvent gateEvent = new GateEvent(leftGateButton.isSelected(), eventTime);
      addEvent(gateEvent);
      timeField.setText("");
    } catch (NumberFormatException e) {
      if (!timeField.getText().equals("")) {
        JOptionPane.showMessageDialog(SwingUtils.getActiveFrame(), "Invalid number: " + timeField.getText());
      }
    }
  }

  private void removeSelectedEvents() {
    int i=guiEventList.getSelectedIndex();
    while (i != -1) {
      listModel.remove(i);
      i=guiEventList.getSelectedIndex();
    }
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == addButton) {
      addEventFromGUI();
    }
    else if (e.getSource() == removeButton) {
      removeSelectedEvents();
    }
  }
}
