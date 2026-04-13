package rajib.automation.framework.v3.round2.enums;

public enum PopulationType {
    SET,      // Set the value (default for textbox, checkbox, etc)
    CLEAR,    // Clear the value (e.g., empty a textbox)
    APPEND    // Append value (e.g., for textbox, add text to current content)
    // You can add more in future: SELECT, ADD, REMOVE, etc, as needed
}