// The BigInteger methods are object methods

import java.math.*;
import java.util.*;
import java.io.*;

public class RSA_Full {
    public static void main (String [] args) {
		//Initializing all variables and setting everything else to defaults)
		//p and q are both prime, the exponent is coprime to phi(n)
        BigInteger one = BigInteger.ONE;
        BigInteger p = new BigInteger("617023734136278431479521514523962841540487069789053248600335696695474177821121308696550302812685072932972585825179769672575922477751969410614881992817423190147509770748962848145207578577450089979917146654138593551586098605520407471826729712327759642649928066695312333910575265780206527711019455823783");
        BigInteger q = new BigInteger("629044003991319590010171142906145019327692790737574328127653340074774299918292780718301788552230155439544979923554222206298722601745731503233958283358251392715074377873992648696504146004642759458343267098875388949976597854121385769847341746026466384420060026330351301856330521771500726796180875270957");
        BigInteger n = BigInteger.ONE;
        BigInteger phi_of_n = BigInteger.ONE;
        BigInteger e = new BigInteger("65537");
        BigInteger d = BigInteger.ONE;
		String ftsTest = fileToString("./awmt.txt");
		
		//Generating n (modulus), phi(n) and d (inverse of the exponent mod phi(n)
            n = multiply(p, q);
            phi_of_n = ((p.subtract(one)).multiply((q.subtract(one))));
            d = e.modInverse(phi_of_n);
        
		//Creating the arrayLists to encrypt/decrypt the message
		ArrayList<String> sBlocks = toStringBlocks(ftsTest);
        ArrayList<BigInteger> plainText = toBigIntBlocks(sBlocks);
        ArrayList<BigInteger> cipherText = encrypt(plainText, e, n);
        ArrayList<BigInteger> decipherText = decrypt(cipherText, d, n);
        ArrayList<String> decipherTextToString = plainTextToStringBlocks(decipherText);
		ArrayList<String> encText = new ArrayList<String>();
				
		//This prints out the encrypted message
		 int start1 = 1;
        for (BigInteger it1 : cipherText) {
			encText.add(it1.toString());
            System.out.println("Block after encryption " + start1+ ": " + it1);
            System.out.println("Size of block: " + it1.toString().length());
            start1++;
        }		
	// ------------------------------------------------------------------------ //
		//This prints out the decrypted message
        int start = 1;
        for (String it : decipherTextToString) {
            System.out.println("unpadded Block " + start+ ": " + it);
            System.out.println("Size of block: " + it.toString().length());
            start++;
        }
		
		/*These two lines let you send the encrypted and decrypted strings to a file
          comment them out for what you'd like to have */
		 
		//Sending the decrypted text to a file
	//	stringToFile(decipherTextToString);
		
		//Sending the encrypted text to a file
		stringToFile(encText);
		
}

	//Method to convert the string blocks of size 200 to integer format
    public static ArrayList<BigInteger> toBigIntBlocks(ArrayList<String> sBlocks){
        ArrayList<BigInteger> plainText = new ArrayList<BigInteger>();
        BigInteger bigIntegerConversion;
        String conversion;
        
        for (int i = 0; i < sBlocks.size(); ++i){
            char [] sBlocksArray = sBlocks.get(i).toCharArray();
            bigIntegerConversion = BigInteger.ONE;
            conversion = "";
            
            for(int j = 0; j < sBlocksArray.length; ++j){
                conversion += toPaddedAscii(sBlocksArray[j]);
            }
            
            bigIntegerConversion = new BigInteger(conversion);
            plainText.add(bigIntegerConversion);
        }
        return plainText;    
        
    }
    //Converting the passed file to string blocks of size 200
    public static ArrayList<String> toStringBlocks(String s){
        ArrayList<String> aString = new ArrayList<String>();
        int size = (s.length() / 200) + 1;
        int firstCharacter = 0;
        int lastCharacter = 200;
        
        for (int i = 0; i < size; ++i){
            if (s.length() < lastCharacter){
                aString.add(s.substring(firstCharacter, s.length()));
            }
            else{
                aString.add(s.substring(firstCharacter, lastCharacter));
                firstCharacter += 200;
                lastCharacter += 200;
            }
        }
        return aString;
    }
	/*This is to reverse from plainText to the string blocks, unpads the ascii
	  then we have a numerical representation of the message*/
	public static ArrayList<String> plainTextToStringBlocks(ArrayList<BigInteger> cipherText){
		ArrayList<String> sBlocks = new ArrayList<String>();
		String character;
		String conversion;
		
		for (int i = 0; i < cipherText.size(); ++i){
			conversion = cipherText.get(i).toString();
			character = "";
			
			for (int j = 0; j < conversion.length(); j+=3){
				if ((j + 3) < conversion.length()){
					character += unPaddedAscii(conversion.substring(j, j +3));
				}
				else {
					character += unPaddedAscii(conversion.substring(j,conversion.length()));
				}
			}
			sBlocks.add(character);
		}
		return sBlocks;
	}
        

