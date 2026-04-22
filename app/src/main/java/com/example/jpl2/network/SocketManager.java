package com.example.jpl2.network;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketManager {

    private static Socket socket;

    public static Socket getSocket() {
        if (socket == null) {
            try {
                socket = IO.socket("https://jpl-backend-6ecq.onrender.com/");
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return socket;
    }
}