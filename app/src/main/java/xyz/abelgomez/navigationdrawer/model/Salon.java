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

    private double latitud;
    private double longitud;
    private File imagen;
    private String urlImagen;

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

    public File getImagen() {
        return imagen;
    }

    public void setImagen(File imagen) {
        this.imagen = imagen;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public Salon2 convertirASalon2() {
        Salon2 salon2 = new Salon2();
        salon2.setSalId((long) this.id_salon);
        salon2.setSalNombre(this.nombre);
        salon2.setSalDireccion(this.direccion);
        salon2.setSalCapacidad(this.capacidad);
        salon2.setSalCostoHora(this.costoHora);
        salon2.setSalEstado(this.estado ? 1 : 0); // Convertir booleano a entero
        salon2.setSalLongitud((float) this.longitud);
        salon2.setSalLatitud((float) this.latitud);
        return salon2;
    }

}
