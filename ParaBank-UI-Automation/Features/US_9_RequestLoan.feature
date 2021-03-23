Feature: US_9_Request Loan
    As a user, I want to be able to request a loan

    @US:9 @ParaBankProduct @Automated @ParaExt
    Scenario: Verify Request Loan link from sidemenu brings you to the Request Loan page
        Given I am on the <Accounts Overview> page
        When I click the <Request Loan> link in the <sidebar> menu
        Then I should be redirected to the <Apply for a Loan> page

    @US:9 @ParaBankProduct @Automated
    Scenario: Verify success message
        When I enter a <asking amount> in the <Loan Amount: $> text box
        And I enter a <downpayment amount> in the <Down Payment: $> text box
        And I select <from account number> from the <From account #> dropdown
        And I click the <Apply Now> button
        Then I should see:
        'Loan Request Processed
        Loan Provider:	Wealth Securities Dynamic Loans (WSDL)
        Date:	<today's date>
        Status:	Approved

        Congratulations, your loan has been approved.

        Your new account number: <account number>'

    @US:9 @ParaBankProduct @Automated
    Scenario: Verify user can follow link to account information
         When I click the account number on the confirmation page
        Then I should be redirected to the <account details> page

