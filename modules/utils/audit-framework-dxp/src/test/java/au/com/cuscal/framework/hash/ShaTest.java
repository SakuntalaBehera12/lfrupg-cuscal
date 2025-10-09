package au.com.cuscal.framework.hash;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ShaTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void sha512() throws Exception {
		String stringToHash = "Hello World";
		String salt = "This is the salt value";

		System.out.printf(
			"hex encoded output: %s\n", Hash.hash(salt, stringToHash));
	}

}