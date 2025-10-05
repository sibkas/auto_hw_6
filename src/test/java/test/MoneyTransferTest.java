package test;

import data.DataHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import page.DashboardPage;
import page.LoginPage;
import page.VerificationPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {

    private DashboardPage dashboardPage;
    private final String firstCardId = "92df3f1c-a033-48e6-8390-206f6b1f56c0";
    private final String secondCardId = "0f3f5c2a-249e-4c3d-8287-09f7a039391d";
    private final String secondCardLastDigits = "0002";

    @BeforeEach
    void setUp() {
        var authInfo = DataHelper.getAuthInfo();
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);

        LoginPage loginPage = open("http://localhost:9999", LoginPage.class);
        VerificationPage verificationPage = loginPage.validLogin(authInfo.getLogin(), authInfo.getPassword());
        dashboardPage = verificationPage.validVerify(verificationCode.getCode());
    }

    @Test
    void shouldTransferMoneyBetweenOwnCards() {

        var firstCard = DataHelper.getFirstCardInfo();
        var secondCard = DataHelper.getSecondCardInfo();

        int balanceFirstCardBefore = dashboardPage.getCardBalance(firstCard.getId());
        int balanceSecondCardBefore = dashboardPage.getCardBalance(secondCard.getId());

        int transferAmount = balanceFirstCardBefore / 2;

        var transferPage = dashboardPage.selectCardToTransferByLastDigits(secondCard.getLastDigits());
        dashboardPage = transferPage.transferFromCard(firstCard.getNumber(), transferAmount);


        dashboardPage = dashboardPage.refresh();

        int balanceFirstCardAfter = dashboardPage.getCardBalance(firstCard.getId());
        int balanceSecondCardAfter = dashboardPage.getCardBalance(secondCard.getId());

        assertEquals(balanceFirstCardBefore - transferAmount, balanceFirstCardAfter);
        assertEquals(balanceSecondCardBefore + transferAmount, balanceSecondCardAfter);
    }


}
