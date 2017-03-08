/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jmmc.swocr.mlp;

import java.io.Serializable;

/**
 * Esta clase representa a una neurona artificial.
 *
 * @author juanmartinez
 */
public class Neurona implements Serializable {

	private static final long serialVersionUID = 1L;
	private Sinapsis[] sinapsisEntrada;
	private Sinapsis[] sinapsisSalida;
	private double bias;
	private double delta;
	private double factorError;
	private double salida;
	private FuncionActivacion funcion;

	public Neurona() {
	}

	public Neurona(double bias, FuncionActivacion funcion) {
		this.bias = bias;
		this.funcion = funcion;
	}

	public Sinapsis[] getSinapsisEntrada() {
		return sinapsisEntrada;
	}

	public void setSinapsisEntrada(Sinapsis[] sinapsisEntrada) {
		this.sinapsisEntrada = sinapsisEntrada;
	}

	public Sinapsis[] getSinapsisSalida() {
		return sinapsisSalida;
	}

	public void setSinapsisSalida(Sinapsis[] sinapsisSalida) {
		this.sinapsisSalida = sinapsisSalida;
	}

	public double getBias() {
		return bias;
	}

	public void setBias(double bias) {
		this.bias = bias;
	}

	public double getDelta() {
		return delta;
	}

	public void setDelta(double delta) {
		this.delta = delta;
	}

	public double getFactorError() {
		return factorError;
	}

	public void setFactorError(double factorError) {
		this.factorError = factorError;
	}

	public double getSalida() {
		return salida;
	}

	public void setSalida(double salida) {
		this.salida = salida;
	}

	public FuncionActivacion getFuncion() {
		return funcion;
	}

	public void setFuncion(FuncionActivacion funcion) {
		this.funcion = funcion;
	}

	@Override
	public String toString() {
		return "Neurona{" + "bias=" + bias + ", delta=" + delta + ", salida=" + salida + '}';
	}

	/**
	 * Cuando se busaca la salida de la neurona se corre el sumador en donde los
	 * valores de las neuronas emisores son multipicados por su peso. Luego este
	 * valor se le añade el bias, luego se pasa por la funcion de activacion.
	 */
	public void actualizarSalida() {
		double v = 0.0;
		// el sumador "el cuerpo de la neurona"
		for (Sinapsis s : sinapsisEntrada) {
			v += (s.getEmisor().getSalida() * s.getPeso());
		}
		v += bias;
		salida = funcion.activarNeurona(v);
	}

	/**
	 * Con este metodo se aculaliza el bias de esta neurona y se actualizan los
	 * pesos de las sinapsis de entrada.
	 *
	 * @param learningRate
	 *            Taza de Aprendizaje
	 */
	public void actualizarBiasyPesos(double learningRate) {

		// primero se calcula el nuevo bias de esta neurona
		bias = bias + learningRate * 1 * delta;

		// luego se actualizan los pesos a esta neurona.
		double peso;
		for (Sinapsis s : sinapsisEntrada) {
			peso = s.getPeso() + learningRate * 1 * s.getEmisor().getSalida() * this.delta;
			s.setPeso(peso);
		}
	}

	/**
	 * Este metodo para actualizar al factor de error y delta solamente es para
	 * las neuronas en una capa oculta.
	 */
	public void actualizarDelta() {

		// primeros se calcula el factor de error
		// para caluclarlo, se calcula a partir de las delta de las neuronas de
		// la capa siguiente y los pesos.
		factorError = 0.0;
		for (Sinapsis s : sinapsisSalida) {
			// System.out.println(s);
			factorError += (s.getReceptor().getDelta() * s.getPeso());
		}

		// luego se calcula delta
		delta = salida * (1 - salida) * factorError;
	}

}
