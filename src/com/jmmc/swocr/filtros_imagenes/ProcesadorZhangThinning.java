//<editor-fold defaultstate="collapsed" desc="licence">
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//</editor-fold>
package com.jmmc.swocr.filtros_imagenes;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author juanmartinez
 */
public class ProcesadorZhangThinning extends ProcesadorImagen {

	private static final ZhangThinning ZHANG_THIN = new ZhangThinning();

	@Override
	public BufferedImage procesar(BufferedImage image) {

		// create a temporary array to hold the image data
		int[][] imageData = new int[image.getHeight()][image.getWidth()];

		// fill the created array with said data
		for (int y = 0; y < imageData.length; y++) {
			for (int x = 0; x < imageData[y].length; x++) {

				if (image.getRGB(x, y) == Color.BLACK.getRGB()) {
					imageData[y][x] = 1;
				} else {
					imageData[y][x] = 0;

				}
			}
		}

		// apply Zhang Suen's Thinning Algorithm
		ZHANG_THIN.doZhangSuenThinning(imageData, true);

		// set the procesed data onto the image object
		for (int y = 0; y < imageData.length; y++) {
			for (int x = 0; x < imageData[y].length; x++) {
				if (imageData[y][x] == 1) {
					image.setRGB(x, y, Color.BLACK.getRGB());
				} else {
					image.setRGB(x, y, Color.WHITE.getRGB());
				}
			}
		}

		return image;
	}

}