    // Return a random integer approximately ndigits in length (base 10)
    // We're converting from Java's base 2.
    public static BigInteger randomInteger (int ndigits){
        Random rand = new Random();
        int len = (int)(3.32*(double)ndigits);
        return new BigInteger(len, rand);
    }

    //    Return a random prime approx. n digits in length
    public static BigInteger randomPrime (int ndigits){
        BigInteger p = randomInteger(ndigits);
        return p.nextProbablePrime();
    }

	//Making sure that the encrypted length is 600 characters and 600 characters only
    public static boolean length(BigInteger n){
        if(n.toString().length() == 600){
            return true;
        }
        return false;
    }

	//Method to limit the size of the blocks to 200
    public static boolean size(BigInteger n){
        String startNumberString = "";
        for (int i = 0; i < 200; ++i){
            startNumberString += "355";
        }

        BigInteger startNumber = new BigInteger(startNumberString);

        if(n.compareTo(startNumber) == 1){
            return true;
        }
        return false;
    }

	//Multiplying BigIntegers
    public static BigInteger multiply (BigInteger a, BigInteger b){
        return a.multiply(b);
    }

	//Getting the GCD of BigIntegers
    public static BigInteger gcd(BigInteger a, BigInteger b){
        return a.gcd(b);
    }

	//Getting the Mod inverse of BigIntegers
    public static BigInteger inverse(BigInteger a, BigInteger m){
        return a.modInverse(m);
    }
    
	//Sending a file to a string so that we would be able to read and start the encryption process
    public static String fileToString (String fileName){
        String result = "";
        try{
            FileInputStream file = new FileInputStream(fileName);
            byte[] b = new byte[file.available()];
        //System.out.println("Number of byes in this file = " + file.available());
            file.read(b);
            file.close();
            result = new String(b);
        }
        catch (Exception e) {
            System.out.println("oops");
        }
        return result;
        //System.out.println(result);
    }
    
	//Method to add padding to our ascii values
    public static String toPaddedAscii (char c){
        String padding = Integer.toString(c + 100);
        return padding;
    }
	
	//Method to unpad our ascii values
	public static char unPaddedAscii (String s) {
		int padding = Integer.valueOf(s) - 100;
		return (char) padding;
	}
	
	//Method to encrypt the plainText and create cipherText
	public static ArrayList<BigInteger> encrypt(ArrayList<BigInteger> plainText, BigInteger e, BigInteger n){
		ArrayList<BigInteger> cipherText = new ArrayList<BigInteger>();
		
		for (int i = 0; i < plainText.size(); ++i){
			BigInteger encrypt = plainText.get(i);
			encrypt = encrypt.modPow(e,n);
			cipherText.add(encrypt);
		}
		return cipherText;
	}
	
	//Method to decrypt the cipherText back to plainText
	public static ArrayList<BigInteger> decrypt(ArrayList<BigInteger> cipherText, BigInteger e, BigInteger n){
		ArrayList<BigInteger> decipherText = new ArrayList<BigInteger>();
		
		for (int i = 0; i < cipherText.size(); ++i){
			BigInteger decrypt = cipherText.get(i);
			decrypt = decrypt.modPow(e,n);
			decipherText.add(decrypt);
		}
		return decipherText;
	}
	
	//Method to take the deciphered message and send it to a file named "Final.txt" so that we can read it
	public static void stringToFile (ArrayList<String> decipherText){
        String result = "";
        
		for(int i =0; i < decipherText.size(); ++i){
			result += decipherText.get(i);
		
		try{
            File file = new File("Final.txt");
			FileOutputStream fileOutput = new FileOutputStream(file);
            byte[] b = result.getBytes();
        //System.out.println("Number of byes in this file = " + file.available());
			fileOutput.write(b);
			fileOutput.flush();
			fileOutput.close();
		}
        catch (Exception e) {
            System.out.println("oops");
			}
		}
    } 
}
