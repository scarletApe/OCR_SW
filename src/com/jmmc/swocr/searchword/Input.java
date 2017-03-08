/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jmmc.swocr.searchword;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Esta clase permite leer un archivo de texto plano con los datos del arreglo,
 * los caracteres separados por un espacio en blanco.
 * 
 * @author juanmartinez
 */
public class Input {

	private static BufferedReader br = null;
	private static StringTokenizer st;

	public char[][] readFileCrossWord(String direccion) {
		ArrayList<String> lines = new ArrayList<String>();
		String line;

		try {
			br = new BufferedReader(new FileReader(direccion));
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
		} catch (Exception e) {
			System.out.println();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				System.err.println("Error in closing BufferedReader");
			}
		}
		return analizeData(lines);
	}

	private char[][] analizeData(ArrayList<String> data) {
		int howmany = data.size();
		st = new StringTokenizer(data.get(0));
		int length = st.countTokens();
		char[][] array = new char[howmany][length];

		for (int i = 0; i < howmany; i++) {
			st = new StringTokenizer(data.get(i));
			for (int j = 0; j < length; j++) {
				array[i][j] = st.nextToken().charAt(0);
			}
		}
		return array;
	}
}
