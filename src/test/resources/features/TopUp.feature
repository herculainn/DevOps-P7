Feature: TopUp Account
  This feature describes various scenarios for users adding funds to their revolut account(s)

  # As a user, I can topup my Revolut account using my debit card

  Scenario: Add money to Revolut account using debit card
    Given Danny has 10 euro in his euro Revolut account
    And Danny selects 100 euro as the topUp amount
    And  Danny selects his DebitCard as his topUp method
    When Danny tops up
    Then The new balance of his euro account should now be 110

  Scenario: Add money to Revolut account using bank account
    Given Danny has 20 euro in his euro Revolut account
    And Danny selects 230 euro as the topUp amount
    And  Danny selects his BankAccount as his topUp method
    When Danny tops up
    Then The new balance of his euro account should now be 250

  # ToDo implement the remaining scenarios listed below

  # To implement this scenario you will need to use data tables
  # https://cucumber.io/docs/cucumber/api/
  Scenario Outline: Add various amounts to Revolut account
    Given Danny has a starting balance of <startBalance>
    And Danny selects his DebitCard as his topUp method
    When Danny now tops up by <topUpAmount>
    Then The balance in his euro account should be <newBalance>
    Examples:
      | startBalance | topUpAmount | newBalance  |
      | 0            | 100         | 100         |
      | 14           | 20          | 34          |
      | 23           | 30          | 53          |

  # As above but with currency conversion from source to target
  # DebitCart default currency will be EUR - I will be paying nEUR from DebitCard
  # 4: top-up a GBP account by 100EUR is a  80GBP top-up
  # 5: top-up a GBP account by  20EUR is a  16GBP top-up
  # 6: top-up a GBP account by  30EUR is a  24GBP top-up
  # 7: top-up a USD account by 100EUR is a 120USD top-up
  # 8: top-up a USD account by  20EUR is a  24USD top-up
  # 9: top-up a USD account by  30EUR is a  36USD top-up
  Scenario Outline: Add various amounts to Revolut account for various currencies
    Given Danny has a starting balance of <startBalance> in their <currency> account
    And Danny selects his DebitCard as his topUp method
    When Danny now tops up by <topUpAmount>
    Then The balance in his euro account should be <newBalance>
    Examples:
      | currency | startBalance| topUpAmount | newBalance  |
      | EUR      |   0         | 100         | 100         |
      | EUR      |  14         |  20         |  34         |
      | EUR      |  23         |  30         |  53         |
      | GBP      |   0         | 100         |  80         |
      | GBP      |  14         |  20         |  30         |
      | GBP      |  23         |  30         |  47         |
      | USD      |   0         | 100         | 120         |
      | USD      |  14         |  20         |  38         |
      | USD      |  23         |  30         |  59         |

  # As above but with Currencies and Currency Triangulation!
  # eg. Take EUR, From a GBP account, and deposit into a USD account.
  # using EUR as a base: 0.8GBP, 1.2USD:

  # 1: Move 100EUR from GBP to EUR:
  # 100EUR = (100 * 0.8) =  80STG; // to come out of the source account
  #  80STG = ( 80 * (1.0/0.8)) = 100EUR; // to enter the destination account
  # new balance 0EUR + 100EUR = 100EUR

  # 2: Move 20GBP from EUR to GBP:
  #  20GBP = ( 20 * (1.0/0.8)) = 25EUR; // to come out of the source account
  #  25EUR = ( 25 * 0.8) = 20GBP; // to enter the destination account
  # new balance 15GBP + 20GBP = 35GBP

  # 3: Move 20EUR from GBP to USD:
  #  20EUR = ( 20 * 0.8) =  16GBP; // to come our of the source account
  #  16GBP = ( 16 * (1.2/0.8)) = 24USD; to enter the destination account
  # new balance 10USD + 24USD = 34USD

  Scenario Outline: Currency Triangulation
    Given Danny has a starting balance of <startBalance> in their <revolutCurrency> account
    And Danny selects his <sourceCurrency> DebitCard as his topUp method
    When Danny now tops up by <topUpAmount> <movementCurrency>
    Then The balance in his euro account should be <newBalance>
    Examples:
      | sourceCurrency | movementCurrency | revolutCurrency | startBalance | topUpAmount | newBalance  |
      | GBP            | EUR              | EUR             |   0          | 100         | 100         |
      | EUR            | GBP              | GBP             |  15          |  20         |  35         |
      | GBP            | EUR              | USD             |  10          |  20         |  34         |

  # https://cucumber.io/docs/gherkin/reference/#rule
  Rule: The account balance shouldn't change if the topup payment request is rejected by the payment service

    #The scenarios below will need a payment service that accepts or rejects a request to add funds
    Scenario: Payment service rejects the request
      Given Danny has 50 euro in his euro Revolut account
      And Danny selects his BankAccount as his topUp method
      When Danny now tops up by 350
      Then The new balance of his euro account should now be 50

    Scenario: Payment service accepts the request
      Given Danny has 50 euro in his euro Revolut account
      And Danny selects his BankAccount as his topUp method
      When Danny now tops up by 250
      Then The new balance of his euro account should now be 300

  # Perform currency exchange between internal Revolut accounts
  # Most actions have been implemented to create an account for the currency targeted
  # Start with a balance in two accounts, then take from one into the other
  # Amount transferred is taken from source, and exchanged to different value for target

    Scenario: Exchange between Revolut Accounts (internal EUR->GBP)
      Given Danny has a starting balance of 50 in their EUR account
      And Danny has a starting balance of 10 in their GBP account
      When Danny transfers 40 from their EUR account to their GBP account
      Then The balance in their EUR account should be 10
      And The balance in their GBP account should be 42

  # As above but to move through EUR as a base in the background of PaymentService
    Scenario: Exchange between Revolut Accounts (internal USD->GBP)
      Given Danny has a starting balance of 50 in their USD account
      And Danny has a starting balance of 10 in their GBP account
      When Danny transfers 40 from their USD account to their GBP account
      Then The balance in their USD account should be 10
      And The balance in their GBP account should be 36.66
