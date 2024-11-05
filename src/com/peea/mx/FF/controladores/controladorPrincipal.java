/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.peea.mx.FF.controladores;

import com.peea.mx.FF.vistas.principalVista;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Sistemas
 */
public class controladorPrincipal {

    private principalVista pv;

    public controladorPrincipal(principalVista pv) {
        this.pv = pv;
        this.pv.menuIAbrir.addActionListener(new Abrir(this.pv.mi));
        this.pv.menuIInfo.addActionListener(new Abrir(this.pv.ci));
        this.pv.menuInfor.addActionListener(new Abrir(this.pv.ii));
        this.pv.ci.btnGuardar.addActionListener(new Cargar());
        this.pv.ci.btnGuardar.doClick();
       // this.pv.mi.btnFinalizarMed.addActionListener(new FinalizarMed());
    }



    private class Abrir implements ActionListener {

        public JInternalFrame jf;

        public Abrir(JInternalFrame j) {
            jf = j;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            jf.show();
            jf.setMaximizable(true);
        }
    }

    private class Cargar implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            pv.mi.lblEmpresa.setText(pv.ci.txtEmpresa.getText());
            pv.mi.lblDireccion.setText(pv.ci.txtDir.getText());
            pv.mi.lblTelefono.setText(pv.ci.txtTel.getText());
            pv.mi.lblTextNum.setText(pv.ci.txtNumMesa.getText());
            if (pv.ci.rbIN.isSelected()) {
                pv.ci.lblMed.setText("IN");
            } else {
                pv.ci.lblMed.setText("MM");
            }
            pv.mi.lblMed.setText(pv.ci.lblMed.getText());
            if(pv.ci.chkImprimir.isSelected())
            {
                pv.mi.chkImprimir.setSelected(true);
            }
            else
            {
                pv.mi.chkImprimir.setSelected(false);
            }
        }

    }
}
