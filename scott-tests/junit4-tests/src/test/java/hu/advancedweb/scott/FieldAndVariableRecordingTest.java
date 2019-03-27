package hu.advancedweb.scott;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import static hu.advancedweb.scott.TestHelper.wrapped;

import org.junit.Test;

public class FieldAndVariableRecordingTest {
	
	int fa = 0;
	boolean fb = false;
	List<String> fl = new ArrayList<String>();

	
	@Test
	public void recordIntegerAndInteger() throws Exception {
		int va = 0;
		this.fa ++;
		this.fa = dummy(this.fa);
		va ++;
		va = dummy(va);
		
		assertThat(TestHelper.getLastRecordedStateForField("this.fa"), equalTo(Integer.toString(this.fa)));
		assertThat(TestHelper.getLastRecordedStateForVariable("va"), equalTo(Integer.toString(va)));
	}
	
	@Test
	public void recordIntegerAndString() throws Exception {
		String vs = "something";
		this.fa ++;
		this.fa = dummy(this.fa);
		vs += "something";
		vs = dummy(vs);
		
		assertThat(TestHelper.getLastRecordedStateForField("this.fa"), equalTo(Integer.toString(this.fa)));
		assertThat(TestHelper.getLastRecordedStateForVariable("vs"), equalTo(wrapped(vs)));
	}
	
	@Test
	public void recordObjectField() {
		List<String> vl = new ArrayList<String>();
		List<String> vl2 = new ArrayList<String>();
		if (!fb) {
			this.fb = true;
			this.fa++;
		}
		try {
			// Push the current vr under-construction on the stack
			this.fl.add("test");
			if (this.fl.contains("?")) {
				return;
			}
			
			
			vl.addAll(this.fl);
			this.fl.addAll(vl);
		}
		finally {
			this.fa++;
			this.fl.clear();
		}
	}
	
	private int dummy(int a) {
		return a + 1;
	}
	
	private String dummy(String s) {
		return s + "something";
	}

}
