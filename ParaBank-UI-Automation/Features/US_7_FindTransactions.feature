Feature: US_7_Find Transactions
    As a user, I want to be able to Find Transactions between accounts

    @US:7 @ParaBankProduct @Automated @ParaExt
    Scenario: Verify Find Transactions link from sidemenu brings you to the Find Transactions page
        Given I am on the <Accounts Overview> page
        When I click the <Find Transactions> link in the <sidebar> menu
        Then I should be redirected to the <Find Transactions> page

    @US:7 @ParaBankProduct @Automated
        Scenario: Verify user can look up transactions using Transaction ID
        When I select <account number> from the <Select an account:> dropdown
        And I enter a <transaction Id> in the <Find by Transaction ID:> text box
        And I click the <Find Transactions> button
        Then I should be redirected to the <Transaction Results> page
        And I should see the transaction matching the transaction id

     @US:7 @ParaBankProduct @Automated
        Scenario: Verify user can look up transactions by date
        When I select <account number> from the <Select an account:> dropdown
        And I enter a <transaction date> in the <Find by Date:> text box
        And I click the <Find Transactions> button
        Then I should be redirected to the <Transaction Results> page
        And I should see the transaction matching the transaction id

    @US:7 @ParaBankProduct @Automated
        Scenario: Verify user can look up transactions using date range
        When I select <account number> from the <Select an account:> dropdown
        And I enter a <start date> in the <between> text box
        And I enter a <end date> in the <and> text box
        And I click the <Find Transactions> button
        Then I should be redirected to the <Transaction Results> page
        And I should see the transaction matching the transaction id

    
    @US:7 @ParaBankProduct @Automated
        Scenario: Verify user can look up transactions by amount
        When I select <account number> from the <Select an account:> dropdown
        And I enter a <amount> in the <Find by Amount:> text box
        And I click the <Find Transactions> button
        Then I should be redirected to the <Transaction Results> page
        And I should see the transaction matching the transaction id

    @US:7 @ParaBankProduct @Automated
        Scenario: Verify error message is shown when information is incorrect
        When I select <account number> from the <Select an account:> dropdown
        And I enter a <incorrect amount> in the <Find by Amount:> text box
        And I click the <Find Transactions> button
        Then I should see 'Error! An internal error has occurred and has been logged.'

    

    

    