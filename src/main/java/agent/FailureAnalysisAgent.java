package agent;

import core.context.FailureContext;

/**
 * FailureAnalysisAgent
 *
 * Purpose:
 * - Analyze automation failures
 * - Provide human-readable insights
 * - Suggest corrective actions
 *
 * NOTE:
 * Rule-based agent for now.
 * Will later delegate reasoning to an LLM.
 */
public class FailureAnalysisAgent {

    public String analyze(FailureContext context) {

        String exceptionType = safe(context.exceptionType());
        String exceptionMessage = safe(context.exceptionMessage());

        if (exceptionType.isBlank()) {
            return defaultAnalysis();
        }

        if (exceptionType.contains("NoSuchElement")) {
            return handleNoSuchElement(context);
        }

        if (exceptionType.contains("Timeout")) {
            return handleTimeout(context);
        }

        if (exceptionType.contains("StaleElement")) {
            return handleStaleElement(context);
        }

        if (exceptionType.contains("Assertion")) {
            return handleAssertion(context);
        }

        return genericAnalysis(context, exceptionType, exceptionMessage);
    }

    /* -----------------------
       Specific handlers
       ----------------------- */

    private String handleNoSuchElement(FailureContext context) {
        return """
        🤖 Agent Insight – NoSuchElement

        Likely Root Cause:
        - Element not present at interaction time
        - Locator outdated or page not fully loaded

        Suggested Action:
        - Verify locator correctness
        - Add explicit wait for presence/visibility
        - Check iframe or shadow DOM

        Context:
        - Page URL: %s
        - Page Title: %s
        - Last Action: %s
        """.formatted(
                context.pageUrl(),
                context.pageTitle(),
                context.lastAction()
        );
    }

    private String handleTimeout(FailureContext context) {
        return """
        🤖 Agent Insight – Timeout

        Likely Root Cause:
        - Application slower than expected
        - Wait condition not satisfied

        Suggested Action:
        - Increase wait timeout
        - Verify wait condition logic
        - Check backend/API latency

        Context:
        - Page URL: %s
        - Last Action: %s
        """.formatted(
                context.pageUrl(),
                context.lastAction()
        );
    }

    private String handleStaleElement(FailureContext context) {
        return """
        🤖 Agent Insight – Stale Element

        Likely Root Cause:
        - DOM refreshed after element lookup
        - Cached WebElement became invalid

        Suggested Action:
        - Re-locate element before interaction
        - Avoid storing WebElement references
        - Add retry logic for dynamic pages

        Context:
        - Page URL: %s
        - Last Action: %s
        """.formatted(
                context.pageUrl(),
                context.lastAction()
        );
    }

    private String handleAssertion(FailureContext context) {
        return """
        🤖 Agent Insight – Assertion Failure

        Likely Root Cause:
        - Expected condition not met
        - Incorrect test data or application behavior

        Suggested Action:
        - Validate expected vs actual data
        - Review assertion logic
        - Confirm application state before validation

        Context:
        - Page URL: %s
        - Last Action: %s
        """.formatted(
                context.pageUrl(),
                context.lastAction()
        );
    }

    private String genericAnalysis(
            FailureContext context,
            String exceptionType,
            String exceptionMessage) {

        return """
        🤖 Agent Insight – Unclassified Failure

        Exception Type:
        - %s

        Message:
        - %s

        Suggested Action:
        - Review stack trace and screenshots
        - Validate test data and application state
        - Re-run test in isolation

        Context:
        - Page URL: %s
        - Last Action: %s
        """.formatted(
                exceptionType,
                exceptionMessage,
                context.pageUrl(),
                context.lastAction()
        );
    }

    private String defaultAnalysis() {
        return """
        🤖 Agent Insight – Unknown Failure

        Unable to determine failure type.

        Suggested Action:
        - Inspect logs and screenshots
        - Add additional diagnostics
        """;
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}
