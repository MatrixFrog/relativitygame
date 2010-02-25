package phcs.gui;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import phcs.objects.Tunnel;
import util.swingutils.SwingUtils;

public class TunnelControllerTest extends JFrame implements ActionListener {

  private TunnelController tunnelController;
  private Tunnel tunnel;
  private Timer timer = new Timer(5, this);

  private JButton goButton = new JButton("Go");
  private JButton resetButton = new JButton("Reset");
  private JButton enableButton = new JButton("Enable/Disable");

  private JPanel panel = new JPanel() {
    @Override
    public void paint(Graphics g) {
      tunnel.paint(g);
    }
  };
  public TunnelControllerTest() {
    tunnel = new Tunnel();
    tunnel.setName("Tunnel");
    tunnelController = new TunnelController(tunnel);

    setTitle(getClass().getSimpleName());

    setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = gbc.gridy = 0;
    gbc.weightx = gbc.weighty = 1;

    gbc.gridwidth = 3;
    gbc.weighty = 12;
    gbc.fill = GridBagConstraints.BOTH;
    add(panel, gbc);
    gbc.fill = GridBagConstraints.NONE;
    gbc.weighty = 1;
    gbc.gridy++;

    add(tunnelController, gbc);
    gbc.gridy++;
    gbc.gridwidth = 1;

    add(goButton, gbc);
    gbc.gridx++;

    add(resetButton, gbc);
    gbc.gridx++;

    add(enableButton, gbc);
    gbc.gridx++;

    goButton.addActionListener(this);
    resetButton.addActionListener(this);
    enableButton.addActionListener(this);

    setSize(800, 600);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    setVisible(true);
  }

  public static void main(String[] args) {
    SwingUtils.useDefaultLookAndFeel();
    new TunnelControllerTest();
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == timer) {
      tunnelController.incrementTime(1);
      tunnel.update();
      repaint();
    }
    else if (e.getSource() == goButton) {
      timer.start();
    }
    else if (e.getSource() == resetButton) {
      timer.stop();
      tunnelController.reset();
      repaint();
    }
    else if (e.getSource() == enableButton) {
      tunnelController.setEnabled(!tunnelController.isEnabled());
    }
  }

}
