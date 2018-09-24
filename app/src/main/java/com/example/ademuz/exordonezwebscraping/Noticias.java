package com.example.ademuz.exordonezwebscraping;

import android.graphics.Bitmap;

public class Noticias {

    String titulo, contenido,hora;
    Bitmap imagen;

    public Noticias(String hora, String titulo, Bitmap imagen, String contenido){
        this.hora = hora;
        this.titulo = titulo;
        this.imagen = imagen;
        this.contenido = contenido;
    }
}
