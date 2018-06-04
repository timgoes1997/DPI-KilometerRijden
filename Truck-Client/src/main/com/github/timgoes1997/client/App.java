package com.github.timgoes1997.client;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class App {
    public static void main(String[] args) {
        try {
            System.out.println(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
