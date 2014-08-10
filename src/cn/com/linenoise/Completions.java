package cn.com.linenoise;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

public class Completions extends Structure {
	public int len;
	public Pointer cvec;

	@Override
	protected List<String> getFieldOrder() {
		return Arrays.asList("len", "cvec");
	}
}
