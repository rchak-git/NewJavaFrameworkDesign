package rajib.automation.framework.steps;
public final class StepResult {
    private final String name;
    private final boolean passed;
    private final long durationMs;
    private final Throwable error;

    private StepResult(String name, boolean passed, long durationMs, Throwable error) {
        this.name = name;
        this.passed = passed;
        this.durationMs = durationMs;
        this.error = error;
    }

    public static StepResult pass(String name, long durationMs) {
        return new StepResult(name, true, durationMs, null);
    }

    public static StepResult fail(String name, long durationMs, Throwable error) {
        return new StepResult(name, false, durationMs, error);
    }

    public String getName() { return name; }
    public boolean isPassed() { return passed; }
    public long getDurationMs() { return durationMs; }
    public Throwable getError() { return error; }
}
