import java.io.IOException;

public interface LeakerListener{
    public void reportMsg(byte[] msgBuffer ) throws IOException;
}