Feature: US_10_Request Loan
    As a user, I want to be able to request a loan

    @US:10 @ParaBankProduct @Automated @ParaExt
    Scenario: Verify Request Loan link from sidemenu brings you to the Request Loan page
        Given I am on the <Accounts Overview> page
        When I click the <Log Out> link in the <sidebar> menu
        Then I should be redirected to the <Landing /Customer Login> page

    
    
