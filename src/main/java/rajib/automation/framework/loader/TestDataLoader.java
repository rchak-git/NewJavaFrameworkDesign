package rajib.automation.framework.loader;



import java.util.Map;

public interface TestDataLoader {

    /**
     * Loads raw test data for a given page and test case.
     *
     * @param pageName name of the page (used to locate JSON file)
     * @param testCaseName name of the test case inside the JSON
     * @return raw test data as Map<String, Object>
     */
    Map<String, Object> load(String pageName, String testCaseName);
}
