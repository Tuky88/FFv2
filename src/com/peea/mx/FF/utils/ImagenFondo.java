/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.peea.mx.FF.utils;

/**
 *
 * @author tuky
 */
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.border.Border;
 

public class ImagenFondo implements Border{
    public BufferedImage back;
 

         public ImagenFondo(String imagen){
        try {
            String ruta="../imagenes/";
            File imagenruta=new File(ruta);
            URL imagePath = new URL(getClass().getResource(ruta+imagen).toString());
            System.out.println(imagePath.getFile());
            back = ImageIO.read(imagenruta.getAbsoluteFile());
        } catch (Exception ex) {            
        }
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.drawImage(back, (x + (width - back.getWidth())/2),(y + (height - back.getHeight())/2), null);
    }
 
    public Insets getBorderInsets(Component c) {
        return new Insets(0,0,0,0);
    }
 
    public boolean isBorderOpaque() {
        return false;
    }
}