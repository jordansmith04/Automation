Feature: US_4_Open New Account
    As a user, I want to be able to open an account

    @US:4 @ParaBankProduct @Automated @ParaExt
    Scenario: Verify Open New Account link redirects to Open New Account Page
        Given I am on the <Accounts Overview> page
        When I click the <Open New Account> link in the <sidebar> menu
        Then I should be redirected to the <Open New Account> page

    @US:4 @ParaBankProduct @Automated
    Scenario: Verify user can open account
        Given I am on the <Open New Account> page
        When I select <account type> from the <What type of Account would you like to open?> dropdown
        And I select <account> from the <Please choose an existing account to transfer funds into the new account.> dropdown
        And I click the <Open New Account> button
        Then I should see 'Account Opened! Congratulations, your account is now open. Your new account number: <account number>' on the confirmation page

    @US:4 @ParaBankProduct @Automated
    Scenario: Verify user can follow link on Account Opened page
        When I click the account number on the confirmation page
        Then I should be redirected to the <account details> page


