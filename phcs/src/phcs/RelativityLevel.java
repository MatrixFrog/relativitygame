package phcs;

import java.util.ArrayList;
import java.util.List;

import phcs.objects.LightClock;
import phcs.objects.PhysicalObject;

/**
 * An instance of this class represents one single "level" or "puzzle" to be solved.
 */
// TODO encode "goal" condition
public class RelativityLevel {

  private List<PhysicalObject> simulationObjects = new ArrayList<PhysicalObject>();

  public void addSimulationObject(PhysicalObject obj) {
    simulationObjects.add(obj);
  }

  public static RelativityLevel getLevel1() {
    /*
    private String instructions = "Instructions: The light clock on the left is stationary,\n" +
        "and it takes 4 seconds for the pulse of light to make a complete cycle.\n" +
        "You can adjust the speed of the light clock on the right using the slider, which\n" +
        "shows the clock's speed as a percentage of the speed of light.\n" +
        "Goal: Set the speed so that the light clock on the right makes a complete cycle in 5 seconds.";
    */
    RelativityLevel level1 = new RelativityLevel();

    PhysicalObject stationaryLightClock = new LightClock(30, 30);
    stationaryLightClock.setName("LC1");
    level1.addSimulationObject(stationaryLightClock);

    PhysicalObject movingLightClock = new LightClock(100, 30);
    movingLightClock.setName("LC2");
    movingLightClock.setVelocityEditable(true);
    level1.addSimulationObject(movingLightClock);

    return level1;
  }

  public List<PhysicalObject> getSimulationObjects() {
    return simulationObjects;
  }

  /**
   * This is what gets called when the player presses the "Go" button in the UI
   */
  public void go() {
    for (PhysicalObject obj : simulationObjects) {
      obj.go();
    }
  }

}
