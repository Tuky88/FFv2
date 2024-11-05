/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.peea.mx.FF.vistas;

import com.peea.mx.FF.controladores.controladorConfig;
import com.peea.mx.FF.controladores.controladorInformacion;
import com.peea.mx.FF.controladores.controladorMedir;
import com.peea.mx.FF.iFrame.configuracioniFrame;
import com.peea.mx.FF.iFrame.informacioniFrame;
import com.peea.mx.FF.iFrame.medicioniFrame;
import java.awt.Dimension;
import static java.awt.Frame.MAXIMIZED_BOTH;
import javax.swing.ImageIcon;

/**
 *
 * @author Sistemas
 */
public class principalVista extends javax.swing.JFrame {

    public medicioniFrame mi;
    public configuracioniFrame ci;
    public informacioniFrame ii;

    public principalVista(String com) {
        initComponents();
        mi = new medicioniFrame(com);
        ci = new configuracioniFrame();
        ii= new informacioniFrame();

        setMinimumSize(new Dimension(700, 400).getSize());
        setExtendedState(MAXIMIZED_BOTH);
        this.setTitle("FIELTROS FINOS S.A. de C.V. ");
        setIconImage(new ImageIcon(getClass().getResource("/com/peea/mx/FF/imagenes/icono.png")).getImage());
        //this.jDesktopPane1.setBorder(new ImagenFondo("icono.png"));
        this.jDesktopPane1.add(mi);
        controladorMedir cmi=new controladorMedir(mi);
        controladorConfig cc=new controladorConfig(ci);
        controladorInformacion cii=new controladorInformacion(ii);
        this.jDesktopPane1.add(ci);
        this.jDesktopPane1.add(ii);
        

    }

    public medicioniFrame getMi() {
        return mi;
    }

    public void setMi(medicioniFrame mi) {
        this.mi = mi;
    }

    public configuracioniFrame getCi() {
        return ci;
    }

    public void setCi(configuracioniFrame ci) {
        this.ci = ci;
    }

    public informacioniFrame getIi() {
        return ii;
    }

    public void setIi(informacioniFrame ii) {
        this.ii = ii;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDesktopPane1 = new javax.swing.JDesktopPane();
        jLabel1 = new javax.swing.JLabel();
        barraMenu = new javax.swing.JMenuBar();
        menuMedicion = new javax.swing.JMenu();
        menuIAbrir = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        menuConfig = new javax.swing.JMenu();
        menuIInfo = new javax.swing.JMenuItem();
        menuAcerca = new javax.swing.JMenu();
        menuInfor = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jDesktopPane1.setBackground(new java.awt.Color(51, 51, 55));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/peea/mx/FF/imagenes/logo3.png"))); // NOI18N

        jDesktopPane1.setLayer(jLabel1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addContainerGap(389, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap(390, Short.MAX_VALUE))
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addContainerGap(192, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap(271, Short.MAX_VALUE))
        );

        menuMedicion.setText("Medición");

        menuIAbrir.setText("Abrir");
        menuMedicion.add(menuIAbrir);
        menuMedicion.add(jSeparator1);

        barraMenu.add(menuMedicion);

        menuConfig.setText("Configuración");

        menuIInfo.setText("Información");
        menuConfig.add(menuIInfo);

        barraMenu.add(menuConfig);

        menuAcerca.setText("Acerca");

        menuInfor.setText("Soporte");
        menuAcerca.add(menuInfor);

        barraMenu.add(menuAcerca);

        setJMenuBar(barraMenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JMenuBar barraMenu;
    public javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel1;
    public javax.swing.JPopupMenu.Separator jSeparator1;
    public javax.swing.JMenu menuAcerca;
    public javax.swing.JMenu menuConfig;
    public javax.swing.JMenuItem menuIAbrir;
    public javax.swing.JMenuItem menuIInfo;
    public javax.swing.JMenuItem menuInfor;
    public javax.swing.JMenu menuMedicion;
    // End of variables declaration//GEN-END:variables
    public static void main(String[] args) {

    }
}
