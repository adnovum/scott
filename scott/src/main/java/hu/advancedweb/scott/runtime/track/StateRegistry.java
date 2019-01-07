package hu.advancedweb.scott.runtime.track;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Track state changes in a test case.
 * This class is called by the instrumented test methods to record stuff,
 * and queried by the test reporter.
 * 
 * @author David Csakvari
 */
public class StateRegistry {
	
	/*
	 * Variable data recording and method start line recording is based on the fact that
	 * in a single test case there can be multiple lambdas, thus multiple methods.
	 */
	
	private static List<StateData> LOCAL_VARIABLE_STATES = new ArrayList<StateData>();
	
	private static List<StateData> LOCAL_VARIABLE_NAMES = new ArrayList<StateData>();
	
	private static List<StateData> FIELD_STATES = new ArrayList<StateData>();

	private static String METHOD_NAME;

	private static String CLASS_NAME;
	
	private static Map<String, Integer> METHOD_START_LINES = new HashMap<String, Integer>();
	
	public static void startTracking(String className, String methodName) {
		CLASS_NAME = className;
		METHOD_NAME = methodName;
		
		LOCAL_VARIABLE_STATES.clear();
		LOCAL_VARIABLE_NAMES.clear();
		FIELD_STATES.clear();
	}
	
	public static List<StateData> getLocalVariableStates() {
		return Collections.unmodifiableList(LOCAL_VARIABLE_STATES);
	}
	
	public static List<StateData> getLocalVariableNames() {
		return Collections.unmodifiableList(LOCAL_VARIABLE_NAMES);
	}
	
	public static List<StateData> getFieldStates() {
		return Collections.unmodifiableList(FIELD_STATES);
	}
	
	public static String getTestClassType() {
		return CLASS_NAME;
	}
	
	public static String getTestMethodName() {
		return METHOD_NAME;
	}
	
	public static Map<String, Integer> getMethodStartLine() {
		return METHOD_START_LINES;
	}
	
	public static String getLocalVariableName(String key, int lineNumber) {
		String name = null;
		for (StateData localVariableName : LOCAL_VARIABLE_NAMES) {
			if (key.equals(localVariableName.key)) {
				if (localVariableName.lineNumber > lineNumber) {
					break;
				} else {
					name = localVariableName.value;
				}	
			}
		}
		return name;
	}
	
	public static void trackMethodStart(int lineNumber, String methodName) {
		METHOD_START_LINES.put(methodName, lineNumber);
	}
	
	public static void trackFieldState(String value, String name, int lineNumber, boolean isStatic, String owner) {
		final String key = getFieldKey(name, isStatic, owner);
		FIELD_STATES.add(new StateData(lineNumber, value, key));
	}
	
	public static void trackFieldState(byte value, String name, int lineNumber, boolean isStatic, String owner) {
		final String key = getFieldKey(name, isStatic, owner);
		FIELD_STATES.add(new StateData(lineNumber, Byte.toString(value), key));
	}
	
	public static void trackFieldState(short value, String name, int lineNumber, boolean isStatic, String owner) {
		final String key = getFieldKey(name, isStatic, owner);
		FIELD_STATES.add(new StateData(lineNumber, Short.toString(value), key));
	}
	
	public static void trackFieldState(int value, String name, int lineNumber, boolean isStatic, String owner) {
		final String key = getFieldKey(name, isStatic, owner);
		FIELD_STATES.add(new StateData(lineNumber, Integer.toString(value), key));
	}
	
	public static void trackFieldState(long value, String name, int lineNumber, boolean isStatic, String owner) {
		final String key = getFieldKey(name, isStatic, owner);
		FIELD_STATES.add(new StateData(lineNumber, Long.toString(value), key));
	}
	
	public static void trackFieldState(float value, String name, int lineNumber, boolean isStatic, String owner) {
		final String key = getFieldKey(name, isStatic, owner);
		FIELD_STATES.add(new StateData(lineNumber, Float.toString(value), key));
	}
	
	public static void trackFieldState(double value, String name, int lineNumber, boolean isStatic, String owner) {
		final String key = getFieldKey(name, isStatic, owner);
		FIELD_STATES.add(new StateData(lineNumber, Double.toString(value), key));
	}
	
	public static void trackFieldState(boolean value, String name, int lineNumber, boolean isStatic, String owner) {
		final String key = getFieldKey(name, isStatic, owner);
		FIELD_STATES.add(new StateData(lineNumber, Boolean.toString(value), key));
	}
	
	public static void trackFieldState(char value, String name, int lineNumber, boolean isStatic, String owner) {
		final String key = getFieldKey(name, isStatic, owner);
		FIELD_STATES.add(new StateData(lineNumber, Character.toString(value), key));
	}
	
	public static void trackFieldState(Object value, String name, int lineNumber, boolean isStatic, String owner) {
		final String key = getFieldKey(name, isStatic, owner);
		
		String stringValue = objectToStringIgnoreMockitoExceptions(value);
		
		if (stringValue != null) {
			FIELD_STATES.add(new StateData(lineNumber, stringValue, key));
		}
	}
	
