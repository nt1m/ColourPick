import java.awt.*;
import java.io.*;
import java.util.*;
import java.io.OutputStreamWriter;

public class Jeu
{
	// Parametres de la fenetre
	static int largeurFenetre = 800;
	static int hauteurFenetre = 750;
	static int largeurPalette,hauteurPalette= 100;
	// Parametres d'affichage des palettes
	static int y1 = 600;
	static int nombrePalette = 4;
	static int nombreCouleurs = 4;
	// Parametres d'affichage de l'image
	static int largeurImage;
	static int hauteurImage;
	// Score
	static int score = 0;

	static void afficherImage(int[][][] tableau, boolean enCouleurs)
	{
			// Affichage
			int i,j,k;
			largeurImage = tableau.length;hauteurImage = tableau[0].length;
			Isn.drawRect(0.5*(largeurFenetre-largeurImage) - 1,50 - 1,largeurImage + 1,hauteurImage + 1,70,70,70);
			for (i=0;i<=largeurImage-1;i++)
			{
				for (j=0;j<=hauteurImage-1;j++)
				{
						double xImage = i+0.5*(largeurFenetre-largeurImage);
						int Moyenne = ((tableau[i][j][0]+tableau[i][j][1]+tableau[i][j][2])/3);
						int[] pixel = tableau[i][j];
						if (enCouleurs)
						{
							Isn.drawPixel(xImage, j+50, pixel[0], pixel[1], pixel[2]);
						}
						else
						{
							Isn.drawPixel(xImage,j+50,Moyenne, Moyenne,Moyenne);
						}
				}
			}

	}
	static int afficherPalettes(String nomdeFichier, int difficulte)
	{
			int tableauPalettes[][][] = Palettes.generateAllPalettes(difficulte, nomdeFichier);

			largeurPalette = (largeurFenetre / nombrePalette) - 10;

			// Permutation aleatoire de la palette correcte
			int indexCorrect = (int) Math.round(Math.random() * 3);
			int[][] cache = tableauPalettes[indexCorrect];
			tableauPalettes[indexCorrect] = tableauPalettes[0];
			tableauPalettes[0] = cache;

			for (int x = 0; x < nombrePalette; x++)
			{
				int xPalette = x * (largeurFenetre / nombrePalette) + 5;
				int yPalette = y1;
				// Nombre de palettes
				Isn.drawRect(xPalette - 1,yPalette - 1,largeurPalette + 1,hauteurPalette +1.5,70,70,70);
				// Nombre de couleurs
				for (int y = 0; y < nombreCouleurs; y++)
				{
					int xTeinte = xPalette;
					int yTeinte = y1 + y * hauteurPalette / nombreCouleurs;

					for (int z = 0; z < 3; z++)
					{
						int[] pixel = tableauPalettes[x][y];
						Isn.paintRect(xTeinte, yTeinte, largeurPalette-1,
													(hauteurPalette / nombreCouleurs),
													pixel[0], pixel[1], pixel[2]);
					}
				}

			}
			//Isn.closeIn(palette);
			return indexCorrect;
	}

