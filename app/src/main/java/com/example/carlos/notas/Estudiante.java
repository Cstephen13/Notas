package com.example.carlos.notas;

/**
 * Created by Carlos on 27/12/2016.
 */

public class Estudiante {

    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private String email;
    private String codigo;
    private boolean estado;
    private int id;
    private String token;

    public Estudiante(String primerNombre,String segundoNombre,String primerApellido,String segundoApellido,
                      String email,String codigo,boolean estado, int id,String token){

            this.primerNombre=primerNombre;
            this.segundoNombre=segundoNombre;
            this.primerApellido=primerApellido;
            this.segundoApellido=segundoApellido;
            this.email=email;
            this.estado=estado;
            this.codigo=codigo;
            this.id=id;
            this.token=token;
    }


    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
