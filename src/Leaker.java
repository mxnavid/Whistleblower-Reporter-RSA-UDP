//******************************************************************************
//
// File:    Leaker.java
// Package: edu.rit.pj2
// Unit:    Class Leaker
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
import java.net.*;
import java.util.Arrays;
import java.util.Scanner;

public class Leaker {
    public static void main(String[] args) {
        if (args.length < 6) argMissing();
        String rHost = args[0];
        int rPort = Integer.parseInt(args[1]);
        InetSocketAddress reportAdd = null;
        try{
            reportAdd = new InetSocketAddress(rHost, rPort);

        } catch (IllegalArgumentException e){
            e.printStackTrace();
            System.err.printf("Illegal reporters address %s:$d\n", rHost, rPort);
            argsError();
        }


        String lHost = args[2];
        int lPort = Integer.parseInt(args[3]);
        DatagramSocket socket = null;
        try{
            socket = new DatagramSocket(new InetSocketAddress(lHost, lPort));
        } catch (SocketException e){
            e.printStackTrace();
            System.err.printf("Can't bind with the given address %s:%d \n", lHost, lPort);
            argsError();
        }

//        FileInputStream publicKeyFile
        Scanner sc = null;
        File publicKeyFile = new File(args[4]);
        try{
            sc = new Scanner(publicKeyFile);
        }catch (FileNotFoundException e){
            e.printStackTrace();
            System.err.printf("Public key not found, please try again: %s.\n", args[2]);
            argsError();
        }
        String dStr = sc.nextLine();
        String nStr = sc.nextLine();
        OAEP oaep = new OAEP();

        BigInteger e = new BigInteger(dStr);
        BigInteger n = new BigInteger(nStr);



        String message = args[5];

        BigInteger encryptedMsg = encryptedMsg(e,n, message, oaep);
        byte[] outputByteArr = encryptedMsg.toByteArray();
        try{
            socket.send(new DatagramPacket(outputByteArr, outputByteArr.length, reportAdd));
        } catch (IOException io){
            io.printStackTrace();
            System.err.println("I can't send package \n");
            System.exit(1);

        }
    }

    public static BigInteger encryptedMsg(BigInteger e,
                                                   BigInteger n,
                                                   String msg,
                                                 OAEP oaep){
        byte[] seed = genSeed(32);

        BigInteger P = oaep.encode(msg, seed);
        BigInteger encryptedMsg = P.modPow(e, n);
        return encryptedMsg;
    }
    private static byte[] genSeed(int seedSize){
        byte[] seed = new byte[seedSize];

        Arrays.fill(seed, (byte) 0);        // fill the array with 0
        return seed;
    }

    public static void argMissing(){
        System.err.println("Missing arguments");
        argsError();
    }

    public static void argsError(){
        System.err.println("Usage: java Leaker <rhost> <rport> <lhost> <lport> <publickeyfile> <message>");
        System.exit(1);
    }
}
