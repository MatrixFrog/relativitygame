package phcs;

import static java.lang.Math.sqrt;
import static util.MathUtils.sq;
import static util.swingutils.SwingUtils.useDefaultLookAndFeel;
import static util.swingutils.SwingUtils.useDialogExceptionHandler;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import phcs.objects.PhysicalObject;

// TODO allow for a different mapping of (x,y) -> actual pixels
// TODO allow user to watch the simulation from any reference frame

public class Relativity extends JFrame implements ActionListener {

  // TODO level loading system
  private RelativityLevel level = RelativityLevel.createSpaceshipInTunnelLevel();

  private Timer timer = new Timer(5, this);
  private JButton goButton = new JButton("Go");
  private JButton resetButton = new JButton("Reset");

  private JPanel controlPanel = new JPanel();
  private List<JPanel> objectControlPanels = new ArrayList<JPanel>();

  private JPanel createSimulationPanel() {
    JPanel simulationPanel = new JPanel() {
      @Override
      public void paint(Graphics g) {
        super.paint(g);
        for (PhysicalObject obj : level.getSimulationObjects()) {
          obj.paint(g);
        }
      }
    };
    return simulationPanel;
  }

  // TODO create a loadLevel() method so that levels can be loaded at runtime
  public Relativity() {
    setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.weightx = gbc.weighty = 1;
    setSize(1024, 768);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    goButton.addActionListener(this);
    resetButton.addActionListener(this);

    gbc.gridx = gbc.gridy = 0;
    gbc.fill = GridBagConstraints.BOTH;
    JPanel simulationPanel = createSimulationPanel();
    add(simulationPanel, gbc);

    gbc.gridy++;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    initControlPanel();
    this.add(controlPanel, gbc);

    reset();
    setVisible(true);
  }

  private void initControlPanel() {
    controlPanel.removeAll();

    controlPanel.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = gbc.gridy = 0;
    gbc.weightx = gbc.weighty = 1;
    gbc.fill = GridBagConstraints.NONE;
    controlPanel.add(goButton, gbc);
    gbc.gridx++;
    controlPanel.add(resetButton, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.NONE;

    // Add the individual objects' control panels to the main control panel
    for (PhysicalObject obj : level.getSimulationObjects()) {
      if (obj.isControllable()) {
        JPanel objectControlPanel = obj.getControlPanel();
        objectControlPanels.add(objectControlPanel);
        controlPanel.add(objectControlPanel, gbc);
        gbc.gridy++;
      }
    }
  }

  public static double gamma(double speed) {
    return 1/sqrt(1 - sq(speed));
  }

  public static double inverseGamma(double gamma) {
    return sqrt(1 - 1/sq(gamma));
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == goButton) {
      go();
    }
    if (e.getSource() == resetButton) {
      reset();
    }
    else if (e.getSource() == timer) {
      for (PhysicalObject obj : level.getSimulationObjects()) {
        obj.update();
      }
      repaint();
    }
  }

  private void go() {
    setControlsEnabled(false);
    goButton.setEnabled(false);
    resetButton.setEnabled(true);
    timer.start();
    level.go();
  }

  private void reset() {
    for (PhysicalObject obj : level.getSimulationObjects()) {
      obj.reset();
    }
    timer.stop();
    goButton.setEnabled(true);
    resetButton.setEnabled(false);
    setControlsEnabled(true);
    repaint();
  }

  private void setControlsEnabled(boolean enable) {
    for (JPanel objectCtrlPanel : objectControlPanels) {
      objectCtrlPanel.setEnabled(enable);
    }
  }


  public static void main(String[] args) {
    useDefaultLookAndFeel();
    useDialogExceptionHandler();
    new Relativity();
  }
}
