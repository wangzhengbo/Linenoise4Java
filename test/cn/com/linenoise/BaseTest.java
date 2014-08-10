package cn.com.linenoise;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

public abstract class BaseTest {
	@Rule
	public TestName testName = new TestName();

	protected Linenoise lineNoise;
	private static final String DEFAULT_ENCODING = System
			.getProperty("file.encoding");

	@Before
	public void setUp() {
		System.out.println(String.format("--------------------Begin test %s",
				testName.getMethodName()));

		try {
			lineNoise = new Linenoise();
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@After
	public void tearDown() {
		System.out.println(String.format(
				" --------------------End   test %s%n",
				testName.getMethodName()));
	}

	protected void assertFileEquals(String expectedFile, String actualFile) {
		Assert.assertEquals(readFileToString(expectedFile),
				readFileToString(actualFile));
	}

	protected String readFileToString(String file) {
		try {
			return FileUtils.readFileToString(new File(file), DEFAULT_ENCODING);
		} catch (IOException e) {
			Assert.fail(String.format("Unable to read file '%s'.", file));
		}
		return null;
	}

	protected String[] readLines(String file) {
		try {
			return FileUtils.readLines(new File(file), DEFAULT_ENCODING)
					.toArray(new String[0]);
		} catch (IOException e) {
			Assert.fail(String.format("Unable to read file '%s'.", file));
		}
		return null;
	}
}
