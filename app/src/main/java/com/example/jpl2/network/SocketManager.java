package com.example.jpl2.network;

import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;

public class SocketManager {

    private static Socket socket;

    public static Socket getSocket() {
        if (socket == null) {
            try {
                IO.Options options = new IO.Options();
                options.transports = new String[]{"websocket"}; // 🔥 FORCE WEBSOCKET

                socket = IO.socket("https://jpl-backend-6ecq.onrender.com/", options);

            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return socket;
    }
}