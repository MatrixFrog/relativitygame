package phcs;

/**
 * Simple logging
 */
public class Trace {

  public static boolean TRACE = false;

  public static void trace(Object o) {
    if (TRACE) {
      System.out.println(o);
    }
  }

}
