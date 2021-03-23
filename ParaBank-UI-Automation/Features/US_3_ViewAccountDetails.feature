Feature: US_3_View Account details
  As a user, I want to be able to view my account information when I click the Account Overview link

  @US:3 @ParaBankProduct @ParaExt
  Scenario: Verify all accounts and balances shown on after signing in
    When I log in as a user
    Then I am redirected to the Accounts Overview page
    And I should see a list of my accounts
    And The balance for each account

  @US:3 @ParaBankProduct @ParaExt
  Scenario: Verify all accounts and balances are shown after clicking on 'Account Overview' link in sidebar menu
    Given The sidebar menu is visible
    When I click the <Account Overview> link in the sidebar menu
    Then I should see a list of accounts
    And The balance for each account
    
        |
