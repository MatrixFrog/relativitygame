package phcs;

/**
 * Simple logging
 */
public class Trace {

  public static boolean TRACE = true;

  public static void trace(Object o) {
    if (TRACE) {
      System.out.println(o);
    }
  }

}
