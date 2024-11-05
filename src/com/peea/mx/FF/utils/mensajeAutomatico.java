/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.peea.mx.FF.utils;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
/**
 * @web http://jc-mouse.blogspot.com/
 * @author Mouse
 */
public class mensajeAutomatico {
//para la animacion    
private Timer tiempo ;
private TimerTask task;
//para almacenar la respuesta
private String respuesta=null;
private int duracion = 3;//tiempo
private JFrame padre;
private boolean isrun=false;

    public mensajeAutomatico(){
    }
    
    public void mostrar_pregunta(String p, int d){
        this.duracion = d;
        //comienza a contar el tiempo
        Comenzar_a_contar();
        //se muestra el JOptionPane
        JOptionPane.showMessageDialog(this.padre, p);
                            try {
                        Robot robo=new Robot();
                        robo.keyPress(KeyEvent.VK_ESCAPE);
                    } catch (AWTException ex) {
                        Logger.getLogger(mensajeAutomatico.class.getName()).log(Level.SEVERE, null, ex);
                    }
        //si contesto antes de que acabe el tiempo
        if(isrun){
            detener();
        }       
        //System.out.println("Respuesta " + this.respuesta);
    }
    
    /* se ejecuta cuando acaba el tiempo */
  
    
    /* actualiza los objetos del frame padre */

    
    /* metodo que lleva el tiempo disponible para contestar*/
    public void Comenzar_a_contar() {   
           isrun=true;
           tiempo = new Timer();
           task = new TimerTask() {
               int contador=0;
               public void run() {
                   contador++;
                   System.out.println("tiempo transcurrido: " + contador);
                   if(contador == duracion){
                        System.out.println("Se acabo el tiempo, mala suerte");
                        detener();
                   }
               }    
           };        
           tiempo.schedule(task,0,1000);    
    }
    
    /* detiene la animacion */
     public void detener() {   
            isrun=false;
            tiempo.cancel();
            task.cancel();
    }        
}   
   
