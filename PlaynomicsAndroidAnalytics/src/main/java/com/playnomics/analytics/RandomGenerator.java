package com.playnomics.analytics;

/**
 * Copyright (c) 2009 Mark S. Kolich
 * http://mark.kolich.com
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class RandomGenerator {
	
	private static final String SHA1_PRNG = "SHA1PRNG";
	
	// Default here is 64-bits of random goodness.
	private static final int DEFAULT_RANDOM_SIZE = 64;
	
	private static final char HEX_DIGIT[] = {
		'0', '1', '2', '3', '4', '5', '6', '7',
		'8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
	};
	
	// This ins't thread safe but we probably don't really care
	// since all we're doing is reading a bunch of random numbers
	// out of the generator.
	private static final SecureRandom sRandom__;
	static {
		try {
			sRandom__ = SecureRandom.getInstance(SHA1_PRNG);
		} catch (NoSuchAlgorithmException e) {
			throw new Error(e);
		}
	}
	
	/**
	 * Get the number of next random bits in this SecureRandom generators'
	 * sequence.
	 * 
	 * @param size
	 *            how many random bits you want
	 * @return
	 * @throws IllegalArgumentException
	 *             if the arg isn't divisible by eight
	 */
	public static byte[] getNextSecureRandom(int bits) {
	
		// Make sure the number of bits we're asking for is at least
		// divisible by 8.
		if ((bits % 8) != 0) {
			throw new IllegalArgumentException("Size is not divisible " +
				"by 8!");
		}
		
		// Usually 64-bits of randomness, 8 bytes
		final byte[] bytes = new byte[bits / 8];
		
		// Get the next 64 random bits. Forces SecureRandom
		// to seed itself before returning the bytes.
		sRandom__.nextBytes(bytes);
		
		return bytes;
		
	}
	
	/**
	 * Convert a byte array into its hex String equivalent.
	 * 
	 * @param bytes
	 * @return
	 */
	public static String toHex(byte[] bytes) {
	
		if (bytes == null) {
			return null;
		}
		
		StringBuilder buffer = new StringBuilder(bytes.length * 2);
		for (byte thisByte : bytes) {
			buffer.append(byteToHex(thisByte));
		}
		
		return buffer.toString();
		
	}
	
	/**
	 * Convert a single byte into its hex String equivalent.
	 * 
	 * @param b
	 * @return
	 */
	private static String byteToHex(byte b) {
	
		char[] array = { HEX_DIGIT[(b >> 4) & 0x0f],
			HEX_DIGIT[b & 0x0f] };
		return new String(array);
	}
	
	public static String createRandomHex() {
	
		// Get 64-bits of secure random goodness.
		final byte[] randBytes = RandomGenerator.getNextSecureRandom(DEFAULT_RANDOM_SIZE);		
		return RandomGenerator.toHex(randBytes);			
	}
	
}
