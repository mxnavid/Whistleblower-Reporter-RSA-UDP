import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class ReporterProxy implements LeakerListener{
    private DatagramSocket mailbox;
    private InetSocketAddress targetAddress;

    public ReporterProxy(DatagramSocket mailbox, InetSocketAddress targetAddress) {
        this.mailbox = mailbox;
        this.targetAddress = targetAddress;
    }

    @Override
    public void reportMsg(byte[] msgBuffer) throws IOException {
        mailbox.send(new DatagramPacket(msgBuffer, msgBuffer.length, targetAddress));
    }
}
