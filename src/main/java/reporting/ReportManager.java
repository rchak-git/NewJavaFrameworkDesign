package reporting;

public final class ReportManager {

    private static ReportSink sink;

    private ReportManager() {}

    public static void registerSink(ReportSink reportSink) {
        sink = reportSink;
    }

    public static void info(String message) {
        if (sink != null) sink.info(message);
    }

    public static void warning(String message) {
        if (sink != null) sink.warning(message);
    }

    public static void pass(String message) {
        if (sink != null) sink.pass(message);
    }

    public static void fail(String message) {
        if (sink != null) sink.fail(message);
    }
}
