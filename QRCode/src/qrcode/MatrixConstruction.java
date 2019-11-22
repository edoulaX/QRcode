package qrcode;

public class MatrixConstruction {
	
	public static int B = 0xFF_00_00_00;
	public static int W = 0xFF_FF_FF_FF;

	/*
	 * Constants defining the color in ARGB format
	 * 
	 * W = White integer for ARGB
	 * 
	 * B = Black integer for ARGB
	 * 
	 * both needs to have their alpha component to 255
	 */
	// TODO add constant for White pixel
	// TODO add constant for Black pixel
	

	// ...  MYDEBUGCOLOR = ...;
	// feel free to add your own colors for debugging purposes

	/**
	 * Create the matrix of a QR code with the given data.
	 * 
	 * @param version
	 *            The version of the QR code
	 * @param data
	 *            The data to be written on the QR code
	 * @param mask
	 *            The mask used on the data. If not valid (e.g: -1), then no mask is
	 *            used.
	 * @return The matrix of the QR code
	 */
	public static int[][] renderQRCodeMatrix(int version, boolean[] data, int mask) {

		/*
		 * PART 2
		 */
		int[][] matrix = constructMatrix(version, mask);
		/*
		 * PART 3
		 */
		addDataInformation(matrix, data, mask);
		
		return matrix;
	}

	/*
	 * =======================================================================
	 * 
	 * ****************************** PART 2 *********************************
	 * 
	 * =======================================================================
	 */

	/**
	 * Create a matrix (2D array) ready to accept data for a given version and mask
	 * 
	 * @param version
	 *            the version number of QR code (has to be between 1 and 4 included)
	 * @param mask
	 *            the mask id to use to mask the data modules. Has to be between 0
	 *            and 7 included to have a valid matrix. If the mask id is not
	 *            valid, the modules would not be not masked later on, hence the
	 *            QRcode would not be valid
	 * @return the qrcode with the patterns and format information modules
	 *         initialized. The modules where the data should be remain empty.
	 */
	public static int[][] constructMatrix(int version, int mask) {
		/* Rassemblement de toutes les methodes crees afin d afficher
		la base du QRcode (sans le message) */
		int matrix [][]= initializeMatrix( version);
		addFinderPatterns( matrix);
		addAlignmentPatterns(matrix,version);
		addTimingPatterns(matrix);
		addDarkModule(matrix);
		addFormatInformation(matrix, mask);
		
		// TODO Implementer
		return matrix ;

	}

	/**
	 * Create an empty 2d array of integers of the size needed for a QR code of the
	 * given version
	 * 
	 * @param version
	 *            the version number of the qr code (has to be between 1 and 4
	 *            included
	 * @return an empty matrix
	 */
	public static int[][] initializeMatrix(int version) {
		
		int taille =  QRCodeInfos.getMatrixSize(version);
		int [][] matrix= new int[taille][taille];
		for(int i=0; i<taille; i++) {
			for(int j=0; j<taille; j++) {
				matrix[i][j]= 0x00_00_00_00;
			}
		}
		
		return matrix;
	}
	
	public static void carre(int [][] matrix, int locationX,int locationY) {
		// Cette fonction cree un Finder patterns et le place suivant la coordonee de l'angle droit haut gauche de celui ci.
		for (int i=locationX;i <locationX+7;i++) {
			for (int j=locationY;j <locationY+7;j++) {
				matrix[i][j]=B;
			}	
		}
		for (int i=locationX+1;i <locationX+6;i++) {
			for (int j=locationY+1;j <locationY+6;j++) {
				matrix[i][j]=W;
			}
		}
		
		for (int i=locationX+2;i <locationX+5;i++) {
			for (int j=locationY+2;j <locationY+5;j++) {
				matrix[i][j]=B;
			}
		}
		
		 
		
	}
	
	/**
	 *  cree un separateur horizontal et place cette ligne a partir de la coordonnee du module le plus a gauche de celle ci 
	 *  ( a utiliser dans la fonction addFinderPatterns )
	 * @param matrix
	 * @param locationX
	 * @param locationY
	 */
	public static void ligneHorBlanche(int [][] matrix, int locationX,int locationY) {
		for (int i=locationX;i <locationX+8;i++) {
			matrix[i][locationY]=W;
		}
	}
	
	/**
	 *  cree un separateur horizontal et place cette ligne a partir de la coordonnee du module le plus a gauche de celle ci 
	 *  ( a utiliser dans la fonction addFinderPatterns )
	 * @param matrix
	 * @param locationX
	 * @param locationY
	 */
	public static void ligneVerBlanche(int [][] matrix, int locationX,int locationY) {
		for (int i=locationY;i <locationY+8;i++) {
			matrix[locationX][i]=W;
		}
	}

