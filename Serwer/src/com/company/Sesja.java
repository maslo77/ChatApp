package com.company;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Sesja extends  Thread{
    public final int portSerwera;

    private ArrayList<GetSerwer> ListaK = new ArrayList<>();
    public Sesja(int portSerwera)
    {
        this.portSerwera = portSerwera;
    }
    public List<GetSerwer> getList(){
        return ListaK;
    }

    @Override
    public void run(){
        try {
            ServerSocket serwersocket = new ServerSocket(8818);
            while(true) {
                Socket polaczenieKlienta = serwersocket.accept();
                OutputStream wyjscie = polaczenieKlienta.getOutputStream();
                wyjscie.write("TEST\n".getBytes());
                GetSerwer nowy = new GetSerwer(this,polaczenieKlienta);
                ListaK.add(nowy);
                nowy.start();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
