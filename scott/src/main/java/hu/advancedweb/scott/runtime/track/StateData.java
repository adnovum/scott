package hu.advancedweb.scott.runtime.track;

/**
 * Represents a data point collected at a given line number.
 * 
 * @author David Csakvari
 */
public class StateData {

	/** Line number where the data is collected. */
	public final int lineNumber;

	/** Unique identifier for the variable or field. */
	public final String key;
	
	/** Recorded value. */
	public final String value;

	public final String niceName;

	public StateData(int lineNumber, String value, String key, String niceName) {
		this.lineNumber = lineNumber;
		this.value = value;
		this.key = key;
		this.niceName = niceName;
	}

	@Override
	public String toString() {
		return "StateData [lineNumber=" + lineNumber + ", key=" + key + ", value=" + value + ", niceName=" + niceName + "]";
	}
	
}
