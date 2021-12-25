import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;

/**
 * TODO: Describe this class (The first line - until the first dot - will interpret as the brief description).
 */
public class TCPServer
{
    private static int MAX_WAIT_FOR_PLAYERS_IN_SECONDS;

    private static int PORT;

    private static ArrayList< Question > questions;

    public static ArrayList< ConnectionThread > connections;

    public static void main( String[] aArgs )
    {
        readProperties();
        startTCPServer( PORT );
    }

    private static void startTCPServer( int aPort )
    {
        try( ServerSocket serverSocket = new ServerSocket( aPort ) )
        {
            acceptPlayers( serverSocket );
            while( true )
            {

            }
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    private static void acceptPlayers( ServerSocket aServerSocket ) throws IOException
    {
        int playersCount = 0;
        connections = new ArrayList<>();
        while( true )
        {
            try
            {
                ConnectionThread connection = new ConnectionThread( aServerSocket.accept(), questions );
                connections.add( connection );
                connection.start();

                playersCount++;
                if( playersCount == 2 )
                {
                    aServerSocket.setSoTimeout( MAX_WAIT_FOR_PLAYERS_IN_SECONDS * 1000 );
                    System.out.println( "Two players are connected, server will stop accepting connections after 30 seconds." );
                }
                else if( playersCount == 4 )
                {
                    System.out.println( "Four players are connected, server stops accepting connections." );
                    connections.forEach( Thread::interrupt );
                    break;
                }
            }
            catch ( SocketTimeoutException e )
            {
                // if there aren't 4 players connected, server will stop accepting players after 30 seconds passes
                // starting from moment when second player connects.
                connections.forEach( Thread::interrupt );
                break;
            }
        }
        System.out.println( "Server has accepted " + playersCount + " players, the game is starting..." );
        try
        {
            Thread.sleep( 3000 );
        }
        catch ( InterruptedException e )
        {
            e.printStackTrace();
        }
    }

    private static void readProperties()
    {
        Properties prop = new Properties();
        try
        {
            prop.load( TCPServer.class.getClassLoader().getResourceAsStream("question.properties" ) );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }

        MAX_WAIT_FOR_PLAYERS_IN_SECONDS = Integer.parseInt( prop.getProperty( "waitForPlayersInSeconds" ) );
        PORT = Integer.parseInt( prop.getProperty( "port" ) );
        questions = new ArrayList<>();
        for( int i = 1; i <= 5; i++ )
        {
            String questionId = "question" + i;
            String[] questionBreakdown = prop.getProperty( questionId ).split( ";" );
            questions.add( new Question( questionBreakdown[ 0 ], questionBreakdown[ 1 ] ) );
        }
        Collections.shuffle( questions );
    }
}


