package cn.com.linenoise;

import org.junit.Test;

public class ClearScreenTest extends BaseTest {
	@Test
	public void clearScreen() {
		System.out.println(String.format(
				"%nType exit to quit the %s, type clear to clear scrren.",
				getClass().getSimpleName()));
		while (true) {
			String line = lineNoise.readLine("> ");
			if (line == null || "exit".equals(line)) {
				break;
			}
			if ("clear".equals(line)) {
				lineNoise.clearScreen();
			}
			lineNoise.historyAdd(line);
		}
	}
}
