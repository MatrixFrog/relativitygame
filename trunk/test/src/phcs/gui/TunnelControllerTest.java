package phcs.gui;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import phcs.objects.Tunnel;
import util.swingutils.SwingUtils;

public class TunnelControllerTest extends JFrame implements ActionListener {

  private TunnelController tunnelController;
  private Tunnel tunnel;
  private Timer timer = new Timer(10, this);

  private JButton goButton = new JButton("Go");
  private JButton resetButton = new JButton("Reset");
  private JButton enableButton = new JButton("Enable/Disable");
  private VelocitySlider velocitySlider;
  private JLabel timeLabel = new JLabel();
  private int time = 0;

  private JPanel panel = new JPanel() {
    @Override
    public void paint(Graphics g) {
      tunnel.paint(g);
    }
  };
  public TunnelControllerTest() {
    tunnel = new Tunnel(300, 100, 100, 60, 0.5, 0);
    tunnelController = new TunnelController(tunnel);
    velocitySlider = new VelocitySlider(tunnel, 0.5);

    setTitle(getClass().getSimpleName());

    layoutGUIcomponents();

    goButton.addActionListener(this);
    resetButton.addActionListener(this);
    enableButton.addActionListener(this);

    // Both of these events should occur at the same time if the tunnel has v=0.5
    tunnelController.addGateEvent(tunnelController.new GateEvent(true, 173.21));
    tunnelController.addGateEvent(tunnelController.new GateEvent(false, 115.47));

    setSize(800, 600);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    setVisible(true);
  }

  private void layoutGUIcomponents() {
    setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = gbc.gridy = 0;
    gbc.weightx = gbc.weighty = 1;

    gbc.gridwidth = 4;
    gbc.weighty = 12;
    gbc.fill = GridBagConstraints.BOTH;
    add(panel, gbc);

    gbc.weighty = 1;
    gbc.gridy++;

    add(tunnelController, gbc);
    gbc.gridy++;

    add(velocitySlider, gbc);
    gbc.gridy++;

    gbc.fill = GridBagConstraints.NONE;
    gbc.gridwidth = 1;

    add(goButton, gbc);
    gbc.gridx++;

    add(resetButton, gbc);
    gbc.gridx++;

    add(enableButton, gbc);
    gbc.gridx++;

    add(timeLabel, gbc);

    gbc.gridy++;
    gbc.gridx = 0;
    gbc.gridwidth = 4;
    add(new JLabel("With the preset speed and gate events, both gates " +
        		"should close at the same time."), gbc);
  }

  public static void main(String[] args) {
    SwingUtils.useDefaultLookAndFeel();
    new TunnelControllerTest();
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == timer) {
      tunnelController.incrementTime(1);
      tunnel.update();
      time++;
      timeLabel.setText(Integer.toString(time));
      repaint();
    }
    else if (e.getSource() == goButton) {
      timer.start();
    }
    else if (e.getSource() == resetButton) {
      timer.stop();
      tunnelController.reset();
      time = 0;
      timeLabel.setText(Integer.toString(time));
      repaint();
    }
    else if (e.getSource() == enableButton) {
      tunnelController.setEnabled(!tunnelController.isEnabled());
    }
  }
}
