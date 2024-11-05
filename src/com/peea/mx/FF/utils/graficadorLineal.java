/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.peea.mx.FF.utils;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Stroke;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Sistemas
 */
public class graficadorLineal {

    private XYSeries[] indices;
    private String[] values;
    private XYDataset dataset;
    private JPanel grafica;
    private double valor;
    private String x;
    private String y;
    private JTextField j1, j2, j3, j4, j5, j6;
    private ChartPanel panelito;
    private static Color COLOR_GRAFICA = Color.WHITE;
    private static Color COLOR_GRAFICA_FONDO = Color.BLACK;
    JFreeChart chart;
    public XYSeriesCollection seriesXY;

    public JFreeChart getChart() {
        return chart;
    }

    public graficadorLineal(String[] indices, JPanel panelin, JTextField j1, JTextField j2, JTextField j3, JTextField j4, JTextField j5, JTextField j6) {
        this.values = indices;
        this.grafica = panelin;
        XYSeries xys1 = new XYSeries("Izquierda");
        XYSeries xys2 = new XYSeries("Centro");
        XYSeries xys3 = new XYSeries("Derecha");
        XYSeries xys4 = new XYSeries("POSITIVO (+)");
        XYSeries xys5 = new XYSeries("NEGATIVO (-)");

        seriesXY = new XYSeriesCollection();
        seriesXY.addSeries(xys1);
        seriesXY.addSeries(xys2);
        seriesXY.addSeries(xys3);
        seriesXY.addSeries(xys4);
        seriesXY.addSeries(xys5);

        dataset = seriesXY;

        chart = ChartFactory.createXYLineChart("", // Título
                "Tiempo", // Etiqueta Coordenada X
                "Grosor", (XYDataset) dataset, // Datos
                PlotOrientation.VERTICAL,
                true, // Muestra la leyenda de los productos (Producto A)
                true,
                true
        );
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(COLOR_GRAFICA_FONDO);
        plot.setOutlinePaint(COLOR_GRAFICA_FONDO);
        plot.setDomainGridlinePaint(Color.black);
        plot.setRangeGridlinePaint(Color.black);
        // XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        XYItemRenderer renderer = plot.getRenderer();

        Stroke stroke = new BasicStroke(3);
        renderer.setSeriesStroke(3, stroke);
        renderer.setSeriesStroke(4, stroke);
        renderer.setSeriesPaint(3, COLOR_GRAFICA);
        renderer.setSeriesPaint(4, COLOR_GRAFICA);
        plot.setRenderer(renderer);
        // change the auto tick unit selection to integer units only...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        chart.setTitle("MESA 2");
        chart.setBackgroundPaint(COLOR_GRAFICA);
        chart.setBorderPaint(COLOR_GRAFICA);

        this.j1 = j1;
        this.j2 = j2;
        this.j3 = j3;
        this.j4 = j4;
        this.j5 = j5;
        this.j6 = j6;

        panelito = new ChartPanel(chart);
        panelito.setBackground(Color.black);
        grafica.setLayout(new java.awt.BorderLayout());
        grafica.setBackground(Color.black);
        grafica.add(panelito, BorderLayout.CENTER);
        grafica.validate();
    }

    public void repaintGrafica() {
        panelito = new ChartPanel(chart);
        panelito.setBackground(Color.black);
        grafica.setLayout(new java.awt.BorderLayout());
        grafica.setBackground(Color.black);
        grafica.add(panelito, BorderLayout.CENTER);
        grafica.validate();
    }

    public JTextField getJ1() {
        return j1;
    }

    public void setJ1(JTextField j1) {
        this.j1 = j1;
    }

    public JTextField getJ2() {
        return j2;
    }

    public void setJ2(JTextField j2) {
        this.j2 = j2;
    }

    public JTextField getJ3() {
        return j3;
    }

    public void setJ3(JTextField j3) {
        this.j3 = j3;
    }

    public void graficar() {

        if (Double.parseDouble(this.j1.getText()) > 0.0
                && Double.parseDouble(this.j2.getText()) > 0.0
                && Double.parseDouble(this.j3.getText()) > 0.0) {
            if (!this.j1.getText().isEmpty() && !this.j2.getText().isEmpty() && !this.j3.getText().isEmpty() && !this.j5.getText().isEmpty()
                    && !this.j6.getText().isEmpty()) {
                this.j4.setText(String.valueOf(Double.parseDouble(this.j4.getText()) + 1));
                System.out.println(j1.getText() + "//" + j2.getText() + "//" + j3.getText() + "//" + j5.getText() + "//" + j6.getText());

                XYSeries xymodi = seriesXY.getSeries(0);
                xymodi.add(Double.parseDouble(this.j4.getText()), Double.parseDouble(this.j1.getText()));
                xymodi = seriesXY.getSeries(1);
                xymodi.add(Double.parseDouble(this.j4.getText()), Double.parseDouble(this.j2.getText()));
                xymodi = seriesXY.getSeries(2);
                xymodi.add(Double.parseDouble(this.j4.getText()), Double.parseDouble(this.j3.getText()));
                xymodi = seriesXY.getSeries(3);
                xymodi.add(Double.parseDouble(this.j4.getText()), Double.parseDouble(this.j5.getText()));
                xymodi = seriesXY.getSeries(4);
                xymodi.add(Double.parseDouble(this.j4.getText()), Double.parseDouble(this.j6.getText()));
                //this.j4.setText(String.valueOf(Double.parseDouble(this.j4.getText()) + 1));

            }
        }
    }

//    @Override
//    public void run() {
//
//        while (true) {
//            synchronized (this) {
//
//                try {
//                    wait(2000);
//                    if (!this.j1.getText().isEmpty() && !this.j2.getText().isEmpty() && !this.j3.getText().isEmpty() && !this.j5.getText().isEmpty()
//                            && !this.j6.getText().isEmpty()) {
//                        this.j4.setText(String.valueOf(Double.parseDouble(this.j4.getText()) + 1));
//                        System.out.println(j1.getText() + "//" + j2.getText() + "//" + j3.getText() + "//" + j5.getText() + "//" + j6.getText());
//                        dataset.addValue(Double.parseDouble(this.j1.getText()), "IZQUIERDA", this.j4.getText());
//                        dataset.addValue(Double.parseDouble(this.j2.getText()), "CENTRO", this.j4.getText());
//                        dataset.addValue(Double.parseDouble(this.j3.getText()), "DERECHA", this.j4.getText());
//                        dataset.addValue(Double.parseDouble(this.j5.getText()), "GROSOR(+)", this.j4.getText());
//                        dataset.addValue(Double.parseDouble(this.j6.getText()), "GROSOR(-)", this.j4.getText());
//                        //this.j4.setText(String.valueOf(Double.parseDouble(this.j4.getText()) + 1));
//
//                    } else {
//                        System.out.println("f:" + j1.getText() + "//" + j2.getText() + "//" + j3.getText() + "//" + j5.getText() + "//" + j6.getText());
//                    }
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(graficadorLineal.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        }
//
//    }
}
