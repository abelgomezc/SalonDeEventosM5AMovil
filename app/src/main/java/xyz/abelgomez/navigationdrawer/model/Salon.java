package xyz.abelgomez.navigationdrawer.model;

import java.io.File;
import java.util.Date;

public class Salon {

    private int id_salon;
    private String nombre ;
    private String descripccion;
    private int capacidad;
    private double costoHora;
    private int estado;
    private Date fecha_hora_registro;
    private File imagen;// lita de files
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

    public String getDescripccion() {
        return descripccion;
    }

    public void setDescripccion(String descripccion) {
        this.descripccion = descripccion;
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

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public Date getFecha_hora_registro() {
        return fecha_hora_registro;
    }

    public void setFecha_hora_registro(Date fecha_hora_registro) {
        this.fecha_hora_registro = fecha_hora_registro;
    }

    public File getImagen() {
        return imagen;
    }

    public void setImagen(File imagen) {
        this.imagen = imagen;
    }
}
