// Importation des paquets necessaires. Le plugin n'est pas lui-meme un paquet (pas de mot-cle package)
import ij.*; 							// pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.*; 					// pour classe ImageProcessor

public class Egalisation_Histogramme implements PlugInFilter {
	ImagePlus imp;
	int colors;
	static int MAX_COLORS = 256*256*256;
	int[] counts = new int[MAX_COLORS];
 		// Histogramme des couleurs

	public int setup(String arg, ImagePlus imp) {
		this.imp = imp; 						// Rem.: pas utilise dans run()
		return DOES_8G; 	// Traite les images couleurs ; pas d'annulation possible
	}
	
	public void run(ImageProcessor ip) {
		
		// Analyse histogramme pour verifier si c'est juste (valeur en bas a gauche)
		byte[] pixels = (byte[])ip.getPixels(); //recuperation des pixels de l'image dans le tableau pixels
	
		
		int N = 256;
		int[] Hi = new int[N];
		// Calcul de Hi(n): nombre de pixels de l'image i de taille L*H qui ont la valeur n / OK
		for(int i = 0; i < pixels.length; i++){
			Hi[ip.get(i)]++;
		}

		double[] Pi = new double[N];
		// Calcul de Pi(n): proportion de pixels de i qui ont la valeur n
		for(int i = 0; i < pixels.length; i++){
			Pi[ip.get(i)] = (double)Hi[ip.get(i)]/(double)pixels.length;
		}

		// Calcul de Ri
		double[] Ri = new double[N];
		for(int i = 0; i < Ri.length; i++){
			double som = 0;
			for(int j = 0; j < i; j++){
				som += Pi[j];
			}
			Ri[i] = som;
		}

		// changement des pixels de l'image
		for(int i = 0; i < pixels.length; i++){
			ip.set(i, (int)((N-1)*Ri[ip.get(i)]));
		}

		


		
		
		IJ.log("Fini");

		
	}
}
