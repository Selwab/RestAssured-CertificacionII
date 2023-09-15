Feature: Employees endpoint
  #descripción
  Background: Employees endpoints should allow to get, create, update and delete employees

    @getAll
    Scenario: /employees should return all the employees
      Given I perform a GET to the employees endpoint
      Then I verify status code 200 is returned
      And I verify that the body does not have size 0

    @post
    Scenario: /create should create an employee
      Given I perform a POST to the create endpoint with the following data
        | Diego | 3500 | 26 |
      Then I verify status code 200 is returned
      And I verify that the body does not have size 0
      And I verify the following data in the post body response
        | Diego | 3500 | 26 |

    @getById
    Scenario: /employees should the employee with id
      Given I perform a GET to the employees endpoint with the following "1"
      Then I verify status code 200 is returned
      And I verify that the body does not have size 0
      And I verify the following data in the getById employee body response
        | success | 1 |


  @delete
    Scenario: /delete should delete an employee
      Given I perform a DELETE to the delete endpoint with id "1"
      Then I verify status code 200 is returned
      And I verify that the body does not have size 0
      And I verify the following data in the delete body response
        | success | 1 | Successfully! Record has been deleted |
      #And I verify that employee with ID 1 no longer exists anymore

    @update
    Scenario: /put should update an employee
      Given I perform a PUT to update endpoint with id "1" and following data
        | Nicole | 3500 | 32 |
      Then I verify status code 200 is returned
      And I verify that the body does not have size 0
      And I verify the following data in the put body response
        | success | Nicole | 3500 | 32 | Successfully! Record has been updated. |

    @updateIncorrectValues
      Scenario: /put should not update an employee
        Given I perform a PUT to update endpoint with id "1" and following data
          | Nicole | salario | 61 |
        Then I verify status code 400 is returned
        And I verify that the body does not have size 0

    @deleteInvalidId
    Scenario: /delete should not delete an employee
      Given I perform a DELETE to the delete endpoint with id "30"
      Then I verify status code 400 is returned
      And I verify that the body does not have size 0




