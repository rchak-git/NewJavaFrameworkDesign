package rajib.automation.framework.v3.round2.ai.schema.models;


public class LocatorSchema {
    public String strategy;
    public String value;

    // Required by Jackson
    public LocatorSchema() {}

    public LocatorSchema(String strategy, String value) {
        this.strategy = strategy;
        this.value = value;
    }

    public String getStrategy() { return strategy; }
    public void setStrategy(String strategy) { this.strategy = strategy; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    @Override
    public String toString() {
        return String.format("{strategy=%s, value=%s}", strategy, value);
    }
}