
package com.peea.mx.FF.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import javax.swing.JLabel;

/**
 *
 * @author Axel Reyez
 */
public class reloj extends Thread{
    private  JLabel lbl; 
public reloj (JLabel lbl){
this.lbl=lbl;
}
public void run(){
while(true){
    
            Calendar calendario = Calendar.getInstance();
Date hoy= calendario.getTime();
SimpleDateFormat s=new SimpleDateFormat("hh:mm:ss");
lbl.setText(s.format(hoy));
try {
    sleep(1000);
}catch(Exception ex){
}
}



}}
