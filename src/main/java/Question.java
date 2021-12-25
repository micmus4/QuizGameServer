/**
 * TODO: Describe this class (The first line - until the first dot - will interpret as the brief description).
 */
public class Question
{
    private final String content;

    private final String  correctAnswer;

    public Question( String aContent, String aCorrectAnswer )
    {
        content = aContent;
        correctAnswer = aCorrectAnswer;
    }

    public String getContent()
    {
        return content;
    }

    public String getCorrectAnswer()
    {
        return correctAnswer;
    }
}
