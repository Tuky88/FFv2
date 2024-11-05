/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.peea.mx.FF.controladores;

import com.barcodelib.barcode.QRCode;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.peea.mx.FF.Serial.LecturaSer;
import com.peea.mx.FF.iFrame.medicioniFrame;
import com.peea.mx.FF.modelos.Medicion;
import com.peea.mx.FF.pruebas.CrearFicherosExcel;
import com.peea.mx.FF.utils.Convertidor;
import com.peea.mx.FF.utils.graficadorLineal;
import com.peea.mx.FF.utils.mensajeAutomatico;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.ColorSupported;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.PrinterName;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.jfree.chart.ChartUtilities;

/**
 *
 * @author Sistemas
 */
public class controladorMedir {

    public medicioniFrame mi;
    String[] conjunto = {"GROSOR(+)", "GROSOR(-)", "IZQUIERDA", "CENTRO", "DERECHA"};

    public controladorMedir(medicioniFrame mi) {
        this.mi = mi;
        this.mi.btnCargarDatos.addActionListener(new Cargar(this.mi.jt, this.mi.valor));
        this.mi.btnIniciarMed.addActionListener(new Medir());
        this.mi.btnFinalizarMed.addActionListener(new FinMed());
        this.mi.btnCalcularDen.addActionListener(new CalcularDen());
        this.mi.chkAPlanchado.addActionListener(new checkExcluyente(this.mi.chkAPlanchado, this.mi.chkDPlanchado));
        this.mi.chkDPlanchado.addActionListener(new checkExcluyente(this.mi.chkDPlanchado, this.mi.chkAPlanchado));
        this.mi.med = new LinkedList();
        this.mi.txtEspesorMM.addKeyListener(new VerificarNumero());
        this.mi.txtDensidad.addKeyListener(new VerificarNumero());
        this.mi.txtEspesorIn.addKeyListener(new VerificarNumero());
        this.mi.txtMetrosM.addKeyListener(new VerificarNumero());
        this.mi.txtPeso.addKeyListener(new VerificarNumero());
        this.mi.txtToleranciaNegMM.addKeyListener(new VerificarNumero());
        this.mi.txtToleranciaNegIn.addKeyListener(new VerificarNumero());
        this.mi.txtToleranciaPosMM.addKeyListener(new VerificarNumero());
        this.mi.txtToleranciaPosIn.addKeyListener(new VerificarNumero());
        this.mi.txtEspesorMM.addKeyListener(new Convertir(mi.txtEspesorMM, mi.txtEspesorIn, 1));
        this.mi.txtToleranciaPosMM.addKeyListener(new Convertir(mi.txtToleranciaPosMM, mi.txtToleranciaPosIn, 1));
        this.mi.txtToleranciaNegMM.addKeyListener(new Convertir(mi.txtToleranciaNegMM, mi.txtToleranciaNegIn, 1));
        this.mi.txtEspesorIn.addKeyListener(new Convertir(mi.txtEspesorIn, mi.txtEspesorMM, 2));
        this.mi.txtToleranciaPosIn.addKeyListener(new Convertir(mi.txtToleranciaPosIn, mi.txtToleranciaPosMM, 2));
        this.mi.txtToleranciaNegIn.addKeyListener(new Convertir(mi.txtToleranciaNegIn, mi.txtToleranciaNegMM, 2));
        this.mi.btnGenerarRepo.addActionListener(new GenerarRepo());
        //this.mi.txtObtenerMed.addActionListener(new Obtener());
        this.mi.btnCargarLector.addActionListener(new CargarLector());
        mi.ls = new LecturaSer(conjunto, mi.grafica, mi.txtMedidaIzq, mi.txtMedidaCentro, mi.txtMedidaDerecha,
                mi.txtPulsos, mi.txtToleranciaPosMM, mi.txtToleranciaNegMM, 9600, mi.getCom(), mi.lblExtado, mi.med);

        mi.lblExtado.setText("X");
        mi.btnFinalizarMed.setEnabled(false);
        mi.btnGenerarRepo.setEnabled(false);

        this.correrHilo();
    }

