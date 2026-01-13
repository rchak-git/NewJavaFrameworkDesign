package agent.login;

import agent.AgentAction;
import agent.AgentDecision;

public class LoginAgent {

    public AgentDecision decide(LoginAgentState state) {

        System.out.println(
                "DECIDE ENTRY → usernameEntered=" + state.usernameEntered +
                        ", passwordEntered=" + state.passwordEntered +
                        ", loginButtonEnabled=" + state.loginButtonEnabled
        );

        if (state.goalReached) {
            return new AgentDecision(AgentAction.STOP, "Login successful", true);
        }

        if (state.captchaDetected) {
            return new AgentDecision(AgentAction.ESCALATE, "Captcha detected", true);
        }

        if (state.attemptCount >= state.maxAttempts) {
            return new AgentDecision(AgentAction.ESCALATE, "Max attempts exceeded", true);
        }

        if (state.errorVisible && state.errorText.contains("Invalid")) {
            return new AgentDecision(AgentAction.STOP, "Invalid credentials", true);
        }
        if (!state.usernameEntered) {
            return new AgentDecision(AgentAction.ENTER_USERNAME, "Username not entered", false);
        }
        if (!state.passwordEntered) {
            return new AgentDecision(AgentAction.ENTER_PASSWORD, "Password not entered", false);
        }

        if (state.loginButtonEnabled) {
            return new AgentDecision(AgentAction.CLICK_LOGIN, "Clicking login", false);
        }

        return new AgentDecision(AgentAction.WAIT, "Waiting for stable state", false);
    }
}
