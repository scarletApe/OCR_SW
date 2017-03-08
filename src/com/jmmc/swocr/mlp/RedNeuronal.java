/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jmmc.swocr.mlp;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Esta red es un Multi-Layered Perceptron (MLP), un Perceptron Multi-Capa. Este
 * MLP solo funciona con la Funcion de Activacion Sigmoidea. En teoria funciona
 * con más de una capa oculta, pero en la actualidad solo funciona con una capa
 * oculta.
 *
 * @author juanmartinez
 */
public class RedNeuronal implements Serializable, MLP {

	private static final long serialVersionUID = 1L;
	private Capa capaEntrada;
	private Capa capasOcultas[];
	private Capa capaSalida;
	private String nombre;
	private ArrayList<Point.Double> errores;

	public RedNeuronal() {
	}

	public RedNeuronal(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public String getNombre() {
		return nombre;
	}

	@Override
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Capa getCapaEntrada() {
		return capaEntrada;
	}

	public void setCapaEntrada(Capa capaEntrada) {
		this.capaEntrada = capaEntrada;
	}

	public Capa[] getCapasOcultas() {
		return capasOcultas;
	}

	public void setCapasOcultas(Capa[] capasOcultas) {
		this.capasOcultas = capasOcultas;
	}

	public Capa getCapaSalida() {
		return capaSalida;
	}

	public void setCapaSalida(Capa capaSalida) {
		this.capaSalida = capaSalida;
	}

	@Override
	public ArrayList<Point.Double> getErrores() {
		return errores;
	}

	@Override
	public void setErrores(ArrayList<Point.Double> errores) {
		this.errores = errores;
	}

	/**
	 * Con este metodo se inicializa la red neuronal, el constructor no hace
	 * nada.
	 *
	 * @param num_entrada
	 *            Numero de neuronas en la capa de entrada
	 * @param num_ocultas
	 *            Numero de capas ocultas y cuantas neuronas en cada oculta.
	 * @param num_salida
	 *            Numero de neuronas en la capa de salida.
	 * @param funcion
	 *            Que funcion de activacion, pero la unica que funcionará es la
	 *            de sigmoide.
	 */
	@Override
	public void crearRed(int num_entrada, int[] num_ocultas, int num_salida, FuncionActivacion funcion) {

		// crear las capas
		capaEntrada = new Capa(funcion, num_entrada);
		capasOcultas = new Capa[num_ocultas.length];
		for (int i = 0; i < capasOcultas.length; i++) {
			capasOcultas[i] = new Capa(funcion, num_ocultas[i]);
		}
		capaSalida = new Capa(funcion, num_salida);

		// conectar las capas
		capaEntrada.setCapaSiguiente(capasOcultas[0]);
		conectarOcultas();
		capaSalida.setCapaAnterior(capasOcultas[capasOcultas.length - 1]);

		// conectar las neuronas, esto crea las sinapsis
		capaEntrada.conectarNeuronas();
		for (Capa capasOculta : capasOcultas) {
			capasOculta.conectarNeuronas();
		}
		capaSalida.conectarNeuronas();
	}

	/**
	 * metodo privado que conecta las capas ocultas con las demas.
	 */
	private void conectarOcultas() {
		int ocultas = capasOcultas.length;
		if (ocultas == 1) {
			// solo hay una capa oculta
			capasOcultas[0].setCapaAnterior(capaEntrada);
			capasOcultas[0].setCapaSiguiente(capaSalida);
		} else if (ocultas == 2) {
			// solo hay dos capas ocultas
			capasOcultas[0].setCapaAnterior(capaEntrada);
			capasOcultas[0].setCapaSiguiente(capasOcultas[1]);

			capasOcultas[1].setCapaAnterior(capasOcultas[0]);
			capasOcultas[1].setCapaSiguiente(capaSalida);
		} else if (ocultas >= 3) {
			// hay mas de tres capas
			for (int i = 0; i < ocultas; i++) {
				if (i == 0) {
					// es la primera conectar con la capa de entrada
					capasOcultas[i].setCapaAnterior(capaEntrada);
					capasOcultas[i].setCapaSiguiente(capasOcultas[i + 1]);
					continue;
				}
				if (i == ocultas - 1) {
					// es la ultima capa oculta
					capasOcultas[i].setCapaAnterior(capasOcultas[i - 1]);
					capasOcultas[i].setCapaSiguiente(capaSalida);
					continue;
				}
				capasOcultas[i].setCapaAnterior(capasOcultas[i - 1]);
				capasOcultas[i].setCapaSiguiente(capasOcultas[i + 1]);
			}
		}
	}

	/**
	 * Metodo muy importante que entrena a la red neuronal. Recueren esta es una
	 * red de aprendizaje supervisado.
	 *
	 * @param entradas
	 *            Datos de entrada
	 * @param salidas
	 *            Datos que se esperan de salida.
	 * @param learningRate
	 *            Taza de aprendizaje
	 * @param numero_epocas
	 *            Por cuantas epocas se correrá el entrenamiento.
	 */
	@Override
	public void entrenarRed(byte[][] entradas, double[][] salidas, double learningRate, int numero_epocas) {

		// paso 1: proveer los datos de entrada a las neuronas de entrada
		// paso 2: Calular las salidas
		// paso 3: retropropagar,
		// calcular delta, factor de error y actualizar los bias y pesos de
		// todas las neuronas
		errores = new ArrayList<>(200);
		int epoca = 0;
		double error_total;
		double error_promedio;

		while (true) {
			error_total = 0.0;
			System.out.println("Epoca " + epoca + "+++++++++");

			for (int i = 0; i < entradas.length; i++) {

				// a las neuronas de entrada, ponerle el valor de entrada como
				// su salida
				Neurona[] neuronas = capaEntrada.getNeuronas();
				for (int j = 0; j < neuronas.length; j++) {
					neuronas[j].setSalida(entradas[i][j]);
				}

				// actulalizar las salidas de las neuronas (correr la red)
				for (Capa capa : capasOcultas) {
					capa.actualizarSalidas();
				}
				capaSalida.actualizarSalidas();

				// calcular error y retropropagar
				double e = capaSalida.caluclarDeltaCapaSalida(salidas[i]);
				error_total += e;
				for (Capa oculta : capasOcultas) {
					oculta.calcularDeltaCapaOculta();
				}
				// actualizar bias y pesos
				capaSalida.actualizarBiasyPesos(learningRate);
				for (Capa oculta : capasOcultas) {
					oculta.actualizarBiasyPesos(learningRate);
				}
				// System.out.println("\tE =" + e);
			}
			error_promedio = error_total / salidas.length; // entre el numero de
															// muestras
			if (epoca % 5 == 0) {
				errores.add(new Point.Double(epoca, error_promedio));
			}

			System.out.println("\tError Promedio=" + error_promedio + " error total=" + error_total
					+ " numero muestras=" + salidas.length);
			epoca++;
			if (epoca == numero_epocas) {
				break;
			}
		}
		System.out.println("Success, se entreno la red con " + epoca + " epocas!!");
	}

	/**
	 * Este metodo "corre" la red, se usa ya cuando la red esta entrenada.
	 *
	 * @param entrada
	 *            Datos de entrada.
	 * @return Los datos de salida (puede que la red tenga más de una neurona de
	 *         salida)
	 */
	@Override
	public double[] clasificar(byte[] entrada) {
		// a las neuronas de entrada, ponerle el valor de entrada como su salida
		Neurona[] neuronas = capaEntrada.getNeuronas();
		for (int i = 0; i < neuronas.length; i++) {
			neuronas[i].setSalida(entrada[i]);
		}

		// actulalizar las salidas de las neuronas (correr la red)
		for (Capa capa : capasOcultas) {
			capa.actualizarSalidas();
		}
		capaSalida.actualizarSalidas();

		// obtenemos las salidas
		Neurona[] ns = capaSalida.getNeuronas();
		double[] salidas = new double[ns.length];
		for (int i = 0; i < salidas.length; i++) {
			salidas[i] = ns[i].getSalida();
		}
		// TODO
		// the salidas is a double but should be a byte have to change this.

		return salidas;
	}

	/**
	 * Metodo muy importante que entrena a la red neuronal. Recueren esta es una
	 * red de aprendizaje supervisado.
	 *
	 * @param entradas
	 *            Datos de entrada
	 * @param salidas
	 *            Datos que se esperan de salida.
	 * @param learningRate
	 *            Taza de aprendizaje
	 * @param error_threshold
	 */
	@Override
	public void entrenarRed(byte[][] entradas, double[][] salidas, double learningRate, double error_threshold) {

		// paso 1: proveer los datos de entrada a las neuronas de entrada
		// paso 2: Calular las salidas
		// paso 3: retropropagar,
		// calcular delta, factor de error y actualizar los bias y pesos de
		// todas las neuronas
		errores = new ArrayList<>(200);
		int epoca = 0;
		double error;
		while (true) {
			error = 0.0;
			System.out.println("Epoca " + epoca + "+++++++++");

			for (int i = 0; i < entradas.length; i++) {

				// a las neuronas de entrada, ponerle el valor de entrada como
				// su salida
				Neurona[] neuronas = capaEntrada.getNeuronas();
				for (int j = 0; j < neuronas.length; j++) {
					neuronas[j].setSalida(entradas[i][j]);
				}

				// actulalizar las salidas de las neuronas (correr la red)
				for (Capa capa : capasOcultas) {
					capa.actualizarSalidas();
				}
				capaSalida.actualizarSalidas();

				// calcular error y retropropagar
				double e = capaSalida.caluclarDeltaCapaSalida(salidas[i]);
				error += e;
				for (Capa oculta : capasOcultas) {
					oculta.calcularDeltaCapaOculta();
				}
				// actualizar bias y pesos
				capaSalida.actualizarBiasyPesos(learningRate);
				for (Capa oculta : capasOcultas) {
					oculta.actualizarBiasyPesos(learningRate);
				}
				System.out.println("\tE =" + e);

			}
			if (epoca % 5 == 0) {
				errores.add(new Point.Double(epoca, error));
			}
			System.out.println("\tError =" + error + " Error threshold =" + error_threshold);
			epoca++;
			if (error < error_threshold) {
				break;
			}
		}
		System.out.println("Success, se entreno la red con " + epoca + " epocas!!");
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Neurona[] n;
		Sinapsis[] ss;

		sb.append("Red Neuronal ").append(this.nombre).append("\n");
		// la capa de entrada
		sb.append("--Capa de Entrada\n");
		n = capaEntrada.getNeuronas();
		for (int i = 0; i < n.length; i++) {
			sb.append("\tNeurona ").append(i + 1).append(": bias=").append(n[i].getBias()).append("\n");
			ss = n[i].getSinapsisSalida();
			for (Sinapsis s : ss) {
				sb.append("\t\t");
				sb.append("Sinapsis: Peso=").append(s.getPeso()).append("\t\tbias de receptor=")
						.append(s.getReceptor().getBias());
				sb.append("\n");
			}
		}

		Capa c;
		// las capas de ocultas
		for (int k = 0; k < capasOcultas.length; k++) {
			sb.append("--Capa de Oculta ").append(k + 1).append("\n");
			c = capasOcultas[k];
			n = c.getNeuronas();
			for (int i = 0; i < n.length; i++) {
				sb.append("\tNeurona ").append(i + 1).append(": bias=").append(n[i].getBias()).append("\n");
				ss = n[i].getSinapsisSalida();
				for (Sinapsis s : ss) {
					sb.append("\t\t");
					sb.append("Sinapsis: Peso=").append(s.getPeso()).append("\t\tbias de receptor=")
							.append(s.getReceptor().getBias());
					sb.append("\n");
				}
			}
		}

		// capa de salida
		sb.append("--Capa de Salida\n");
		n = capaSalida.getNeuronas();
		for (int i = 0; i < n.length; i++) {
			sb.append("\tNeurona ").append(i + 1).append(": bias=").append(n[i].getBias()).append("\n");
		}

		return sb.toString();
	}

	@Override
	public int getSizeofCapaEntrada() {
		return getCapaEntrada().getNeuronas().length;
	}

	@Override
	public int getSizeofCapaSalida() {
		return getCapaSalida().getNeuronas().length;
	}

	@Override
	public int[] getSizeofCapasOcultas() {
		int[] numeroCapasOcultas = new int[capasOcultas.length];
		for (int i = 0; i < numeroCapasOcultas.length; i++) {
			numeroCapasOcultas[i] = capasOcultas[i].getNeuronas().length;
		}
		return numeroCapasOcultas;
	}
}