    public void imprimirArchivo(File archivo) {
        java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
        if (desktop.isSupported(Desktop.Action.PRINT)) {
            try {
                desktop.print(archivo);
            } catch (Exception e) {
                System.out.print("El sistema no permite imprimir usando la clase Desktop");
                e.printStackTrace();
            }
        } else {
            System.out.print("El sistema no permite imprimir usando la clase Desktop");
        }
    }

    public double calcularProm() {
        double prom = 0;
        LinkedList lss = mi.med;

        double acumulador = 0;
        for (int i = 0; i < lss.size(); i++) {
            Medicion med = (Medicion) lss.get(i);
            acumulador += med.getCentro() + med.getIzquierda() + med.getDerecha();
            System.out.println(acumulador);
            System.out.println(med.toString());
        }
        prom = acumulador / (lss.size() * 3);
        return prom;
    }

    public void generarArchivo() {
        String nombreArchivo;
        Date hoy = new Date();
        SimpleDateFormat s = new SimpleDateFormat("dd-MM-yy");
        SimpleDateFormat d = new SimpleDateFormat("hh-mm-ss");
        String nombreG = mi.txtCliente.getText() + "_" + mi.txtPieza.getText();
        nombreArchivo = "Reporte_" + s.format(hoy) + "_" + d.format(hoy) + "_" + nombreG;
        CrearFicherosExcel cfe = new CrearFicherosExcel(nombreArchivo + ".xlsx", "C:\\FieltrosFinosFiles\\reportes\\",
                "Hoja1", "C:\\FieltrosFinos\\src\\com\\peea\\mx\\FF\\Archivos\\format.xlsx");
        if (mi.lblMed.getText().equals("IN")) {
            cfe.setStatus(false);
        } else {
            cfe.setStatus(true);
        }
        int iniciox = 11;
        //cfe.escribir(1, 1, mi.lblEmpresa.getText());
        //cfe.escribir(2, 2, mi.lblDireccion.getText());
        cfe.Sescribir(1, 8, mi.txtCliente.getText(), 1);
        cfe.Sescribir(7, 10, mi.txtDiametro.getText(), 1);
        //cfe.Sescribir(7, 10, mi.txtEncargado.getText(), 1);
        cfe.Sescribir(8, 1, mi.txtEncargado.getText(), 1);
        cfe.Sescribir(5, 8, mi.txtPartida.getText(), 1);
        cfe.Sescribir(6, 8, mi.txtPieza.getText(), 1);
        cfe.Sescribir(7, 8, mi.txtPeso.getText(), 1);
        //cfe.Sescribir(3, 8, mi..getText(), 1);
        cfe.Sescribir(4, 8, mi.txtPO.getText(), 1);
        cfe.Sescribir(5, 1, mi.lblFecha.getText(), 1);
        cfe.Sescribir(6, 1, mi.txtTiempoInicio.getText(), 1);
        cfe.Sescribir(7, 1, mi.txtTiempoFin.getText(), 1);
        cfe.Sescribir(3, 1, mi.txtEstilo.getText(), 1);
        cfe.Sescribir(5, 4, mi.txtEspesorMM.getText(), 0);
        cfe.Sescribir(5, 5, mi.txtEspesorIn.getText(), 0);
        cfe.Sescribir(6, 4, mi.txtToleranciaPosMM.getText(), 0);
        cfe.Sescribir(6, 5, mi.txtToleranciaPosIn.getText(), 0);
        cfe.Sescribir(7, 4, mi.txtToleranciaNegMM.getText(), 0);
        cfe.Sescribir(7, 5, mi.txtToleranciaNegIn.getText(), 0);
        if (mi.txtRangoMM.getText().length() > 8) {
            cfe.Sescribir(8, 4, mi.txtRangoMM.getText().substring(0, 7), 0);
        } else {
            cfe.Sescribir(8, 4, mi.txtRangoMM.getText(), 0);
        }
        cfe.Sescribir(4, 10, mi.txtMetrosM.getText(), 0);
        cfe.Sescribir(5, 10, mi.txtAnchoM.getText(), 0);
        double superior, inferior;
        superior = Double.parseDouble(mi.txtToleranciaPosMM.getText());
        inferior = Double.parseDouble(mi.txtToleranciaNegMM.getText());
        double prom = calcularProm();
        DecimalFormat df = new DecimalFormat("#0.000");
        while (!mi.med.isEmpty()) {
            Medicion medi = (Medicion) mi.med.pop();
            System.out.println(medi.toString());
            cfe.escribir(medi, iniciox, superior, inferior);
            //cfe.escribir(iniciox, inicioy, String.valueOf(medi.getIzquierda()));
            iniciox++;
        }
        String formula = "AVERAGE(D12:D" + iniciox + ")";
        System.out.println(formula);

        //////PROMEDIO
        cfe.escribir(iniciox + 2, 1, "PROMEDIO:", 1);
        cfe.escribir(iniciox + 2, 3, formula, 0);
        formula = "AVERAGE(E12:E" + iniciox + ")";
        System.out.println(formula);

        cfe.escribir(iniciox + 2, 4, formula, 0);
        formula = "AVERAGE(G12:G" + iniciox + ")";
        System.out.println(formula);

        cfe.escribir(iniciox + 2, 6, formula, 0);
        formula = "AVERAGE(H12:H" + iniciox + ")";
        System.out.println(formula);

        cfe.escribir(iniciox + 2, 7, formula, 0);
        formula = "AVERAGE(J12:J" + iniciox + ")";
        System.out.println(formula);

        cfe.escribir(iniciox + 2, 9, formula, 0);
        formula = "AVERAGE(K12:K" + iniciox + ")";
        System.out.println(formula);

        cfe.escribir(iniciox + 2, 10, formula, 0);
        /////PROMEDIO GRAL
        cfe.escribir(iniciox + 4, 1, "PROMEDIO GRAL:", 1);
        mi.txtGral.setText(df.format(prom));
        cfe.escribir(iniciox + 4, 3, mi.txtGral.getText(), 1);
        formula = "AVERAGE(E" + (iniciox + 3) + ",H" + (iniciox + 3) + ",K" + (iniciox + 3) + ")";
        System.out.println(formula);

        cfe.escribir(iniciox + 4, 4, formula, 0);
        /////MIN MAX
        cfe.escribir(iniciox + 5, 1, "LECTURA MINIMA:", 1);
        formula = "MIN(D12:D" + iniciox + ",G12:G" + iniciox + ",J12:J" + iniciox + ")";
        System.out.println(formula);

        cfe.escribir(iniciox + 5, 3, formula, 0);
        formula = "MIN(E12:E" + iniciox + ",H12:H" + iniciox + ",K12:K" + iniciox + ")";
        System.out.println(formula);

        cfe.escribir(iniciox + 5, 4, formula, 0);
        cfe.escribir(iniciox + 6, 1, "LECTURA MAXIMA:", 1);
        formula = "MAX(D12:D" + iniciox + ",G12:G" + iniciox + ",J12:J" + iniciox + ")";
        System.out.println(formula);

        cfe.escribir(iniciox + 6, 3, formula, 0);
        formula = "MAX(E12:E" + iniciox + ",H12:H" + iniciox + ",K12:K" + iniciox + ")";
        cfe.escribir(iniciox + 6, 4, formula, 0);
        System.out.println(formula);

        /////DESVIACION ESTANDAR
        cfe.escribir(iniciox + 7, 1, "DESVIACION ESTANDAR:", 1);
        formula = "STDEV(D12:D" + iniciox + ",G12:G" + iniciox + ",J12:J" + iniciox + ")";
        System.out.println(formula);

        cfe.escribir(iniciox + 7, 3, formula, 0);
        double desv = cfe.leer(iniciox + 7, 3);
        cfe.escribir(iniciox + 7, 3, df.format(desv), 0);
        /////CAPACIDAD DEL PROCESO CP
        cfe.escribir(iniciox + 8, 1, "CAPACIDAD DEL PROCESO CP:", 1);
        formula = "(E7-E8)/(6*" + desv + ")";
        System.out.println(formula);
        cfe.escribir(iniciox + 8, 3, formula, 0);
        cfe.escribir(iniciox + 8, 3, df.format(cfe.leer(iniciox + 8, 3)), 0);
        /////CAPACIDAD DEL PROCESO CPK
        cfe.escribir(iniciox + 9, 1, "CAPACIDAD DEL PROCESO CPK:", 1);
        //=MIN((E8-D49)/(3*0.122),(D49-E7)/(3*0.122))
        double v1 = Double.parseDouble(mi.txtToleranciaPosMM.getText()) - prom;
        double v2 = prom - Double.parseDouble(mi.txtToleranciaNegMM.getText());
        double divisor = desv * 3;
        v1 = v1 / divisor;
        v2 = v2 / divisor;
        //JOptionPane.showMessageDialog(mi,v1+"////"+v2);
        if (v1 <= v2) {
            cfe.escribir(iniciox + 9, 3, df.format(v1), 1);
        } else {
            cfe.escribir(iniciox + 9, 3, df.format(v2), 1);
        }
        /////%DES
        cfe.escribir(iniciox + 4, 6, "%DES:", 1);
        formula = "(D" + (iniciox + 5) + "/E6)*100";
        System.out.println(formula);
        cfe.escribir(iniciox + 4, 7, formula, 0);
        cfe.escribir(iniciox + 4, 7, df.format(cfe.leer(iniciox + 4, 7)), 0);
        ///////%RAN
        cfe.escribir(iniciox + 5, 6, "%RAN:", 1);
        formula = "((D" + (iniciox + 7) + "-D" + (iniciox + 6) + ")/D" + (iniciox + 5) + ")*100";
        System.out.println(formula);
        cfe.escribir(iniciox + 5, 7, formula, 0);

        cfe.escribir(iniciox + 5, 7, df.format(cfe.leer(iniciox + 5, 7)), 0);
        //////%ACEP
        cfe.escribir(iniciox + 6, 6, "%ACEP:", 1);
        formula = "(" + cfe.leer(iniciox + 5, 7) + "/E9)*100";
        System.out.println(formula);
        cfe.escribir(iniciox + 6, 7, formula, 0);

        cfe.escribir(iniciox + 6, 7, df.format(cfe.leer(iniciox + 6, 7)), 0);
        ///DENSIDAD
        cfe.Sescribir(6, 10, mi.txtDensidad.getText(), 0);

        System.out.println("terminando de cargar los datos...");
        cfe.cargarArchivo();
        
        if (mi.chkImprimir.isSelected()) {
            //ponerImpresora("epson");
            cfe.imprimirArchivo();
        }
        cfe.abrirArchivo();
    }

