// Importation des paquets necessaires. Le plugin n'est pas lui-meme un paquet (pas de mot-cle package)

import ij.*;                            // pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter;    // pour interface PlugInFilter
import ij.process.*;                    // pour classe ImageProcessor
import ij.gui.*;

import java.lang.Math;                        // pour classe GenericDialog et Newimage

public class Seuillage_Hysteresis implements PlugInFilter {

    public int setup(String arg, ImagePlus imp) {

        return DOES_8G;
    }

    public void run(ImageProcessor ip) {
        int w = ip.getWidth();
        int h = ip.getHeight();
        byte[] pixels = (byte[]) ip.getPixels();
        ImagePlus result = NewImage.createByteImage("Seuillage Hysteresis", w, h, 1, NewImage.FILL_BLACK);
        ImageProcessor ipr = result.getProcessor();


        // pas utilisé
        //byte[] pixelsr = (byte[]) ipr.getPixels();

        // Modifier le masque si on change la taille
        // masque taille 3, n = 1
        int[][] masqueSx = {
                {1, 0, -1},
                {2, 0, -2},
                {1, 0, -1}
        };
        int[][] masqueSy = {
                {1, 2, 1},
                {0, 0, 0},
                {-1, -2, -1}
        };
        // masque taille 5, n = 3
        //int[][] masque = {{1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}};
        // masque taille 7, n = 5
        //int[][] masque = {{1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1}};

        int n = 1; // taille du demi - masque

        // Recopie des bords
        // Gauche
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < h; x++) {
                ipr.putPixel(x, y, ip.getPixel(x, y));
            }
        }
        // Droit
        for (int y = w - n; y < w; y++) {
            for (int x = 0; x < h; x++) {
                ipr.putPixel(x, y, ip.getPixel(x, y));
            }
        }
        // Haut
        for (int y = 0; y < w; y++) {
            for (int x = 0; x < n; x++) {
                ipr.putPixel(x, y, ip.getPixel(x, y));
            }
        }
        // Bas
        for (int y = 0; y < w; y++) {
            for (int x = h - n; x < h; x++) {
                ipr.putPixel(x, y, ip.getPixel(x, y));
            }
        }

        double Sh = 30;
        double Sb = 150; // On essaie avec 0.4

        double[][] pixMoyTab = new double[w][h];
        boolean[][] pixContourTab = new boolean[w][h];
        //int[][] pixDirTab = new int[w][h];
        for (int y = n; y < w - n; y++) {
            for (int x = n; x < h - n; x++) {


                double pixMoySx = 0;
                double pixMoySy = 0;
                for (int i = 0; i < masqueSx.length; i++) {
                    for (int j = 0; j < masqueSx[i].length; j++) {
                        pixMoySx += ip.getPixel(y - n + i, x - n + j) * masqueSx[i][j];
                        pixMoySy += ip.getPixel(y - n + i, x - n + j) * masqueSy[i][j];
                    }
                }


                double pixMoy = Math.sqrt((pixMoySx * pixMoySx) + (pixMoySy * pixMoySy));

                pixMoyTab[y][x] = pixMoy;
                if(pixMoy < Sb){
                    pixContourTab[y][x] = false;
                }else if(pixMoy > Sh){
                    pixContourTab[y][x] = true;
                }else{
                    if(pixContourTab[y][x-1]) pixContourTab[y][x] = true; // on test a gauche
                    else if(pixContourTab[y-1][x-1]) pixContourTab[y][x] = true; // on test en haut a gauche
                    else if(pixContourTab[y-1][x]) pixContourTab[y][x] = true; // on en haut
                    else pixContourTab[y][x] = false;
                }


            }
        }

        for (int y = n; y < w - n; y++) {
            for (int x = n; x < h - n; x++) {
                if(pixContourTab[y][x]) ipr.putPixel(y, x, (int) pixMoyTab[y][x]);
                else if(pixMoyTab[y][x] > Sb && pixMoyTab[y][x] < Sh){
                    if (pixContourTab[y][x - 1]) {
                        pixContourTab[y][x] = true; // on test a gauche
                        ipr.putPixel(y, x, (int) pixMoyTab[y][x]);
                    } else if (pixContourTab[y - 1][x - 1]) {
                        pixContourTab[y][x] = true; // on test en haut a gauche
                        ipr.putPixel(y, x, (int) pixMoyTab[y][x]);
                    } else if (pixContourTab[y - 1][x]) {
                        pixContourTab[y][x] = true; // on en haut
                        ipr.putPixel(y, x, (int) pixMoyTab[y][x]);
                    } else {
                        pixContourTab[y][x] = false;
                        ipr.putPixel(y, x, 0);
                    }
                }else{
                    ipr.putPixel(y, x, 0);
                }
            }
        }


        result.show();
        result.updateAndDraw();

    }
}