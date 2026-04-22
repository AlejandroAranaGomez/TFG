package trabajo.aplicacionSaludable.Servicios;

import trabajo.aplicacionSaludable.Dominio.Genero;
import trabajo.aplicacionSaludable.Dominio.NivelDeActividad;
import trabajo.aplicacionSaludable.Dominio.Objetivo;

import java.time.LocalDate;
import java.time.Period;

public class CalculoCalorias {

    public static int calcularCalorias (
            float peso,
            int altura,
            LocalDate fechaNacimiento,
            Genero genero,
            NivelDeActividad nivelActividad,
            Objetivo objetivo
    ) {

        int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();

        double tmb;

        if (genero == Genero.HOMBRE) {
            tmb = 66.5 + (13.75 * peso) + (5.003 * altura) - (6.75 * edad);
        } else if (genero == Genero.MUJER) {
            tmb = 655.1 + (9.563 * peso) + (1.850 * altura) - (4.676 * edad);
        } else {
            tmb =  (66.5 + (13.75 * peso) + (5.003 * altura) - (6.75 * edad)
                    + 655.1 + (9.563 * peso) + (1.850 * altura) - (4.676 * edad)) / 2;
        }

        //FACTOR DE ACTIVIDAD
        double factorActividad = switch (nivelActividad) {
            case SEDENTARIO -> 1.2;
            case EJERCICIO_LIGERO -> 1.375;
            case EJERCICIO_MODERADO -> 1.55;
            case EJERCICIO_INTENSO -> 1.725;
            case ATLETA -> 1.9;
        };

        double getd = tmb * factorActividad; //Gasto Energético Total Diario

        //AJUSTE SEGUN OBJETIVO
        double caloriasFinales;

        switch (objetivo) {
            case PERDER_GRASA -> caloriasFinales = getd - 400;
            case GANAR_MASA_MUSCULAR -> caloriasFinales = getd + 400;
            case MANTENER_PESO -> caloriasFinales = getd;
            default -> caloriasFinales = getd;
        }

        return (int) caloriasFinales;
    }
}
