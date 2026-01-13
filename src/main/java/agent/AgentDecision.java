package agent;

import agent.login.LoginAgentState;

import static agent.AgentAction.ESCALATE;
import static agent.AgentAction.STOP;

public class AgentDecision {

    private final AgentAction action;
    private final String reason;
    private final boolean terminal;

    public AgentDecision(AgentAction action, String reason, boolean terminal) {
        this.action = action;
        this.reason = reason;
        this.terminal = terminal;
    }

    public AgentAction getAction() {
        return action;
    }

    public String getReason() {
        return reason;
    }

    public boolean isTerminal() {
        return terminal;
    }


    public AgentDecision decide(LoginAgentState state) {


            if (state.goalReached) {
                return new AgentDecision(AgentAction.STOP, "Login successful", true);
            }

            if (state.captchaDetected) {
                return new AgentDecision(AgentAction.ESCALATE, "Captcha detected", true);
            }

            if (state.attemptCount >= state.maxAttempts) {
                return new AgentDecision(AgentAction.ESCALATE, "Max attempts exceeded", true);
            }

            // 🔑 CRITICAL RULES (these were missing)
            if (!state.usernameEntered) {
                return new AgentDecision(
                        AgentAction.ENTER_USERNAME,
                        "Username not entered yet",
                        false
                );
            }

            if (!state.passwordEntered) {
                return new AgentDecision(
                        AgentAction.ENTER_PASSWORD,
                        "Password not entered yet",
                        false
                );
            }

            if (state.errorVisible && state.errorText.contains("Invalid")) {
                return new AgentDecision(
                        AgentAction.STOP,
                        "Invalid credentials",
                        true
                );
            }

            if (state.loginButtonEnabled) {
                return new AgentDecision(
                        AgentAction.CLICK_LOGIN,
                        "Credentials entered, clicking login",
                        false
                );
            }

            return new AgentDecision(
                    AgentAction.WAIT,
                    "Waiting for stable state",
                    false
            );


    }


}
