//******************************************************************************
//
// File:    LeakerProxy.java
// Package: edu.rit.pj2
// Unit:    Class LeakerProxy
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
import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Class LeakerProxy provides the network proxy for the Reporter main program
 * to receive the encrypted message from the leakers and decrypt them and allow
 * the reporter to display it as a standard output.
 *
 * @author Navid Nafiuzzaman
 * @version 29 April 2021
 */
public class LeakerProxy {
    // Data Member
    private final DatagramSocket mailbox;       // mailbox socket
    private final BigInteger d;                 // decimal of private key
    private final BigInteger n;                 // modulus val of private key
    private final OAEP oaep;                    // oaep obj

    /**
     * Constructs a LeakerProxy
     * @param mailbox mailbox socket
     * @param d decimal
     * @param n modulus
     * @param oaep  oaep obj from pj2
     */
    public LeakerProxy(DatagramSocket mailbox, BigInteger d, BigInteger n, OAEP oaep) {
        this.mailbox = mailbox;
        this.d = d;
        this.n = n;
        this.oaep = oaep;
    }

    /**
     * Starts the Thread for receiving the messages from the Leakers.
     */
    public void startProxy (){
        new ReaderThread().start();
    }

    /**
     * Class ReaderThread extends the Thread to receive the encrypted messages from the
     * Leakers and decrypt the message -> show it as a standard output.
     */
    public class ReaderThread extends Thread{
        public void run(){
            byte[] buffer = new byte[260];
            while (true){
                try{
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    mailbox.receive(packet);        // receive the message
                    byte[] incomingBuffer = packet.getData();
                    BigInteger msg = new BigInteger(incomingBuffer, 0, packet.getLength());
                    String output = decryption(d, n, msg, oaep);        // decrypt
                    System.out.println(output);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * Function decrypting the received message
     * @param d decimal from private key
     * @param n modulus from private key
     * @param msg   encrypted message
     * @param oaep  oaep from pj2 to decode
     * @return  string of decrypted message
     */
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
