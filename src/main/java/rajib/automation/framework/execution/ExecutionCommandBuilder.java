package rajib.automation.framework.execution;


import rajib.automation.framework.planning.ExecutionPlan;
import rajib.automation.framework.routing.RoutingResult;
import rajib.automation.framework.td.model.Phase;

import java.util.ArrayList;
import java.util.List;

public class ExecutionCommandBuilder {

    public List<ExecutionCommand> build(ExecutionPlan plan) {

        List<ExecutionCommand> commands = new ArrayList<>();

        appendPhase(plan, Phase.PRE, commands);
        appendPhase(plan, Phase.MAIN, commands);
        appendPhase(plan, Phase.POST, commands);

        return commands;
    }

    private void appendPhase(ExecutionPlan plan,
                             Phase phase,
                             List<ExecutionCommand> out) {

        for (RoutingResult rr : plan.getPhase(phase)) {
            out.add(new ExecutionCommand(
                    phase,
                    rr.target(),
                    rr.targetKey(),
                    rr.payload()
            ));
        }
    }
}
