package qrcode;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import reedsolomon.ErrorCorrectionEncoding;

public final class DataEncoding {

	/**
	 * @param input
	 * @param version
	 * @return
	 */
	public static boolean[] byteModeEncoding(String input, int version) {
		// TODO Implementer
		int maxCaracteres = QRCodeInfos.getMaxInputLength( version);
		int sequenceEncode [] =encodeString(input, maxCaracteres);
		int SequenceaInfo[]= addInformations(sequenceEncode) ;
		
		int maxOctets = QRCodeInfos.getCodeWordsLength(version);
		int sequenceRemplie[] = fillSequence(SequenceaInfo, maxOctets);
		
		int nbOctets = QRCodeInfos.getECCLength(version);
		int sequenceErreur [] =addErrorCorrection(sequenceRemplie, nbOctets);
		boolean sequenceFin [] = bytesToBinaryArray(sequenceErreur);
		
		
		return sequenceFin;
	}

	/**
	 * @param input
	 *            The string to convert to ISO-8859-1
	 * @param maxLength
	 *          The maximal number of bytes to encode (will depend on the version of the QR code) 
	 * @return A array that represents the input in ISO-8859-1. The output is
	 *         truncated to fit the version capacity
	 */
	public static int[] encodeString(String input, int maxLength) {
		
		String charlist[]= new String[input.length()];
		int longChaine=input.length();
		
		for (int i=0;i < longChaine ;i++) {
			String caractere = input.substring(i, i+1) ;
			charlist[i]=caractere;
		}
		
		int max=0;
		if (longChaine <= maxLength ) {
			max=longChaine;
		}
		else {
			max=maxLength;
		}
		
		int tabInt[] = new int[max];
		
		for (int i=0;i < max ;i++) {
			byte tabByte[] =charlist[i].getBytes(StandardCharsets.ISO_8859_1);
			int val = tabByte[0] & 0xFF;
			tabInt[i]=val;
		}
		
		return tabInt;
	}

	/**
	 * Add the 12 bits information data and concatenate the bytes to it
	 * 
	 * @param inputBytes
	 *            the data byte sequence
	 * @return The input bytes with an header giving the type and size of the data
	 */
	public static int[] addInformations(int[] inputBytes) {
		
		int [] tab = new int[inputBytes.length + 2];
		tab [0] = (0b0100_0000 | inputBytes.length >> 4)  & 0xFF;
		for (int i = 1 ; i < tab.length ; ++i) {
			if ( i == 1) {
				tab[i] = ((inputBytes[0] >> 4) | (inputBytes.length << 4)) & 0xFF ;
		   }
			else if (i == tab.length -1) {
				tab[i] = (inputBytes[inputBytes.length - 1] << 4) & 0xFF ;
		   }
		   else {
			   tab[i] = ((inputBytes[i-2] << 4) | (inputBytes[i-1] >> 4)) & 0xFF ;
		   }
		  }
		  
		  return tab;
		 }


	/**
	 * Add padding bytes to the data until the size of the given array matches the
	 * finalLength
	 * 
	 * @param encodedData
	 *            the initial sequence of bytes
	 * @param finalLength
	 *            the minimum length of the returned array
	 * @return an array of length max(finalLength,encodedData.length) padded with
	 *         bytes 236,17
	 */
	public static int[] fillSequence(int[] encodedData, int finalLength) {
		
		int tab[] = new int[finalLength];
		for (int i = 0 ; i < encodedData.length ; ++i) {
			tab[i] = encodedData[i];
		  }
		for (int i = encodedData.length ; i < finalLength ; ++i) {
			if ( i % 2 == 0) {
		    tab[i] = 17;
		   }
		   else {
		    tab[i] = 236;
		   }
		  }
		  
		  return tab;
		  
		  // TODO Implementer
		  
		 }


	/**
	 * Add the error correction to the encodedData
	 * 
	 * @param encodedData
	 *            The byte array representing the data encoded
	 * @param eccLength
	 *            the version of the QR code
	 * @return the original data concatenated with the error correction
	 */
	public static int[] addErrorCorrection(int[] encodedData, int eccLength) {
		// TODO Implementer
		int tab[]=new int[encodedData.length+ eccLength];
		
		for(int i=0; i<encodedData.length;i++ ) {
			tab[i]=encodedData[i];	
		}
		for(int i=encodedData.length; i<tab.length;i++ ) {
			tab[i]=ErrorCorrectionEncoding.encode(encodedData,eccLength)[i-encodedData.length];
		}
			
		return tab;
	}

	/**
	 * Encode the byte array into a binary array represented with boolean using the
	 * most significant bit first.
	 * 
	 * @param data
	 *            an array of bytes
	 * @return a boolean array representing the data in binary
	 */
	public static boolean[] bytesToBinaryArray(int[] data) {
		  boolean tab[] = new boolean[data.length*8];
		  
		  for ( int j = 0 ; j < data.length ; ++j)
		  {
			  String binary = Integer.toBinaryString(data[j]);
			  while( binary.length() < 8)
			  {
				  binary = "0" + binary;
			  }
			  for (int k = 0 ; k < 8 ; ++k)
			  {
				  String c = binary.substring(k , k+1);
				  if ( c.equals("1")) 
				  {
					  tab[j*8 + k] = true;
				  }
				  else tab[j*8 + k] = false;
			  }
		   } 
		  // TODO Implementer
		  return tab;
		 }


}