	static int click(int indexCorrect)
	{
		//System.out.println(indexCorrect);
		while (true)
		{
			Point pos = new Point();
			pos = Isn.readClick();
			int numero = (int) (5+pos.x)/(largeurPalette+10);

			//System.out.println(hauteurPalette +" " +y1 );
			if (pos.y < y1+hauteurPalette && pos.y > y1)
			{
					if (numero == indexCorrect)
					{
						System.out.println("Gagne !");
						score++;
					}
					else
					{
						System.out.println("Perdu !");
					}
					return numero;

			}
		}
	}
	static void afficherResultat(int indexCorrect, int indexClick, int[][][] tableauPixels)
	{
		int rayon = 30, x = (indexClick * (largeurFenetre/nombrePalette) + 5)+(largeurPalette/2), y = y1 + hauteurPalette / 2;
		Isn.paintCircle(x, y, rayon, 255,255,255);

		if (indexCorrect == indexClick)
		{
			// Isn.paintCircle(x, y, rayon / 2, 10,255,10);
			int longueurTrait = 25;
			Isn.drawCircle(x, y, rayon + 1, 39, 174, 96);
			Isn.drawLine(x + longueurTrait/2 + longueurTrait/4, y - longueurTrait/2, x - longueurTrait/2 + longueurTrait/4, y + longueurTrait/2, 39, 174, 96);
			Isn.drawLine(x - longueurTrait/2 + longueurTrait/4, y + longueurTrait/2, x - longueurTrait + longueurTrait/4, y - longueurTrait/8, 39, 174, 96);
			afficherImage(tableauPixels, true);
		}
		else
		{
			int longueurTrait = 25;
			Isn.drawCircle(x, y, rayon + 1, 255,10,10);
			Isn.drawLine(x - longueurTrait/2, y - longueurTrait/2, x + longueurTrait/2, y + longueurTrait/2, 255, 10, 10);
			Isn.drawLine(x + longueurTrait/2, y - longueurTrait/2, x - longueurTrait/2, y + longueurTrait/2, 255, 10, 10);
			// Isn.paintCircle(x, y, rayon / 2, 255,10,10);
		}
		Isn.sleep(1000);
	}
	static void Score()
	{
		java.util.Scanner f3 = Isn.openIn("donnees/meilleurScore.txt");
		int meilleurScore = Isn.readIntFromFile(f3);
		Isn.closeIn(f3);
		if (score > meilleurScore)
		{
			OutputStreamWriter f2;
			f2 = Isn.openOut("donnees/meilleurScore.txt");
			Isn.printToFile(f2,score);
			Isn.closeOut(f2);
			meilleurScore = score;
			System.out.println("Bravo! Vous avez le meilleur score: "+score+"!");
		}
		else
		{
			System.out.println("Votre score: "+score);
			System.out.println("Meilleur score: "+meilleurScore);
		}
		afficherScore(score, meilleurScore);
	}
	public static void defilerImages(int difficulte)
	{
				String Images[] = {
												// "kitten.ppm",
												"blossom.ppm",
												// "blossomalt.ppm",
												// "corn.ppm",
												"lisa.ppm",
												"panda.ppm",
												"nature.ppm",
												// "beach.ppm",
												"canyon.ppm",
												// "bristol.ppm",
												// "seawalk.ppm",
												// "rocks.ppm",
												// "stars.ppm",
												// "bike.ppm",
												// "tunnel.ppm",
												// "autumn.ppm",
												// "beachsunset.ppm",
												// "steelwheels.ppm",
												// "searocks.ppm",
												// "sunsetseawalk.ppm",
												// "pontsuspendu.ppm"
												//"mimosa.ppm"
												// "pinkflowers.ppm",
												// "london.ppm",
												// "strawberries.ppm",
												// "flowers.ppm",
												// "eiffel.ppm",
												// "dragonbridge.ppm"
											};
		int z = Images.length;
		Boolean ImagesDejaFaites[] = new Boolean[z];
		for (int j = 0; j < z; j++) {
			ImagesDejaFaites[j] = false;
		}
		int i = 0;

		while (i != z)
		{
			int a = (int) Math.floor(Math.random() * (z));
			while (ImagesDejaFaites[a])
			{
				a = (int) Math.floor(Math.random() * (z));
			}

			String Tour = "photos/" + Images[a];
			ImagesDejaFaites[a] = true;
			int[][][] tableauPixels = Isn.getImageData(Tour);
			afficherImage(tableauPixels, false);
			int indexCorrect = afficherPalettes(Tour, difficulte);
			int numeroChoisi = click(indexCorrect);
			afficherResultat(indexCorrect, numeroChoisi, tableauPixels);
			effacerFenetre();
			i++;
		}
	}

	public static void effacerFenetre()
	{
		Isn.paintRect(0,0,largeurFenetre,hauteurFenetre,20,20,20);
	}

	public static void afficherScore(int score, int meilleurScore)
	{
		int tableau[][][] = Isn.getImageData("ecrans/score.ppm");
		for (int i=0;i < largeurFenetre;i++)
		{
			for (int j=0;j < hauteurFenetre;j++)
			{
					int[] pixel = tableau[i][j];

					Isn.drawPixel(i, j, pixel[0], pixel[1], pixel[2]);
			}
		}
		Isn.paintNumber(score, largeurFenetre/2, hauteurFenetre/2, 200);
		Isn.paintNumber(meilleurScore, largeurFenetre/2 + 100, hauteurFenetre/2 + 171, 25);
	}

	public static void main(String[] args)
	{
		Isn.initDrawing("ColourPick",10,10,largeurFenetre,hauteurFenetre);
		Menu.affichageMenu();
		String choixMenu = Menu.choixMenu();
		effacerFenetre();
		while (choixMenu != "jeu") {
			if (choixMenu == "regles") {
				Menu.affichageRegles();
				Isn.readClick();
				Menu.affichageMenu();
				choixMenu = Menu.choixMenu();
				effacerFenetre();
			}
		}	

		int difficulte = Menu.affichageDifficultes();
		effacerFenetre();
		defilerImages(difficulte);
		Score();
		Isn.readClick();
		System.exit(1);
	}
}
