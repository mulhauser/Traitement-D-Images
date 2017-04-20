/**
 * Created by mulhauser on 20/04/2017.
 */
public class Test {

    public static void main(String[] args){
        int[] a = {5, 7, 5, 3, 255, 5, 3, 0, 2};
        System.out.println(mediane(a));
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
