package phcs;

import static phcs.Relativity.inverseGamma;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import phcs.objects.LightClock;
import phcs.objects.Spaceship;
import phcs.objects.Tunnel;

/**
 * An instance of this class represents one single "level" or "puzzle" to be solved.
 */
// TODO encode "goal" condition into the level
public class RelativityLevel {

  private List<PhysicalObject> simulationObjects = new ArrayList<PhysicalObject>();
  private ReferenceFrame referenceFrame = ReferenceFrame.DEFAULT_FRAME;
  private boolean running;

  public void addSimulationObject(PhysicalObject obj) {
    simulationObjects.add(obj);
  }

  public List<PhysicalObject> getSimulationObjects() {
    return simulationObjects;
  }

  /**
   * This is what gets called when the player presses the "Go" button in the UI
   */
  public void go() {
    running = true;
    for (PhysicalObject obj : simulationObjects) {
      referenceFrame.transformVelocity(obj);
    }
    for (PhysicalObject obj : simulationObjects) {
      obj.go();
    }
  }

  public void update() {
    for (PhysicalObject obj : simulationObjects) {
      obj.update();
    }
  }

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

  public static RelativityLevel createLightClocksOnTrainLevel() {
    /*
    private String instructions = "Instructions: The light clock on the left is stationary,\n" +
        "and it takes 4 seconds for the pulse of light to make a complete cycle.\n" +
        "You can adjust the speed of the light clock on the right using the slider, which\n" +
        "shows the clock's speed as a percentage of the speed of light.\n" +
        "Goal: Set the speed so that the light clock on the right makes a complete cycle in 5 seconds.";
     */
    RelativityLevel level1 = new RelativityLevel();

    PhysicalObject stationaryLightClock = new LightClock(430, 30);
    stationaryLightClock.setName("LC1");
    level1.addSimulationObject(stationaryLightClock);

    PhysicalObject movingLightClock = new LightClock(500, 30);
    movingLightClock.setName("LC2");
    movingLightClock.setVelocityEditable(true);
    level1.addSimulationObject(movingLightClock);

    return level1;
  }

  public static RelativityLevel createSpaceshipInTunnelLevel() {
    RelativityLevel level2 = new RelativityLevel();

    PhysicalObject tunnel = new Tunnel(500, 285, 50, 30, 0, 0);
    level2.addSimulationObject(tunnel);

    PhysicalObject spaceship = new Spaceship(30, 290, 100, 20, inverseGamma(2), 0);
    level2.addSimulationObject(spaceship);

    return level2;
  }

  public void paint(Graphics g) {
    for (PhysicalObject obj : simulationObjects) {
      obj.paint(g);
    }
  }

}
