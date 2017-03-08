/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jmmc.swocr.mlp;

import java.io.Serializable;

/**
 * Esta clase representa a la conexion entre dos neuronas.
 * 
 * @author juanmartinez
 */
public class Sinapsis implements Serializable {

	private static final long serialVersionUID = 1L;
	private Neurona emisor;
	private Neurona receptor;
	private double peso;

	public Sinapsis() {
	}

	public Sinapsis(Neurona emisor, Neurona receptor, double peso) {
		this.emisor = emisor;
		this.receptor = receptor;
		this.peso = peso;
	}

	public Neurona getEmisor() {
		return emisor;
	}

	public void setEmisor(Neurona emisor) {
		this.emisor = emisor;
	}

	public Neurona getReceptor() {
		return receptor;
	}

	public void setReceptor(Neurona receptor) {
		this.receptor = receptor;
	}

	public double getPeso() {
		return peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}

	@Override
	public String toString() {
		return "Sinapsis{" + "emisor=" + emisor + ", receptor=" + receptor + ", peso=" + peso + '}';
	}

}
