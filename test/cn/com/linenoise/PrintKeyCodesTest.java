package cn.com.linenoise;

import org.junit.Assert;
import org.junit.Test;

import com.sun.jna.Platform;

public class PrintKeyCodesTest extends BaseTest {
	@Test
	public void printKeyCodes() {
		if (Platform.isWindows()) {
			try {
				lineNoise.printKeyCodes();
				Assert.fail();
			} catch (UnsupportedOperationException e) {
			}
		} else {
			lineNoise.printKeyCodes();
		}
	}
}
