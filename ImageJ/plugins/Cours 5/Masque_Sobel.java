// Importation des paquets necessaires. Le plugin n'est pas lui-meme un paquet (pas de mot-cle package)

import ij.*;                            // pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter;    // pour interface PlugInFilter
import ij.process.*;                    // pour classe ImageProcessor
import ij.gui.*;                        // pour classe GenericDialog et Newimage

public class Masque_Sobel implements PlugInFilter {

    public int setup(String arg, ImagePlus imp) {

        return DOES_8G;
    }

    public void run(ImageProcessor ip) {
        int w = ip.getWidth();
        int h = ip.getHeight();
        byte[] pixels = (byte[]) ip.getPixels();
        ImagePlus resultSx = NewImage.createByteImage(" Masque Sobel Sx", w, h, 1, NewImage.FILL_BLACK);
        ImageProcessor iprSx = resultSx.getProcessor();

        ImagePlus resultSy = NewImage.createByteImage(" Masque Sobel Sy", w, h, 1, NewImage.FILL_BLACK);
        ImageProcessor iprSy = resultSy.getProcessor();

        // pas utilisé
        //byte[] pixelsr = (byte[]) ipr.getPixels();

        // Modifier le masque si on change la taille
        // masque taille 3, n = 1
        int[][] masqueSx = {
            {-1, -2, -1}, 
            {0, 0, 0}, 
            {1, 2, 1}
        };
        int[][] masqueSy = {
            {-1, 0, 1}, 
            {-2, 0, 2}, 
            {-1, 0, 1}
        };
        // masque taille 5, n = 3
        //int[][] masque = {{1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}};
        // masque taille 7, n = 5
        //int[][] masque = {{1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1}};

        int n = 1; // taille du demi - masque

        // Recopie des bords
        // Gauche
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < w; x++) {
                iprSx.putPixel(x, y, ip.getPixel(x, y));
                iprSy.putPixel(x, y, ip.getPixel(x, y));            }
        }
        // Droit
        for (int y = h - n; y < h; y++) {
            for (int x = 0; x < w; x++) {
                iprSx.putPixel(x, y, ip.getPixel(x, y));
                iprSy.putPixel(x, y, ip.getPixel(x, y));

            }
        }
        // Haut
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < n; x++) {
                iprSx.putPixel(x, y, ip.getPixel(x, y));
                iprSy.putPixel(x, y, ip.getPixel(x, y));            }
        }
        // Bas
        for (int y = 0; y < h; y++) {
            for (int x = w - n; x < h; x++) {
                iprSx.putPixel(x, y, ip.getPixel(x, y));
                iprSy.putPixel(x, y, ip.getPixel(x, y));            }
        }


        for (int y = n; y < w - n; y++) {
            for (int x = n; x < h - n; x++) {
                

                int pixMoySx = 0;
                int pixMoySy = 0;
                for (int i = 0; i < masqueSx.length; i++) {
                    for (int j = 0; j < masqueSx[i].length; j++) {
                        pixMoySx += ip.getPixel(y - n + i, x - n + j) * masqueSx[i][j];
                        pixMoySy += ip.getPixel(y - n + i, x - n + j) * masqueSy[i][j];
                    }
                }

                if (pixMoySx > 255) pixMoySx = 255;
                else if (pixMoySx < 0) pixMoySx = 0;

                if (pixMoySy > 255) pixMoySy = 255;
                else if (pixMoySy < 0) pixMoySy = 0;

                iprSx.putPixel(y, x, pixMoySx);
                iprSy.putPixel(y, x, pixMoySy);
            }
        }

        resultSx.show();
        resultSx.updateAndDraw();

        resultSy.show();
        resultSy.updateAndDraw();
    }
}