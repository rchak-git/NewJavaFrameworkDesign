package rajib.automation.framework.resolution;


import rajib.automation.framework.intent.Intent;

import java.util.Map;

public interface IntentResolver {

    Map<String, ResolvedIntent> resolve(Map<String, Intent> intents);
}
