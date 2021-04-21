public class Reporter {
    public static void main(String[] args) {
        String rHost = args[0];
        int rPort = Integer.parseInt(args[1]);
//        FileInputStream
        if (args.length < 3 ) argsError();

    }
    public static void argsError(){
        System.err.println("Usage: java Reporter <rhost> <rport> <privatekeyfile>");
        System.exit(1);
    }


}
