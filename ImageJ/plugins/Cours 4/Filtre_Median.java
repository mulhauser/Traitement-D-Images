// Importation des paquets necessaires. Le plugin n'est pas lui-meme un paquet (pas de mot-cle package)

import ij.*;                            // pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter;    // pour interface PlugInFilter
import ij.process.*;                    // pour classe ImageProcessor
import ij.gui.*;                        // pour classe GenericDialog et Newimage

public class Filtre_Median implements PlugInFilter {

    public int setup(String arg, ImagePlus imp) {

        return DOES_8G;
    }

    public void run(ImageProcessor ip) {
        int w = ip.getWidth();
        int h = ip.getHeight();
        byte[] pixels = (byte[]) ip.getPixels();
        ImagePlus result = NewImage.createByteImage(" Filtrage Median", w, h, 1, NewImage.FILL_BLACK);
        ImageProcessor ipr = result.getProcessor();
        byte[] pixelsr = (byte[]) ipr.getPixels();

        // Modifier le masque si on change la taille
        // masque taille 3, n = 1
        int[][] masque = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
        // masque taille 5, n = 3
        //int[][] masque = {{1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}};
        // masque taille 7, n = 5
        //int[][] masque = {{1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1}};

        int n = 1; // taille du demi - masque

        //A COMPLETER
        // Recopie des bords
        // Gauche
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < h; j++) {
                ipr.putPixel(i, j, ip.getPixel(i, j));
            }
        }
        // Droit
        for (int i = w - n; i < w; i++) {
            for (int j = 0; j < h; j++) {
                ipr.putPixel(i, j, ip.getPixel(i, j));
            }
        }
        // Haut
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < n; j++) {
                ipr.putPixel(i, j, ip.getPixel(i, j));
            }
        }
        // Bas
        for (int i = 0; i < w; i++) {
            for (int j = h - n; j < h; j++) {
                ipr.putPixel(i, j, ip.getPixel(i, j));
            }
        }

        int cptr = 0;
        for (int y = n; y < w - n; y++) {
            for (int x = n; x < h - n; x++) {


                int pixMoy = 0;
                int normalisation = 0;
                int k = 0;
                int[] a = new int[masque.length*masque.length];
                for (int i = 0; i < masque.length; i++) {
                    for (int j = 0; j < masque[i].length; j++) {

                        a[k] = ip.getPixel(y - n + i, x - n + j);
                        //int k = i+j;
                        //if(cptr < 2) IJ.log(k+" : "+a[i+(i*j)]);
                        k++;
                    }
                }
                cptr++;
                pixMoy = mediane(a);

                ipr.putPixel(y, x, pixMoy);
            }
        }

        result.show();
        result.updateAndDraw();
    }

    static int mediane(int a[]) {
        int[] effectifs = new int[256]; // tableau des effectifs
        for (int i = 0; i < a.length; i++) {
            effectifs[a[i]]++;
        }
        int somme = 0;
        for (int i = 0; i <= 255; i++) {
            somme = somme + effectifs[i];
            if (2 * somme >= a.length) return i;
        }
        return -1;
    }


}