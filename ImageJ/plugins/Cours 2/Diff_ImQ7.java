import ij .*;
import ij. process .*;
import ij.gui .*;
import java .awt .*;
import ij. plugin . frame .*;

public class Diff_ImQ7 extends PlugInFrame {
	public Diff_ImQ7 (){
		super ( "Soustraction entre images ");
	}

	public void run ( String arg) {
		ImagePlus imgDiffA = new ImagePlus ("/home/profil/mulhause1u/Espace-Etudiant/M1/Intro TI/Cours2/ImageJ/plugins/Exemples_TI_S2/diff1.jpg");
		ImageProcessor ipDiffA = imgDiffA . getProcessor ();
		byte [] pixelsDiffA = ( byte []) ipDiffA.getPixels ();
	
		ImagePlus imgDiffB = new ImagePlus ("/home/profil/mulhause1u/Espace-Etudiant/M1/Intro TI/Cours2/ImageJ/plugins/Exemples_TI_S2/diff2.jpg");
		ImageProcessor ipDiffB = imgDiffB . getProcessor ();
		byte [] pixelsDiffB = ( byte []) ipDiffB.getPixels ();

		int w = ipDiffB.getWidth ();
		int h = ipDiffB.getHeight ();

		ImageProcessor ipRes = new ByteProcessor (w,h);
		ImagePlus imgRes = new ImagePlus (" Soustraction ",ipRes );
		ImageProcessor ipResDiff = new ByteProcessor (w,h);
		ImagePlus imgResDiff = new ImagePlus (" Soustraction ",ipResDiff );
		
		byte [] pixelsRes = ( byte []) ipRes.getPixels ();
		
		// Calcul de la différence
		int[] Si = new int[w*h];
		for(int i = 0; i < w*h; i++){
			Si[i] = Math.max((pixelsDiffA[i]&0xff - pixelsDiffB[i]&0xff) - (pixelsDiffB[i]&0xff - pixelsDiffA[i]&0xff),0); 
		}

		// Changement des pixels de l'image
		for(int i = 0; i < w*h; i++){
			ipRes.set(i, Si[i]);
		}		
		
		imgRes.show ();
		imgRes.updateAndDraw ();
	}	
}	
		
		