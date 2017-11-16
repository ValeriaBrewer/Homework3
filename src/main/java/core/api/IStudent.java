package core.api;

/**
 * Created by Vincent on 21/2/2017.
 */
public interface IStudent {

    /**
     * Register student {@code studentName} for class {@code className} in year {@code year},
     * provided this class exists and has not met its enrollment capacity.
     *
     * @param className The name of the class to register for
     * @param year The year in which the class is taught
     */
    void registerForClass(String studentName, String className, int year);

    /**
     * Drop class {@code className} in year {@code year} for student {@code studentName},
     * provided the student is registered and the class has not ended.
     *
     * @param studentName Name of the student to drop class for
     * @param className Name of the class to drop
     * @param year Year in which class is taught
     */
    void dropClass(String studentName, String className, int year);

    /**
     * Submit {@code studentName}'s homework solution {@code answerString} for homework {@code homeworkName} of class {@code className},
     * provided homework exists, student is registered and the class is taught in the current year
     *
     * @param studentName Name of the student
     * @param homeworkName Name of the homework
     * @param answerString Solution to the homework
     * @param className Name of class
     * @param year Year in which class is taught
     */
    void submitHomework(String studentName, String homeworkName, String answerString, String className, int year);


    // Getters for testing purposes
    /**
     * @return Whether student {@code studentName} is registered for this class
     */
    boolean isRegisteredFor(String studentName, String className, int year);

    /**
     * @return Whether student {@code studentName} has submitted homework {@code homeworkName} for this class
     */
    boolean hasSubmitted(String studentName, String homeworkName, String className, int year);
}
