package agent.login;

public class LoginAgentState {

    public boolean usernamePresent;
    public boolean passwordPresent;

    public boolean usernameEntered = false;
    public boolean passwordEntered = false;


    public boolean loginButtonEnabled;

    public boolean errorVisible;
    public String errorText;

    public boolean captchaDetected;
    public boolean goalReached;

    public int attemptCount;
    public int maxAttempts = 3;
}
