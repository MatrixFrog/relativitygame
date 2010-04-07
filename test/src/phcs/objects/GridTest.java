package phcs.objects;

import static util.swingutils.SwingUtils.useDefaultLookAndFeel;
import static util.swingutils.SwingUtils.useDialogExceptionHandler;

import java.awt.Graphics;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GridTest extends JFrame {

  private Grid.HorizontalGrid grid = new Grid.HorizontalGrid();

  private JPanel panel = new JPanel() {
    @Override
    public void paint(Graphics g) {
      grid.paint(g);
    }
  };

  public GridTest() {
    setTitle(getClass().getSimpleName());

    setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
    add(panel);

    setSize(800, 600);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    setVisible(true);
  }

  public static void main(String[] args) {
    useDefaultLookAndFeel();
    useDialogExceptionHandler();
    new GridTest();
  }
}
