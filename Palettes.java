import java.util.*;
import java.io.*;
public class Palettes {

  public static void main(String[] args) {
    int[][][] p = generateAllPalettes(0, "photos/panda.ppm");
    System.out.println("done with palettes");
    drawPalettes(p);
  }

  public static int[][][] generateAllPalettes(int difficulty, String filename) {
    int[][][] palettes = new int[4][4][3];
    int[][] correctOne = getColorPalettes(filename);
    palettes[0] = correctOne;
    for (int i = 1; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        switch (difficulty) {
          case 0:
            palettes[1][j][0] = 255 - palettes[0][j][0];
            palettes[1][j][1] = 255 - palettes[0][j][1];
            palettes[1][j][2] = 255 - palettes[0][j][2];

            palettes[2][j][0] = (int) Math.round(Math.random() * 255);
            palettes[2][j][1] = (int) Math.round(Math.random() * 255);
            palettes[2][j][2] = (int) Math.round(Math.random() * 255);

            palettes[3][j][0] = Math.min(255, palettes[0][j][0] + 5 + (int) (Math.random() * 90));
            palettes[3][j][1] = Math.min(255, palettes[0][j][1] + 5 + (int) (Math.random() * 90));
            palettes[3][j][2] = Math.min(255, palettes[0][j][2] + 5 + (int) (Math.random() * 90));
            break;
          case 1:
            for (int k = 0; k < 3; k++) {
              int delta = (int) (Math.random() * 100) - 50;
              if (delta < 0) {
                palettes[i][j][k] = Math.max(palettes[0][j][k] + delta, 0);
              } else {
                palettes[i][j][k] = Math.min(palettes[0][j][k] + delta, 255);
              }

            }
            break;
        }
      }
    }
    return palettes;
  }
  static void printTable(int[][] tableau) {
    System.out.print("r ");
    for (int i = 0; i < tableau.length; i++) {
      System.out.print(tableau[i][0] + " ");
    }

    System.out.print("\ng ");
    for (int i = 0; i < tableau.length; i++) {
      System.out.print(tableau[i][1] + " ");
    }

    System.out.print("\nb ");
    for (int i = 0; i < tableau.length; i++) {
      System.out.print(tableau[i][2] + " ");
    }
    System.out.println();
  }
  static void drawPalettes(int[][][] palettes) {
    Isn.initDrawing("Palettes", 0,0,200,200);
    for (int i = 0; i < palettes.length; i++) {
      for (int j = 0; j < 4; j++) {
        Isn.paintRect(j * 50, i * 50, 50, 50, palettes[i][j][0], palettes[i][j][1], palettes[i][j][2]);
      }
    }
  }

  static int[][] getColorPalettes(String filename) {
    int pixelArray[][][] = Isn.getImageData(filename);
    int palettesAlt[][][] = new int[4][(int) pixelArray.length / 4][3];
    int tableau[][] = convertToColorList(pixelArray);
    sortColorList(tableau, 0, tableau.length - 1, getBiggestColorRange(tableau));

    int[][] firstHalf = getSplitArray(tableau, 0);
    sortColorList(firstHalf, 0, firstHalf.length - 1, getBiggestColorRange(firstHalf));


    int[][] firstQuarter = getSplitArray(firstHalf, 0);
    int[][] secondQuarter = getSplitArray(firstHalf, 1);
    // sortColorList(firstQuarter, 0, firstQuarter.length - 1, getBiggestColorRange(firstQuarter));
    // sortColorList(secondQuarter, 0, secondQuarter.length - 1, getBiggestColorRange(secondQuarter));
    // printTable(firstQuarter);
    // printTable(secondQuarter);

    int[][] secondHalf = getSplitArray(tableau, 1);
    sortColorList(secondHalf, 0, secondHalf.length - 1, getBiggestColorRange(secondHalf));

    int[][] thirdQuarter = getSplitArray(secondHalf, 0);
    int[][] forthQuarter = getSplitArray(secondHalf, 1);
    // sortColorList(thirdQuarter, 0, thirdQuarter.length - 1, getBiggestColorRange(thirdQuarter));
    // sortColorList(forthQuarter, 0, forthQuarter.length - 1, getBiggestColorRange(forthQuarter));
    // printTable(thirdQuarter);
    // printTable(forthQuarter);

    palettesAlt[0] = firstQuarter;
    palettesAlt[1] = secondQuarter;
    palettesAlt[2] = thirdQuarter;
    palettesAlt[3] = forthQuarter;

    return convertToListAverage(palettesAlt);

    // int j = 0;
    // while (j < colorCount) {
    //   int[][] firstHalf = getSplitArray(tableau, 0);
    //   int[][] secondHalf = getSplitArray(tableau, 1);
    //   cache[j] = firstHalf;
    //   j++;
    //   cache[j] = secondHalf;
    //   j++;
    //   if (j !== colorCount) {
    //     j = 0;
    //     tableau = firstHalf;
    //     j =
    //   }
    // }
    // palettesAlt = cache;
    // int j = 0;
    // for (int i = 1; i <= Math.log((double) colorCount) / Math.log(2); i++) {
    //   for (int j = 0; j < Math.pow(2, i) / 2; j++) {
    //     sortColorList(tableau, 0, tableau.length - 1, getBiggestColorRange(tableau));
    //     cache[j] = getSplitArray(tableau, 0);
    //     cache[j + 1] = getSplitArray(tableau, 1);
    //
    //     tableau = cache[i];
    //     cache
    //   }
    //   int[][] firstHalf = getSplitArray(tableau, 0);
    //   palettesAlt[j] = firstHalf;
    //   j++;
    //
    //   palettesAlt[j] = secondHalf;
    //   j++;
    //   int[][] secondHalf = getSplitArray(tableau, 1);
    // }
  }
  static int[][] convertToListAverage(int[][][] array) {
    int [][] palettes = new int[array.length][3];
    // for (int i = 0; i < array.length; i++) {
    //   for (int j = 0; j < array[i].length; j++) {
    //     for (int k = 0; k < 3; k++) {
    //       palettes[i][k] += array[i][j][k];
    //     }
    //     palettes[i][0] /= array[i][j].length;
    //     palettes[i][1] /= array[i][j].length;
    //     palettes[i][2] /= array[i][j].length;
    //   }
    // }

    for (int i = 0; i < array.length; i++) {
      // int index = i == 1 || i == 2 ? (int) (array[i].length) / 2 :
      //                                (i == 0) ? 0 : array.length - 1;
      int index = (int) (array[i].length / 2);
      palettes[i][0] = array[i][index][0];
      palettes[i][1] = array[i][index][1];
      palettes[i][2] = array[i][index][2];
    }

    // for (int i = 0; i < array.length; i++) {
    //   int index = i % 2 == 0 ? 0 : array[i].length - 1;
    //   // int index = (int) (array[i].length / 2);
    //   palettes[i][0] = array[i][index][0];
    //   palettes[i][1] = array[i][index][1];
    //   palettes[i][2] = array[i][index][2];
    // }
    
    return palettes;
  }
  static int[][] getSplitArray(int[][] table, int index) {
    int half = (int) (table.length / 2);
    int[][] array = new int[half][3];
    for (int i = 0; i < half; i++) {
      int indexalt = index * half + i;
      array[i] = table[indexalt];
    }
    return array;
  }
  public static int partition(int[][] tab, int debut, int fin, int c){
    int k = debut;
    int indicePivot = (int) Math.floor((debut + fin)/2);

    //Placer le pivot au debut
    int tmp[] = tab[indicePivot];
    tab[indicePivot] = tab[debut];
    tab[debut] = tmp;

    for (int i = debut+1; i <= fin; i=i+1){
      // Si valeur inferieure au pivot
      if (tab[i][c] > tab[debut][c]) {
        tmp = tab[i];
        tab[i] = tab[k+1];
        tab[k+1] = tmp;
        k++;
      }
    }

    tmp = tab[debut];
    tab[debut] = tab[k];
    tab[k] = tmp;
    return k;
  }

  public static void sortColorList(int[][] list, int debut, int fin, int c) {
    if (fin > debut) {
      int indicePivot = partition(list, debut, fin, c);
      sortColorList(list, debut, indicePivot - 1, c);
      sortColorList(list, indicePivot + 1, fin, c);
    }
  }
  static int[][] convertToColorList(int[][][] array) {
    int colorList[][] = new int[array.length * array[0].length][3];
    int i = 0;
    for (int x = 0; x < array.length; x++) {
      for (int y = 0; y < array[0].length; y++) {
        colorList[i] = array[x][y];
        i++;
      }
    }
    return colorList;
  }


  static int getBiggestColorRange(int[][] table) {
    int frequencies[][] = new int[3][256];
    int rCount = 0, gCount = 0, bCount = 0;
    int rMax = 0, gMax = 0, bMax = 0;
    int rMin = 255, gMin = 255, bMin = 255;
    for (int i = 0; i < table.length; i++) {
      for (int ch = 0; ch < 3; ch++) {
        int chval = table[i][ch];
        if (frequencies[ch][chval] == 0) {
          frequencies[ch][chval] = 1;
        }
        switch (ch) {
          case 0:
            rMax = Math.max(rMax, chval);
            rMin = Math.min(rMin, chval);
            break;
          case 1:
            gMax = Math.max(gMax, chval);
            gMin = Math.min(gMin, chval);
            break;
          case 2:
            bMax = Math.max(bMax, chval);
            bMin = Math.min(bMin, chval);
            break;
        }
      }
    }
    for (int c = 0; c < 3; c++) {
      for (int i = 0; i < 256; i++) {
        if (c == 0 && frequencies[c][i] == 1) {
          rCount++;
        } else if (c == 1 && frequencies[c][i] == 1) {
          gCount++;
        } else if (c == 2 && frequencies[c][i] == 1) {
          bCount++;
        }
      }
    }

    int deltaR = rMax - rMin, deltaG = gMax - gMin, deltaB = bMax - bMin;
    int deltaMax = Math.max(Math.max(deltaR, deltaG), deltaB);
    int cMax = Math.max(Math.max(rCount, gCount), bCount);
    if ((cMax == rCount && cMax == gCount) ||
        (cMax == rCount && cMax == bCount) ||
        (cMax == bCount && cMax == gCount)) {
      if (deltaMax == deltaR) {
        return 0;
      } else if (deltaMax == deltaG) {
        return 1;
      } else {
        return 2;
      }
    } else if (cMax == rCount) {
      return 0;
    } else if (cMax == gCount) {
      return 1;
    } else {
      return 2;
    }
  }
}
