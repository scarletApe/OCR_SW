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
package com.jmmc.swocr.filtros_imagenes;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author juanmartinez
 */
public class ProcesadorInclinacion extends ProcesadorImagen {

	@Override
	public BufferedImage procesar(BufferedImage imagen) {
		int alto;
		int b1 = imagen.getHeight() - 1;
		int b2 = -imagen.getWidth() + 1;
		int b3 = imagen.getWidth() + imagen.getHeight() - 2;
		int b4 = 0;

		boolean encontrado;

		// alto
		ProcesadorBoundingBox boundingBox = new ProcesadorBoundingBox();
		alto = boundingBox.getMaxY(imagen) - boundingBox.getMinY(imagen);

		// B1
		encontrado = false;
		for (int b = -imagen.getWidth() + 1; !encontrado && b < imagen.getHeight(); b++) {
			for (int x = imagen.getWidth() - 1; !encontrado && x >= 0 && x + b >= 0 & x + b < imagen.getHeight(); x--) {
				if (imagen.getRGB(x, x + b) != BLANCO) {
					b1 = b;
					encontrado = true;
				}

			}
		}

		// B2
		encontrado = false;
		for (int b = imagen.getHeight() - 1; !encontrado && imagen.getWidth() - 1 + b >= 0; b--) {
			for (int x = -b <= 0 ? 0 : -b; !encontrado && x < imagen.getWidth() && x + b >= 0
					&& x + b < imagen.getHeight(); x++) {
				if (imagen.getRGB(x, x + b) != BLANCO) {
					b2 = b;
					encontrado = true;
				}
			}
		}

		// B3
		encontrado = false;
		for (int b = 0; !encontrado && b < imagen.getHeight() + imagen.getWidth() - 1; b++) {
			for (int x = b - imagen.getHeight() + 1 <= 0 ? 0 : b - imagen.getHeight() + 1; !encontrado
					&& x < imagen.getWidth() && -x + b >= 0 && -x + b < imagen.getHeight(); x++) {
				if (imagen.getRGB(x, -x + b) != BLANCO) {
					b3 = b;
					encontrado = true;
				}
			}
		}

		// B4
		encontrado = false;
		for (int b = imagen.getWidth() + imagen.getHeight() - 2; !encontrado && b >= 0; b--) {
			for (int x = b - imagen.getHeight() + 1 <= 0 ? 0 : b - imagen.getHeight() + 1; !encontrado
					&& x < imagen.getWidth() && -x + b >= 0; x++) {
				if (imagen.getRGB(x, -x + b) != BLANCO) {
					b4 = b;
					encontrado = true;
				}
			}

		}

		// factor de inclinacion
		double factor = Math.tan(Math.atan2((b4 + b1 - b3 - b2) / 2.0, alto));

		// se obtiene el menor y mayor X corregidos
		int menor = 0;
		int mayor = imagen.getWidth() - 1;

		for (int x = 0; x < imagen.getWidth(); x++) {
			for (int y = 0; y < imagen.getHeight(); y++) {
				if (imagen.getRGB(x, y) != BLANCO) {
					int xc = (int) (x - (y * factor));
					if (xc < menor) {
						menor = xc;
					}
					if (xc > mayor) {
						mayor = xc;
					}
				}
			}
		}

		// se genera la nueva imagen
		BufferedImage imagenProcesada = new BufferedImage(mayor - menor + 1, imagen.getHeight(), imagen.getType());
		pintar(imagenProcesada, Color.WHITE);

		for (int x = 0; x < imagen.getWidth(); x++) {
			for (int y = 0; y < imagen.getHeight(); y++) {
				int xc = (int) (x - (y * factor)) - menor;
				if (imagen.getRGB(x, y) != BLANCO) {
					imagenProcesada.setRGB(xc, y, imagen.getRGB(x, y));
				}
			}
		}
		return imagenProcesada;
	}
}
