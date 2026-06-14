package rajib.automation.framework.v3.round2.ai.schema.models;

public abstract class BaseSchema {

    protected String key;
    protected String logicalName;

    public BaseSchema() {
        // Required for Jackson
    }

    public BaseSchema(String key, String logicalName) {
        this.key = key;
        this.logicalName = logicalName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLogicalName() {
        return logicalName;
    }

    public void setLogicalName(String logicalName) {
        this.logicalName = logicalName;
    }
}