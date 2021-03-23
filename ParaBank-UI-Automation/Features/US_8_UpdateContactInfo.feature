Feature: US_8_Update Contact Info
    As a user, I want to be able update my account information

    @US:8 @ParaBankProduct @Automated @ParaExt
    Scenario: Verify Update Contact Info link from sidemenu brings you to the Update Contact Info page
        Given I am on the <Accounts Overview> page
        When I click the <Update Contact Info> link in the <sidebar> menu
        Then I should be redirected to the <Update Contact Info> page
        And my information should be pre-populated in the contact info form

    @US:8 @ParaBankProduct @Automated
    Scenario: Verify form validation requires information to be entered
        When I clear all the information in the contact info form
        And I click the <Update Profile> button
        Then I should see the following errors:
            | Field Name  | Field Value             |
            | First Name: | First name is required. |
            | Last Name:  | Last name is required.  |
            | Address:    | Address is required.    |
            | City:       | City is required.       |
            | State:      | State is required.      |
            | Zip Code:   | Zip Code is required.   |

    @US:8 @ParaBankProduct @Automated
    Scenario: Verify confirmation message displays
        When I enter values for each of the contact info form fields
        And I click the <Update Profile> button
        Then I should see 'Profile Updated
Your updated address and phone number have been added to the system.'

     @US:8 @ParaBankProduct @Automated
    Scenario: Verify user entered values are saved
         When I click the <Update Contact Info> link in the sidebar menu
        Then I should be redirected to the <Update Contact Info> page
        And The updated information should be pre-populated in the contact info form

