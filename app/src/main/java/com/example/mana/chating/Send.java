package com.example.mana.chating;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

public class Send implements Runnable {
DataOutputStream outs;
BufferedReader in2 = new BufferedReader(new InputStreamReader(System.in));
    public Send(DataOutputStream outs){
        this.outs = outs;
    }
    @Override
    public void run() {
        while(true)
        {

            try
            {
                String msg = in2.readLine();    //Ű����κ��� �Է��� ����
                outs.writeUTF(msg);                //������ ����
            }catch(Exception e) {}
        }
    }
}
