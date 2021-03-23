Feature: US_2_Login
 As a user, I want to be able to log in with my information
 
  @US:2 @ParaBankProduct @Automated @ParaExt
  Scenario: Verify when user enters correct credentials they are redirected to the homepage
    Given I have navigated to the ParaBank login page
    When I enter my <username> and <password> into the login fields 
    And I click the <Login> button
    Then I should be redirected to the <Accounts Overview> page
    
  @US:2 @ParaBankProduct @Automated @ParaExt
  Scenario: Verify validation errors appear when incorrect credentials supplied
  Given I have navigated to the ParaBank login page
    When I enter a <username> and <password> for a non-existent account into the login fields 
    And I click the <Login> button
    Then I should see an 'Error! The username and password could not be verified.' message on the page
    
  @US:2 @ParaBankProduct @Automated @ParaExt
  Scenario: Verify validation errors appear when no credentials supplied
  Given I have navigated to the ParaBank login page
    When I do not enter a <username> and <password> into the login fields 
    And I click the <Login> button
    Then I should see an 'Error! Please enter a username and password.' message on the page
