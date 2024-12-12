package com.aluracursos.screenmatch.principal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EjemploStreams {
    public void muestraEjemplo(){
        List<String> nombres = Arrays.asList("Charley","Pancha","Alex","Marta","Juan","Penelope");

        nombres.stream()
                .sorted()
                .limit(4)
                .filter(n -> n.startsWith("C"))
                .map(n -> n.toUpperCase())
                .forEach(System.out::println);
    }
}
