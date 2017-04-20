// Importation des paquets necessaires. Le plugin n'est pas lui-meme un paquet (pas de mot-cle package)
import ij.*; 							// pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.*; 					// pour classe ImageProcessor

public class Egalisation_HistogrammeCouleur implements PlugInFilter {
	ImagePlus imp;
	int colors;
	static int MAX_COLORS = 256*256*256;
	int[] counts = new int[MAX_COLORS];
 		// Histogramme des couleurs

	public int setup(String arg, ImagePlus imp) {
		this.imp = imp; 						// Rem.: pas utilise dans run()
		return DOES_RGB; 	// Traite les images couleurs ; pas d'annulation possible
	}
	
	public void run(ImageProcessor ip) {
		
		// Analyse histogramme pour verifier si c'est juste (valeur en bas a gauche)
		
		int[] pixels = (int[])ip.getPixels(); //recuperation des pixels de l'image dans le tableau pixels

		int N = 256;

		// On recupere les composantes RGB
		int[][] I = new int[ip.getWidth()][ip.getHeight()];
		int[] rgb = new int[3];
		for(int i = 0; i < I.length; i++){
			for(int j = 0; j < I[i].length; j++){
				ip.getPixel(i,j,rgb);
				I[i][j] = (int)(0.3*rgb[0] + 0.59*rgb[1] + 0.11*rgb[2]);
			}
		}

		int[] Hi = new int[N];
		// Calcul de Hi(n): nombre de pixels de l'image i de taille L*H qui ont la valeur n / OK
		for(int i = 0; i < I.length; i++){
			for(int j = 0; j < I[i].length; j++){
				Hi[I[i][j]]++;	
			}			
		}

		double[] Pi = new double[N];
		// Calcul de Pi(n): proportion de pixels de i qui ont la valeur n
		for(int i = 0; i < I.length; i++){
			for(int j = 0; j < I[i].length; j++){
				Pi[I[i][j]] = (double)Hi[I[i][j]]/(double)(ip.getWidth()*ip.getHeight());
			}
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

		// remplacement des pixels de l'image
		for(int i = 0; i < ip.getWidth(); i++){
			for(int j = 0; j < ip.getHeight(); j++){
				ip.getPixel(i,j,rgb);

				int[] iArray = {
								(int)((N-1)*Ri[rgb[0]]),
								(int)((N-1)*Ri[rgb[1]]),
								(int)((N-1)*Ri[rgb[2]])
								};
				ip.putPixel(i, j, iArray);

			}
		}

		IJ.log("Fini");

	}
}
