// Importation des paquets necessaires. Le plugin n'est pas lui-meme un paquet (pas de mot-cle package)
import ij.*; 							// pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.*; 					// pour classe ImageProcessor
import ij.gui.*;						// pour classe GenericDialog et Newimage

public class Filtre_Moyenneur implements PlugInFilter {

	public int setup(String arg, ImagePlus imp) {

		return DOES_8G;
	}

	public void run( ImageProcessor ip){
		int w = ip.getWidth ();
		int h = ip.getHeight ();
		byte [] pixels = ( byte []) ip.getPixels ();
		ImagePlus result = NewImage.createByteImage (" Filtrage ", w, h, 1,NewImage.FILL_BLACK );
		ImageProcessor ipr = result.getProcessor ();
		byte [] pixelsr = ( byte []) ipr.getPixels ();

		// Modifier le masque si on change la taille
		int [][] masque = {{1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1}};
		int n = 5; // taille du demi - masque

		//A COMPLETER
		// Recopie des bords
		// Gauche
		for(int i = 0; i < n; i++){
			for(int j = 0; j < h; j++){
				ipr.putPixel(i, j, ip.getPixel(i,j));
			}
		}
		// Droit
		for(int i = w-n; i < w; i++){
			for(int j = 0; j < h; j++){
				ipr.putPixel(i, j, ip.getPixel(i,j));
			}
		}
		// Haut
		for(int i = 0; i < w; i++){
			for(int j = 0; j < n; j++){
				ipr.putPixel(i, j, ip.getPixel(i,j));
			}
		}
		// Bas
		for(int i = 0; i < w; i++){
			for(int j = h-n; j < h; j++){
				ipr.putPixel(i, j, ip.getPixel(i,j));
			}
		}

		for (int y = n; y < w - n; y++){
			for ( int x = n; x < h - n; x++) {
				//A COMPLETER

				/*
				1/9(I(i-1,j-1)+I(i,j-1)+I(i+1,j-1)
				+I(i-1,j)+I(i,j)+I(i+1,j)
				+I(i-1,j+1)+I(i,j+1)+I(i+1,j+1))
				*/
				//pixelsr [x+y*w] = () ;

				// Il faut recopier la valeur de l'image initiale sur les bords
				// Fonctionne avec la methode brute 
				/*if(y == n){
					// coin
					if(x == n) ipr.putPixel(y-n,x-n,ip.getPixel(y-n,x-n));
					else if(x == h-n-1) ipr.putPixel(y-n,x+n,ip.getPixel(y-n,x+n));
					// cote gauche
					ipr.putPixel(y-n,x,ip.getPixel(y-n,x));					
				}else if(y == w-n-1){
					// coin
					if(x == n) ipr.putPixel(y+n,x-n,ip.getPixel(y+n,x-n));
					else if(x == h-n-1) ipr.putPixel(y+n,x+n,ip.getPixel(y+n,x+n));		
					// cote droit
					ipr.putPixel(y+n,x,ip.getPixel(y+n,x));					
				}

				if(x == n){

					ipr.putPixel(y,x-n,ip.getPixel(y,x-n));					
				}else if(x == h-n-1){
					ipr.putPixel(y,x+n,ip.getPixel(y,x+n));					
				}*/

				// Methode brute
				/*int pixMoy = (ip.getPixel(y-1,x-1)+ip.getPixel(y,x-1)+ip.getPixel(y+1,x-1)
							+ip.getPixel(y-1,x)+ip.getPixel(y,x)+ip.getPixel(y+1,x)
							+ip.getPixel(y-1,x+1)+ip.getPixel(y,x+1)+ip.getPixel(y+1,x+1))/9;*/


				int pixMoy = 0;
				int normalisation = 0;
				for(int i = 0; i < masque.length; i ++){
					for(int j = 0; j < masque[i].length; j++){
						normalisation += masque[i][j];
						pixMoy += ip.getPixel(y-n+i, x-n+j);
					}
				}
				pixMoy = pixMoy/normalisation;

				ipr.putPixel(y,x,pixMoy);
			}
		}

		result.show ();
		result.updateAndDraw ();
	}
}