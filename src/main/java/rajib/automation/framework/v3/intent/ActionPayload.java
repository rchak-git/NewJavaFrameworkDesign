package rajib.automation.framework.v3.intent;



import java.util.Map;

public class ActionPayload {

    private final Map<String, Object> actions;

    public ActionPayload(Map<String, Object> actions) {
        this.actions = actions;
    }

    public Map<String, Object> actions() {
        return actions;
    }
}
