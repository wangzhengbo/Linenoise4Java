package cn.com.linenoise;

import com.sun.jna.Callback;

public interface CompletionCallback extends Callback {
	public void callback(String buf, Completions completions);
}
