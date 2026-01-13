package rajib.automation.framework.normalization;

import rajib.automation.framework.intent.Intent;

import java.util.Map;

public interface TestDataNormalizer {

    Map<String, Intent> normalize(Map<String, Object> rawTestData);
}
