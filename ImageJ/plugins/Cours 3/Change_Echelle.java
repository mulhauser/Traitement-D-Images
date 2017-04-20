// Importation des paquets n�cessaires. Le plugin n'est pas lui-m�me un paquet (pas de mot-cl� package)

import ij.*;                            // pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter;    // pour interface PlugInFilter
import ij.process.*;                    // pour classe ImageProcessor
import ij.gui.*                            // pour GenericDialog

public class Change_Echelle implements PlugInFilter {

    public int setup(String arg, ImagePlus imp) {
        return DOES_8G;
    }

    public void run(ImageProcessor ip) {
        // dialogue permettant de fixer la valeur du facteur d�echelle
        GenericDialog gd = new GenericDialog(" Facteur d�echelle ", IJ.getInstance());

        gd.addSlider(" facteur ", 0.0, 10.0, 2.0);
        gd.showDialog();
        if (gd.wasCanceled()) {
            IJ.error(" PlugIn cancelled ");
            return;
        }

        double ratio = (double) gd.getNextNumber();
        int w = ip.getWidth();
        int h = ip.getHeight();
        byte[] pixels = (byte[]) ip.getPixels();
        //A COMPLETER
        ImagePlus result = NewImage.createByteImage(" Retailler ",/*A COMPLETER */, /*A COMPLETER*/, 1, NewImage.FILL_BLACK);
        ImageProcessor ipr = result.getProcessor();
        byte[] pixelsr = (byte[]) ipr.getPixels();
        //A COMPLETER
        result.show();
        result.updateAndDraw();
    }
}
