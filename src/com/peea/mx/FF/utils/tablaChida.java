/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.peea.mx.FF.utils;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Axel Reyez
 */
public class tablaChida extends DefaultTableModel
{
    public tablaChida(String[][] cadena ,String[] cadenita)
    {
        super(cadena,cadenita);
    }
   public boolean isCellEditable (int row, int column)
   {
       // Aquí devolvemos true o false según queramos que una celda
       // identificada por fila,columna (row,column), sea o no editable
       if (column <-1)
          return true;
       return false;
   }
}