package phcs;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;
import static phcs.PhysicalObject.gamma;
import static phcs.PhysicalObject.inverseGamma;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class PhysicalObjectTest {

  @DataPoints
  public static Double[] data() {
    List<Double> list = new ArrayList<Double>();
    for (double d = 0; d < 1; d+= 0.01) {
      list.add(d);
    }
    return list.toArray(new Double[list.size()]);
  }

  /**
   * Confirming that inverseGamma() is in fact the inverse of gamma()
   */
  @Theory
  public void testGamma(double d) {
    assertEquals(d, inverseGamma(gamma(d)), 0.00000001);
  }

  /**
   * Confirming that gamma() is a monotonically increasing function
   */
  @Theory
  public void testGammaIncreasing(double smaller, double larger) {
    assumeTrue(smaller < larger);
    assertTrue(gamma(smaller) < gamma(larger));
  }

  @Test
  public void testGammaZeroIsOne() {
    assertThat(gamma(0), is(1.0));
  }

}
