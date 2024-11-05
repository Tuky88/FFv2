/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.peea.mx.FF.modelos;

/**
 *
 * @author Sistemas
 */
public class Cliente {
    private String Nombre;
    private String Direccion;
    private String Telefono;
    private int PrefMed;

    public Cliente(String Nombre, String Direccion, String Telefono, String PrefMed) {
        this.Nombre = Nombre;
        this.Direccion = Direccion;
        this.Telefono = Telefono;
        if(PrefMed.equals("1"))
            this.PrefMed = 1;
        else
            this.PrefMed= 2;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String Direccion) {
        this.Direccion = Direccion;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String Telefono) {
        this.Telefono = Telefono;
    }

    public int getPrefMed() {
        return PrefMed;
    }

    public void setPrefMed(int PrefMed) {
        this.PrefMed = PrefMed;
    }

    @Override
    public String toString() {
        return "Cliente{" + "Nombre=" + Nombre + ", Direccion=" + Direccion + ", Telefono=" + Telefono + ", PrefMed=" + PrefMed + '}';
    }
    
}
