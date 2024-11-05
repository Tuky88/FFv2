/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.peea.mx.FF.utils;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class ImagenIco
{
    public static void setIconImage(Window window)
    {
        try
        {
            InputStream imageInputStream = window.getClass().getResourceAsStream("imagen/peea.ico");
            BufferedImage bufferedImage = ImageIO.read(imageInputStream);
            window.setIconImage(bufferedImage);
        } catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }
}