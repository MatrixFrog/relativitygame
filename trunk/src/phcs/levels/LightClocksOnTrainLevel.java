package phcs.levels;

import javax.swing.BorderFactory;

import phcs.RelativityLevel;
import phcs.gui.VelocitySlider;
import phcs.objects.LightClock;
import util.swingutils.RecursiveEnablePanel;

public class LightClocksOnTrainLevel extends RelativityLevel {
  public LightClocksOnTrainLevel() {
    setName("Level 1: A light clock on a train.");

    /*
    private String instructions = "Instructions: The light clock on the left is stationary,\n" +
        "and it takes 4 seconds for the pulse of light to make a complete cycle.\n" +
        "You can adjust the speed of the light clock on the right using the slider, which\n" +
        "shows the clock's speed as a percentage of the speed of light.\n" +
        "Goal: Set the speed so that the light clock on the right makes a complete cycle in 5 seconds.";
     */

    LightClock stationaryLightClock = new LightClock(430, 30);
    stationaryLightClock.setName("LC1");
    this.addSimulationObject(stationaryLightClock);

    LightClock movingLightClock = new LightClock(500, 30);
    movingLightClock.setName("LC2");
    this.addSimulationObject(movingLightClock);

    this.controlPanel = new RecursiveEnablePanel();
    this.controlPanel.add(new VelocitySlider(movingLightClock));
    this.controlPanel.setBorder(BorderFactory.createTitledBorder(movingLightClock.getName()));
  }
}