	/**
	 * Add all finder patterns to the given matrix with a border of White modules.
	 * Utilisation de la methode "carre","ligneHorBlanche" et "ligneVerBlanche"
	 * @param matrix
	 *            the 2D array to modify: where to add the patterns
	 */
	
	public static void addFinderPatterns(int[][] matrix) {
		
		int longMatrix=matrix.length;
		carre(matrix,0, 0);
		carre(matrix,longMatrix-7, 0);
		carre(matrix,0,longMatrix-7);
		
		ligneHorBlanche(matrix,0,7);
		ligneVerBlanche(matrix,7,0);
		
		ligneHorBlanche(matrix,longMatrix-8,7);
		ligneVerBlanche(matrix,longMatrix-8,0);
		
		ligneHorBlanche(matrix,0,longMatrix-8);
		ligneVerBlanche(matrix,7,longMatrix-8);
		
	//
		
	}

	/**
	 * Add the alignment pattern if needed, does nothing for version 1
	 * 
	 * @param matrix
	 *            The 2D array to modify
	 * @param version
	 *            the version number of the QR code needs to be between 1 and 4
	 *            included
	 */
	public static void addAlignmentPatterns(int[][] matrix, int version) {
		  int taille = matrix.length;

		  if(version != 1) {
		   for (int i = -9; i <= -5 ; ++i) {
		    for (int j = -9; j <= -5 ; ++j) {
		    	matrix[taille + i][taille + j] = B;
		    }
		   }
		   for (int i = -8; i <= -6; ++i) {
			   for (int j = -8; j <= -6 ; ++j) {
			    	matrix[taille + i][taille + j] = W;
			    }
		   }
		   matrix[taille - 7][taille - 7] = B;
		   
		  }
		  // TODO Implementer
		 }


	/**
	 * Add the timings patterns
	 * 
	 * @param matrix
	 *            The 2D array to modify
	 */
	public static void addTimingPatterns(int[][] matrix) {
	
		  int taille = matrix.length;

		  for (int i = 8; i <= taille - 8 ; ++i) {
		   if ( i % 2 == 0 ) {
		    matrix [6][i] = B ;
		    matrix [i][6] = B ;
		    
		   }
		   else {
		    matrix [6][i] = W ;
		    matrix [i][6] = W ;
		   }
		  }
		  
		  // TODO Implementer
		 }


	/**
	 * Add the dark module to the matrix
	 * 
	 * @param matrix
	 *            the 2-dimensional array representing the QR code
	 */
	public static void addDarkModule(int[][] matrix) {
		 
		  int taille = matrix.length;
		  matrix [8][taille - 8] = B;
		  // TODO Implementer
		 }


	/**
	 * Add the format information to the matrix
	 * 
	 * @param matrix
	 *            the 2-dimensional array representing the QR code to modify
	 * @param mask
	 *            the mask id
	 */
	public static void addFormatInformation(int[][] matrix, int mask) {
			
		  int couleur;
		  int taille = matrix.length;
		  boolean[] information = QRCodeInfos.getFormatSequence(mask) ;
		  for (int i = 0 ; i < information.length ; ++i) {
		   if (information[i]) {
		    couleur = B;
		   }
		   else {
		    couleur = W;
		   }
		   
		   if ( i < 6) {
		    matrix[i][8] = couleur;
		    matrix[8][taille -i -1] = couleur;
		   }
		   else if (i > 8) {
		    matrix[taille -15 + i][8] = couleur;
		    matrix[8][14 - i] = couleur;
		    
		   }
		   else if ( i == 6) {
		    matrix[7][8] = couleur;
		    matrix[8][taille - 7] = couleur;
		   }
		   else if (i == 7) {
		    matrix[8][8] = couleur;
		    matrix[taille - 8][8] = couleur;
		   } 
		   else if (i == 8) {
			   matrix[8][7] = couleur;
			   matrix[taille - 7][8] = couleur;
		   }
		  }
		  
		  // TODO Implementer
		 }


	/*
	 * =======================================================================
	 * ****************************** PART 3 *********************************
	 * =======================================================================
	 */

