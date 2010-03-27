package phcs;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;

import phcs.levels.LightClocksOnTrainLevel;
import phcs.levels.SpaceshipInTunnelLevel;

/**
 * An instance of this class represents one single "level" or "puzzle" to be solved.
 */
public abstract class RelativityLevel {

  private List<PhysicalObject> simulationObjects = new ArrayList<PhysicalObject>();
  private ReferenceFrame referenceFrame = ReferenceFrame.DEFAULT_FRAME;
  private boolean running;
  protected JPanel controlPanel;
  private String name;
  private List<GoalListener> goalListeners = new ArrayList<GoalListener>();

  public void addSimulationObject(PhysicalObject obj) {
    simulationObjects.add(obj);
  }

  public List<PhysicalObject> getSimulationObjects() {
    return Collections.unmodifiableList(simulationObjects);
  }

  /**
   * This is what gets called when the player presses the "Go" button in the UI. It calls
   * rf.transformVelocity(obj) for all the objects in the level where rf is the reference
   * frame being used.
   */
  public void go() {
    running = true;
    for (PhysicalObject obj : simulationObjects) {
      referenceFrame.transformVelocity(obj);
    }
    for (PhysicalObject obj : this.simulationObjects) {
      obj.go();
    }
  }

  public void update() {
    for (PhysicalObject obj : this.simulationObjects) {
      obj.update();
    }
    if (goalAchieved()) {
      notifyGoalListeners();
    }
  }


  /**
   * Add the specified goalListener (if it was added)
   */
  public void addGoalListener(GoalListener listener) {
    goalListeners.add(listener);
  }

  /**
   * Remove the specified goalListener (if it was added)
   */
  public void removeGoalListener(GoalListener listener) {
    goalListeners.remove(listener);
  }

  /**
   * Notify all installed GoalListeners. (Observer pattern)
   */
  private void notifyGoalListeners() {
    for (GoalListener listener : goalListeners) {
      listener.goalAchieved();
    }
  }

  /**
   * @return true if the goal condition has been met, meaning the player has achieved
   * the goal in this level
   */
  protected abstract boolean goalAchieved();

  /**
   * This is what gets called when the player presses the "Reset" button in the UI. It calls
   * rf.untransformVelocity(obj) for all the objects in the level where rf is the reference
   * frame being used.
   */
  public void reset() {
    for (PhysicalObject obj : simulationObjects) {
      obj.reset();
    }
    for (PhysicalObject obj : simulationObjects) {
      referenceFrame.untransformVelocity(obj);
    }
    running = false;
  }

  /**
   * Indicates that we want to view the level in the specified reference frame.
   */
  public void setFrame(ReferenceFrame referenceFrame) {
    if (referenceFrame.vy != 0) {
      throw new UnsupportedOperationException("Reference frames with a vertical component are not yet supported.");
    }
    if (running) {
      throw new UnsupportedOperationException("You can't switch reference frames while the simulation is running.");
    }

    this.referenceFrame = referenceFrame;
  }

  public void paint(Graphics g) {
    for (PhysicalObject obj : simulationObjects) {
      obj.paint(g);
    }
  }

  public JPanel getControlPanel() {
    return controlPanel;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return name;
  }

  public static List<RelativityLevel> levels = Arrays.asList(
      new LightClocksOnTrainLevel(),
      new SpaceshipInTunnelLevel()
  );
}
