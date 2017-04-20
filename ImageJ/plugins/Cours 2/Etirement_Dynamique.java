// Importation des paquets necessaires. Le plugin n'est pas lui-meme un paquet (pas de mot-cle package)
import ij.*; 							// pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.*; 					// pour classe ImageProcessor

public class Etirement_Dynamique implements PlugInFilter {
	ImagePlus imp;
	int colors;
	static int MAX_COLORS = 256*256*256;
	int[] counts = new int[MAX_COLORS];
	double moyenne;
	double contrast;
	int[] LUT;
	double min;
	double max;
 		// Histogramme des couleurs

	public int setup(String arg, ImagePlus imp) {
		this.imp = imp; 						// Rem.: pas utilise dans run()
		this.moyenne = 0.0;
		this.contrast = 0.0;
		this.LUT = new int[256];
		this.min = 255;
		this.max = 0;
		return DOES_8G; 	// Traite les images couleurs ; pas d'annulation possible
	}
	
	public void run(ImageProcessor ip) {
		
		// Analyse histogramme pour verifier si c'est juste (valeur en bas a gauche)
		
		byte[] pixels = (byte[])ip.getPixels(); //recuperation des pixels de l'image dans le tableau pixels
		byte[][] pixelsEtire = new byte[ip.getHeight()][ip.getWidth()];
		
		
		int somme = 0;
		for(int i = 0; i <  pixels.length; i++){
		  // Calcul du min et du max
		  if((pixels[i]&0xff) > max) max = pixels[i]&0xff;
		  if((pixels[i]&0xff) < min) min = pixels[i]&0xff;
		}

		// Initialisation de la LUT
		for(int ng = 0; ng < 256; ng++){
			LUT[ng] = (int)(255*(ng-min)/(max - min));
			//IJ.log(LUT[ng]+"");
		}

		// Calcul de la transformation
		// Sans applyTable
		/*for(int i = 0; i < pixels.length; i++){
			ip.set(i, LUT[pixels[i]&0xff]);
		}*/


		// Application de applyTable
		ip.applyTable(LUT);

		
		IJ.log("Fini");

		
	}
}
