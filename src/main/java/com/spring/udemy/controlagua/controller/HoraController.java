package com.spring.udemy.controlagua.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@RestController
public class HoraController {

//    @GetMapping("/api/hora")
//    public String obtenerHoraActual() {
//        LocalTime hora = LocalTime.now();
//        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("hh:mm a", Locale.of("es", "ES"));
//        return hora.format(formatoHora).toLowerCase(); // ejemplo: 11:15 a. m.
//    }

//        @GetMapping("/api/hora")
//        public String obtenerHoraActual() {
//            LocalTime hora = LocalTime.now();
//            // CORREGIDO: Locale.of(...) no existe → usar new Locale(...)
//            //Cambio corregido
//            DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("hh:mm a", new Locale("es", "ES"));
//            return hora.format(formatoHora).toLowerCase(); // ejemplo: 11:15 a. m.
//        }

    @GetMapping("/api/hora")
    public String obtenerHoraActual() {
        // Obtener la hora actual en la zona horaria de Lima
        ZonedDateTime horaLima = ZonedDateTime.now(ZoneId.of("America/Lima"));

        // Formatear la hora en el formato deseado
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("hh:mm a", new Locale("es", "PE"));

        // Devolver la hora formateada en minúsculas
        return horaLima.format(formatoHora).toLowerCase(); // ejemplo: 11:15 a. m.
    }
}

