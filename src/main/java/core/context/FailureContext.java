package core.context;


public record FailureContext(
        String exceptionType,
        String exceptionMessage,
        String pageUrl,
        String pageTitle,
        String lastAction
) {}
