package cn.com.linenoise;

import org.junit.Assert;
import org.junit.Test;

import com.sun.jna.Platform;

public class SetMultiLineTest extends BaseTest {
	@Test
	public void setMultiLine() {
		if (Platform.isWindows()) {
			try {
				lineNoise.setMultiLine(true);
				Assert.fail();
			} catch (UnsupportedOperationException e) {
			}

			try {
				lineNoise.setMultiLine(false);
				Assert.fail();
			} catch (UnsupportedOperationException e) {
			}
		} else {
			lineNoise.setMultiLine(true);
			lineNoise.setMultiLine(false);
		}
	}
}
