Feature: US_3_View Account details
  As a user, I want to be able to view my account information when I click the Account Overview link

  @US:3 @ParaBankProduct @Automated @ParaExt
  Scenario: Verify all accounts and balances shown on after signing in
    When I log in as a user
    Then I should be redirected to the <Account Overview> page

  @US:3 @ParaBankProduct @Automated
  Scenario: Verify all accounts and balances are shown after clicking on 'Account Overview' link in sidebar menu
    Given The sidebar menu is visible
    When I click the <Account Overview> link in the <sidebar> menu
    Then I should be redirected to the <Account Overview> page
    And I should see a list of accounts
    And The balance for each account

  @US:3 @ParaBankProduct @Automated
  Scenario: Verify account details available for each account
    Given I am on the <Account Overview> page
    When I click on the account number in the Accounts Overview section
    Then I should be redirected to the <account details> page
    And I should see the following information:
      | information    | field name in ParaBank |
      | Account Number | accountId              |
      | Account Type   | accountType            |
      | Balance        | balance                |
      | Available      | availableBalance       |

  @US:3 @ParaBankProduct @Automated
  Scenario: Verify transaction details searchable for each account
    Given I am on the <Account Overview> page
    When I click on the account number in the Accounts Overview section
    And I select <month> from the <Activity Period> dropdown
    And I select <type> from the <Type> dropdown
    Then I should see the correct transactions

  @US:3 @ParaBankProduct @Automated
  Scenario: Verify transactions list is visible, sorted by date on the Account Overview page
    Given I am on the <Account Overview> page
    When I click on the account number in the Accounts Overview section
    Then I should see a list of transactions sorted by date descending

  @US:3 @ParaBankProduct @Automated
  Scenario: Verify user can view transaction details by clicking on transaction
    When I click on the transaction type in the transactions list
    Then I should see the following information:
      | information    | field name in ParaBank |
      | Transaction ID |                        |
      | Date           |                        |
      | Description    |                        |
      | Type           |                        |
      | Amount         |                        |



