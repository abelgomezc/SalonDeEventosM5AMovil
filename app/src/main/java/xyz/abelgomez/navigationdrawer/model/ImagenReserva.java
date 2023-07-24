package xyz.abelgomez.navigationdrawer.model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class ImagenReserva implements Serializable {


    private Bitmap datosImagen;
    private Reserva reservaId;

    public Bitmap getDatosImagen() {
        return datosImagen;
    }

    public void setDatosImagen(Bitmap datosImagen) {
        this.datosImagen = datosImagen;
    }

    public Reserva getReservaId() {
        return reservaId;
    }

    public void setReservaId(Reserva reservaId) {
        this.reservaId = reservaId;
    }
}
