package phcs.levels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;

import phcs.LightClockListener;
import phcs.Relativity;
import phcs.RelativityLevel;
import phcs.gui.VelocitySlider;
import phcs.objects.LightClock;
import util.swingutils.RecursiveEnablePanel;

/**
 * The famous Einstein thought experiment involving two light clocks -- one on a train moving
 * at a relativistic speed, and one stationary on the ground next to it.
 */
// Solution: v = inverseGamma(5.0/4) = 0.6
public class LightClocksOnTrainLevel extends RelativityLevel implements LightClockListener {

  private boolean goalAchieved = false;

  public LightClocksOnTrainLevel() {
    setName("Level 1: A light clock on a train.");

    instructions = "Instructions: The light clock on the left is stationary, \n" +
      "and it takes 4 seconds for the pulse of light to make a complete cycle. \n" +
      "You can adjust the speed of the light clock on the right using the slider, which \n" +
      "shows the clock's speed as a percentage of the speed of light. Goal: Adjust the speed \n" +
      "so that the light clock on the right makes a complete cycle in 5 seconds when viewed \n" +
      "in the rest frame of the light clock on the left.";

    LightClock stationaryLightClock = new LightClock(430, 30);
    stationaryLightClock.setName("LC1");
    this.addSimulationObject(stationaryLightClock);

    LightClock movingLightClock = new LightClock(500, 30);
    movingLightClock.setName("LC2");
    movingLightClock.addLightClockListener(this);
    this.addSimulationObject(movingLightClock);

    this.controlPanel = new RecursiveEnablePanel();
    this.controlPanel.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    this.controlPanel.add(new VelocitySlider(movingLightClock), gbc);
    this.controlPanel.setBorder(BorderFactory.createTitledBorder(movingLightClock.getName()));
  }

  @Override
  public void reset() {
    super.reset();
    goalAchieved = false;
  }

  @Override
  protected boolean goalAchieved() {
    return goalAchieved;
  }

  public void cycleCompleted(int counter) {
    if (Relativity.DEBUG) {
      System.out.println("counter: " + counter);
    }
    if (499 <= counter && counter < 502) {
      goalAchieved = true;
    }
  }
}
