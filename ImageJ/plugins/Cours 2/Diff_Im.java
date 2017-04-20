import ij .*;
import ij. process .*;
import ij.gui .*;
import java .awt .*;
import ij. plugin . frame .*;

public class Diff_Im extends PlugInFrame {
	public Diff_Im (){
		super ( "Soustraction entre images ");
	}

	public void run ( String arg) {
		ImagePlus imgDiffA = new ImagePlus ("/home/profil/mulhause1u/Espace-Etudiant/M1/Intro TI/Cours2/ImageJ/plugins/Exemples_TI_S2/imA.pgm");
		ImageProcessor ipDiffA = imgDiffA . getProcessor ();
		byte [] pixelsDiffA = ( byte []) ipDiffA.getPixels ();
	
		ImagePlus imgDiffB = new ImagePlus ("/home/profil/mulhause1u/Espace-Etudiant/M1/Intro TI/Cours2/ImageJ/plugins/Exemples_TI_S2/imB.pgm");
		ImageProcessor ipDiffB = imgDiffB . getProcessor ();
		byte [] pixelsDiffB = ( byte []) ipDiffB.getPixels ();

		int w = ipDiffB.getWidth ();
		int h = ipDiffB.getHeight ();

		ImageProcessor ipRes = new ByteProcessor (w,h);
		ImagePlus imgRes = new ImagePlus (" Soustraction ",ipRes );
		
		byte [] pixelsRes = ( byte []) ipRes.getPixels ();
		
		// A COMPLETER
		int[] Si = new int[w*h];
		for(int i = 0; i < w*h; i++){
			// a - b 
			Si[i] = Math.max(pixelsDiffA[i]&0xff - pixelsDiffB[i]&0xff,0); 
			//pixelsRes[i] = (byte)(Math.max(pixelsDiffA[i] - pixelsDiffB[i],0));
		}

		for(int i = 0; i < w*h; i++){
			ipRes.set(i, Si[i]);	
			//ipRes.set(i, pixelsRes[i]);
		}		
		
		imgRes.show ();
		imgRes.updateAndDraw ();
	}	
}	
		
		