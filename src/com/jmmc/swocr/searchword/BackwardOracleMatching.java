/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jmmc.swocr.searchword;

/**
 * Con esta clase se busaca un patron dentro de una cadena.
 * @author juanmartinez
 */
public class BackwardOracleMatching {

	private int teta = 1000;
	private int k = teta;
	private int s = teta;

	public void oracleAddLetter(int states[], int transitions[][], int m, char sigma) {
		int sigmaNum = sigma;

		transitions[m][sigmaNum] = m + 1;
		k = states[m];

		while (k != teta && transitions[k][sigmaNum] == teta) {
			transitions[k][sigmaNum] = m + 1;
			k = states[k];
		}

		if (k == teta) {
			s = 0;
		} else {
			s = transitions[k][sigmaNum];
		}
		states[m + 1] = s;
	}

	public void oracleOnLine(int m, int states[], int transitions[][], String patronRv) {

		for (int i = 0; i <= m; i++) {
			states[i] = teta;
			for (int j = 0; j < 255; j++) {
				transitions[i][j] = teta;
			}
		}

		for (int j = 1; j <= m; j++) {
			oracleAddLetter(states, transitions, j - 1, patronRv.charAt(j - 1));
		}
	}

	public int findWord(String t, String p) {
		String texto = t;
		String patron = p;
		String patronRv = reverseAString(patron);
		int n = texto.length();
		int m = patronRv.length();
		int states[] = new int[m + 1];
		int transitions[][] = new int[m + 1][255];
		int pos = 0;
		int current = 0;
		int z = 0;
		int send = -1;

		oracleOnLine(m, states, transitions, patronRv);

		pos = -1;
		while (pos <= n - (m + 1)) {
			current = 0;
			int j = m;

			while (j > 0 && current != teta) {
				int textPos = texto.charAt(pos + j);
				current = transitions[current][textPos];
				j = j - 1;
			}

			if (current != teta) {
				// System.out.println("La palabra comienza en la posicion: " +
				// (pos + 2) + " hasta la posicion: " + (m+pos+1));
				z = 1;
				send = pos + 1;
			}

			pos = pos + j + 1;
		}
		if (z != 1) {
			// System.out.println("La palabra no se encuentra en el texto");
			return -1;
		}
		// System.exit(0);
		return send;
	}

	public String reverseAString(String word) {
		int numwrds = word.length();
		char[] wordArray = new char[numwrds];

		for (int i = 0; i < word.length(); i++) {
			wordArray[numwrds - 1] = word.charAt(i);
			numwrds--;
		}
		String wordRv = new String(wordArray);
		return wordRv;
	}
}
