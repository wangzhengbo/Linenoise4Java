package cn.com.linenoise;

import org.junit.Test;

public class SetCompletionCallbackTest extends BaseTest {
	@Test
	public void setCompletionCallback() {
		System.out
				.println(String
						.format("%nType exit to quit the %s, type h and press tab to autocomplete.",
								getClass().getSimpleName()));
		lineNoise.setCompletionCallback(new CompletionCallback() {
			public void callback(String buf, Completions completions) {
				if ("h".equals(buf)) {
					lineNoise.addCompletion(completions, "help");
					lineNoise.addCompletion(completions, "halt");
				}
			}
		});
		while (true) {
			String line = lineNoise.readLine("> ");
			if (line == null || "exit".equals(line)) {
				break;
			}
			lineNoise.historyAdd(line);
		}
	}
}
