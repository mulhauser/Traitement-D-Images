// Importation des paquets necessaires. Le plugin n'est pas lui-meme un paquet (pas de mot-cle package)
import ij.*; 							// pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter; 	// pour interface PlugInFilter
import ij.process.*; 					// pour classe ImageProcessor
import java.awt.*; 						// pour classe Rectangle

// Nom de la classe = nom du fichier.  Implemente l'interface PlugInFilter
public class Image_Inverter implements PlugInFilter {
	
public int setup(String arg, ImagePlus imp) {
	// Accepte tous types d'images, piles d'images et RoIs, meme non rectangulaires
	return DOES_8G+DOES_RGB+DOES_STACKS+SUPPORTS_MASKING;
}

public void run(ImageProcessor ip) {
	Rectangle r = ip.getRoi(); // Region d'interet selectionnee (r.x=r.y=0 si aucune)

	for (int y=r.y; y<(r.y+r.height); y++)
		for (int x=r.x; x<(r.x+r.width); x++)
			ip.set(x, y, ~ip.get(x,y)); // Complement bit a bit des valeurs des pixels
}

}
