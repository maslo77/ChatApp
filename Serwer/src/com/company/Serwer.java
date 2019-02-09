package com.company;

import java.io.IOException;


public class Serwer {
    public static void main(String[] args) throws IOException {
        Sesja serwerowa = new Sesja(8818);
        serwerowa.start();
    }

}
