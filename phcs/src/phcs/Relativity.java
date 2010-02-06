package phcs;

import static common.swingutils.SwingUtils.useDefaultLookAndFeel;
import static common.swingutils.SwingUtils.useDialogExceptionHandler;
import static java.lang.Math.sqrt;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import phcs.objects.PhysicalObject;

public class Relativity extends JFrame implements ActionListener {

  private RelativityLevel level = RelativityLevel.getLevel1(); // TODO for now

  private Timer timer = new Timer(5, this);
  private JButton goButton = new JButton("Go");
  private JButton resetButton = new JButton("Reset");

  private List<JComponent> controlComponents;

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
    simulationPanel.setBackground(Color.CYAN);
    return simulationPanel;
  }


  public Relativity() {
    setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.weightx = gbc.weighty = 1;
    setSize(1024, 768);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    gbc.gridx = gbc.gridy = 0;
    gbc.fill = GridBagConstraints.BOTH;
    JPanel simulationPanel = createSimulationPanel();
    add(simulationPanel, gbc);

    gbc.gridy++;
    gbc.fill = GridBagConstraints.NONE;
    this.add(createControlPanel(), gbc);

    reset();
    setVisible(true);
  }

  private JPanel createControlPanel() {
    goButton.addActionListener(this);
    resetButton.addActionListener(this);

    JPanel controlPanel = new JPanel();
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
    gbc.fill = GridBagConstraints.HORIZONTAL;
    // Add the individual objects' control panels to the main control panel
    for (PhysicalObject obj : level.getSimulationObjects()) {
      if (obj.isControllable()) {
        controlPanel.add(obj.getControlPanel(), gbc);
        gbc.gridy++;
      }
    }

    return controlPanel;
  }

  public static double gamma(double speed) {
    return 1/sqrt(1 - speed * speed);
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
    setControlsEnabled(true);
    timer.start();
    level.go();
  }

  private void setControlsEnabled(boolean enable) {
    // FIXME!

    if (true) {
      return;
    }

    for (JComponent component : controlComponents) {
      component.setEnabled(enable);
    }
  }

  private void reset() {
    for (PhysicalObject obj : level.getSimulationObjects()) {
      obj.reset();
    }
    timer.stop();
    setControlsEnabled(true);
    repaint();
  }

  public static void main(String[] args) {
    useDefaultLookAndFeel();
    useDialogExceptionHandler();
    new Relativity();
  }
}
