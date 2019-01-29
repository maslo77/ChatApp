package com.company;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Serwer {
    public static void main(String[] args) throws IOException {
        try {
            ServerSocket serwersocket = new ServerSocket(8818);
           while(true) {
               Socket polaczenieKlienta = serwersocket.accept();
               OutputStream wyjscie = polaczenieKlienta.getOutputStream();
               wyjscie.write("TEST\n".getBytes());
               GetSerwer nowy = new GetSerwer(polaczenieKlienta);
               nowy.start();
           }
        }catch(IOException e){
            e.printStackTrace();
        }

    }

}
