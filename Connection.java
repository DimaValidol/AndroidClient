package com.example.camera;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Arrays;

public class Connection extends AsyncTask<Void, Void, String> {
    private final String SERVER_IP = "";
    private final int SERVER_PORT = 80;
    private Socket socket = null;
    private TextView prediction;
    private byte[] imageData;
    public Connection(){}
    private String response;

    void setImageData(byte[] imageData){
        this.imageData = imageData;
    }
    Connection(TextView prediction){
        this.prediction = prediction;
    }
    @SuppressLint("SetTextI18n")
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        prediction.setText("Connecting");
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            OutputStream outputStream = socket.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            String s = "predict";
            s = URLEncoder.encode(s, "UTF-8");
            bufferedWriter.write(s);
            bufferedWriter.flush();
            if (socket.isClosed()){
                throw new Exception("socket is closed, error occured");
            }
            try {
                socket.getOutputStream().write(imageData, 0 , imageData.length);
                socket.getOutputStream().flush();
            }
            catch (IOException e){
                e.printStackTrace();
            }

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String msg= URLDecoder.decode(bufferedReader.readLine(), "UTF-8");
        String  m [] =msg.split(" ");
        Log.i("Got", Arrays.toString(m));
        if (m[0].equals("Got")){
            Log.d("Next Step", "waiting for the msg from server ");
            return response = m[1];
        }
    }catch (UnknownHostException e){
            e.printStackTrace();
            response = "UnkownHost"+e.getMessage();
        }
        catch (IOException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket !=null){
                try {
                    socket.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return response;
        }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        prediction.setText(s);
        try {
            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
