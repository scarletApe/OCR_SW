//<editor-fold defaultstate="collapsed" desc="licence">
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
//</editor-fold>
package com.jmmc.swocr.image_loaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;

/**
 * Esta classe tiene metodo para guardar y cargar un arreglo bidimensional a
 * formato PBM, es un tipo de imagen muy simple. Esta classe solo funciona con
 * archivos .pbm que estan en texto plan (no en binario) y solo archivos con
 * formato P1. Wikipedia tiene un articulo muy bueno explicando el formato.
 *
 * @author juanmartinez
 */
public class PbmManager implements ManejadorImagen {

	/**
	 * Este metodo permite reducir la dimensionalidad de un arreglo
	 * bidimensional a de un arreglo de solo una dimension.
	 *
	 * @param arreglo
	 *            El arreglo a aplastar
	 * @return un arreglo uni dimensional
	 */
	@Override
	public byte[] flattenArray(byte[][] arreglo) {
		byte[] flat = new byte[arreglo.length * arreglo[0].length];

		int j = 0, k = 0;
		for (int i = 0; i < flat.length; i++) {
			flat[i] = arreglo[j][k];
			k++;
			if (k >= arreglo[0].length) {
				k = 0;
				j++;
			}
		}
		return flat;
	}

	/**
	 * Este metodo permite guardar el arreglo bidimensional como una imagen PBM
	 * en el archivo especificado.
	 *
	 * @param archivo
	 *            La ruta y nombre del archivo a escribir.
	 * @param arreglo
	 *            El arreglo bidimensional
	 */
	@Override
	public void guardarArregloEnImagen(String archivo, byte[][] arreglo) {
		String cadena = construirContenido(arreglo);

		try {
			FileWriter fichero = new FileWriter(new File(archivo), false);
			PrintWriter pw = new PrintWriter(fichero);

			pw.print(cadena);

			fichero.close();
			pw.close();

		} catch (Exception e) {
			System.out.println("Error " + e);
		}
	}

	/**
	 * Este metodo permite leer un archivo PBM y transformar su contenido a un
	 * arreglo bidimensional.
	 *
	 * @param archivo
	 *            La ruta y nombre del archivo a leer.
	 * @return una arreglo bidimensional, el contenido del PBM
	 */
	@Override
	public byte[][] cargarArregloDeImagen(URI archivo) {
		ArrayList<String> contenido = new ArrayList<>();

		try {
			String line;
			BufferedReader br = new BufferedReader(new FileReader(new File(archivo)));
			while ((line = br.readLine()) != null) {
				contenido.add(line);
			}
			br.close();
		} catch (Exception e) {
			System.out.println("Error!!! " + e);
			return null;
		}

		// analizar el Contenido
		return analizarContenido(contenido);
	}

	/**
	 * Este metodo analiza el contenido.
	 *
	 * @param s
	 *            La informacion en una lista
	 * @return el arreglo
	 */
	private byte[][] analizarContenido(ArrayList<String> s) {
		if (s.size() < 1) {
			return null;
		}
		String line;

		line = s.get(2);
		String[] split = line.split(" ");
		int width = Byte.parseByte(split[0]);
		int rows = Byte.parseByte(split[1]);

		// System.out.println("width=" + width);
		// System.out.println("rows=" + rows);

		// se crea el arreglo del tamaño apropiado
		byte[][] arreglo = new byte[rows][width];

		for (int i = 3, k = 0; i < s.size(); i++, k++) {
			line = s.get(i);
			split = line.split(" ");
			for (int j = 0; j < width; j++) {
				arreglo[k][j] = Byte.parseByte(split[j]);
			}
		}

		return arreglo;
	}

	/**
	 * Este metodo construye un string, lo cual representa el contendido de una
	 * archivo pbm.
	 *
	 * @param arreglo
	 * @return
	 */
	public String construirContenido(byte[][] arreglo) {
		StringBuilder sb = new StringBuilder();
		sb.append("P1\n");
		sb.append("# Sin comentario\n");
		sb.append(arreglo[0].length).append(" ").append(arreglo.length).append("\n");

		for (int i = 0; i < arreglo.length; i++) {
			for (int j = 0; j < arreglo[i].length; j++) {
				sb.append(arreglo[i][j]).append(" ");
			}
			sb.append("\n");
		}

		return sb.toString();
	}

	@Override
	public byte[] cargarArregloFlatDeImagen(URI archivo) {
		byte[][] arr = cargarArregloDeImagen(archivo);
		return flattenArray(arr);
	}
}
