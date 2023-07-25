package xyz.abelgomez.navigationdrawer.model;

import java.io.Serializable;
import java.security.Timestamp;
import java.util.Date;

public class Reserva implements Serializable {

    private Long resId;
    private int resEstado;
    private int resImagenRerserva;
    private String resComprobante;
    private Timestamp resFechaRegistro;
    private Cotizacion reCotiId;
    private String resFechaEvento;
    private Usuario usuId;

    public Long getResId() {
        return resId;
    }

    public void setResId(Long resId) {
        this.resId = resId;
    }

    public int getResEstado() {
        return resEstado;
    }

    public void setResEstado(int resEstado) {
        this.resEstado = resEstado;
    }

    public String getResComprobante() {
        return resComprobante;
    }

    public void setResComprobante(String resComprobante) {
        this.resComprobante = resComprobante;
    }

    public Timestamp getResFechaRegistro() {
        return resFechaRegistro;
    }

    public void setResFechaRegistro(Timestamp resFechaRegistro) {
        this.resFechaRegistro = resFechaRegistro;
    }

    public Cotizacion getReCotiId() {
        return reCotiId;
    }

    public void setReCotiId(Cotizacion reCotiId) {
        this.reCotiId = reCotiId;
    }

    public String getResFechaEvento() {
        return resFechaEvento;
    }

    public void setResFechaEvento(String resFechaEvento) {
        this.resFechaEvento = resFechaEvento;
    }

    public Usuario getUsuId() {
        return usuId;
    }

    public void setUsuId(Usuario usuId) {
        this.usuId = usuId;
    }

    public int getResImagenRerserva() {
        return resImagenRerserva;
    }

    public void setResImagenRerserva(int resImagenRerserva) {
        this.resImagenRerserva = resImagenRerserva;
    }
}



