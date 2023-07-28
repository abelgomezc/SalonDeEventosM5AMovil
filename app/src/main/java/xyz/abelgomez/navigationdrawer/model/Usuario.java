package xyz.abelgomez.navigationdrawer.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Usuario implements Serializable {

    private Long usuId;
    private String usuNombreUsuario;
    private String usuContrasena;
    private Timestamp usuFechaRegistro;
    private int usuEstado;



    public Usuario(Long usuId, String usuNombreUsuario, String usuContrasena) {
        this.usuId = usuId;
        this.usuNombreUsuario = usuNombreUsuario;
        this.usuContrasena = usuContrasena;
    }

    public Usuario() {

    }

    public Long getUsuId() {
        return usuId;
    }

    public void setUsuId(Long usuId) {
        this.usuId = usuId;
    }

    public String getUsuNombreUsuario() {
        return usuNombreUsuario;
    }

    public void setUsuNombreUsuario(String usuNombreUsuario) {
        this.usuNombreUsuario = usuNombreUsuario;
    }

    public String getUsuContrasena() {
        return usuContrasena;
    }

    public void setUsuContrasena(String usuContrasena) {
        this.usuContrasena = usuContrasena;
    }

    public Timestamp getUsuFechaRegistro() {
        return usuFechaRegistro;
    }

    public void setUsuFechaRegistro(Timestamp usuFechaRegistro) {
        this.usuFechaRegistro = usuFechaRegistro;
    }

    public int getUsuEstado() {
        return usuEstado;
    }

    public void setUsuEstado(int usuEstado) {
        this.usuEstado = usuEstado;
    }
}
