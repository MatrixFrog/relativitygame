package phcs.gui;

import static util.swingutils.SwingUtils.useDefaultLookAndFeel;
import static util.swingutils.SwingUtils.useDialogExceptionHandler;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import phcs.GoalListener;
import phcs.PhysicalObject;
import phcs.RelativityLevel;
import phcs.Trace;

public class Relativity extends JFrame implements GoalListener {

  private int counter;
  private JLabel counterLabel = new JLabel();

  private RelativityLevel level;

  private Timer timer;

  private JPanel controlPanelContainer = new JPanel();
  private JPanel simulationPanel  = new JPanel(true) {
    {
      setBorder(BorderFactory.createLineBorder(Color.BLACK, 10));
    }
    @Override
    public void paint(Graphics g) {
      if (level != null) {
        level.paint(g);
      }
    }
  };

  private List<SetReferenceFrameAction> referenceFrameActions = new ArrayList<SetReferenceFrameAction>();
  private JMenu referenceFrameMenu;

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
      Relativity.this.loadLevel(this.level);
      goAction.setEnabled(true);
      resetAction.setEnabled(true);
    }
  }

  private Action goAction = new AbstractAction("Go") {
    {
      setEnabled(false);
    }
    public void actionPerformed(ActionEvent e) {
      go();
    }
  };

  private Action resetAction = new AbstractAction("Reset") {
    {
      setEnabled(false);
    }
    public void actionPerformed(ActionEvent e) {
      reset();
    }
  };

  private Action timestepAction = new AbstractAction("timestep") {
    public void actionPerformed(ActionEvent arg0) {
      level.update();
      repaint();
      if (Trace.TRACE) {
        counter++;
        counterLabel.setText(counter + "");
      }
    }
  };

  /**
   * Pauses or unpauses the simulation.
   */
  // FIXME This should only be callable while the simulation is running.
  private Action pauseAction = new AbstractAction("Pause/resume") {
    public void actionPerformed(ActionEvent arg0) {
      if (timer.isRunning()) {
        timer.stop();
      }
      else {
        timer.start();
      }
    }
  };

  public Relativity() {
    super("Relativity");

    setSize(1024, 768);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    controlPanelContainer.setLayout(new BoxLayout(controlPanelContainer, BoxLayout.X_AXIS));

    initMenu();
    layoutGUIcomponents();

    timer = new Timer(5, timestepAction);

    setVisible(true);
  }

  private void layoutGUIcomponents() {
    setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.weightx = gbc.weighty = 5;
    gbc.gridx = gbc.gridy = 0;
    gbc.fill = GridBagConstraints.BOTH;

    add(simulationPanel, gbc);

    gbc.fill = GridBagConstraints.NONE;
    gbc.gridy++;
    gbc.weighty = 1;

    this.add(controlPanelContainer, gbc);
    if (Trace.TRACE) {
      gbc.gridy++;
      add(counterLabel, gbc);
    }
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

    if (Trace.TRACE) {
      JMenu debugMenu = new JMenu("Debug");
      menuBar.add(debugMenu);
      debugMenu.add(new AbstractAction("Repaint") {
        public void actionPerformed(ActionEvent arg0) {
          repaint();
        }
      });
      debugMenu.add(new AbstractAction("Validate") {
        public void actionPerformed(ActionEvent arg0) {
          validate();
        }
      });
      debugMenu.add(pauseAction);
    }

    setJMenuBar(menuBar);
  }

  private void initReferenceFrameMenu() {
    referenceFrameActions.clear();
    referenceFrameMenu.removeAll();
    for (PhysicalObject obj : this.level.getSimulationObjects()) {
      SetReferenceFrameAction frameAction = new SetReferenceFrameAction(obj);
      referenceFrameMenu.add(frameAction);
      referenceFrameActions.add(frameAction);
    }
  }

  /**
   * Notice that the control panel (in the context of this class) is not the same as
   * Level.controlPanel. The level control panel only contains the sliders/buttons/etc.
   * relevant to that level. This control panel contains the level control panel, and the
   * Go/Reset buttons.
   */
  private JPanel createControlPanel() {
    JPanel controlPanel = new JPanel();

    controlPanel.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = gbc.gridy = 0;
    gbc.weightx = gbc.weighty = 1;
    gbc.fill = GridBagConstraints.NONE;
    controlPanel.add(new JButton(goAction), gbc);
    gbc.gridx++;
    controlPanel.add(new JButton(resetAction), gbc);
    if (Trace.TRACE) {
      gbc.gridx++;
      controlPanel.add(new JButton(pauseAction), gbc);
    }

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = Trace.TRACE ? 3 : 2; // in debug mode, make it 3 to make room for the extra button
    gbc.fill = GridBagConstraints.BOTH;
    controlPanel.add(level.getControlPanel(), gbc);

    return controlPanel;
  }

  private void loadLevel(RelativityLevel newLevel) {
    level = newLevel;

    controlPanelContainer.removeAll();
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    controlPanelContainer.add(createControlPanel(), gbc);

    initReferenceFrameMenu();

    reset();
    validate();

    level.showInstructions();
    level.addGoalListener(this);
    log("Finished loading " + newLevel);
  }

  private void go() {
    level.getControlPanel().setEnabled(false);
    resetAction.setEnabled(true);
    goAction.setEnabled(false);
    for (SetReferenceFrameAction frameAction : referenceFrameActions) {
      frameAction.setEnabled(false);
    }
    referenceFrameMenu.setEnabled(false);
    level.go();
    timer.start();
  }

  private void reset() {
    timer.stop();
    level.reset();
    resetAction.setEnabled(false);
    goAction.setEnabled(true);
    for (SetReferenceFrameAction frameAction : referenceFrameActions) {
      frameAction.setEnabled(true);
    }
    referenceFrameMenu.setEnabled(true);
    level.getControlPanel().setEnabled(true);
    repaint();
    if (Trace.TRACE) {
      counter = 0;
      counterLabel.setText("0");
    }
  }

  public void goalAchieved() {
    JOptionPane.showMessageDialog(this, "You win!");
    reset();
  }

  public static void log(String msg) {
    if (Trace.TRACE) {
      System.out.println(msg);
    }
  }

  public static void main(String[] args) {
    useDefaultLookAndFeel();
    useDialogExceptionHandler();
    new Relativity();
  }
}
