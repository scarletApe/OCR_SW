/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jmmc.swocr.mlp;

import java.io.Serializable;

/**
 * Esta funcion produce un valor suave de 0 a 1.
 *
 * @author juanmartinez
 */
public class FuncionSigmoidea implements FuncionActivacion, Serializable {

    private static final long serialVersionUID = 1L;
    
    @Override
    public double activarNeurona(double v) {
        return 1.0 / (1.0 + Math.exp(-v));
    }

}
