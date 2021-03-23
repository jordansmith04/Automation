Feature: US_5_Transfer Funds
    As a user, I want to be able to transfer funds between accounts

    @US:5 @ParaBankProduct @Automated @ParaExt
    Scenario: Verify Transfer Funds link from sidemenu brings you to the Transfer Funds page
        Given I am on the <Accounts Overview> page
        When I click the <Transfer Funds> link in the <sidebar> menu
        Then I should be redirected to the <Transfer Funds> page

    @US:5 @ParaBankProduct @Automated
    Scenario: Verify user can transfer funds
        When I enter an amount in the amount text box
        And I select <from account number> from the <From account #> dropdown
        And I select <to account number> from the <to account #> dropdown
        And I click the <Transfer> button
        Then I should see 'Transfer Complete! <Dollar amount> has been transferred from account #<from account number> to account #<to account number>. See Account Activity for more details.'


