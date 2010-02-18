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

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.Timer;


public class Relativity extends JFrame implements ActionListener {

  private RelativityLevel level = RelativityLevel.createLightClocksOnTrainLevel();

  private Timer timer;
  private JButton transformButton1 = new JButton("Transform (+)");
  private JButton transformButton2 = new JButton("Transform (-)");

  private ReferenceFrame vt = new ReferenceFrame(0.5, 0);

  //private JPanel controlPanel = new JPanel();
  private List<JPanel> objectControlPanels = new ArrayList<JPanel>();

  private Action goAction = new AbstractAction("Go") {
    public void actionPerformed(ActionEvent e) {
      go();
    }
  };

  private Action resetAction = new AbstractAction("Reset") {
    public void actionPerformed(ActionEvent e) {
      reset();
    }
  };

  private Action timestepAction = new AbstractAction("timestep") {
    public void actionPerformed(ActionEvent arg0) {
      level.update();
      repaint();
    }
  };

  private JPanel createSimulationPanel() {
    JPanel simulationPanel = new JPanel() {
      @Override
      public void paint(Graphics g) {
        level.paint(g);
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

    initMenu();

    gbc.gridx = gbc.gridy = 0;
    gbc.fill = GridBagConstraints.BOTH;
    JPanel simulationPanel = createSimulationPanel();
    add(simulationPanel, gbc);

    gbc.gridy++;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    this.add(createControlPanel(), gbc);

    reset();
    timer = new Timer(5, timestepAction);
    setVisible(true);
  }

  private void initMenu() {
    JMenuBar menuBar = new JMenuBar();

    JMenu simulationMenu = new JMenu("Simulation");
    simulationMenu.add(goAction);
    simulationMenu.add(resetAction);
    menuBar.add(simulationMenu);

    setJMenuBar(menuBar);
  }

  private JPanel createControlPanel() {
    JPanel controlPanel = new JPanel();

    JButton goButton = new JButton(goAction);
    JButton resetButton = new JButton(resetAction);
    transformButton1.addActionListener(this);
    transformButton2.addActionListener(this);

    controlPanel.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = gbc.gridy = 0;
    gbc.weightx = gbc.weighty = 1;
    gbc.fill = GridBagConstraints.NONE;
    controlPanel.add(goButton, gbc);
    gbc.gridx++;
    controlPanel.add(resetButton, gbc);
    gbc.gridx++;
    controlPanel.add(transformButton1, gbc);
    gbc.gridx++;
    controlPanel.add(transformButton2, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 4;
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
    return controlPanel;
  }

  public static double gamma(double speed) {
    return 1/sqrt(1 - sq(speed));
  }

  public static double inverseGamma(double gamma) {
    return sqrt(1 - 1/sq(gamma));
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == timer) {
      level.update();
      repaint();
    }
    else if (e.getSource() == transformButton1) {
      level.setFrame(vt);
    }
    else if (e.getSource() == transformButton2) {
      level.setFrame(vt.inverted());
    }
  }

  private void go() {
    setControlsEnabled(false);
    goAction.setEnabled(false);
    resetAction.setEnabled(true);
    transformButton1.setEnabled(false);
    transformButton2.setEnabled(false);
    timer.start();
    level.go();
  }

  private void reset() {
    level.reset();
    timer.stop();
    goAction.setEnabled(true);
    resetAction.setEnabled(false);
    transformButton1.setEnabled(true);
    transformButton2.setEnabled(true);
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
