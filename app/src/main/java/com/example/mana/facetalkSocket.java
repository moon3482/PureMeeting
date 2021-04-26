package com.example.mana;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class facetalkSocket extends Thread {
    Socket socket;
    DataInputStream in;
    public DataOutputStream out;
    String data;
    boolean inter = true;

    @Override
    public void run() {
        super.run();
        socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(new ServerIP().ip, 9000), 1000);
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());

            while (inter) {
                data = in.readUTF();

                if(data != null){
                    JSONObject jsonObject = new JSONObject(data);


                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void interrupt() {
        super.interrupt();
        inter = false;
    }
}
