package xyz.abelgomez.navigationdrawer.model;

import java.security.Timestamp;

public class Reserva {

    private int resId;
    private boolean resEstado;
    private String resComprobante;
    private Timestamp resFechaRegistro;

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public boolean isResEstado() {
        return resEstado;
    }

    public void setResEstado(boolean resEstado) {
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
}



