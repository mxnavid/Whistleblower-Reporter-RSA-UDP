import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class LeakerProxy {
    private final DatagramSocket mailbox;
    private final BigInteger d;
    private final BigInteger n;
    private final OAEP oaep;

    public LeakerProxy(DatagramSocket mailbox, BigInteger d, BigInteger n, OAEP oaep) {
        this.mailbox = mailbox;
        this.d = d;
        this.n = n;
        this.oaep = oaep;
    }


    public void startProxy (){
        new ReaderThread().start();
    }

    public class ReaderThread extends Thread{
        public void run(){
            byte[] buffer = new byte[260];
            while (true){
                try{
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    mailbox.receive(packet);
                    byte[] incomingBuffer = packet.getData();
                    BigInteger msg = new BigInteger(incomingBuffer, 0, packet.getLength());
                    String output = decryption(d, n, msg, oaep);
                    System.out.println(output);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private static String decryption(BigInteger d,
                                     BigInteger n,
                                     BigInteger msg,
                                     OAEP oaep){
        try{
            String decodedMsg = oaep.decode(msg.modPow(d, n));
            return decodedMsg;
        } catch (IllegalArgumentException e){
            e.printStackTrace();
            System.err.println("Private key cannot decrypt the message sent using public key");
            System.exit(1);
        }
        return "";
    }
}
