package phcs.levels;

import static phcs.PhysicalObject.inverseGamma;
import static phcs.Trace.TRACE;
import phcs.CollisionException;
import phcs.RelativityLevel;
import phcs.gui.TunnelController;
import phcs.objects.HorizontalGrid;
import phcs.objects.Spaceship;
import phcs.objects.Tunnel;
import util.swingutils.RecursiveEnablePanel;

/**
 * This level enacts the famous thought experiment known as the "Ladder Paradox"
 *
 * @see http://en.wikipedia.org/wiki/Ladder_paradox
 */
// TODO (?) split this into two separate challenges: one to figure out the speed, given that gamma=2 and another to figure out when the gates need to be toggled
public class SpaceshipInTunnelLevel extends RelativityLevel {

  /** The spaceship actually goes slightly faster than inverseGamma(2)
   *  so that it is contained completely inside the tunnel.
   */
  private double epsilon = 0.0001;


  private Spaceship spaceship = new Spaceship(300, 290, 100, 20, inverseGamma(2)+epsilon, 0);
  private Tunnel tunnel = new Tunnel(600, 285, 50, 30, 0, 0);

  public SpaceshipInTunnelLevel() {
    setName("Level 2: Spaceship in a Tunnel");

    instructions = "A spaceship of rest length 100 travels through a tunnel of \n" +
    		"rest length 50. The spaceship travels at a speed of v/c = sqrt(3)/2 \n" +
    		"which means that it contracts to a length of 50, when viewed in the rest frame of " +
    		"the tunnel. "; // TODO add more details

    tunnel.setDefaultGateStates(true, false);
    this.addSimulationObject(tunnel);
    this.addSimulationObject(spaceship);
    this.addGrid(new HorizontalGrid(0, 345, 800, 10));

    this.controlPanel = new RecursiveEnablePanel();
    TunnelController controller = new TunnelController();
    tunnel.setController(controller);
    this.controlPanel.add(controller);

    if (TRACE) {
      /* Solution:
       * The spaceship travels a distance of 300 from when it starts moving until when it is fully
       * inside the tunnel. Its Lorentz factor (gamma) is 2, so its speed must be sqrt(3)/2.
       * It arrives at t = 300 / (sqrt(3)/2) which is about 346.41.
       * Both gates should be toggled at that time (in the tunnel's rest frame).
       */
      controller.addEvent(controller.new GateEvent(true, 346.41));
      controller.addEvent(controller.new GateEvent(false, 346.41));
    }
  }

  @Override
  public void update() {
    super.update();
    double leftGatePosition = tunnel.getX();
    double rightGatePosition = tunnel.getX() + tunnel.getWidth();
    if (spaceship.getX() < leftGatePosition &&
        leftGatePosition < spaceship.getX() + spaceship.getWidth() &&
        !tunnel.isLeftGateOpen()) {
      throw new CollisionException(spaceship, tunnel);
    }
    if (spaceship.getX() < rightGatePosition &&
        rightGatePosition < spaceship.getX() + spaceship.getWidth() &&
        !tunnel.isRightGateOpen()) {
      throw new CollisionException(spaceship, tunnel);
    }
  }

  @Override
  protected boolean goalAchieved() {
    return spaceship.getX() > tunnel.getX() + tunnel.getWidth() + 25;
  }
}
