package com.trabajo.fitnessapp.datos.Utils;

import com.trabajo.fitnessapp.dominio.Musculo;

import java.util.LinkedHashMap;
import java.util.Map;

public class MusculosFlitro {
    public static Map<String, String> MUSCULOS = new LinkedHashMap<>();

    static {
        MUSCULOS.put("all", "Todos");
        MUSCULOS.put("chest", "Pecho");
        MUSCULOS.put("lower back" , "Zona lumbar");
        MUSCULOS.put("middle back", "Espalda media");
        MUSCULOS.put("shoulders", "Hombros");
        MUSCULOS.put("lats", "Dorsales");
        MUSCULOS.put("biceps", "Bíceps");
        MUSCULOS.put("triceps", "Tríceps");
        MUSCULOS.put("abdominals", "Abdominales");
        MUSCULOS.put("quadriceps", "Cuádriceps");
        MUSCULOS.put("hamstrings", "Isquiotibiales");
        MUSCULOS.put("glutes", "Glúteos");
        MUSCULOS.put("calves", "Gemelos");
        MUSCULOS.put("forearms", "Antebrazo");
        MUSCULOS.put("traps", "Trapecios");
        MUSCULOS.put("neck", "Cuello");
        MUSCULOS.put("adductors", "Aductores");
        MUSCULOS.put("abductors", "Ab   ductores");
    }

    public static String traducirMusculo(String muscle) {
        if (muscle == null) return "No especificado";

        String clave = muscle.toLowerCase();
        return MUSCULOS.getOrDefault(clave, muscle);
    }

    public static Musculo mapearMusculoEnum(String musculoApi) {
        if (musculoApi == null) return null;

        String musculoEsp = MusculosFlitro.traducirMusculo(musculoApi);

        switch (musculoEsp) {
            case "Abdominales": return Musculo.ABDOMINALES;
            case "Pecho": return Musculo.PECHO;
            case "Zona lumbar": return Musculo.ZONA_LUMBAR;
            case "Espalda media": return Musculo.ESPALDA_MEDIA;
            case "Hombros": return Musculo.HOMBROS;
            case "Dorsales": return Musculo.DORSALES;
            case "Bíceps": return Musculo.BICEPS;
            case "Tríceps": return Musculo.TRICEPS;
            case "Cuádriceps": return Musculo.CUADRICEPS;
            case "Isquiotibiales": return Musculo.ISQUIOTIBIALES;
            case "Glúteos": return Musculo.GLUTEOS;
            case "Gemelos": return Musculo.GEMELOS;
            case "Antebrazo": return Musculo.ANTEBRAZO;
            case "Trapecios": return Musculo.TRAPECIOS;
            case "Cuello": return Musculo.CUELLO;
            case "Aductores": return Musculo.ADUCTORES;
            case "Abductores": return Musculo.ABDUCTORES;
            default: return null;
        }
    }
}
