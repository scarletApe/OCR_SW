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
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * {@link ProcesadorImagen} abstracto
 *
 *
 * @author Gabriel Roberto Constenla
 */
public abstract class ProcesadorImagen {

	/**
	 * Codigo de color de un pixel blanco
	 */
	public static final int BLANCO = -1;
	/**
	 * Codigo de color de un pixel negro (para una imagen binaria)
	 */
	public static final int NEGRO = 1;

	/**
	 * Procesamiento de la imagen
	 *
	 * * @param imagen
	 * 
	 * @return imagenProcesada
	 */
	public abstract BufferedImage procesar(BufferedImage imagen);

	/**
	 * Rellena una imagen con el color dado
	 *
	 *
	 * @param imagen
	 * @param color
	 */
	public static void pintar(BufferedImage imagen, Color color) {
		Graphics2D g = (Graphics2D) imagen.getGraphics();
		g.setColor(color);
		g.fillRect(0, 0, imagen.getWidth(), imagen.getHeight());
		g.dispose();
	}
}
