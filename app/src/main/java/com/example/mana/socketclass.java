package com.example.mana;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class socketclass {
    Socket socket;
    DataOutputStream out;
    DataInputStream in;

    public socketclass(Socket socket) {
        this.socket = socket;

    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public void setOut(DataOutputStream out) {
        this.out = out;
    }

    public DataInputStream getIn() {
        return in;
    }

    public void setIn(DataInputStream in) {
        this.in = in;
    }

}