	private static String getFieldKey(String name, boolean isStatic, String owner) {
		final String key;
		if (isStatic) {
			key = owner.substring(owner.lastIndexOf("/") + 1) + "." + name;
		} else {
			final String prefix;
			if (CLASS_NAME.equals(owner)) {
				prefix = "this.";
			} else if (owner.contains("$")) {
				prefix = "(in enclosing " + owner.substring(owner.lastIndexOf("$") + 1) + ") ";
			} else if (owner.contains("/")) {
				prefix = "(in enclosing " + owner.substring(owner.lastIndexOf("/") + 1) + ") ";
			} else {
				prefix = "(in enclosing " + owner + ") ";
			}
			
			key = prefix + name;
		}
		return key;
	}

	public static void trackVariableName(String name, int lineNumber, int var, String methodName) {
		final String key = getVariableKey(var, methodName);
		LOCAL_VARIABLE_NAMES.add(new StateData(lineNumber, name, key));
	}
	
	public static void trackLocalVariableState(byte value, int lineNumber, int var, String methodName) {
		final String key = getVariableKey(var, methodName);
		LOCAL_VARIABLE_STATES.add(new StateData(lineNumber, Byte.toString(value), key));
	}

	public static void trackLocalVariableState(short value, int lineNumber, int var, String methodName) {
		final String key = getVariableKey(var, methodName);
		LOCAL_VARIABLE_STATES.add(new StateData(lineNumber, Short.toString(value), key));
	}

	public static void trackLocalVariableState(int value, int lineNumber, int var, String methodName) {
		final String key = getVariableKey(var, methodName);
		LOCAL_VARIABLE_STATES.add(new StateData(lineNumber, Integer.toString(value), key));
	}

	public static void trackLocalVariableState(long value, int lineNumber, int var, String methodName) {
		final String key = getVariableKey(var, methodName);
		LOCAL_VARIABLE_STATES.add(new StateData(lineNumber, Long.toString(value), key));
	}

	public static void trackLocalVariableState(float value, int lineNumber, int var, String methodName) {
		final String key = getVariableKey(var, methodName);
		LOCAL_VARIABLE_STATES.add(new StateData(lineNumber, Float.toString(value), key));
	}

	public static void trackLocalVariableState(double value, int lineNumber, int var, String methodName) {
		final String key = getVariableKey(var, methodName);
		LOCAL_VARIABLE_STATES.add(new StateData(lineNumber, Double.toString(value), key));
	}

	public static void trackLocalVariableState(boolean value, int lineNumber, int var, String methodName) {
		final String key = getVariableKey(var, methodName);
		LOCAL_VARIABLE_STATES.add(new StateData(lineNumber, Boolean.toString(value), key));
	}

	public static void trackLocalVariableState(char value, int lineNumber, int var, String methodName) {
		final String key = getVariableKey(var, methodName);
		LOCAL_VARIABLE_STATES.add(new StateData(lineNumber, Character.toString(value), key));
	}
	
	public static void trackLocalVariableState(Object value, int lineNumber, int var, String methodName) {
		final String key = getVariableKey(var, methodName);
		
		String stringValue = objectToStringIgnoreMockitoExceptions(value);
		
		if (stringValue != null) {
			LOCAL_VARIABLE_STATES.add(new StateData(lineNumber, stringValue, key));
		}
	}

	private static String getVariableKey(int var, String methodName) {
		return methodName + "\\" + var;
	}
	
	private static String objectToStringIgnoreMockitoExceptions(Object value) {
		try {
			return objectToString(value);
		} catch (Throwable t) {
			if (t.getClass().getName().startsWith("org.mockito")) {
				/*
				 * Calling toString on mocks might result in a MockitoException.
				 * Under normal circumstances it might happen when we try
				 * to verify toString (see: https://github.com/mockito/mockito/wiki/FAQ):
				 * verify(foo, times(1)).toString();
				 * 
				 * Due to Scott's bytecode instrumentation, the tests
				 * might accidentally call toString on mocks during the construction
				 * of normal verify() as well.
				 * See Issue #25.
				 */
				return null;
			} else {
				throw t;
			}
		}
	}

	private static String objectToString(Object value) {
		if (value == null) {
			return "null";
		} else if (value instanceof Object[]) {
			return Arrays.toString((Object[])value);
		} else if (value instanceof boolean[]) {
			return Arrays.toString((boolean[])value);
		} else if (value instanceof byte[]) {
			return Arrays.toString((byte[])value);
		} else if (value instanceof short[]) {
			return Arrays.toString((short[])value);
		} else if (value instanceof char[]) {
			return Arrays.toString((char[])value);
		} else if (value instanceof int[]) {
			return Arrays.toString((int[])value);
		} else if (value instanceof long[]) {
			return Arrays.toString((long[])value);
		} else if (value instanceof float[]) {
			return Arrays.toString((float[])value);
		} else if (value instanceof double[]) {
			return Arrays.toString((double[]) value);
		} else if (value instanceof String) {
			return wrapped(value.toString(), '"');
		} else {
			return value.toString();
		}
	}

	private static String wrapped(String original, char wrappingChar) {
		return new StringBuilder().append(wrappingChar).append(original).append(wrappingChar).toString();
	}

}
