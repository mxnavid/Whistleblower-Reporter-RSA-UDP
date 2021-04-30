//******************************************************************************
//
// File:    Reporter.java
// Package: edu.rit.pj2
// Unit:    Class Reporter
//
// Copyright (c) 2021 - 2022 Navid Nafiuzzaman
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//
// This Java source file is part of the Parallel Java 2 Library ("PJ2"). PJ2 is
// free software; you can redistribute it and/or modify it under the terms of
// the GNU General Public License as published by the Free Software Foundation;
// either version 3 of the License, or (at your option) any later version.
//******************************************************************************

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Scanner;

/**
 * Class Reporter is the reporter main program which will receive encrypted
 * messages from the leakers and try to decrypt them using the private
 * key file and show the output as the standard output.
 *
 * Usage: java Reporter <rhost> <rport> <privatekeyfile>
 * @author Navid Nafiuzzaman
 * @version Apr 29, 2021
 */
public class Reporter {
    //Data Members
    private static BigInteger d;        // decimal val of the private key
    private static BigInteger n;        // modulus val of the private key
    private static DatagramSocket mailbox = null;   // mailbox

    /**
     * Main program taking multiple user inputs and starting the reporter
     * program to decrypt the received messages from user.
     * @param args User arguments from the user.
     */
    public static void main(String[] args) {
        if (args.length < 3 ) argMissing();     // if less than 3 args, there is error

        String rHost = args[0];                 // reporter host
        int rPort = Integer.parseInt(args[1]);

        // creates the mailbox
        try{
            mailbox = new DatagramSocket(new InetSocketAddress(rHost, rPort));
        }catch (SocketException e){
            e.printStackTrace();
            System.err.printf("Error in accessing the specific address %s:%d \n", rHost, rPort);
            argsError();
            System.exit(1);
        }

        // handling private key File
        Scanner sc = null;                              // creates a scanner
        File privateKeyFile = new File(args[2]);        //creates a file
        try{
            sc = new Scanner(privateKeyFile);
        }catch (FileNotFoundException e){
            System.err.println("Error: nosuchfile (No such file or directory)");
            System.exit(1);
        }

        //convert to big integer
        String dStr = null;                             // create a decimal str
        String nStr = null;                             // creates a modulus str
        try{
            dStr = sc.nextLine();
        }catch (Exception e){
            System.err.println("Decimal text missing");
            System.exit(1);
        }

        // tries to read the string from the private key file
        try{
            nStr = sc.nextLine();

        }catch (Exception e){
            System.err.println("Modulus text missing");
            System.exit(1);
        }

        try{
            d = new BigInteger(dStr);

        } catch (NumberFormatException e){
            System.err.println("Couldn't read decimal value in private key file");
            System.exit(1);
        }

        // converts the strings to BigInteger decimal and modulus.
        try{
            n = new BigInteger(nStr);
        } catch (NumberFormatException e){
            System.err.println("Couldn't read modulus value in private key file");
            System.exit(1);
        }

        OAEP oaep = new OAEP();                     // creates the OAEP object from pj2
        LeakerProxy proxy = new LeakerProxy(mailbox, d, n, oaep);
        proxy.startProxy();                         // starts the threaded proxy prog
    }


    /**
     * Function showing the missing arguments error
     */
    public static void argMissing(){
        System.err.println("Missing arguments");
        argsError();
    }


    /**
     * Function showing the usage error message
     */
    public static void argsError(){
        System.err.println("Usage: java Reporter <rhost> <rport> <privatekeyfile>");
        System.exit(1);
    }

}
