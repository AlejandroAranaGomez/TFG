package com.trabajo.fitnessapp.datos.GestionMensajes;

public abstract class Result<T> {
    private Result() {}

    public static final class Success<T> extends Result<T> {
        public final T datos;

        public Success(T datos) {
            this.datos = datos;
        }
    }

    public static final class Error<T> extends Result<T> {
        public final String error;

        public Error(String error) {
            this.error = error;
        }
    }
}
