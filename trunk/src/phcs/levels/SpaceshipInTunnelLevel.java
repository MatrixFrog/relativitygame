package phcs.levels;

import static phcs.PhysicalObject.inverseGamma;
import phcs.RelativityLevel;
import phcs.gui.TunnelController;
import phcs.objects.Spaceship;
import phcs.objects.Tunnel;
import util.swingutils.RecursiveEnablePanel;

public class SpaceshipInTunnelLevel extends RelativityLevel {
  public SpaceshipInTunnelLevel() {
    setName("Level 2: Spaceship in a Tunnel");

    Tunnel tunnel = new Tunnel(500, 285, 50, 30, 0, 0);
    this.addSimulationObject(tunnel);

    Spaceship spaceship = new Spaceship(30, 290, 100, 20, inverseGamma(2), 0);
    this.addSimulationObject(spaceship);

    this.controlPanel = new RecursiveEnablePanel();
    TunnelController controller = new TunnelController();
    tunnel.setController(controller);
    this.controlPanel.add(controller);
  }
}
