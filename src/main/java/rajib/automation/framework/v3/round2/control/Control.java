package rajib.automation.framework.v3.round2.control;


import rajib.automation.framework.intent.VerifySpec;

public interface Control {

    void populate(ControlCommand command);

    void verify(ControlCommand command);

    Object read();
}