Feature: US_6_Bill Pay
    As a user, I want to be able to Bill Pay between accounts

    @US:6 @ParaBankProduct @Automated @ParaExt
    Scenario: Verify Bill Pay link from sidemenu brings you to the Bill Pay page
        Given I am on the <Accounts Overview> page
        When I click the <Bill Pay> link in the <sidebar> menu
        Then I should be redirected to the <Bill Pay> page

    @US:6 @ParaBankProduct @Automated
    Scenario: Verify validation requires fields to be filled in Bill Pay form
        When I do not enter any information in the Bill Pay form
        And I click the <Send Payment> button
        Then I should see the following validation messages:
            | Field Name        | Field Value                 |
            | Payee Name:       | Payee name is required.     |
            | Address:          | Address is required.        |
            | City:             | City is required.           |
            | State:            | State is required.          |
            | Zip Code:         | Zip Code is required.       |
            | Phone #:          | Phone number is required.   |
            | Account #:        | Account number is required. |
            | Verify Account #: | Account number is required. |
            | Amount: $         | The amount cannot be empty. |

    @US:6 @ParaBankProduct @Automated
    Scenario: Verify validation requires valid account numbers and amount in Bill Pay form
        When I only enter the following information in the Bill Pay form
            | Field Name        | Field Value |
            | Account #:        | alabama     |
            | Verify Account #: | alabama     |
            | Amount: $         | (500.00)    |
        And I click the <Send Payment> button
        Then I should see the following validation messages:
            | Field Name        | Field Value                  |
            | Payee Name:       | Payee name is required.      |
            | Address:          | Address is required.         |
            | City:             | City is required.            |
            | State:            | State is required.           |
            | Zip Code:         | Zip Code is required.        |
            | Phone #:          | Phone number is required.    |
            | Account #:        | Please enter a valid number. |
            | Verify Account #: | Please enter a valid number. |
            | Amount: $         | Please enter a valid amount. |

    @US:6 @ParaBankProduct @Automated
    Scenario: Verify validation requires account numbers to match in Bill Pay form
        When I only enter the following information in the Bill Pay form
            | Field Name        | Field Value |
            | Account #:        | 11111111    |
            | Verify Account #: | 11111112    |
        And I click the <Send Payment> button
        Then I should see the following validation messages:
            | Field Name        | Field Value                       |
            | Payee Name:       | Payee name is required.           |
            | Address:          | Address is required.              |
            | City:             | City is required.                 |
            | State:            | State is required.                |
            | Zip Code:         | Zip Code is required.             |
            | Phone #:          | Phone number is required.         |
            | Account #:        |                                   |
            | Verify Account #: | The account numbers do not match. |
            | Amount: $         | The amount cannot be empty.       |

    @US:6 @ParaBankProduct @Automated
    Scenario: Verify user can use Bill Pay
        When I enter the following information in the Bill Pay form
            | Field Name        | Field Value            |
            | Payee Name:       | #payee.name            |
            | Address:          | #payee.address.street  |
            | City:             | #payee.address.city    |
            | State:            | #payee.address.state   |
            | Zip Code:         | #payee.address.zipCode |
            | Phone #:          | #payee.phoneNumber     |
            | Account #:        | #payee.accountNumber   |
            | Verify Account #: | #verifyAccount         |
            | Amount: $         | #amount                |
        And I select <from account number> from the <From account #> dropdown
        And I click the <Send Payment> button
        Then I should see 'Bill Payment Complete
        Bill Payment to <payee name> in the amount of $<amount> from account <account number> was successful.
        See Account Activity for more details.'

'
