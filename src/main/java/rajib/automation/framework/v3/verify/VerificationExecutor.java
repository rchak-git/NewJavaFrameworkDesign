package rajib.automation.framework.v3.verify;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import rajib.automation.framework.enums.FieldType;
import rajib.automation.framework.enums.ValidationType;
import rajib.automation.framework.intent.VerifySpec;

public final class VerificationExecutor {

    private VerificationExecutor() {}

    public static void verifyElement(
            String logicalKey,
            FieldType fieldType,
            WebElement el,
            VerifySpec verifySpec
    ) {

        ValidationType type = verifySpec.type();
        Object expectedObj = verifySpec.expectedValue();

        switch (type) {

            // ---------------------------
            // BOOLEAN STATE CHECKS
            // ---------------------------
            case IS_VISIBLE:
            case IS_ENABLED:
            case IS_SELECTED: {

                if (expectedObj == null) {
                    throw new IllegalArgumentException(
                            "VerifySpec.expectedValue must not be null for " + type +
                                    " | key=" + logicalKey
                    );
                }

                boolean expected = (Boolean) expectedObj;

                boolean actual;
                if (type == ValidationType.IS_VISIBLE) {
                    actual = el.isDisplayed();
                } else if (type == ValidationType.IS_ENABLED) {
                    actual = el.isEnabled();
                } else {
                    actual = el.isSelected();
                }

                Assert.assertEquals(
                        actual,
                        expected,
                        "Key: " + logicalKey +
                                " | Validation: " + type +
                                " | Expected: " + expected +
                                " | Actual: " + actual
                );
                break;
            }

            // ---------------------------
            // TEXT CHECKS
            // ---------------------------
            case TEXT_EQUALS: {

                if (expectedObj == null) {
                    throw new IllegalArgumentException(
                            "VerifySpec.expectedValue must not be null for TEXT_EQUALS | key=" + logicalKey
                    );
                }

                String expected = String.valueOf(expectedObj);
                String actual = readText(el, fieldType);

                Assert.assertEquals(
                        actual,
                        expected,
                        "Key: " + logicalKey +
                                " | Validation: TEXT_EQUALS" +
                                " | Expected: " + expected +
                                " | Actual: " + actual
                );
                break;
            }

            case TEXT_CONTAINS: {

                if (expectedObj == null) {
                    throw new IllegalArgumentException(
                            "VerifySpec.expectedValue must not be null for TEXT_CONTAINS | key=" + logicalKey
                    );
                }

                String expected = String.valueOf(expectedObj);
                String actual = readText(el, fieldType);

                Assert.assertTrue(
                        actual != null && actual.contains(expected),
                        "Key: " + logicalKey +
                                " | Validation: TEXT_CONTAINS" +
                                " | Expected fragment: " + expected +
                                " | Actual: " + actual
                );
                break;
            }

            // ---------------------------
            // STATIC TEXT
            // ---------------------------
            case STATIC_TEXT: {

                if (expectedObj == null) {
                    throw new IllegalArgumentException(
                            "VerifySpec.expectedValue must not be null for STATIC_TEXT | key=" + logicalKey
                    );
                }

                String expected = String.valueOf(expectedObj);
                String actual = el.getText();

                Assert.assertEquals(
                        actual,
                        expected,
                        "Key: " + logicalKey +
                                " | Validation: STATIC_TEXT" +
                                " | Expected: " + expected +
                                " | Actual: " + actual
                );
                break;
            }

            default:
                throw new UnsupportedOperationException(
                        "Unsupported ValidationType: " + type +
                                " | key=" + logicalKey
                );
        }
    }

    private static String readText(WebElement el, FieldType fieldType) {
        if (fieldType == FieldType.TEXTBOX
                || fieldType == FieldType.DROPDOWN) {
            return el.getAttribute("value");
        }
        return el.getText();
    }
}