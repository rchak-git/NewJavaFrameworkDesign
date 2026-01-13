package agent.login;

import agent.AgentAction;
import agent.AgentDecision;
import agent.AgentExecutor;
import rajib.automation.framework.steps.Steps;

public class LoginAgentExecutor implements AgentExecutor<LoginContext> {

    private final LoginAgent agent = new LoginAgent();
    private final LoginActionExecutor actionExecutor = new LoginActionExecutor();
    public void execute(LoginContext context) {
        System.out.println(
                "LoginAgent loaded from: " +
                        agent.getClass().getProtectionDomain()
                                .getCodeSource()
                                .getLocation()
        );

        LoginAgentState state = new LoginAgentState();

        // Initial observation (DO NOT recreate state later)
        LoginStateObserver.observe(context.getDriver(), state);

        int safetyCounter = 0;

        while (true) {

            // Hard safety guard
            if (safetyCounter++ > 20) {
                throw new IllegalStateException("Agent exceeded max loop iterations");
            }

            // 🔑 Agent decision (THIS is where decide() is called)
            AgentDecision decision = agent.decide(state);

            // Terminal decisions exit the loop
            if (decision.isTerminal()) {
                break;
            }

            // 🔹 Execute the chosen action as a Step
            Steps.run(
                    context.getStepContext(),
                    decision.getAction().name(),
                    () -> {
                        // Execute Selenium action
                        actionExecutor.execute(decision.getAction(), context);

                        // 🔑 IMPORTANT: Mark intent AFTER execution
                        if (decision.getAction() == AgentAction.ENTER_USERNAME) {
                            state.usernameEntered = true;
                        }

                        if (decision.getAction() == AgentAction.ENTER_PASSWORD) {
                            state.passwordEntered = true;
                        }
                    }
            );

            // Increment attempt counter
            state.attemptCount++;

            // Re-observe UI state WITHOUT replacing agent memory
            LoginStateObserver.observe(context.getDriver(), state);
        }
    }


}
