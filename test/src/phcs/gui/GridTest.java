package phcs.gui;

import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import phcs.objects.HorizontalGrid;
import phcs.objects.VerticalGrid;
import util.swingutils.SwingUtils;

public class GridTest extends JPanel {

  private HorizontalGrid horizontalGrid = new HorizontalGrid();
  private VerticalGrid verticalGrid = new VerticalGrid();

  @Override
  public void paint(Graphics g) {
    horizontalGrid.paint(g);
    verticalGrid.paint(g);
  }

  public GridTest() {
    JFrame frame = new JFrame();
    frame.add(this);
    frame.setSize(800, 600);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }

  public static void main(String[] args) {
    SwingUtils.useDefaultLookAndFeel();
    new GridTest();
  }

}
