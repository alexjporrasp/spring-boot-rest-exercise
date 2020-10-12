# spring-boot-rest-exercise
Model of an account service according to REST guidelines.

# Design Decisions

- Accounts cannot be directly edited nor deleted. The requirements didn't explicitly state so.
- I have chosen H2 as embedded database due to its simplicity.

## Notes
- Execute the tests one by one, they are sharing the same context, so the database may behave differently when executing all the tests at the same time.
- Currency rates are dependent on external services, so availablity may be compromised.
## Endpoints

````
method: GET
path: /api/accounts
description: Shows every account in the database.
````

````
method: GET
path: /api/accounts/{id}
description: Shows account with the given id.
````

````
method: POST
path: /api/accounts/
description: Adds a new account to the database.
body: account object.
headers:
    Content-Type = application/json
````

````
method: PUT
path: /api/accounts/transfer
description: Transfer <amount> money from <originId> account to <targetId> account.
params:
    originId: id of the account to withdraw money from.
    targetId: id of the account to add money to.
    amount: the amount of money to exchange.
````

