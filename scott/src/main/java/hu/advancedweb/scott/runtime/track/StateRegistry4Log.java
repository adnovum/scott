package hu.advancedweb.scott.runtime.track;

import java.util.Arrays;

/**
 * TODO: quick and dirty logging solution
 */
public class StateRegistry4Log {



	public static void startTracking(String className, String methodName) {
		System.out.println("Start tracking" + className + "#" + methodName);
	}
	
	public static void trackMethodStart(int lineNumber, String methodName) {
		System.out.println("trackMethodStart" + methodName +"@"+ lineNumber);
	}
	
	public static void trackFieldState(String value, String name, int lineNumber, boolean isStatic, String owner) {
		final String key = getFieldKey(name, isStatic, owner);
		System.out.println("field " + new StateData(lineNumber, value, key, key));
	}
	
	public static void trackFieldState(byte value, String name, int lineNumber, boolean isStatic, String owner) {
		final String key = getFieldKey(name, isStatic, owner);
		System.out.println("field " + new StateData(lineNumber, Byte.toString(value), key, key));
	}
	
	public static void trackFieldState(short value, String name, int lineNumber, boolean isStatic, String owner) {
		final String key = getFieldKey(name, isStatic, owner);
		System.out.println("field " + new StateData(lineNumber, Short.toString(value), key, key));
	}
	
	public static void trackFieldState(int value, String name, int lineNumber, boolean isStatic, String owner) {
		final String key = getFieldKey(name, isStatic, owner);
		System.out.println("field " + new StateData(lineNumber, Integer.toString(value), key, key));
	}
	
	public static void trackFieldState(long value, String name, int lineNumber, boolean isStatic, String owner) {
		final String key = getFieldKey(name, isStatic, owner);
		System.out.println("field " + new StateData(lineNumber, Long.toString(value), key, key));
	}
	
	public static void trackFieldState(float value, String name, int lineNumber, boolean isStatic, String owner) {
		final String key = getFieldKey(name, isStatic, owner);
		System.out.println("field " + new StateData(lineNumber, Float.toString(value), key, key));
	}
	
	public static void trackFieldState(double value, String name, int lineNumber, boolean isStatic, String owner) {
		final String key = getFieldKey(name, isStatic, owner);
		System.out.println("field " + new StateData(lineNumber, Double.toString(value), key, key));
	}
	
	public static void trackFieldState(boolean value, String name, int lineNumber, boolean isStatic, String owner) {
		final String key = getFieldKey(name, isStatic, owner);
		System.out.println("field " + new StateData(lineNumber, Boolean.toString(value), key, key));
	}
	
	public static void trackFieldState(char value, String name, int lineNumber, boolean isStatic, String owner) {
		final String key = getFieldKey(name, isStatic, owner);
		System.out.println("field " + new StateData(lineNumber, Character.toString(value), key, key));
	}
	
	public static void trackFieldState(Object value, String name, int lineNumber, boolean isStatic, String owner) {
		final String key = getFieldKey(name, isStatic, owner);
		
		String stringValue = objectToStringIgnoreMockitoExceptions(value);
		
		if (stringValue != null) {
			System.out.println("field " + new StateData(lineNumber, stringValue, key, key));
		}
	}
	
	private static String getFieldKey(String name, boolean isStatic, String owner) {
		final String key;
		if (isStatic) {
			key = owner.substring(owner.lastIndexOf("/") + 1) + "." + name;
		} else {
			key = "this." + name;
		}
		return key;
	}

	public static void trackVariableName(String name, int lineNumber, int var, String methodName) {
		// noop
	}
	
	public static void trackLocalVariableState(byte value, String name, int lineNumber, int var, String methodName) {
		final String key = getVariableKey(var, methodName);
		System.out.println(new StateData(lineNumber, Byte.toString(value), key, name));
	}

	public static void trackLocalVariableState(short value, String name, int lineNumber, int var, String methodName) {
		final String key = getVariableKey(var, methodName);
		System.out.println(new StateData(lineNumber, Short.toString(value), key, name));
	}

	public static void trackLocalVariableState(int value, String name, int lineNumber, int var, String methodName) {
		final String key = getVariableKey(var, methodName);
		System.out.println(new StateData(lineNumber, Integer.toString(value), key, name));
	}

	public static void trackLocalVariableState(long value, String name, int lineNumber, int var, String methodName) {
		final String key = getVariableKey(var, methodName);
		System.out.println(new StateData(lineNumber, Long.toString(value), key, name));
	}

	public static void trackLocalVariableState(float value, String name, int lineNumber, int var, String methodName) {
		final String key = getVariableKey(var, methodName);
		System.out.println(new StateData(lineNumber, Float.toString(value), key, name));
	}

	public static void trackLocalVariableState(double value, String name, int lineNumber, int var, String methodName) {
		final String key = getVariableKey(var, methodName);
		System.out.println(new StateData(lineNumber, Double.toString(value), key, name));
	}

	public static void trackLocalVariableState(boolean value, String name, int lineNumber, int var, String methodName) {
		final String key = getVariableKey(var, methodName);
		System.out.println(new StateData(lineNumber, Boolean.toString(value), key, name));
	}

	public static void trackLocalVariableState(char value, String name, int lineNumber, int var, String methodName) {
		final String key = getVariableKey(var, methodName);
		System.out.println(new StateData(lineNumber, Character.toString(value), key, name));
	}
	
	public static void trackLocalVariableState(Object value, String name, int lineNumber, int var, String methodName) {
		final String key = getVariableKey(var, methodName);
		
		String stringValue = objectToStringIgnoreMockitoExceptions(value);
		
		if (stringValue != null) {
			System.out.println(new StateData(lineNumber, stringValue, key, name));
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
