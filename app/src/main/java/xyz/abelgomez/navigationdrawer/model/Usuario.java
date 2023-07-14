package xyz.abelgomez.navigationdrawer.model;

import java.sql.Timestamp;

public class Usuario {

    private Long usuId;
    private String usuNombreUsuario;
    private String usuContrasena;
    private Timestamp usuFechaRegistro;


    public Usuario(Long usuId, String usuNombreUsuario, String usuContrasena) {
        this.usuId = usuId;
        this.usuNombreUsuario = usuNombreUsuario;
        this.usuContrasena = usuContrasena;
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
}
