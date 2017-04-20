// Importation des paquets necessaires. Le plugin n'est pas lui-meme un paquet (pas de mot-cle package)
import ij.*; 							// pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.*; 					// pour classe ImageProcessor

public class Egalisation_HistogrammeCouleurRGB implements PlugInFilter {
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

		// 
		// On recupere les composantes RGB
		int[] Hir = new int[N];
		int[] Hig = new int[N];
		int[] Hib = new int[N];
		int[] rgb = new int[3];
		for(int i = 0; i < ip.getWidth(); i++){
			for(int j = 0; j < ip.getHeight(); j++){
				ip.getPixel(i,j,rgb);
				Hir[rgb[0]]++;
				Hig[rgb[1]]++;
				Hib[rgb[2]]++;
			}
		}


		double[] Pir = new double[N];
		double[] Pig = new double[N];
		double[] Pib = new double[N];
		// Calcul de Pi(n): proportion de pixels de i qui ont la valeur n pour chaque composante
		for(int i = 0; i < ip.getWidth(); i++){
			for(int j = 0; j < ip.getHeight(); j++){
				ip.getPixel(i,j,rgb);
				Pir[rgb[0]] = (double)Hir[rgb[0]]/(double)(ip.getWidth()*ip.getHeight());
				Pig[rgb[1]] = (double)Hig[rgb[1]]/(double)(ip.getWidth()*ip.getHeight());
				Pib[rgb[2]] = (double)Hib[rgb[2]]/(double)(ip.getWidth()*ip.getHeight());

			}
		}

		// Calcul de Ri pour chaque composantes
		double[] Rir = new double[N];
		double[] Rig = new double[N];
		double[] Rib = new double[N];
		for(int i = 0; i < Rir.length; i++){
			double somr = 0;
			double somg = 0;
			double somb = 0;
			for(int j = 0; j < i; j++){
				somr += Pir[j];
				somg += Pig[j];
				somb += Pib[j];
			}
			Rir[i] = somr;
			Rig[i] = somg;
			Rib[i] = somb;
		}

		// Remplacement des pixels pour chaque composantes
		for(int i = 0; i < ip.getWidth(); i++){
			for(int j = 0; j < ip.getHeight(); j++){
				ip.getPixel(i,j,rgb);

				int[] iArray = {
								(int)((N-1)*Rir[rgb[0]]),
								(int)((N-1)*Rig[rgb[1]]),
								(int)((N-1)*Rib[rgb[2]])
								};
				ip.putPixel(i, j, iArray);

			}
		}

		IJ.log("Fini");

	}
}
