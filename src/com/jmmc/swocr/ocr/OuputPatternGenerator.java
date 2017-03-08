
//<editor-fold defaultstate="collapsed" desc="licence">
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//</editor-fold>

package com.jmmc.swocr.ocr;

/**
 *
 * @author juanmartinez
 */
public class OuputPatternGenerator {
	public static void main(String[] args) {
		for (int i = 0; i < 26; i++) {
			System.out.print("{");
			for (int j = 0; j < 26; j++) {
				if (i == j) {
					System.out.print("1,");
					continue;
				}
				if (j == 25) {
					System.out.print("0");
					continue;
				}
				System.out.print("0,");
			}
			System.out.print("},");
			System.out.println();
		}
	}
}
