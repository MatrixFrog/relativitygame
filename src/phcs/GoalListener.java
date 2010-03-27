package phcs;

/**
 * Implementing the Observer pattern. See {@link RelativityLevel#addGoalListener(GoalListener)}
 */
public interface GoalListener {

  /**
   * The method in the listener that gets called when a level's goal is achieved
   */
  void goalAchieved();

}
