import ij .*;
import ij. process .*;
import ij.gui .*;
import java .awt .*;
import ij. plugin . frame .*;

public class Diff_Im extends PlugInFrame {
	public DiffBIS_Images (){
		super ( "Soustraction entre images ");
	}

	public void run ( String arg) {
		ImagePlus imgDiffA = new ImagePlus ("CheminD_acces_imA.pgm");
		ImageProcessor ipDiffA = imgDiffA . getProcessor ();
		byte [] pixelsDiffA = ( byte []) ipDiffA.getPixels ();
	
		ImagePlus imgDiffB = new ImagePlus ("CheminD_acces_imB.pgm");
		ImageProcessor ipDiffB = imgDiffB . getProcessor ();
		byte [] pixelsDiffB = ( byte []) ipDiffB.getPixels ();

		int w = ipDiffB.getWidth ();
		int h = ipDiffB.getHeight ();

		ImageProcessor ipRes = new ByteProcessor (w,h);
		ImagePlus imgRes = new ImagePlus (" Soustraction ",ipRes );
		
		byte [] pixelsRes = ( byte []) ipRes.getPixels ();
		
		// A COMPLETER
		
		
		imgRes.show ();
		imgRes.updateAndDraw ();
	}	
}	
		
		