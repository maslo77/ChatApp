package com.company;

import java.io.*;
import java.net.Socket;

public class GetSerwer extends  Thread{
        private final Socket uzytkownik;
    public  GetSerwer(Socket uzytkownik)
    {
        this.uzytkownik = uzytkownik;

    }
    @Override
    public void run(){
        try {
            polaczKlienta();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void polaczKlienta() throws IOException,InterruptedException{
        InputStream inputStream = uzytkownik.getInputStream();
        OutputStream outputStream = uzytkownik.getOutputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String read;
        while((read = reader.readLine())!=null)
        {
            if("quit".equalsIgnoreCase(read))
            {
                break;
            }
            String Message = read + "\n";
            outputStream.write(Message.getBytes());
        }
        uzytkownik.close();
    }

}