	/**
	 * Choose the color to use with the given coordinate using the masking 0
	 * 
	 * @param col
	 *            x-coordinate
	 * @param row
	 *            y-coordinate
	 * @param color
	 *            : initial color without masking
	 * @return the color with the masking
	 */
	public static int maskColor(int col, int row, boolean dataBit, int masking) 
	{
		// application de la formule liee a un mask donne
		// Et return la couleur du bit (B or W) apres le passage du mask
		int color;
		switch (masking) 
		{
		case 0:
			if((col + row) % 2 == 0)
			{
				dataBit = !dataBit;
			}
			break;
		case 1:
			if(row % 2 == 0)
			{
				dataBit = !dataBit;
			}
			break;
		case 2:
			if(col % 3 == 0)
			{
				dataBit = !dataBit;
			}
			break;
		case 3:
			if((col + row) % 3 == 0)
			{
				dataBit = !dataBit;
			}
			break;
		case 4:
			if((row/2 + col/3) % 2 == 0)
			{
				dataBit = !dataBit;
			}
			break;
		case 5:
			if(((col * row) % 2) + ((col * row) % 3) == 0)
			{
				dataBit = !dataBit;
			}
			break;
		case 6:
			if((((col * row) % 2) + ((col * row) % 3)) % 2 == 0)
			{
				dataBit = !dataBit;
			}
			break;
		case 7:
			if((((col + row) % 2) + ((col * row) % 3)) % 2 == 0)
			{
				dataBit = !dataBit;
			}
			break;
		}
		
		if(dataBit) color = B;
		else color = W;
		
		// TODO Implementer
		return color;
	}

	/**
	 * Add the data bits into the QR code matrix
	 * 
	 * @param matrix
	 *            a 2-dimensionnal array where the bits needs to be added
	 * @param data
	 *            the data to add
	 */
	
	public static void addDataInformation(int[][] matrix, boolean[] data, int mask)
	{
		
		int taille = matrix.length ;
		/* inf represente l index utilise pour appeler les elements du Array data
		 * A chaque utilisation il sera incremente
		 */
		int inf = 0;
		// k represente le decallage de la colonne par deux a chaque fois que deux colonnes sont remplis
		for (int k = taille - 1 ; k >= 0 ; k -= 2) 
		{
			
			if (k == 6) 
			{
				k-= 1;
			}
			
			
			for (int i = taille - 1 ; i >=0 ; --i)
			{
				for (int j = k; j >= k -1 ; --j)
				{
					
					if (matrix[j][i] == 0x00_00_00_00 && inf < data.length)
					{
						matrix[j][i] = maskColor(j, i, data[inf], mask);
						++inf;
					}
				}
			}
			k -= 2;
			
			if (k == 6) 
			{
				k-= 1;
			}
			
			for (int i = 0 ; i < taille ; ++i)
			{
				for (int j = k; j >= k -1 ; --j)
				{
					if (matrix[j][i] == 0x00_00_00_00 && inf < data.length)
					{
						matrix[j][i] = maskColor(j, i, data[inf], mask);
						++inf;
					}
				}
			}
		}
		
		
		
		// Remplissage de la matrice
		for (int i = 0 ; i < taille ; ++i)
		{
			for (int j = 0 ; j < taille ; ++j)
			{
				if (matrix[i][j] == 0x00_00_00_00)
				{
					matrix[i][j] = maskColor(j, i, false, mask);
				}
			}
		}
		// TODO Implementer

	}
	
	/*
	 * =======================================================================
	 * 
	 * ****************************** BONUS **********************************
	 * 
	 * =======================================================================
	 */

	/**
	 * Create the matrix of a QR code with the given data.
	 * 
	 * The mask is computed automatically so that it provides the least penalty
	 * 
	 * @param version
	 *            The version of the QR code
	 * @param data
	 *            The data to be written on the QR code
	 * @return The matrix of the QR code
	 */
	public static int[][] renderQRCodeMatrix(int version, boolean[] data) {

		int mask = findBestMasking(version, data);

		return renderQRCodeMatrix(version, data, mask);
	}

	/**
	 * Find the best mask to apply to a QRcode so that the penalty score is
	 * minimized. Compute the penalty score with evaluate
	 * 
	 * @param data
	 * @return the mask number that minimize the penalty
	 */
	public static int findBestMasking(int version, boolean[] data) {
		// TODO BONUS
		int matrix[][] = constructMatrix(version, 0);
		int finalMask = 0;
		int maskPenalite = 0;
		int maskIndex = 0;
		for (int i = 0; i < 8; ++i )
		{
			matrix = renderQRCodeMatrix(version, data, i);
			maskPenalite = evaluate(matrix);
			if (maskPenalite < maskIndex || maskIndex == 0)
			{
				finalMask = i;
				maskIndex = maskPenalite;
			}
		}
		
		return finalMask;
	}
	
