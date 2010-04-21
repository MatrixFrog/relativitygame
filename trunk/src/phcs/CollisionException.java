package phcs;

public class CollisionException extends RuntimeException {
  public CollisionException(PhysicalObject obj1, PhysicalObject obj2) {
    super(obj1 + " has collided with " + obj2 + "!");
  }
}
