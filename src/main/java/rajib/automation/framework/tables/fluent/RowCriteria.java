package rajib.automation.framework.tables.fluent;

import java.util.Map;

public record RowCriteria(Map<String, String> criteria) {

    public static RowCriteria where(String column, String value) {
        return new RowCriteria(Map.of(column, value));
    }

    public static RowCriteria where(Map<String, String> criteria) {
        return new RowCriteria(criteria);
    }
}
