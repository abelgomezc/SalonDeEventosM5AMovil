package xyz.abelgomez.navigationdrawer.model;

import java.io.File;
import java.io.Serializable;
import java.security.Timestamp;
import java.util.Date;

public class Salon implements Serializable {

    private int id_salon;
    private String nombre ;
    private String direccion;
    private int capacidad;
    private double costoHora;
    private boolean estado;
    private Timestamp fecha_hora_registro;
    private String ciudad;
    private double latitud;
    private double longitud;

    public int getId_salon() {
        return id_salon;
    }

    public void setId_salon(int id_salon) {
        this.id_salon = id_salon;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public double getCostoHora() {
        return costoHora;
    }

    public void setCostoHora(double costoHora) {
        this.costoHora = costoHora;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Timestamp getFecha_hora_registro() {
        return fecha_hora_registro;
    }

    public void setFecha_hora_registro(Timestamp fecha_hora_registro) {
        this.fecha_hora_registro = fecha_hora_registro;
    }
}
