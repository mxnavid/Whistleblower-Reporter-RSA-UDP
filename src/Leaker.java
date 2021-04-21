public class Leaker {
    public static void main(String[] args) {
        String rHost = args[0];
        int rPort = Integer.parseInt(args[1]);

        String lHost = args[2];
        int lPost = Integer.parseInt(args[3]);
//        FileInputStream publicKeyFile
        String message = args[4];
        if (args.length < 6) argsError();


    }
    public static void argsError(){
        System.err.println("java Leaker <rhost> <rport> <lhost> <lport> <publickeyfile> <message>");
        System.exit(1);
    }
}
