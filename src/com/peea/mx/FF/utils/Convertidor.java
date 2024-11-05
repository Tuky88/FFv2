/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.peea.mx.FF.utils;

import java.text.DecimalFormat;

/**
 *
 * @author Sistemas
 */
public class Convertidor {

    private double valor;
    DecimalFormat df = new DecimalFormat("#.0000");

    public Convertidor() {

    }

    public double MMtoIN(double valor) {
        System.out.println(valor / 25.4);
        double res=valor / 25.4;
        return Double.parseDouble(df.format(res));
    }

    public double INtoMM(double valor) {
        System.out.println(valor * 25.4);
        double res=valor * 25.4;
        return Double.parseDouble(df.format(res));
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

}
