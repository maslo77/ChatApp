package com.company;



import com.sun.deploy.util.StringUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class ObslugaSerwera extends  Thread{
        private final Socket uzytkownik;
    private final Sesja sesja;
    private String login =null;
    private OutputStream outputStream;

    public  ObslugaSerwera(Sesja sesja, Socket uzytkownik)
    {   this.sesja = sesja;
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
        this.outputStream = uzytkownik.getOutputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String read;
        while((read = reader.readLine())!=null)
        {   String[] pobrane=read.split(" ");
            if(pobrane !=null && pobrane.length >0)
            {   String wczytane = pobrane[0];
                if("quit".equalsIgnoreCase(read)||"wyloguj".equals(read))
                {
                    Wyloguj();
                    break;
                }else if("logowanie".equalsIgnoreCase(wczytane))
                {
                    Logowanie(outputStream, pobrane);
                }else if("-w".equalsIgnoreCase(wczytane))
                {    String[] wiadomosc = read.split(" ",3);
                    Szept(wiadomosc);
                }
                else {
                    String Message = "Blad Wykonania! " +read + "\n";
                    outputStream.write(Message.getBytes());
                }
            }

        }
        uzytkownik.close();
    }

    private void Szept(String[] pobrane) throws IOException {
        String wiadomosc = pobrane[2];
        String odbiorca = pobrane[1];
        List<ObslugaSerwera> ListaK =  sesja.getList();
        for(ObslugaSerwera k : ListaK)
        {   if(odbiorca.equalsIgnoreCase(k.getLogin()))
        {
            String w = login+" pisze: " + wiadomosc+ "\n";
            k.send(w);
        }
        }

    }

    private void Wyloguj() throws IOException {
        Sesja.usun(this);
        List<ObslugaSerwera> ListaK =  sesja.getList();

        String dostepny = "Niedostepny: " + login + "\n";
        for(ObslugaSerwera k : ListaK)
        {   if(!login.equals(k.getLogin()))
        {
            k.send(dostepny);
        }
        }
        uzytkownik.close();

    }

    public String getLogin() {
        return login;
    }

    private void Logowanie(OutputStream outputStream, String[] dane) throws IOException {
        if(dane.length == 3){
            String login = dane[1];
            String haslo = dane[2];
            if(login.equals("gosc") && haslo.equals("gosc")||login.equals("Michal") && haslo.equals("1234"))
            {
                String wiadomosc = "Zalogowano jako " + login+"\n";
                outputStream.write(wiadomosc.getBytes());
                this.login = login;
                System.out.println("Witamy na serwerze "+login+" !\n");

                List<ObslugaSerwera> ListaK =  sesja.getList();
                // odswiezam liste dostepnych
               for(ObslugaSerwera k : ListaK)
               {
                   if (k.getLogin() != null) {
                   if(!login.equals(k.getLogin())) {
                       String a = "Dostepny: " + k.getLogin() + "\n";
                       send(a);
                   }
               }
               }

                String dostepny = "Online: " + login + "\n";
                for(ObslugaSerwera k : ListaK)
                {   if(!login.equals(k.getLogin()))
                    {
                    k.send(dostepny);
                    }
                }
            }else
            {
                String wiadomosc = "Logowanie nie powiodlo sie\n";
                outputStream.write(wiadomosc.getBytes());
            }
        }
    }

    private void send(String d) throws IOException {
    outputStream.write(d.getBytes());
    }
}
