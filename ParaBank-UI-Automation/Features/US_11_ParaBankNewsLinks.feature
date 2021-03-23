Feature: US_11_ParaBank news links
    As a user, I want to be able to see news about the new services offered by the bank

    @US:10 @ParaBankProduct @Automated @ParaExt
    Scenario: Verify user can view ParaBank news
        Given I have navigated the the ParaBank login page
        When I click the <top link> in the Top News section
        Then I should be redirected to the <ParaBank News> page
        And I should see the ParaBank news articles

    Scenario: Verify user can access news when logged in
        Given I am on the Accounts Overview page
        When I click the <home> link in the <top> menu
        And I click the <top link> in the Top News section
        Then I should be redirected to the <ParaBank News> page
        And I should see the ParaBank news articles

    
