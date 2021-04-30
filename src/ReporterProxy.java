//******************************************************************************
//
// File:    ReporterProxy.java
// Package: edu.rit.pj2
// Unit:    Class ReporterProxy
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
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * Class ReporterProxy provides the Leaker with the network proxy which allows
 * leaker to send message to the reporter. The leaker will send encrypted message
 * to the reporter using UDP protocol.
 * @author Navid Nafiuzzaman
 * @version 29 Apr 2021
 */
public class ReporterProxy implements LeakerListener{
    // Data member
    private DatagramSocket mailbox;             // DataGramSocket for MailBox
    private InetSocketAddress targetAddress;    // Target Address

    /**
     * Construct a new Reporter Proxy
     * @param mailbox Mailbox DatagramSocket
     * @param targetAddress IP Address to which message will be sent
     */
    public ReporterProxy(DatagramSocket mailbox, InetSocketAddress targetAddress) {
        this.mailbox = mailbox;
        this.targetAddress = targetAddress;
    }


    /**
     * Report Message sends messages to the target address
     * @param msgBuffer byte array containing the encrypted message
     * @return boolean value after message being sent
     * @throws IOException Input/Output Exception
     */
    @Override
    public boolean reportMsg(byte[] msgBuffer) throws IOException {
        mailbox.send(new DatagramPacket(msgBuffer, msgBuffer.length, targetAddress));
        return true;
    }
}
