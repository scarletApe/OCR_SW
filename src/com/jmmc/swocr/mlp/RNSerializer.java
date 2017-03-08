/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jmmc.swocr.mlp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juanmartinez
 */
public class RNSerializer {

	// Use Java's logging facilities to record exceptions.
	// The behavior of the logger can be configured through a
	// text file, or programmatically through the logging API.
	private static final Logger FLOGGER = Logger.getLogger(RNSerializer.class.getPackage().getName());

	public void serializeRed(RedNeuronal red, String direccion) {
		// serialize the network
		try {
			// use buffering
			OutputStream file = new FileOutputStream(direccion);
			OutputStream buffer = new BufferedOutputStream(file);
			ObjectOutput output = new ObjectOutputStream(buffer);
			try {
				output.writeObject(red);
			} finally {
				output.close();
			}
		} catch (IOException ex) {
			FLOGGER.log(Level.SEVERE, "Cannot perform output.", ex);
			System.out.println("Cannot perform output.");
		}
	}

	public RedNeuronal deserializeRed(String direccion) {
		RedNeuronal red = null;

		// deserialize the .ser file
		try {
			// use buffering
			InputStream file = new FileInputStream(direccion);
			InputStream buffer = new BufferedInputStream(file);
			ObjectInput input = new ObjectInputStream(buffer);
			try {
				// deserialize the network
				red = (RedNeuronal) input.readObject();

			} finally {
				input.close();
			}
		} catch (ClassNotFoundException ex) {
			FLOGGER.log(Level.SEVERE, "Cannot perform input. Class not found.", ex);
			System.out.println("Cannot perform input. Class not found.");
		} catch (IOException ex) {
			FLOGGER.log(Level.SEVERE, "Cannot perform input.", ex);
			System.out.println("Cannot perform input");
		}
		return red;
	}

	public RedNeuronal deserializeRed(InputStream in) {
		RedNeuronal red = null;

		// deserialize the .ser file
		try {
			// use buffering
			InputStream buffer = new BufferedInputStream(in);
			ObjectInput input = new ObjectInputStream(buffer);
			try {
				// deserialize the network
				red = (RedNeuronal) input.readObject();

			} finally {
				input.close();
			}
		} catch (ClassNotFoundException ex) {
			FLOGGER.log(Level.SEVERE, "Cannot perform input. Class not found.", ex);
			System.out.println("Cannot perform input. Class not found.");
		} catch (IOException ex) {
			FLOGGER.log(Level.SEVERE, "Cannot perform input.", ex);
			System.out.println("Cannot perform input");
		}
		return red;
	}

	public void serializeObject(Object obj, String direccion) {
		// serialize the network
		try {
			// use buffering
			OutputStream file = new FileOutputStream(direccion);
			OutputStream buffer = new BufferedOutputStream(file);
			ObjectOutput output = new ObjectOutputStream(buffer);
			try {
				output.writeObject(obj);
			} finally {
				output.close();
			}
		} catch (IOException ex) {
			FLOGGER.log(Level.SEVERE, "Cannot perform output.", ex);
			System.out.println("Cannot perform output.");
		}
	}

	public Object deserializeObject(String direccion) {
		Object obj = null;

		// deserialize the .ser file
		try {
			// use buffering
			InputStream file = new FileInputStream(direccion);
			InputStream buffer = new BufferedInputStream(file);
			ObjectInput input = new ObjectInputStream(buffer);
			try {
				// deserialize the object
				obj = input.readObject();

			} finally {
				input.close();
			}
		} catch (ClassNotFoundException ex) {
			FLOGGER.log(Level.SEVERE, "Cannot perform input. Class not found.", ex);
			System.out.println("Cannot perform input. Class not found.");
		} catch (IOException ex) {
			FLOGGER.log(Level.SEVERE, "Cannot perform input.", ex);
			System.out.println("Cannot perform input");
		}
		return obj;
	}
}
