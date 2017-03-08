/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jmmc.swocr.mlp;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author juanmartinez
 */
public class Capa implements Serializable {

	private static final long serialVersionUID = 1L;
	private final Neurona[] neuronas;
	private Capa capaAnterior;
	private Capa capaSiguiente;

	public Capa(FuncionActivacion funcion, int numero_neuronas) {

		// crear las neuronas de esta capa
		neuronas = new Neurona[numero_neuronas];
		Random ran = new Random();

		for (int i = 0; i < numero_neuronas; i++) {
			neuronas[i] = new Neurona(ran.nextDouble(), funcion); 
			// una nueva neurona con bias aleatorio
		}
	}

	public Neurona[] getNeuronas() {
		return neuronas;
	}

	public Capa getCapaAnterior() {
		return capaAnterior;
	}

	public void setCapaAnterior(Capa capaAnterior) {
		this.capaAnterior = capaAnterior;
	}

	public Capa getCapaSiguiente() {
		return capaSiguiente;
	}

	public void setCapaSiguiente(Capa capaSiguiente) {
		this.capaSiguiente = capaSiguiente;
	}

	@Override
	public String toString() {
		return "Capa{" + "neuronas=" + Arrays.toString(neuronas) + ", capaAnterior=" + capaAnterior + ", capaSiguiente="
				+ capaSiguiente + '}';
	}

	public void conectarNeuronas() {
		Random ran = new Random();

		// verificar si hay una capa antierior para conectar sinapsis.
		if (capaAnterior != null) {
			for (int i = 0; i < neuronas.length; i++) {
				Sinapsis[] ss = new Sinapsis[capaAnterior.getNeuronas().length];
				for (int j = 0; j < capaAnterior.getNeuronas().length; j++) {
					ss[j] = capaAnterior.getNeuronas()[j].getSinapsisSalida()[i];
				}
				neuronas[i].setSinapsisEntrada(ss);
			}
		}

		// verificar si hay una capa que sigue, para crear sinapsis.
		if (capaSiguiente != null) {
			for (int i = 0; i < neuronas.length; i++) {
				Sinapsis[] ss = new Sinapsis[capaSiguiente.getNeuronas().length];
				for (int j = 0; j < capaSiguiente.getNeuronas().length; j++) {
					ss[j] = new Sinapsis(neuronas[i], capaSiguiente.getNeuronas()[j], ran.nextDouble());
				}
				neuronas[i].setSinapsisSalida(ss);
			}
		}
	}

	/**
	 * Este metodo se encarga de actualizar las salidas de todas las neuronas de
	 * esta capa.
	 */
	public void actualizarSalidas() {
		for (Neurona neurona : neuronas) {
			neurona.actualizarSalida();
		}
	}

	/**
	 * Este metodo se para calcular el delta y factor de error pero solo para
	 * una capa que sea la capa de salida.
	 *
	 * @param salidaEsperada
	 * @return
	 */
	public double caluclarDeltaCapaSalida(double[] salidaEsperada) {
		double factorError;
		double delta;
		double salida;
		double error = 0.0;

		for (int i = 0; i < neuronas.length; i++) {
			salida = neuronas[i].getSalida();
			factorError = salidaEsperada[i] - salida;

			// error += Math.abs(factorError); //TODO
			error += Math.pow(factorError, 2);

			delta = salida * (1 - salida) * factorError;
			neuronas[i].setDelta(delta);
		}
		// return error;
		return error / 2.0;
	}

	/**
	 * Este metodo se utiliza para calcular el delta y el factor de error de las
	 * neuronas en una capa oculata. El caluclo es diferente que a una neurona
	 * en la capa de salida.
	 */
	public void calcularDeltaCapaOculta() {
		for (Neurona neurona : neuronas) {
			neurona.actualizarDelta();
		}
	}

	/**
	 * Este metodo se usa para actualizar los bias y pesos de las neuronas de
	 * esta capa, de acuerdo con la taza de aprendizaje que se tiene.
	 *
	 * @param learningRate
	 */
	public void actualizarBiasyPesos(double learningRate) {
		for (Neurona neurona : neuronas) {
			neurona.actualizarBiasyPesos(learningRate);
		}
	}
}
