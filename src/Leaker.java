public class Leaker {
    private String rHost;
    private int rPort;
    private String lHost;
    private int lPort;



    public static void main(String[] args) {
        if (args.length < 6) argsError();
        String rHost = args[0];

        int rPort = Integer.parseInt(args[1]);

        String lHost = args[2];
        int lPost = Integer.parseInt(args[3]);
//        FileInputStream publicKeyFile
        String message = args[4];

    }

    public static void argsError(){
        System.err.println("java Leaker <rhost> <rport> <lhost> <lport> <publickeyfile> <message>");
        System.exit(1);
    }
}
