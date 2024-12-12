package com.aluracursos.screenmatch.principal;

import com.aluracursos.screenmatch.model.DatosEpisodio;
import com.aluracursos.screenmatch.model.DatosSerie;
import com.aluracursos.screenmatch.model.DatosTemporadas;
import com.aluracursos.screenmatch.model.Episodio;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConvierteDatos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=3d3cab75";
    private ConvierteDatos conversor = new ConvierteDatos();

    public void muestraMenu() {
        System.out.println("Porfa escribe el nombre de la serie que deseas buscar");

        //busca datos generales de series
        var nombreSerie = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
        var datos = conversor.obtenerDatos(json, DatosSerie.class);
        System.out.println(datos);

        //Buscar datos de todas las temps
        List<DatosTemporadas> temporadas = new ArrayList<>();
        for (int i = 1; i <= datos.totalDeTemporadas(); i++) {
            json = consumoAPI.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + "&Season=" + i + API_KEY);
            var datosTemporadas = conversor.obtenerDatos(json, DatosTemporadas.class);
            temporadas.add(datosTemporadas);
        }
        //temporadas.forEach(System.out::println);
        /* Mostrar solo titulo de episodios de las temp
        for (int i = 0; i < datos.totalDeTemporadas(); i++) {
            List<DatosEpisodio> episodiosTemp = temporadas.get(i).episodios();
            for (int j = 0; j < episodiosTemp.size(); j++) {
                System.out.println(episodiosTemp.get(j).titulo());
            }
        } */
        //Mostrar lo mismo pero con funcion Lambda , acoratar el codigo
        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        List<DatosEpisodio> datosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        // Obtener los top 5 episodios
//        System.out.println("\n Top 5 episodios");
//        datosEpisodios.stream()
//                .filter(e -> !e.evaluacion().equalsIgnoreCase("N/A"))
//                .peek(e -> System.out.println("primer filtro NA" +e))
//                .sorted(Comparator.comparing(DatosEpisodio::evaluacion).reversed())
//                .peek(e -> System.out.println("segundo filtro odenar M-Mn" +e))
//                .map(e -> e.titulo().toUpperCase())
//                .peek(e -> System.out.println("tercer filtro Mayus" +e))
//                .limit(5)
//                .forEach(System.out::println);

        //Convirtiendo los datos a una lista del tipo Episodio
       List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numeroTemporada(), d)))
                .collect(Collectors.toList());
//
//        episodios.forEach(System.out::println);

        // Busqueda de episodios a partir de x año
//        System.out.println("a partir de que año deseas ver los episodios?");
//        var fecha = teclado.nextInt();
//        teclado.nextLine();
//
//        LocalDate fechaBusqueda = LocalDate.of(fecha, 1, 1);

//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        episodios.stream()
//                .filter(e -> e.getFechaDeLanzamiento() != null && e.getFechaDeLanzamiento().isAfter(fechaBusqueda))
//                .forEach(e -> System.out.println(
//                        "Temporada: " + e.getTemporada() +
//                                " Episodio: " + e.getTitulo() +
//                                " Fecha de Lanzamiento: " + e.getFechaDeLanzamiento().format(dtf)
//                ));

        //busca caps por titulo
        System.out.println("plox da el titulo o pedazo de titulo q buscas");
        var pedazoTitulo = teclado.nextLine();
        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(pedazoTitulo.toUpperCase()))
                .findFirst();
       if(episodioBuscado.isPresent()){
            System.out.println("Episodio encontrado");
            System.out.println("Los datos son: "+episodioBuscado.get());
        }else{
            System.out.println("No encontrado :( ");
        }



    }

}