    private void correrHilo() {
        System.out.println("corriendo...");
        mi.ls.setBandera(true);
        mi.ls.start();
    }

    public void calcularRango() {
        double rango = (Double.parseDouble(mi.txtToleranciaPosMM.getText()) - Double.parseDouble(mi.txtToleranciaNegMM.getText()))
                / Double.parseDouble(mi.txtEspesorMM.getText());

        mi.txtRangoMM.setText("" + rango * 100);
    }

    public void ponerImpresora(String archivo) throws IOException {

        ProcessBuilder pb = new ProcessBuilder("cmd", "/c", archivo + ".bat");
        File dir = new File("C:\\FieltrosFinos\\src\\com\\peea\\mx\\FF\\EXEC\\");
        pb.directory(dir);
        Process p = pb.start();
    }

    private class CargarLector implements ActionListener {

        public CargarLector() {
        }

        @Override
        public void actionPerformed(ActionEvent ae) {

            String resp = JOptionPane.showInputDialog(null, "Utiliza el lector para leer el QR", "LECTOR DE QR", 1);
            System.out.println(resp);
            if (resp != null) {
                String[] corte = resp.split("-");

                String[] datos = corte[0].split("//");
                if (datos.length == 16) {
                    mi.txtAnchoM.setText(datos[0]);
                    mi.txtCliente.setText(datos[1]);
                    mi.txtDensidad.setText(datos[2]);
                    mi.txtEspesorIn.setText(datos[3]);
                    mi.txtEspesorMM.setText(datos[4]);
                    mi.txtEstilo.setText(datos[5]);
                    mi.txtMetrosM.setText(datos[6]);
                    mi.txtPO.setText(datos[7]);
                    mi.txtPartida.setText(datos[8]);
                    mi.txtPeso.setText(datos[9]);
                    mi.txtPieza.setText(datos[10]);
                    mi.txtRangoMM.setText(datos[11]);
                    mi.txtToleranciaNegIn.setText(datos[12]);
                    mi.txtToleranciaNegMM.setText(datos[13]);
                    mi.txtToleranciaPosIn.setText(datos[14]);
                    mi.txtToleranciaPosMM.setText(datos[15]);
                } else {
                    JOptionPane.showMessageDialog(mi, "CODIGO INVALIDO", "ERROR", 0);
                }
            }

        }
    }

