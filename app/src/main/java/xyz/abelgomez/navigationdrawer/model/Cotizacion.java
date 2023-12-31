package xyz.abelgomez.navigationdrawer.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Cotizacion implements Serializable {


    private Long cotiId;
    private String cotiTipoEvento;
    private String cotiDescripcion;
    private int cotiEstado;
    private double cotiMonto;
    private String cotiHoraFin;
    private String cotiHoraInicio;
    private Timestamp cotiFechaRegistro;

    private Producto producto;
    private Salon2 salId;
    private Usuario usuId;

    public Cotizacion() {
    }

    public Long getCotiId() {
        return cotiId;
    }

    public void setCotiId(Long cotiId) {
        this.cotiId = cotiId;
    }

    public String getCotiTipoEvento() {
        return cotiTipoEvento;
    }

    public void setCotiTipoEvento(String cotiTipoEvento) {
        this.cotiTipoEvento = cotiTipoEvento;
    }



    public String getCotiDescripcion() {
        return cotiDescripcion;
    }

    public void setCotiDescripcion(String cotiDescripcion) {
        this.cotiDescripcion = cotiDescripcion;
    }

    public int getCotiEstado() {
        return cotiEstado;
    }

    public void setCotiEstado(int cotiEstado) {
        this.cotiEstado = cotiEstado;
    }

    public double getCotiMonto() {
        return cotiMonto;
    }

    public void setCotiMonto(double cotiMonto) {
        this.cotiMonto = cotiMonto;
    }

    public String getCotiHoraFin() {
        return cotiHoraFin;
    }

    public void setCotiHoraFin(String cotiHoraFin) {
        this.cotiHoraFin = cotiHoraFin;
    }

    public String getCotiHoraInicio() {
        return cotiHoraInicio;
    }

    public void setCotiHoraInicio(String cotiHoraInicio) {
        this.cotiHoraInicio = cotiHoraInicio;
    }

    public Timestamp getCotiFechaRegistro() {
        return cotiFechaRegistro;
    }

    public void setCotiFechaRegistro(Timestamp cotiFechaRegistro) {
        this.cotiFechaRegistro = cotiFechaRegistro;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Salon2 getSalId() {
        return salId;
    }

    public void setSalId(Salon2 salId) {
        this.salId = salId;
    }

    public Usuario getUsuId() {
        return usuId;
    }

    public void setUsuId(Usuario usuId) {
        this.usuId = usuId;
    }
}
