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
import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Scanner;


public class Reporter {
    private static BigInteger d;
    private static BigInteger n;
    private static Thread reporterThread;

    private static DatagramSocket mailbox = null;

    public static void main(String[] args) throws SocketException {
        if (args.length < 3 ) argMissing();

        String rHost = args[0];
        int rPort = Integer.parseInt(args[1]);

        try{
            mailbox = new DatagramSocket(new InetSocketAddress(rHost, rPort));
        }catch (SocketException e){
            e.printStackTrace();
            System.err.printf("Error in accessing the specific address %s:%d \n", rHost, rPort);
            argsError();
            System.exit(1);
        }

        // handling privatekeyFile
        Scanner sc = null;
        File privateKeyFile = new File(args[2]);
        try{
            sc = new Scanner(privateKeyFile);
        }catch (FileNotFoundException e){
            e.printStackTrace();
            System.err.println("Error: nosuchfile (No such file or directory)\n");
            System.exit(1);
        }

        // TODO: handle if modulus or exponent missing
        //convert to big integer
        String dStr = sc.nextLine();
        String nStr = sc.nextLine();



        d = new BigInteger(dStr);
        n = new BigInteger(nStr);
        OAEP oaep = new OAEP();

        Thread thread = new Thread("New Thread"){
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
        };
        thread.start();


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

    public static void argMissing(){
        System.err.println("Missing arguments");
        argsError();
    }

    public static void argsError(){
        System.err.println("Usage: java Reporter <rhost> <rport> <privatekeyfile>");
        System.exit(1);
    }


}
