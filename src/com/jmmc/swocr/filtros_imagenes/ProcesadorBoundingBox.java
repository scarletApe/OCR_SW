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

import java.awt.image.BufferedImage;

/**
 *
 * @author juanmartinez
 */
public class ProcesadorBoundingBox extends ProcesadorImagen {

	@Override
	public BufferedImage procesar(BufferedImage imagen) {
		int minX = getMinX(imagen);
		int minY = getMinY(imagen);
		int maxX = getMaxX(imagen);
		int maxY = getMaxY(imagen);

		System.out.println("minx=" + minX + " miny=" + minY + " maxX=" + maxX + " maxY=" + maxY + " w="
				+ (maxX - minX + 1) + " h=" + (maxY - minY + 1));

		int width = maxX - minX + 1;
		int height = maxY - minY + 1;

		// if(width<minX){
		// width = minX+2;
		// }
		//
		// if(height<minY){
		// height = minY+2;
		// }

		BufferedImage subImagen = imagen.getSubimage(minX, minY, width, height);
		BufferedImage imagenProcesada = new BufferedImage(subImagen.getWidth(), subImagen.getHeight(),
				subImagen.getType());
		imagenProcesada.setData(subImagen.getData());

		return imagenProcesada;
	}

	public int getMinX(BufferedImage img) {
		int minX = img.getWidth() - 1;
		boolean encontrado = false;

		for (int x = 0; !encontrado && x < img.getWidth(); x++) {
			for (int y = 0; !encontrado && y < img.getHeight(); y++) {
				if (img.getRGB(x, y) != BLANCO && x < minX) {
					minX = x;
					encontrado = true;
				}
			}
		}
		return minX;
	}

	public int getMinY(BufferedImage img) {
		int minY = img.getHeight() - 1;
		boolean encontrado = false;

		for (int y = 0; !encontrado && y < img.getHeight(); y++) {
			for (int x = 0; !encontrado && x < img.getWidth(); x++) {
				if (img.getRGB(x, y) != BLANCO && y < minY) {
					minY = y;
					encontrado = true;
				}
			}
		}
		return minY;
	}

	public int getMaxX(BufferedImage img) {
		int maxX = 0;
		boolean encontrado = false;

		for (int x = img.getWidth() - 1; !encontrado && x >= 0; x--) {
			for (int y = 0; !encontrado && y < img.getHeight(); y++) {
				if (img.getRGB(x, y) != BLANCO && x > maxX) {
					maxX = x;
					encontrado = true;
				}
			}
		}
		return maxX;
	}

	public int getMaxY(BufferedImage img) {
		int maxY = 0;
		boolean encontrado = false;

		for (int y = img.getHeight() - 1; !encontrado && y >= 0; y--) {
			for (int x = 0; !encontrado && x < img.getWidth(); x++) {
				if (img.getRGB(x, y) != BLANCO && y > maxY) {
					maxY = y;
					encontrado = true;
				}
			}
		}
		return maxY;
	}
}
