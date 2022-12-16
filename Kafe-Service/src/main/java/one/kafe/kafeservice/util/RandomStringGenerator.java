package one.kafe.kafeservice.util;

import java.security.SecureRandom;
import java.util.Random;

public class RandomStringGenerator {

	private static final char[] POOL = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
		'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
		'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7',
		'8', '9'};

	private static final Random random = new SecureRandom();

	public static String generateString(int length) {
		StringBuilder stringBuilder = new StringBuilder();
		while (length-- > 0) {
			stringBuilder.append(POOL[random.nextInt(POOL.length)]);
		}
		return stringBuilder.toString();
	}

}
