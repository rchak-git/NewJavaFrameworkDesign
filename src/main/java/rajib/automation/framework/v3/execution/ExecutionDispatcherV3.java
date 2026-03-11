package rajib.automation.framework.v3.execution;

import rajib.automation.framework.base.BasePage;
import rajib.automation.framework.v2.context.RuntimeContext;
import rajib.automation.framework.v2.resolver.PlaceholderResolver;

import rajib.automation.framework.v3.intent.ActionPayload;
import rajib.automation.framework.v3.intent.NormalizedIntent;
import rajib.automation.framework.v3.intent.PopulateFieldsPayload;
import rajib.automation.framework.v3.intent.VerifyFieldsPayload;
import rajib.automation.framework.intent.VerifySpec;

import java.util.HashMap;
import java.util.Map;

public class ExecutionDispatcherV3 {

    public void dispatch(
            BasePage page,
            NormalizedIntent intent,
            RuntimeContext context
    ) {

        switch (intent.getIntentType()) {

            case POPULATE -> handlePopulate(page, intent, context);

            case VERIFY -> handleVerify(page, intent, context);

            case ACTION -> handleAction(page, intent, context);

            default -> throw new IllegalStateException(
                    "Unsupported intent type: " + intent.getIntentType()
            );
        }
    }

    // ---------------------------------------------------------
    // POPULATE
    // ---------------------------------------------------------

    private void handlePopulate(
            BasePage page,
            NormalizedIntent intent,
            RuntimeContext context
    ) {

        PopulateFieldsPayload payload =
                (PopulateFieldsPayload) intent.getPayload();

        page.populate(payload.fields(), context);
    }

    // ---------------------------------------------------------
    // VERIFY
    // ---------------------------------------------------------

    private void handleVerify(
            BasePage page,
            NormalizedIntent intent,
            RuntimeContext context
    ) {

        VerifyFieldsPayload payload =
                (VerifyFieldsPayload) intent.getPayload();

        Map<String, VerifySpec> resolved =
                resolveVerifySpecs(payload.fields(), context);

        page.verify(resolved);
    }



    private void handleAction(
            BasePage page,
            NormalizedIntent intent,
            RuntimeContext context
    ) {

        ActionPayload payload = (ActionPayload) intent.getPayload();

        for (Map.Entry<String, Object> entry : payload.actions().entrySet()) {

            String actionKey = entry.getKey();
            Object value = entry.getValue();

            if (Boolean.TRUE.equals(value)) {
                page.performAction(actionKey);
            }
        }
    }


    // ---------------------------------------------------------
    // Runtime placeholder resolution
    // ---------------------------------------------------------

    private Map<String, VerifySpec> resolveVerifySpecs(
            Map<String, VerifySpec> original,
            RuntimeContext context
    ) {

        Map<String, VerifySpec> resolved = new HashMap<>();

        for (Map.Entry<String, VerifySpec> entry : original.entrySet()) {

            VerifySpec spec = entry.getValue();

            Object expected = spec.expectedValue();
            Object resolvedExpected = expected;

            if (expected instanceof String s) {
                resolvedExpected =
                        PlaceholderResolver.resolve(
                                Map.of("temp", s),
                                context
                        ).get("temp");
            }

            resolved.put(
                    entry.getKey(),
                    new VerifySpec(
                            spec.type(),
                            resolvedExpected,
                            spec.params(),
                            spec.phase()
                    )
            );
        }

        return resolved;
    }
}