	public static int sequence5color(int[][] matrix)
	{
		int penalite = 0;
		int taille = matrix.length;
		
		int wSequenceLi = 0;
		int bSequenceLi = 0;
		
		int wSequenceCol = 0;
		int bSequenceCol = 0;
		
		for (int i = 0; i < taille; ++i)
		{
			wSequenceLi = 0;
			bSequenceLi = 0;
			wSequenceCol = 0;
			bSequenceCol = 0;
			for (int j = 0; j < taille; ++j)
			{
				
				if (matrix[i][j] == W )
				{
					wSequenceLi += 1;
					bSequenceLi = 0;
				}
				else if(matrix[i][j] == B)
				{
					bSequenceLi += 1;
					wSequenceLi = 0;
				}
				
				if ( bSequenceLi == 5 || wSequenceLi == 5)
				{
					penalite += 3;
				}
				else if(bSequenceLi > 5 || wSequenceLi > 5)
				{
					penalite += 1;
				}
				
				
				if (matrix[j][i] == W )
				{
					wSequenceCol += 1;
					bSequenceCol = 0;
				}
				else if(matrix[j][i] == B)
				{
					bSequenceCol += 1;
					wSequenceCol = 0;
				}
				
				if ( bSequenceCol == 5 || wSequenceCol == 5)
				{
					penalite += 3;
				}
				else if(bSequenceCol > 5 || wSequenceCol > 5)
				{
					penalite += 1;
				}
			}	
		}
		
		return penalite;
	}
	
	public static int sequence(int matrix[][])
	{
		int taille = matrix.length;
		int penalite = 0;
		int sequenceW[] = new int[]{W, W, W, W, B, W, B, B, B, W, B};
		int sequenceB[] = new int[]{B, W, B, B, B, W, B, W, W, W, W};
		
		int tailleSeq = sequenceB.length;
		
		int sequence[] = new int [tailleSeq];
		int color = 0;
		// pour les colonnes
		for (int i = 0; i < taille ; ++i)
		{
			for(int j = 0; j < taille - tailleSeq + 1; ++j)
			{
				color = matrix[i][j];
				if(color == W)
				{
					for (int k = 0 ; k < tailleSeq ; ++k)
					{
						sequence[k] = sequenceW[k];
					}
				}
				else if (color == B)
				{
					for (int k = 0 ; k < tailleSeq ; ++k)
					{
						sequence[k] = sequenceB[k];
					}
				}
				
				boolean verification = true;
				for (int k = 0; k < tailleSeq; ++k)
				{
					if(matrix[i][j + k] != sequence[k])
					{
						verification = false;
					}
				}
				
				if(verification) {penalite += 40;
				
				}
				
			}
		}
		
		//pour les lignes
		for (int i = 0; i < taille ; ++i)
		{
			for(int j = 0; j < taille - tailleSeq + 1; ++j)
			{
				color = matrix[j][i];
				if(color == W)
				{
					for (int k = 0 ; k < tailleSeq ; ++k)
					{
						sequence[k] = sequenceW[k];
					}
				}
				else if (color == B)
				{
					for (int k = 0 ; k < tailleSeq ; ++k)
					{
						sequence[k] = sequenceB[k];
					}
				}
				
				boolean verification = true;
				for (int k = 0; k < tailleSeq; ++k)
				{
					if(matrix[j + k][i] != sequence[k])
					{
						verification = false;
					}
				}
				
				if(verification) {penalite += 40;
				
				}
				
			}
		}

		return penalite;
	}

	/**
	 * Compute the penalty score of a matrix
	 * 
	 * @param matrix:
	 *            the QR code in matrix form
	 * @return the penalty score obtained by the QR code, lower the better
	 */
	public static int evaluate(int[][] matrix) {
		//TODO BONUS
 		int taille = matrix.length;
		int penalite = 0;
		
		// ajoute les penalite pour une sequence de 5 couleurs consecutifs ou plus
		penalite += sequence5color(matrix);
		
		// Ajoute penalite pour les carres
		
		for (int i = 0; i < taille - 1; ++i)
		{
			for ( int j = 0; j < taille - 1; ++j)
			{
				if (matrix[i][j] == matrix[i+1][j] && matrix[i][j] == matrix[i][j+1] && matrix[i][j] == matrix[i+1][j+1])
				{
					penalite += 3;
				}
			}
		}
		
		// ajout des penalite pour les deux sequences predefinies
		
		penalite += sequence(matrix);
		
		int nModules = taille*taille;
		int blackModules = 0;
		for (int i = 0; i < taille; ++i)
		{
			for (int j = 0; j < taille; ++j)
			{
				if(matrix[i][j] == B) { blackModules += 1;}
			}
		}
		
		double pourcentage = (blackModules*100/nModules);
		double pourcentage1 = Math.floor(pourcentage/5) * 5;
		double pourcentage2 = pourcentage1 + 5;
		pourcentage1 -= 50;
		pourcentage2 -= 50;
		pourcentage1 = Math.abs(pourcentage1);
		pourcentage2 = Math.abs(pourcentage2);
		if (pourcentage1 < pourcentage2)
		{
			pourcentage = pourcentage1 * 2;
		}
		else 
		{
			pourcentage = pourcentage2 * 2;
		}
		penalite += pourcentage;
		
		return penalite;
	}


}



