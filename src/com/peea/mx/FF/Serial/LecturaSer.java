/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.peea.mx.FF.Serial;

import com.peea.mx.FF.modelos.Medicion;
import com.peea.mx.FF.utils.graficadorLineal;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import java.io.IOException;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import jdk.nashorn.internal.scripts.JO;

/**
 *
 * @author Sistemas
 */
public class LecturaSer extends Thread {

    CommPortIdentifier portId;
    Enumeration puertos;
    SerialPort serialport;
    InputStream entrada = null;
    JTextField label1;
    JTextField label2;
    JTextField label3;
    JTextField label5;
    JTextField label6;
    JTextField cont;
    int baudrate;
    String numport;
    Boolean bandera;
    graficadorLineal graf;
    JLabel estado;
    LinkedList linked;
    String[] indices;
    JPanel panelin;

    public graficadorLineal getGraf() {
        return graf;
    }

    public void setGraf(graficadorLineal graf) {
        this.graf = graf;
    }

    public LinkedList getLinked() {
        return linked;
    }

    public void setLinked(LinkedList linked) {
        this.linked = linked;
    }

    public LecturaSer(String[] indices, JPanel panelin, JTextField label1, JTextField label2, JTextField label3, JTextField cont, JTextField label5,
            JTextField label6, int baudrate, String numport, JLabel lbl, LinkedList ls) {
        this.label1 = label1;
        this.label2 = label2;
        this.label3 = label3;
        this.cont = cont;
        this.baudrate = baudrate;
        this.numport = numport;
        this.estado = lbl;
        bandera = false;
        this.linked = ls;
        this.indices = indices;
        this.panelin = panelin;
        this.label5 = label5;
        this.label6 = label6;
        this.iniGrafica();
        puertos = CommPortIdentifier.getPortIdentifiers();
        while (puertos.hasMoreElements()) { //para recorrer el numero de los puertos, y especificar con cual quiero trabajar 
            //hasmorelements mientras tenga mas eleementos
            portId = (CommPortIdentifier) puertos.nextElement(); //next elemento recorre uno por uno
            System.out.println(portId.getName()); //puertos disponbibles
            if (portId.getName().equalsIgnoreCase(this.numport)) {
                //System.out.println(portId.getName());
                try {
                    serialport = (SerialPort) portId.open("LecturaSerial", 100);//tiempo en ms
                    serialport.setSerialPortParams(baudrate, 8, 1, 0);
                    serialport.setDTR(true);
                    System.out.println(serialport.getBaudRate() + "//" + serialport.getDataBits() + "//");
                    
                    entrada = serialport.getInputStream();//esta variable del tipo InputStream obtiene el dato serial
                    //System.out.println("dfsdfsdf");// inciamos el hilo para realizar nuestra accion de imprimir el dato serial
                } catch (Exception e) {
                }
            }
        }
    }

    public Boolean getBandera() {
        return bandera;
    }

    public void setBandera(Boolean bandera) {
        this.bandera = bandera;
    }

    public void iniGrafica() {
        graf = new graficadorLineal(this.indices, this.panelin, this.label1, this.label2, this.label3, this.cont, this.label5, this.label6);

    }

    @Override
    public void run() {

        String valor = "", valorsito = "";
        int aux;
        int contador = 0;
        int contadorPulsos = 0;
//        try {
//            aux=entrada.read();
//            while(aux>0)
//            {
//                aux=entrada.read();
//                System.out.println(aux);
//            }
//        } catch (IOException ex) {
//            Logger.getLogger(LecturaSer.class.getName()).log(Level.SEVERE, null, ex);
//        }
        while (true) {
            //System.out.println("esperando...");
            try {
                valorsito = valor;
                aux = entrada.read(); // aqui estamos obteniendo nuestro dato serial
                //Thread.sleep(5);
                //System.out.println(aux);
                if (aux > 0) {

                    //System.out.print();//imprimimos el dato serial
                    //System.out.print(Integer.decode(Integer.toHexString(aux)));
                    System.out.print((char) (aux));
                    valor += (char) (aux);
                    //System.out.println(valor);
                    contador++;
                    //System.out.println(valor +"//"+valorsito);

                } else {
                }
            } catch (Exception e) {
            }

            //System.out.println(valorsito +"//" +valorsito.length());
            if (valorsito.length() == 38 && !estado.getText().equals("X")) {
                String[] c = valorsito.split(":");
                System.out.println(valorsito + "--"+ valorsito.length());
                //01A+0006.789
                //3:+0012.46
                //JOptionPane.showMessageDialog(cont,valorsito);
                /*String xx= valorsito.substring(3, 10)+"//"+
                        valorsito.substring(13, 21)+"//"+
                        valorsito.substring(25, 32)+"//";
                JOptionPane.showMessageDialog(cont,xx);
                JOptionPane.showMessageDialog(cont,valorsito);
                System.out.println(valorsito.substring(3, 10));
                System.out.println(valorsito.substring(14, 21));
                System.out.println(valorsito.substring(25, 32));
                */
                //3:+0014.06
                System.out.println(valorsito);
                //01A+00002.98
                //JOptionPane.showMessageDialog(null, valorsito);
                for (int i = 0; i < valorsito.length(); i++) {
                    System.out.println(i+"--"+valorsito.charAt(i));
                }
                if (valorsito.charAt(1) == '1') {
                    label1.setText(valorsito.substring(4, 12));
                }
                if (valorsito.charAt(1) == '2') {
                    label2.setText(valorsito.substring(4, 12));
                }
                if (valorsito.charAt(1) == '3') {
                    label3.setText(valorsito.substring(4, 12));
                }
                if (valorsito.charAt(14) == '1') {
                    label1.setText(valorsito.substring(17, 25));
                }
                if (valorsito.charAt(14) == '2') {
                    label2.setText(valorsito.substring(17, 25));
                }
                if (valorsito.charAt(14) == '3') {
                    label3.setText(valorsito.substring(17, 25));
                }
                if (valorsito.charAt(27) == '1') {
                    label1.setText(valorsito.substring(30, 38));
                }
                if (valorsito.charAt(27) == '2') {
                    label2.setText(valorsito.substring(30, 38));
                }
                if (valorsito.charAt(27) == '3') {
                    label3.setText(valorsito.substring(30, 38));
                }

                 //JOptionPane.showMessageDialog(null, valorsito);
                valorsito = "";

            }
            //System.out.println("Contador:" + contador);
            if (estado.getText().equals("X")) {
                contadorPulsos = 0;
            }
            if (contador == 39) {
                contador = 0;
                if (!estado.getText().equals("X")) {

                    contadorPulsos++;
                    Medicion md = new Medicion(contadorPulsos, Double.parseDouble(graf.getJ1().getText()), Double.parseDouble(graf.getJ2().getText()),
                            Double.parseDouble(graf.getJ3().getText()));
                    this.linked.add(md);
                    graf.graficar();
                    contador = 0;
                    valorsito = "";
                    valor = "";

                }
            }

        }

    }

    public void close() {
        bandera = false;
        serialport.close();
    }
}
