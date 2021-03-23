Feature: US_1_UserRegistration
 As a new user, I want to be able to register for an account

@US:1 @ParaBankProduct @Automated  @ParaExt
Scenario: Verify Register button redirects user to the Registration page
	  Given I am a new user to ParaBank
	  And I have navigated to the <ParaBank login> page
	  When I click the <Register> button
	  Then I should be redirected to the <Registration> page
  
@US:1 @ParaBankProduct @Automated @ParaExt
Scenario: Verify, once registered, user is redirected to the homepage
	  Given I am on the <Registration> page
	  And I enter all my user information in the Registration form
	  When I click the <Register> button
	  Then I should see the text 'Welcome <USER_NAME>'

@US:1 @ParaBankProduct @Automated @ParaExt
Scenario: Verify that validation is required in Registration form
	 Given I am on the <Registration> page
	 When I leave the registration form blank
	 And I click the <Register> button
	 Then I should see valiation errors for each field as 'Required'
	 
 
	
