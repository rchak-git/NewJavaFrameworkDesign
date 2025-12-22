package reporting;

/**
 * ReportSink
 *
 * Contract between framework core (main)
 * and reporting implementation (test).
 */
public interface ReportSink {

    void info(String message);

    void warning(String message);

    void pass(String message);

    void fail(String message);
}
