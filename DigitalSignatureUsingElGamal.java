import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class DigitalSignatureUsingElGamal {
	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {

		BigInteger primeModulus = new BigInteger(
				"b59dd79568817b4b9f6789822d22594f376e6a9abc0241846de426e5dd8f6eddef00b465f38f509b2b18351064704fe75f012fa346c5e2c442d7c99eac79b2bc8a202c98327b96816cb8042698ed3734643c4c05164e739cb72fba24f6156b6f47a7300ef778c378ea301e1141a6b25d48f1924268c62ee8dd3134745cdf7323",
				16);
		BigInteger generator = new BigInteger(
				"44ec9d52c8f9189e49cd7c70253c2eb3154dd4f08467a64a0267c9defe4119f2e373388cfa350a4e66e432d638ccdc58eb703e31d4c84e50398f9f91677e88641a2d2f6157e2f4ec538088dcf5940b053c622e53bab0b4e84b1465f5738f549664bd7430961d3e5a2e7bceb62418db747386a58ff267a9939833beefb7a6fd68",
				16);

		// read in the file to sign
		Path path = Paths.get("C:\\Test\\DigitalSignatureUsingElGamal.zip");
		byte[] fileInBytes = Files.readAllBytes(path);
		// **************************************************************************************************//

		// SETTING UP PRIVATE/PUBLIC KEY PAIR

		// Generate a random secret key x with 1 < x < p-1
		BigInteger privateKey = generateRandomValue(primeModulus, 1);
		System.out.println("Private key: " + privateKey.toString(16));

		// Compute the public key: y = g^x (mod p)
		BigInteger publicKey = generator.modPow(privateKey, primeModulus);
		System.out.println("Public key: " + publicKey.toString(16));
		System.out.println("********************************************************");

		// **************************************************************************************************//

		// SIGNING THE MESSAGE

		// hash the message
		byte[] hashedFileInBytes = hashFile(fileInBytes);

		BigInteger s = BigInteger.ZERO;
		BigInteger r = null;
		while (s.compareTo(BigInteger.ZERO) == 0) {
			// Choose a random value k with 0 < k < p-1 and gcd(k,p-1) = 1
			BigInteger k = null;
			BigInteger multiplicativeInverse = BigInteger.valueOf(-1);
			while (multiplicativeInverse.compareTo(BigInteger.valueOf(-1)) == 0) {
				k = generateRandomValue(primeModulus, 0);
				multiplicativeInverse = multiplicativeInverse(k, primeModulus);
			}

			// Compute r as r = g^k (mod p)
			r = generator.modPow(k, primeModulus);
			System.out.println("R value: " + r.toString(16));

			// Compute s as s = (H(m)-xr)k^(-1) (mod p-1): If s=0 start over again
			// calculate xr
			BigInteger xr = privateKey.multiply(r);
			// calculate (H(m)-xr)
			BigInteger hashedFile = new BigInteger(1, hashedFileInBytes);
			s = ((hashedFile.subtract(xr)).multiply(multiplicativeInverse).mod(primeModulus.subtract(BigInteger.ONE)));
		}
		System.out.println("********************************************************");
		System.out.println("S value: " + s.toString(16));

		// **************************************************************************************************//

		// WRITE TO FILE

		FileWriter assignment = new FileWriter("ElGamalSignature.txt", false);
		BufferedWriter out = new BufferedWriter(assignment);
		out.write("Public key: " + publicKey.toString(16) + "\r\n");
		out.write("\r\n");
		out.write("R value: " + r.toString(16) + "\r\n");
		out.write("\r\n");
		out.write("S value: " + s.toString(16) + "\r\n");
		out.close();
		assignment.close();
	}

	public static BigInteger[] calcXGCD(BigInteger a, BigInteger N) {
		// for d = gcd(a,N) = xa + yN

		// xgcd(a, 0) = a
		if (N.equals(BigInteger.ZERO))
			return new BigInteger[] { a, BigInteger.ONE, BigInteger.ZERO };

		// xgcd(a, b) = gcd(b, a mod b)
		BigInteger[] d_x_y = calcXGCD(N, a.mod(N));
		BigInteger d = d_x_y[0];
		BigInteger x = d_x_y[2];
		BigInteger y = d_x_y[1].subtract((a.divide(N)).multiply(d_x_y[2]));
		return new BigInteger[] { d, x, y };
	}

	private static BigInteger multiplicativeInverse(BigInteger k, BigInteger primeModulus) {
		// the multiplicative inverse exists only if gcd(a,N) = 1
		BigInteger[] checkMultInv = calcXGCD(k, primeModulus.subtract(BigInteger.ONE));

		if (!checkMultInv[0].equals(BigInteger.ONE)) {
			// System.out.println("The multiplicative inverse does not exist");
			return BigInteger.valueOf(-1);
		}

		// check if the multiplicative inverse is negative
		if (checkMultInv[1].compareTo(BigInteger.ZERO) == 1)
			return checkMultInv[1];
		else
			return checkMultInv[1].add(primeModulus);
	}

	private static byte[] hashFile(byte[] fileInBytes) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hashedFile = digest.digest(fileInBytes);
		return hashedFile;
	}

	private static BigInteger generateRandomValue(BigInteger primeModulus, int min) {
		BigInteger randomValue;
		Random rand = new Random();
		do {
			randomValue = new BigInteger(primeModulus.bitLength(), rand);
		} while (randomValue.compareTo(primeModulus.subtract(BigInteger.ONE)) != -1
				&& randomValue.compareTo(BigInteger.valueOf(min)) != 1);
		return randomValue;
	}
}
