package phcs;

import static org.junit.Assert.assertEquals;
import static phcs.PhysicalObject.gamma;
import static phcs.PhysicalObject.inverseGamma;

import org.junit.Test;
public class PhysicalObjectTest {

  /**
   * Just a test to confirm that inverseGamma() is in fact the inverse of gamma()
   */
  @Test
  public void testGamma() {
    assertEquals(1, gamma(0), 0);
    for (double d = 0; d < 1; d += 0.001) {
      assertEquals(d, inverseGamma(gamma(d)), 0.00000001);
    }
  }

}
