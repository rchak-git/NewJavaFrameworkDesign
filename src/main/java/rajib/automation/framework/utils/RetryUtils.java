package rajib.automation.framework.utils;

public class RetryUtils {

    private static final int MAX_ATTEMPTS = 3;
    private static final long SLEEP_MS = 200;

    @SafeVarargs
    public static <T extends Exception> void retryOnException(
            Runnable action,
            Class<? extends Throwable>... retryOn
    ) {
        int attempts = 0;

        while (true) {
            try {
                action.run();
                return;
            } catch (Exception e) {
                attempts++;
                if (attempts >= MAX_ATTEMPTS || !shouldRetry(e, retryOn)) {
                    throw e;
                }
                sleep(SLEEP_MS);
            }
        }
    }

    private static boolean shouldRetry(Exception e, Class<?>[] retryOn) {
        for (Class<?> clazz : retryOn) {
            if (clazz.isInstance(e)) {
                return true;
            }
        }
        return false;
    }

    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}
