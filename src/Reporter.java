
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Scanner;

public class Reporter {
    public static void main(String[] args) throws SocketException {
        if (args.length < 3 ) argsError();

        String rHost = args[0];

        int rPort = Integer.parseInt(args[1]);
        DatagramSocket mailbox = null;


        try{
            mailbox = new DatagramSocket(new InetSocketAddress(rHost, rPort));
        }catch (SocketException e){
            e.printStackTrace();
            System.err.printf("Error in accessing the specific address %s:%d", rHost, rPort);
            argsError();
            System.exit(1);
        }


        Scanner sc = null;
        File privateKeyFile = new File(args[2]);
        try{
            sc = new Scanner(privateKeyFile);
        }catch (FileNotFoundException e){
            e.printStackTrace();
            System.err.printf("Private key not found, please try again: %s.\n", args[2]);
            argsError();
        }

        //convert to big integer
        String exponentStr = sc.nextLine();
        String modStr = sc.nextLine();

        BigInteger exponent = new BigInteger(exponentStr);
        BigInteger mod      = new BigInteger(modStr);


















//        FileInputStream



    }
    public static void argsError(){
        System.err.println("Usage: java Reporter <rhost> <rport> <privatekeyfile>");
        System.exit(1);
    }


}
