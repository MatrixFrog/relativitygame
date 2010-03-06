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

  private RelativityLevel level = RelativityLevel.createSpaceshipInTunnelLevel();

  private Timer timer;

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

  private Action loadLevelAction = new AbstractAction("Load Level") {
    public void actionPerformed(ActionEvent arg0) {
      // TODO show a "load level" dialog and then load the level
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
    JPanel simulationPanel = createSimulationPanel();
    add(simulationPanel, gbc);

    gbc.gridy++;
    gbc.weighty = 1;
    gbc.fill = GridBagConstraints.NONE;
    this.add(createControlPanel(), gbc);

    timer = new Timer(5, timestepAction);
    reset();
    setVisible(true);
  }

  private void initMenu() {
    JMenuBar menuBar = new JMenuBar();

    JMenu fileMenu = new JMenu("File");
    menuBar.add(fileMenu);
    fileMenu.add(loadLevelAction);

    JMenu simulationMenu = new JMenu("Simulation");
    menuBar.add(simulationMenu);
    simulationMenu.add(goAction);
    simulationMenu.add(resetAction);

    JMenu referenceFrameMenu = new JMenu("Reference Frame");
    simulationMenu.add(referenceFrameMenu);
    for (PhysicalObject obj : level.getSimulationObjects()) {
      Action setReferenceFrameAction = new SetReferenceFrameAction(obj);
      referenceFrameMenu.add(setReferenceFrameAction);
    }

    setJMenuBar(menuBar);
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
    // TODO re-create control panel and menu
    level = newLevel;
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
