package page;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class VerificationPage {
    public DashboardPage validVerify(String code) {
        $("[data-test-id=code] input").shouldBe(visible).setValue(code);
        $("[data-test-id=action-verify]").click();
        return page(DashboardPage.class);
    }
}
