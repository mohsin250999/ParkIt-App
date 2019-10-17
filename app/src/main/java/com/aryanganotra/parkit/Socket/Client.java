package com.aryanganotra.parkit.Socket;

import android.util.Log;

import com.aryanganotra.parkit.Singleton.SingletonClient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class Client implements Runnable {
    private Socket sock = null;
    private InputStreamReader inr;
    private BufferedReader br;
    private String ip, msg;
    private int port;
    private DataOutputStream out;

    public Client(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void run() {

            while(true) {
                try {
                    sock = new Socket(InetAddress.getByName(ip), port);
                    if (sock.isConnected()) {
                        Log.i("ConnectionSocket", "Connected");
                        break;
                    }
                }
                catch (Exception e){
                    Log.i("ConnectionSocket","Failed");
                }
            }
        try {
            inr = new InputStreamReader(sock.getInputStream());
            out = new DataOutputStream(sock.getOutputStream());
            while (true) {
                br = new BufferedReader(inr);
                msg = br.readLine();
                Log.i("Message",msg);
                if (msg.startsWith("vacant")){
                    SingletonClient.getInstance().getVacant().postValue(Integer.valueOf(msg.substring(7)));
                }
                if(SingletonClient.getInstance().getLicense_num().getValue().startsWith("license:")){
                    out.writeBytes(SingletonClient.getInstance().getLicense_num().getValue());
                }
                if(sock.isClosed() || !sock.isConnected())
                {
                    Log.i("ConnectionSocket","Disconnected");
                    break;
                }
            }
            if (!sock.isClosed())
            sock.close();
        }
        catch (Exception e)
        {
            Log.i("Exception",e.getMessage());
            try {
                if(sock!=null && !sock.isClosed())
                sock.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
