package com.jmmc.swocr.ocr;

import java.io.File;

public class TestEnsemble {
	
	public static void main(String args[]){
		EnsembleClassifier ec = new EnsembleClassifier();
		
		ec.clasificar(new File("/Users/juanmartinez/Desktop/line_1_char_14.png"));
	}
}
