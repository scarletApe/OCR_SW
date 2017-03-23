
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

public class Histogram{

  private int[]  histogram;
  private int    N=0;

  public Histogram(int slots){
    histogram=new int[slots];
  }

  public void write(int colour){
    histogram[colour]++;
    N++;
  }

  /*
	[1] Shunji Mori; Optical Character Recognition (1999); John Wiley & Sons
    	4.2 Thresholding selection based on global discriminant analysis; pp.112

  [2] Ioannis Pavlidis, Vassilios Morellas, Pete Roeber; Programming Cameras and Pan-Tilts with DirectX and Java
      Morgan-Kaufmann Publishers; ISBN 1-55860-756-0
      pp.69

  Original Paper:

  [3] Otsu,N.A Threshold Selection Method from Gray-Level Histograms.
      IEEE Transactions on Systems,Man, and Cybernetics,1979,vol.9,pp.62-66
  */

  public int getThreshold(){
    double[] p=new double[histogram.length];

    for(int i=0;i<histogram.length;i++){              // probabilities
      p[i]=((double)histogram[i])/((double)N);
    }
    double mu=0.0;                                    // total mean value
    for(int i=0;i<p.length;i++){mu+=(i+1)*p[i];}

    int    threshold=0;
    double w1=0.0;
    double mu1=0.0;
    double sigmaBSqr;
    double sigmaBSqrMax=0.0;

    for(int i=0;i<p.length;i++){
      w1  +=      p[i];
      mu1 +=(i+1)*p[i];
      if((0<w1)&&(w1<1)){
        sigmaBSqr=mu*w1-mu1;
        sigmaBSqr=(sigmaBSqr*sigmaBSqr)/(w1*(1.0-w1));
        if(sigmaBSqr>sigmaBSqrMax){
          sigmaBSqrMax=sigmaBSqr;
          threshold=i;
        }
      }
    }      
    return threshold;
  }
}

//</pre></body></html>
