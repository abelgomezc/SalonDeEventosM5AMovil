package xyz.abelgomez.navigationdrawer.model;

import java.sql.Timestamp;

public class Salon2 {


    private Long salId;
    private String salNombre;
    private String salDireccion;
    private int salCapacidad;
    private double salCostoHora;
    private int salEstado;
    private float salLongitud;
    private float salLatitud;
    private Timestamp salFechaRegistro;

    public Salon2() {
    }

    public Long getSalId() {
        return salId;
    }

    public void setSalId(Long salId) {
        this.salId = salId;
    }

    public String getSalNombre() {
        return salNombre;
    }

    public void setSalNombre(String salNombre) {
        this.salNombre = salNombre;
    }

    public String getSalDireccion() {
        return salDireccion;
    }

    public void setSalDireccion(String salDireccion) {
        this.salDireccion = salDireccion;
    }

    public int getSalCapacidad() {
        return salCapacidad;
    }

    public void setSalCapacidad(int salCapacidad) {
        this.salCapacidad = salCapacidad;
    }

    public double getSalCostoHora() {
        return salCostoHora;
    }

    public void setSalCostoHora(double salCostoHora) {
        this.salCostoHora = salCostoHora;
    }

    public int getSalEstado() {
        return salEstado;
    }

    public void setSalEstado(int salEstado) {
        this.salEstado = salEstado;
    }

    public float getSalLongitud() {
        return salLongitud;
    }

    public void setSalLongitud(float salLongitud) {
        this.salLongitud = salLongitud;
    }

    public float getSalLatitud() {
        return salLatitud;
    }

    public void setSalLatitud(float salLatitud) {
        this.salLatitud = salLatitud;
    }

    public Timestamp getSalFechaRegistro() {
        return salFechaRegistro;
    }

    public void setSalFechaRegistro(Timestamp salFechaRegistro) {
        this.salFechaRegistro = salFechaRegistro;
    }


}