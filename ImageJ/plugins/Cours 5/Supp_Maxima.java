// Importation des paquets necessaires. Le plugin n'est pas lui-meme un paquet (pas de mot-cle package)

import ij.*;                            // pour classes ImagePlus et IJ
import ij.plugin.filter.PlugInFilter;    // pour interface PlugInFilter
import ij.process.*;                    // pour classe ImageProcessor
import ij.gui.*;

import java.lang.Math;                        // pour classe GenericDialog et Newimage

public class Supp_Maxima implements PlugInFilter {

    public int setup(String arg, ImagePlus imp) {

        return DOES_8G;
    }

    public void run(ImageProcessor ip) {
        int w = ip.getWidth();
        int h = ip.getHeight();
        byte[] pixels = (byte[]) ip.getPixels();
        ImagePlus result = NewImage.createByteImage(" Norme Gradient Seuil", w, h, 1, NewImage.FILL_BLACK);
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

        double[][] pixMoyTab = new double[w][h];
        int[][] pixDirTab = new int[w][h];
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

                //if (pixMoySx < 25) pixMoySx = 0;
                //else if (pixMoySx < 0) pixMoySx = 0;

                //if (pixMoySy < 25) pixMoySy = 0;
                //else if (pixMoySy < 0) pixMoySy = 0;

                // Angle en degres
                double dir = Math.toDegrees(Math.atan(pixMoySy / pixMoySx));

                if(Double.isNaN(dir)) dir = 0.0;

                // Partie droite
                if (pixMoySx >= 0) {
                    if (pixMoySy >= 0) {
                        // Partie Haute
                        // Regarde entre 90 et 45
                        // Partie 90
                        if (dir > (45 + 45 / 2)) {
                            pixDirTab[y][x] = 90;
                            // Partie 45
                        } else {
                            pixDirTab[y][x] = 45;
                        }
                        // Regarde entre 45 et 0
                        // Partie 45
                        if (dir > 45 / 2) {
                            pixDirTab[y][x] = 45;
                            // Partie 0
                        } else {
                            pixDirTab[y][x] = 0;
                        }
                    } else {
                        // Partie Basse
                        // Regarde entre 90 et 45
                        // Partie 90
                        if (dir > (45 + 45 / 2)) {
                            pixDirTab[y][x] = 270;
                            // Partie 45
                        } else {
                            pixDirTab[y][x] = 315;
                        }
                        // Regarde entre 45 et 0
                        // Partie 45
                        if (dir > 45 / 2) {
                            pixDirTab[y][x] = 315;
                            // Partie 0
                        } else {
                            pixDirTab[y][x] = 0;
                        }
                    }
                } else {
                    // Partie Gauche
                    if (pixMoySy >= 0) {
                        // Partie Haute
                        // Regarde entre 90 et 45
                        // Partie 90
                        if (dir > (45 + 45 / 2)) {
                            pixDirTab[y][x] = 90;
                            // Partie 45
                        } else {
                            pixDirTab[y][x] = 135;
                        }
                        // Regarde entre 45 et 0
                        // Partie 45
                        if (dir > 45 / 2) {
                            pixDirTab[y][x] = 135;
                            // Partie 0
                        } else {
                            pixDirTab[y][x] = 180;
                        }
                    } else {
                        // Partie Basse
                        // Regarde entre 90 et 45
                        // Partie 90
                        if (dir > (45 + 45 / 2)) {
                            pixDirTab[y][x] = 270;
                            // Partie 45
                        } else {
                            pixDirTab[y][x] = 225;
                        }
                        // Regarde entre 45 et 0
                        // Partie 45
                        if (dir > 45 / 2) {
                            pixDirTab[y][x] = 225;
                            // Partie 0
                        } else {
                            pixDirTab[y][x] = 180;
                        }
                    }
                }

                double pixMoy = Math.sqrt((pixMoySx * pixMoySx) + (pixMoySy * pixMoySy));

                pixMoyTab[y][x] = pixMoy;
                //ipr.putPixel(y, x, (int) pixMoy);
            }
        }

        for (int y = n; y < w - n; y++) {
            for (int x = n; x < h - n; x++) {
                if (pixDirTab[y][x] == 0) {
                    if (pixMoyTab[y][x - 1] > pixMoyTab[y][x] || pixMoyTab[y][x + 1] > pixMoyTab[y][x])
                        ipr.putPixel(x, y, 0);
                    else ipr.putPixel(x, y, (int) pixMoyTab[y][x]);
                } else if (pixDirTab[y][x] == 45) {
                    if (pixMoyTab[y - 1][x - 1] > pixMoyTab[y][x] || pixMoyTab[y + 1][x + 1] > pixMoyTab[y][x])
                        ipr.putPixel(x, y, 0);
                    else ipr.putPixel(x, y, (int) pixMoyTab[y][x]);
                } else if (pixDirTab[y][x] == 90) {
                    if (pixMoyTab[y - 1][x] > pixMoyTab[y][x] || pixMoyTab[y + 1][x] > pixMoyTab[y][x])
                        ipr.putPixel(x, y, 0);
                    else ipr.putPixel(x, y, (int) pixMoyTab[y][x]);
                } else if (pixDirTab[y][x] == 135) {
                    if (pixMoyTab[y - 1][x + 1] > pixMoyTab[y + 1][x - 1] || pixMoyTab[y][x + 1] > pixMoyTab[y][x])
                        ipr.putPixel(x, y, 0);
                    else ipr.putPixel(x, y, (int) pixMoyTab[y][x]);
                } else if (pixDirTab[y][x] == 180) {
                    if (pixMoyTab[y][x + 1] > pixMoyTab[y][x] || pixMoyTab[y][x - 1] > pixMoyTab[y][x])
                        ipr.putPixel(x, y, 0);
                    else ipr.putPixel(x, y, (int) pixMoyTab[y][x]);
                } else if (pixDirTab[y][x] == 225) {
                    if (pixMoyTab[y + 1][x + 1] > pixMoyTab[y][x] || pixMoyTab[y - 1][x - 1] > pixMoyTab[y][x])
                        ipr.putPixel(x, y, 0);
                    else ipr.putPixel(x, y, (int) pixMoyTab[y][x]);
                } else if (pixDirTab[y][x] == 270) {
                    if (pixMoyTab[y + 1][x] > pixMoyTab[y][x] || pixMoyTab[y - 1][x] > pixMoyTab[y][x])
                        ipr.putPixel(x, y, 0);
                    else ipr.putPixel(x, y, (int) pixMoyTab[y][x]);
                } else if (pixDirTab[y][x] == 315) {
                    if (pixMoyTab[y + 1][x - 1] > pixMoyTab[y][x] || pixMoyTab[y - 1][x + 1] > pixMoyTab[y][x])
                        ipr.putPixel(x, y, 0);
                    else ipr.putPixel(x, y, (int) pixMoyTab[y][x]);
                }
            }
        }


        result.show();
        result.updateAndDraw();

    }
}