import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * TODO: Describe this class (The first line - until the first dot - will interpret as the brief description).
 */
public class TCPClient
{
    public static void main( String[] args )
    {
        connectNewClientToServer();
    }

    public static void connectNewClientToServer()
    {
        try( Socket clientSocket = new Socket( "localhost", 1234 ) )
        {
            Scanner scanner = new Scanner( System.in );
            BufferedReader serverReader = new BufferedReader( new InputStreamReader( clientSocket.getInputStream() ) );
            PrintWriter serverWriter = new PrintWriter( clientSocket.getOutputStream(), true );

            establishHandshakeWithServer( scanner, serverReader, serverWriter );
            for(int i = 0; i < 5; i++)
            {
                String questionOrPause = serverReader.readLine();
                if( questionOrPause.contains( "PAUSE" ) )
                {
                    System.out.println( questionOrPause );
                    continue;
                }
                System.out.println( questionOrPause );
                System.out.print( "ANSWER: " );
                String answer = scanner.nextLine();
                serverWriter.println( answer );
                System.out.println( serverReader.readLine() );
            }
            System.out.println( serverReader.readLine() );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    public static void establishHandshakeWithServer( Scanner aScanner, BufferedReader aReader, PrintWriter aWriter )
            throws IOException
    {
        System.out.print( "Introduce yourself: " );
        String playerName = aScanner.nextLine();
        aWriter.println( "Player " + playerName + " has connected to the server." );
        System.out.println( aReader.readLine() );
    }
}
