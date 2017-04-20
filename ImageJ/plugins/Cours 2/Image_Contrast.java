// Importation des paquets necessaires. Le plugin n'est pas lui-meme un paquet (pas de mot-cle package)
import ij.*; 							// pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.*; 					// pour classe ImageProcessor

public class Image_Contrast implements PlugInFilter {
	ImagePlus imp;
	int colors;
	static int MAX_COLORS = 256*256*256;
	int[] counts = new int[MAX_COLORS];
	double moyenne;
	double contrast;
 		// Histogramme des couleurs

	public int setup(String arg, ImagePlus imp) {
		this.imp = imp; 						// Rem.: pas utilise dans run()
		this.moyenne = 0.0;
		this.contrast = 0.0;
		return DOES_8G; 	// Traite les images couleurs ; pas d'annulation possible
	}
	
	public void run(ImageProcessor ip) {
		
		// Analyse histogramme pour verifier si c'est juste (valeur en bas a gauche)
		
		byte[] pixels = (byte[])ip.getPixels(); //recuperation des pixels de l'image dans le tableau pixels
		
		int somme = 0;
		for(int y = 0; y <  pixels.length; y++){
		  somme += pixels[y]&0xff;
		}
		moyenne = somme/pixels.length;
		
		somme = 0;
		for(int y = 0; y < pixels.length; y++){
		  somme += Math.pow((double)(pixels[y]&0xff) - moyenne,2);
		}
		
		contrast = Math.sqrt(somme/pixels.length);
		
		
		IJ.log("Contraste : "+contrast);
		
		
		
		
	}
}
