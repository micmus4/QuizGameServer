import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * TODO: Describe this class (The first line - until the first dot - will interpret as the brief description).
 */
public class ConnectionThread extends Thread
{
    private final Socket socket;

    public final ArrayList< Question > questions;

    private int points = 0;

    private boolean pause = Boolean.FALSE;

    public ConnectionThread( Socket aSocket, ArrayList< Question > aQuestions )
    {
        socket = aSocket;
        questions = aQuestions;
    }

    @Override
    public void run()
    {
        try
        {
            BufferedReader clientReader = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
            PrintWriter clientWriter = new PrintWriter( socket.getOutputStream(), true );

            System.out.println( clientReader.readLine() );
            clientWriter.println( "Server confirms your connection!" );

            try
            {
                // wait for server to start the game for all players at the same time.
                Thread.sleep( Integer.MAX_VALUE );
            }
            catch ( InterruptedException e )
            {
                // start game.
            }

            int pauseCount = 0; // if it raises to 3, player has to pause 1 question.
            for( Question question : questions )
            {
                if( pauseCount == 3 )
                {
                    clientWriter.println( "Question: " + question.getContent() + " ||| YOU HAVE TO PAUSE THIS QUESTION" );
                    pauseCount = 0;
                }
                else
                {
                    clientWriter.println( "Question: " + question.getContent() );
                    String clientAnswer = clientReader.readLine();
                    if( clientAnswer.equalsIgnoreCase( question.getCorrectAnswer() ) )
                    {
                        clientWriter.println( "Correct answer!" );
                        points += 1;
                        pauseCount = 0;
                    }
                    else
                    {
                        clientWriter.println( "Wrong answer..." );
                        points -= 2;
                        pauseCount++;
                    }
                }
                }
            clientWriter.println( "You've got " + points + " points." );
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                socket.close();
            }
            catch( IOException e )
            {
                e.printStackTrace();
            }
        }
    }
}
