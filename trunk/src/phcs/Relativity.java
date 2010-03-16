package phcs;

import static util.swingutils.SwingUtils.useDefaultLookAndFeel;
import static util.swingutils.SwingUtils.useDialogExceptionHandler;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Relativity extends JFrame {

  //private RelativityLevel level = RelativityLevel.createSpaceshipInTunnelLevel();
  private RelativityLevel level;

  private Timer timer;

  private JPanel controlPanelContainer = new JPanel();
  private JPanel simulationPanel  = new JPanel() {
    @Override
    public void paint(Graphics g) {
      if (level != null) {
        level.paint(g);
      }
    }
  };

  private JMenu referenceFrameMenu;

  // TODO disable all instances of this type of action while simulation is running
  private class SetReferenceFrameAction extends AbstractAction {

    private PhysicalObject obj;

    public SetReferenceFrameAction(PhysicalObject obj) {
      super(obj.getName());
      this.obj = obj;
    }

    public void actionPerformed(ActionEvent e) {
      level.setFrame(obj.getRestFrame());
    }
  }

  private class LoadLevelAction extends AbstractAction {
    private RelativityLevel level;

    public LoadLevelAction(RelativityLevel level) {
      super(level.getName());
      this.level = level;
    }

    public void actionPerformed(ActionEvent ae) {
      loadLevel(level);
    }
  }

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

  public Relativity() {
    super("Relativity");

    setSize(1024, 768);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    initMenu();

    setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.weightx = 1;

    gbc.gridx = gbc.gridy = 0;
    gbc.weighty = 4;
    gbc.fill = GridBagConstraints.BOTH;
    add(simulationPanel, gbc);

    gbc.gridy++;
    gbc.weighty = 1;
    gbc.fill = GridBagConstraints.NONE;
    this.add(controlPanelContainer, gbc);

    timer = new Timer(5, timestepAction);

    setVisible(true);
  }

  private void initMenu() {
    JMenuBar menuBar = new JMenuBar();

    JMenu fileMenu = new JMenu("File");
    menuBar.add(fileMenu);

    for (RelativityLevel aLevel : RelativityLevel.levels) {
      fileMenu.add(new LoadLevelAction(aLevel));
    }

    JMenu simulationMenu = new JMenu("Simulation");
    menuBar.add(simulationMenu);
    simulationMenu.add(goAction);
    simulationMenu.add(resetAction);

    referenceFrameMenu = new JMenu("Reference Frame");
    simulationMenu.add(referenceFrameMenu);

    if (true) {
      JMenu testMenu = new JMenu("Test");
      menuBar.add(testMenu);
      testMenu.add(new AbstractAction("Repaint") {
        public void actionPerformed(ActionEvent arg0) {
          repaint();
        }
      });
      testMenu.add(new AbstractAction("Validate") {
        public void actionPerformed(ActionEvent arg0) {
          validate();
        }
      });
    }

    setJMenuBar(menuBar);
  }

  private void initReferenceFrameMenu() {
    referenceFrameMenu.removeAll();
    for (PhysicalObject obj : level.getSimulationObjects()) {
      referenceFrameMenu.add(new SetReferenceFrameAction(obj));
    }
  }

  private JPanel createControlPanel() {
    JPanel controlPanel = new JPanel();

    JButton goButton = new JButton(goAction);
    JButton resetButton = new JButton(resetAction);

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
    controlPanel.add(level.getControlPanel(), gbc);

    return controlPanel;
  }

  private void loadLevel(RelativityLevel newLevel) {
    level = newLevel;

    controlPanelContainer.removeAll();
    controlPanelContainer.add(createControlPanel());

    initReferenceFrameMenu();

    reset();
    validate();
  }

  private void go() {
    level.getControlPanel().setEnabled(false);
    goAction.setEnabled(false);
    resetAction.setEnabled(true);
    timer.start();
    level.go();
  }

  private void reset() {
    level.reset();
    timer.stop();
    goAction.setEnabled(true);
    resetAction.setEnabled(false);
    level.getControlPanel().setEnabled(true);
    repaint();
  }

  public static void main(String[] args) {
    useDefaultLookAndFeel();
    useDialogExceptionHandler();
    new Relativity();
  }
}
