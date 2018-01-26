# CA4005_Assignment2
Digital Signature Using ElGamal

The aim of this assignment is to implement a digital signature using the ElGamal signature scheme.

The prime modulus p is the following 1024-bit prime (given in hexadecimal):

b59dd795 68817b4b 9f678982 2d22594f 376e6a9a bc024184 6de426e5 dd8f6edd
ef00b465 f38f509b 2b183510 64704fe7 5f012fa3 46c5e2c4 42d7c99e ac79b2bc
8a202c98 327b9681 6cb80426 98ed3734 643c4c05 164e739c b72fba24 f6156b6f
47a7300e f778c378 ea301e11 41a6b25d 48f19242 68c62ee8 dd313474 5cdf7323

The generator g is the following (again in hexadecimal):

44ec9d52 c8f9189e 49cd7c70 253c2eb3 154dd4f0 8467a64a 0267c9de fe4119f2
e373388c fa350a4e 66e432d6 38ccdc58 eb703e31 d4c84e50 398f9f91 677e8864
1a2d2f61 57e2f4ec 538088dc f5940b05 3c622e53 bab0b4e8 4b1465f5 738f5496
64bd7430 961d3e5a 2e7bceb6 2418db74 7386a58f f267a993 9833beef b7a6fd68

Before the digital signature can be implemented, you will need to set up an appropriate public/private ElGamal key pair as follows:

- Generate a random secret key x with 1 < x < p-1
- Compute the public key y as y = gx (mod p)
- To sign a message m you will need to do the following:

- Choose a random value k with 0 < k < p-1 and gcd(k,p-1) = 1
- Compute r as r = gk (mod p)
- Compute s as s = (H(m)-xr)k-1 (mod p-1) where H is the hash function SHA-256. This should use your own implementation of the extended Euclidean GCD algorithm to calculate the inverse rather than using a library method for this purpose.
- If s=0 start over again
- The pair (r,s) is the digital signature of m

Once your implementation is complete, you should create a zip file containing all your code, digitally sign this as shown above, and send me the following by email:

Your public key y in hexadecimal.
Your zipped code file which was digitally signed.
The digital signature values r and s in hexadecimal.
A declaration that this is solely your own work (except elements that are explicitly attributed to another source).
The implementation language must be Java. You can make use of the BigInteger class (java.math.BigInteger), the security libraries (java.security.*) and the crypto libraries (javax.crypto.*). You must not make use of the multiplicative inverse or GCD methods provided by the BigInteger class; you will need to implement these yourself. You can however make use of the crypto libraries to perform the SHA-256 hashing.

When I receive your email I will verify your digital signature as follows (you should also verify your digital signature in the same way):

I will check that 0 < r < p and 0 < s < p-1
I will generate the 256-bit digest H(m) of your submitted zipped code file m
I will verify that gH(m) (mod p) = yrrs (mod p)
This assignment is due 10am on Monday 4th December. Submissions without the declaration will not be assessed. This assignment carries 15 marks and late submissions will have 1.5 marks deducted for each 24 hours the assignment is overdue.
