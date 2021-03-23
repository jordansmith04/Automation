Feature: US_12_Login
 As a user, I want to be able to recover my login information
 
  @US:12 @ParaBankProduct @Automated @ParaExt
  Scenario: Verify 'Forgot Login Info?' link redirects user to 'Customer Lookup' page
    Given I have navigated the the <ParaBank login> page
    When I click the <Forgot Login Info?> link
    Then I should be redirected to the <Customer Lookup> page

 @US:12 @ParaBankProduct @Automated @ParaExt
  Scenario: Verify user can lookup their information
    When I enter my user information into the lookup form
    Then I should see my username and password