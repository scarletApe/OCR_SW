/*
 * Copyright (C) 2016 juanmartinez
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jmmc.swocr.mlp;

import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author juanmartinez
 */
public interface MLP {

	public String getNombre();

	public void setNombre(String nombre);

	public ArrayList<Point.Double> getErrores();

	public void setErrores(ArrayList<Point.Double> errores);

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
	public void crearRed(int num_entrada, int[] num_ocultas, int num_salida, FuncionActivacion funcion);

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
	public void entrenarRed(byte[][] entradas, double[][] salidas, double learningRate, int numero_epocas);

	/**
	 * Este metodo "corre" la red, se usa ya cuando la red esta entrenada.
	 *
	 * @param entrada
	 *            Datos de entrada.
	 * @return Los datos de salida (puede que la red tenga más de una neurona de
	 *         salida)
	 */
	public double[] clasificar(byte[] entrada);

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
	public void entrenarRed(byte[][] entradas, double[][] salidas, double learningRate, double error_threshold);

	public int getSizeofCapaEntrada();

	public int getSizeofCapaSalida();

	public int[] getSizeofCapasOcultas();
}
