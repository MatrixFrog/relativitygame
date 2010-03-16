package phcs;

import static phcs.PhysicalObject.inverseGamma;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import phcs.gui.TunnelController;
import phcs.gui.VelocitySlider;
import phcs.objects.LightClock;
import phcs.objects.Spaceship;
import phcs.objects.Tunnel;
import util.swingutils.RecursiveEnablePanel;

/**
 * An instance of this class represents one single "level" or "puzzle" to be solved.
 */
// TODO encode "goal" condition into the level
public class RelativityLevel {

  private List<PhysicalObject> simulationObjects = new ArrayList<PhysicalObject>();
  private ReferenceFrame referenceFrame = ReferenceFrame.DEFAULT_FRAME;
  private boolean running;
  private JPanel controlPanel;
  private String name;

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
    for (PhysicalObject obj : simulationObjects) {
      obj.go();
    }
  }

  public void update() {
    for (PhysicalObject obj : simulationObjects) {
      obj.update();
    }
  }

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
    System.out.println(this+".paint()");
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
      createLightClocksOnTrainLevel(), createSpaceshipInTunnelLevel()
  );

  public static RelativityLevel createLightClocksOnTrainLevel() {
    /*
    private String instructions = "Instructions: The light clock on the left is stationary,\n" +
        "and it takes 4 seconds for the pulse of light to make a complete cycle.\n" +
        "You can adjust the speed of the light clock on the right using the slider, which\n" +
        "shows the clock's speed as a percentage of the speed of light.\n" +
        "Goal: Set the speed so that the light clock on the right makes a complete cycle in 5 seconds.";
     */
    RelativityLevel level = new RelativityLevel();

    PhysicalObject stationaryLightClock = new LightClock(430, 30);
    stationaryLightClock.setName("LC1");
    level.addSimulationObject(stationaryLightClock);

    PhysicalObject movingLightClock = new LightClock(500, 30);
    movingLightClock.setName("LC2");
    level.addSimulationObject(movingLightClock);

    level.controlPanel = new JPanel();
    level.controlPanel.add(new VelocitySlider(movingLightClock));
    level.controlPanel.setBorder(BorderFactory.createTitledBorder(movingLightClock.getName()));

    level.setName("Level 1");

    return level;
  }

  public static RelativityLevel createSpaceshipInTunnelLevel() {
    RelativityLevel level = new RelativityLevel();

    Tunnel tunnel = new Tunnel(500, 285, 50, 30, 0, 0);
    level.addSimulationObject(tunnel);

    Spaceship spaceship = new Spaceship(30, 290, 100, 20, inverseGamma(2), 0);
    level.addSimulationObject(spaceship);

    level.controlPanel = new RecursiveEnablePanel();
    TunnelController controller = new TunnelController();
    tunnel.setController(controller);
    level.controlPanel.add(controller);

    level.setName("Level 2");

    return level;
  }

}
