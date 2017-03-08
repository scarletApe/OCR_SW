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
package com.jmmc.swocr.ocr;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.jmmc.swocr.filtros_imagenes.Binarization;
import com.jmmc.swocr.filtros_imagenes.GrayScale;
import com.jmmc.swocr.filtros_imagenes.ProcesadorBoundingBox;
import com.jmmc.swocr.filtros_imagenes.ProcesadorEscala;
import com.jmmc.swocr.filtros_imagenes.ProcesadorImagen;
import com.jmmc.swocr.filtros_imagenes.ProcesadorInclinacion;
import com.jmmc.swocr.filtros_imagenes.ProcesadorZhangThinning;

/**
 * Esta clase solo funciona para aplicar filtros de pre procesamiento a imagenes
 * en bruto, recore carpetas y las nuevas generadas las coloca en otra carpeta.
 * 
 * @author juanmartinez
 */
public class ApplyFilters {

	public static void main(String[] args) throws IOException {
		new ApplyFilters().run();
	}

	public void run() throws IOException {
		// processFolder('A');
		// processFolder('B');
		// processFolder('C');

		for (int i = 0; i < 26; i++) {
			processFolder((char) (65 + i));
		}
	}

	GrayScale grey = new GrayScale();
	Binarization bin = new Binarization();

	static final ArrayList<ProcesadorImagen> PROCESADORES = new ArrayList<>();

	static {
		PROCESADORES.add(new ProcesadorInclinacion());
		// PROCESADORES.add(new ProcesadorEsqueleto());
		PROCESADORES.add(new ProcesadorZhangThinning());
		PROCESADORES.add(new ProcesadorBoundingBox());
		PROCESADORES.add(new ProcesadorEscala(new Dimension(6, 8)));// 10 x 14
	}

	private void processFolder(char letter) {
		String directorioFuente = "/Users/juanmartinez/Desktop/raw_letters/" + letter;

		String directorioDestino = "/Users/juanmartinez/Desktop/letter_samples3";

		File folder = new File(directorioFuente);
		File[] listOfFiles = folder.listFiles();

		int i = 0;
		for (final File file : listOfFiles) {
			try {
				String name = file.getName();
				if (name.endsWith(".png")) {

					BufferedImage img = ImageIO.read(file.getAbsoluteFile());
					// threshold
					img = grey.filter(img);
					img = bin.filter(img);

					for (ProcesadorImagen procesador : PROCESADORES) {
						img = procesador.procesar(img);
					}
					ImageIO.write(img, "png", new File(directorioDestino + "/" + letter + "_" + i + "." + "png"));
				}
				i++;
			} catch (IOException ex) {
				System.out.println("Error " + ex);
			}
		}
	}
}
