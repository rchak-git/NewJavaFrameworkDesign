package rajib.automation.framework.v2.context;

public class RuntimeContextHolder {
    private static final ThreadLocal<RuntimeContext> threadContext = ThreadLocal.withInitial(RuntimeContext::new);

    public static RuntimeContext get() {
        return threadContext.get();
    }

    public static void clear() {
        threadContext.remove();
    }
}