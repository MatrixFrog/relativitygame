package phcs.objects;

import static util.swingutils.SwingUtils.useDefaultLookAndFeel;
import static util.swingutils.SwingUtils.useDialogExceptionHandler;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SpaceshipTest extends JFrame implements ActionListener {

  private Spaceship spaceship;
  private Timer timer = new Timer(5, this);

  private JPanel panel = new JPanel() {
    @Override
    public void paint(Graphics g) {
      super.paint(g);
      spaceship.paint(g);
    }
  };

  public SpaceshipTest(Spaceship spaceship) {
    setTitle(getClass().getSimpleName());
    this.spaceship = spaceship;

    setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
    add(panel);

    setSize(800, 600);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    setVisible(true);
    timer.start();
  }

  public static void main(String[] args) {
    useDefaultLookAndFeel();
    useDialogExceptionHandler();
    new SpaceshipTest(new Spaceship());
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == timer) {
      spaceship.update();
      repaint();
    }
  }

}
