package rajib.automation.framework.form;

import rajib.automation.framework.codegen.schema.FieldSchema;
import rajib.automation.framework.codegen.schema.PageSchema;
import rajib.automation.framework.enums.FieldType;
import rajib.automation.framework.intent.Intent;
import rajib.automation.framework.model.PageField;
import rajib.automation.framework.resolution.ResolvedIntent;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class FormFiller {
    public Map<String, ResolvedIntent> resolve(
            PageSchema schema,
            Map<String, Object> testData
    ) {

        Map<String, ResolvedIntent> resolvedIntents =
                new LinkedHashMap<>();

        for (FieldSchema field : schema.fields()) {

            String fieldKey = field.key(); // or field.getKey()

            if (!testData.containsKey(fieldKey)) {
                continue;
            }

            Object value = testData.get(fieldKey);

            Intent intent = new Intent(
                    Optional.ofNullable(value),
                    Optional.empty()
            );

            ResolvedIntent resolvedIntent =
                    new ResolvedIntent(
                            true,   // shouldPopulate
                            false,  // shouldVerify
                            intent
                    );

            resolvedIntents.put(fieldKey, resolvedIntent);
        }

        return resolvedIntents;
    }


}