    public void imprimirEspecifico(String ruta) throws FileNotFoundException, PrintException, IOException, PrinterException {

        //Formato de Documento
        String name = "C:\\FieltrosFinos\\src\\com\\peea\\mx\\FF\\etiquetas\\" + ruta + ".pdf";
//        System.out.println(name);
        PDDocument document = PDDocument.load(new File(name));
        PrintService myPrintService = findPrintService("DYMO LabelWriter 450");

        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPageable(new PDFPageable(document));
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        aset.add(MediaSizeName.ISO_A7);
        job.setPrintService(myPrintService);
        PageFormat format = new PageFormat();
        Paper p = new Paper();
        p.setSize(2, 10);
        format.setPaper(p);
        job.print(aset);

        document.close();

    }

    public PrintService findPrintService(String printerName) {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService printService : printServices) {
            if (printService.getName().trim().equals(printerName)) {
                return printService;
            }
        }
        return null;
    }

    private class VerificarNumero implements KeyListener {

        public VerificarNumero() {
        }

        @Override
        public void keyTyped(KeyEvent e) {
            char x = e.getKeyChar();
            if (Character.isAlphabetic(x)) {
                e.consume();
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        }

        @Override
        public void keyReleased(KeyEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }

    private class checkExcluyente implements ActionListener {

        private JCheckBox cha, chd;

        public checkExcluyente(JCheckBox cha, JCheckBox chd) {
            this.cha = cha;
            this.chd = chd;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (cha.isSelected()) {
                chd.setSelected(false);
            }
        }
    }

    private class CalcularDen implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (mi.chkAPlanchado.isSelected()) {
                double densidad = 0;
                if (mi.txtPeso.getText().isEmpty() || mi.txtAnchoM.getText().isEmpty()
                        || mi.txtMetrosM.getText().isEmpty() || mi.txtEspesorMM.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(mi, "¡Faltan campos!", "ERROR", 0);
                } else {
                    densidad = (Double.parseDouble(mi.txtPeso.getText())) / (Double.parseDouble(mi.txtAnchoM.getText())
                            * Double.parseDouble(mi.txtMetrosM.getText()) * Double.parseDouble(mi.txtEspesorMM.getText()));
                    DecimalFormat df = new DecimalFormat("#0.0000");
                    mi.txtDensidad.setText("" + df.format(densidad));
                }
            } else if (mi.chkDPlanchado.isSelected()) {
                double densidad = 0;
                if (mi.txtPeso.getText().isEmpty() || mi.txtAnchoM.getText().isEmpty()
                        || mi.txtMetrosM.getText().isEmpty() || mi.txtEspesorMM.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(mi, "¡Faltan campos!", "ERROR", 0);
                } else {
                    double espGral = 0;
                    espGral = calcularProm();
                    //JOptionPane.showMessageDialog(mi,espGral);
                    densidad = (Double.parseDouble(mi.txtPeso.getText())) / (Double.parseDouble(mi.txtAnchoM.getText())
                            * Double.parseDouble(mi.txtMetrosM.getText()) * espGral);
                    DecimalFormat df = new DecimalFormat("#0.0000");
                    mi.txtDensidad.setText("" + df.format(densidad));
                    mi.txtGral.setText("" + df.format(espGral));
                }
            } else {
                JOptionPane.showMessageDialog(mi, "SELECCIONE UN MÉTODO DE PLANCHADO", "ERROR", 1);
            }
        }

    }

    public double calcularDensidad(double peso, double ancho, double metros, double espesor) {
        double densidad;
        densidad = (peso) / (ancho) * metros * espesor;
        DecimalFormat df = new DecimalFormat("#0.0000");
        return Double.parseDouble(df.format(densidad));
    }

    private class FinMed implements ActionListener {

        public FinMed() {

        }

        @Override
        public void actionPerformed(ActionEvent e) {

            //mi.ls.wait();
            if (!mi.btnIniciarMed.isEnabled()) {
                mi.ls.setBandera(false);
                System.out.println(mi.med.size());
                mi.txtTiempoFin.setText(mi.lblHora.getText());
                mi.btnFinalizarMed.setEnabled(false);
                //mi.btnIniciarMed.setEnabled(true);
                //mi.btnCargarDatos.setEnabled(true);
                mi.lblExtado.setText("X");
                mi.btnGenerarRepo.setEnabled(true);
                //JOptionPane.showMessageDialog(mi, "¡Fin de medición!", "AVISO", 2);

            } else {
                JOptionPane.showMessageDialog(mi, "¡No hay ninguna medición activa!", "ERROR", 0);
            }

        }

    }

    private class Medir implements ActionListener {

        public Medir() {

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            //mi.ls=new LecturaSer(mi.txtMedidaIzq,4800,"COM4");
            //mi.ls.start();
            if (mi.txtCliente.getText().isEmpty() || mi.txtEspesorIn.getText().isEmpty() || mi.txtEspesorMM.getText().isEmpty()
                    || mi.txtEstilo.getText().isEmpty() || mi.txtPO.getText().isEmpty()
                    || mi.txtPeso.getText().isEmpty() || mi.txtPieza.getText().isEmpty() || mi.txtRangoMM.getText().isEmpty() || mi.txtToleranciaNegIn.getText().isEmpty()
                    || mi.txtToleranciaNegMM.getText().isEmpty() || mi.txtToleranciaPosIn.getText().isEmpty() || mi.txtToleranciaPosMM.getText().isEmpty()) {
                JOptionPane.showMessageDialog(mi, "¡Faltan campos!", "ERROR", 0);
            } else {
                JOptionPane.showMessageDialog(mi, "¡Comenzando medición!", "AVISO", 2);
                mi.txtTiempoInicio.setText(mi.lblHora.getText());
                mi.btnIniciarMed.setEnabled(false);
                mi.btnFinalizarMed.setEnabled(true);
                mi.btnCargarDatos.setEnabled(false);
                mi.lblExtado.setText("-");
                if (!mi.txtPulsos.getText().equals("0.0")) {

                    //mi.ls.iniGrafica();
                    mi.txtPulsos.setText("0.0");
                    //mi.ls.getGraf().getChart().
                    mi.ls.setBandera(true);
                    graficadorLineal gf;
                    if (mi.lblMed.getText().equals("MM")) {

                        gf = new graficadorLineal(conjunto, mi.grafica, mi.txtMedidaIzq, mi.txtMedidaCentro, mi.txtMedidaDerecha,
                                mi.txtPulsos, mi.txtToleranciaPosMM, mi.txtToleranciaNegMM);
                    } else {
                        gf = new graficadorLineal(conjunto, mi.grafica, mi.txtMedidaIzq, mi.txtMedidaCentro, mi.txtMedidaDerecha,
                                mi.txtPulsos, mi.txtToleranciaPosIn, mi.txtToleranciaNegIn);
                    }
                    mi.ls.setGraf(gf);
                    //mi.ls.getGraf().iniGrafica();

                }
//
            }
        }
    }

    private class Cargar implements ActionListener {

        LinkedList jf;
        String[] valor;

        public void Convertir() {
            DecimalFormat df = new DecimalFormat("#0.0000");
            DecimalFormat df1 = new DecimalFormat("#0.000");
            Convertidor cv = new Convertidor();

            mi.txtEspesorMM.setText("" + df.format(cv.INtoMM(Double.parseDouble(mi.txtEspesorIn.getText()))));
            mi.txtToleranciaNegMM.setText("" + df.format(cv.INtoMM(Double.parseDouble(mi.txtToleranciaNegIn.getText()))));
            mi.txtToleranciaPosMM.setText("" + df.format(cv.INtoMM(Double.parseDouble(mi.txtToleranciaPosIn.getText()))));

            calcularRango();

        }

        public Cargar(LinkedList jf, String[] valor) {
            this.jf = jf;
            this.valor = valor;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            Object[] obj = new Object[jf.size()];
            jf.toArray(obj);
            int i = 0;
            for (int j = 0; j < jf.size(); j++) {

                JTextField text = (JTextField) obj[i];
                String value = "-";
                try {
                    while (value.equals("-")) {
                        value = JOptionPane.showInputDialog(mi, "Valor del " + valor[i] + ":", valor[i] + ":", 2);

                        if (value != null) {
                            if (value.isEmpty()) {
                                value = "-";
                            }
                            text.setText(value);
                        }
                        if (i == 2) {
                            Convertir();
                        }

                    }
                    i++;
                } catch (NullPointerException np) {
                    JOptionPane.showMessageDialog(mi, "Valor incorrecto");
                }
            }

        }
    }

    private class Convertir implements KeyListener {

        private JTextField j1, j2;
        private int tipo;

        public Convertir(JTextField j1, JTextField j2, int tipo) {
            this.j1 = j1;
            this.j2 = j2;
            this.tipo = tipo;

        }

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                Convertidor c = new Convertidor();
                if (tipo == 1) {
                    j2.setText(Double.toString(c.MMtoIN(Double.parseDouble(j1.getText()))));
                } else {
                    j2.setText(Double.toString(c.INtoMM(Double.parseDouble(j1.getText()))));
                }
                calcularRango();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        }

    }

    private class GenerarRepo implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if ((mi.chkAPlanchado.isSelected() && !mi.txtMetrosM.getText().isEmpty() && !mi.txtAnchoM.getText().isEmpty() && !mi.txtDensidad.getText().isEmpty())
                    || (mi.chkDPlanchado.isSelected() && !mi.txtMetrosM.getText().isEmpty() && !mi.txtAnchoM.getText().isEmpty())) {

                if (!mi.txtPulsos.getText().equals("0.0")) {
                    
                    try {
                        mensajeAutomatico ma = new mensajeAutomatico();
                        //ma.mostrar_pregunta("Generando reporte...", 4);
                        String nombreArchivo;
                        Date hoy = new Date();
                        SimpleDateFormat s = new SimpleDateFormat("dd-MM-yy");
                        SimpleDateFormat d = new SimpleDateFormat("hh-mm-ss");
                        String nombreG = mi.txtCliente.getText() + "_" + mi.txtPieza.getText();
                        nombreArchivo = "Grafica_" + s.format(hoy) + "_" + d.format(hoy) + "_" + nombreG;
                        ChartUtilities.saveChartAsPNG(
                                new File("C:\\FieltrosFinosFiles\\grafica\\" + nombreArchivo + ".png"),
                                mi.ls.getGraf().getChart(), 1200, 480);
                        generarArchivo();

                        generarQR(nombreArchivo);
                        try {
                            generarEtiqueta(nombreArchivo);
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(controladorMedir.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (DocumentException ex) {
                            Logger.getLogger(controladorMedir.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        //ponerImpresora("dymo");
                        //imprimirArchivo(new File("C:\\FieltrosFinos\\src\\com\\peea\\mx\\FF\\etiquetas\\" + nombreArchivo + ".pdf"));
                    } catch (IOException ex) {
                        Logger.getLogger(controladorMedir.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    }else {
                    JOptionPane.showMessageDialog(mi, "NO SE CAPTURÓ NINGUNA MEDICIÓN", "ERROR", 1);
                }

                    mi.btnCargarDatos.setEnabled(true);
                    mi.btnFinalizarMed.setEnabled(false);
                    mi.btnIniciarMed.setEnabled(true);
                    mi.btnGenerarRepo.setEnabled(false);
                } else {
                    JOptionPane.showMessageDialog(mi, "FALTAN CAMPOS PARA GENERAR EL REPORTE!");
                }
            }

        }

        public void generarEtiqueta(String nombre) throws FileNotFoundException, DocumentException, BadElementException, IOException {
            String dest = "C:\\FieltrosFinos\\src\\com\\peea\\mx\\FF\\etiquetas\\" + nombre + ".pdf";
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(dest));
            Rectangle one = new Rectangle(200, 400);
            document.setPageSize(one);
            document.setMargins(0.2f, 0.5f, 0.2f, 0.2f);
            document.open();
            Paragraph p = new Paragraph("\nFIELTROS FINOS S.A. DE C.V. \n");
            Paragraph p2 = new Paragraph("CLIENTE: " + mi.txtCliente.getText() + "  \n"
                    + "ESPESOR:" + mi.txtEspesorMM.getText() + "MM \t" + mi.txtEspesorIn.getText() + "IN \n"
                    + "ESTILO:" + mi.txtEstilo.getText()
                    + "P.O # "+ mi.txtPO.getText());
            document.add(p);
            String imFile = "C:\\FieltrosFinos\\src\\com\\peea\\mx\\FF\\qr\\" + nombre + ".png";
            Image image = Image.getInstance(imFile);
            float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                    - document.rightMargin()) / image.getWidth()) * 85;

            image.scalePercent(scaler);
            document.add(p2);
            p.setAlignment(2);
            image.setAlignment(1);
            document.add(image);
            Paragraph p1 = new Paragraph("\n\n\nFECHA:" + mi.lblFecha.getText() + " HORA:" + mi.lblHora.getText());
            document.add(p1);
            document.close();
        }

        public void generarQR(String nombre) {
            try {
                QRCode qr = new QRCode();
                String cadena = "";
                cadena = mi.txtAnchoM.getText() + "//" + mi.txtCliente.getText() + "//" + mi.txtDensidad.getText() + "//" + mi.txtEspesorIn.getText() + "//"
                        + mi.txtEspesorMM.getText() + "//" + mi.txtEstilo.getText() + "//" + mi.txtMetrosM.getText() + "//"
                        + mi.txtPO.getText() + "//" + mi.txtPartida.getText() + "//" + mi.txtPeso.getText() + "//" + mi.txtPieza.getText() + "//"
                        + mi.txtRangoMM.getText() + "//" + mi.txtToleranciaNegIn.getText() + "//" + mi.txtToleranciaNegMM.getText() + "//" + mi.txtToleranciaPosIn.getText() + "//"
                        + mi.txtToleranciaPosMM.getText();
                qr.setData(cadena + "-");
                qr.setDataMode(QRCode.MODE_BYTE);
                qr.setUOM(0);
                qr.setResolution(500);
                qr.setRightMargin(0.000f);
                qr.setTopMargin(0.000f);
                qr.setLeftMargin(0.000f);
                qr.setBottomMargin(0.000f);
                qr.setModuleSize(10);
                //String f="C:\\FieltrosFinos\\src\\com\\peea\\mx\\FF\\imagenes\\codigo.png";
                String f = "C:\\FieltrosFinos\\src\\com\\peea\\mx\\FF\\qr\\" + nombre + ".png";
                qr.renderBarcode(f);
    

//To change body of generated methods, choose Tools | Templates.
            } catch (Exception ex) {
                Logger.getLogger(controladorMedir.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
//resortes deportivos para sonic chevrolet 2017 

