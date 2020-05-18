# Risk Frontiers Risk Rating Database API

This repository is a collection of usage examples in different languages for Risk Frontiers' Risk Rating Database API.

Authentication for this API follows the OAuth 2.0 protocol standard for machine-to-machine API access. In OAuth terminology this known as client access - the client in this case meaning a software client.

Calling the API requires a _JWT Bearer_ token. A bearer token may be obtained from the authentication service by making a `POST` request to:

https://auth.riskfrontiers.com/connect/token

with the following 4 parameters in the body of the request encoded using `x-www-form-urlencoded` format:

```
client_id:my_client_id
client_secret:my_client_secret
scope:rrd1
grant_type:client_credentials
```

Contact Risk Frontiers to obtain your client id and secret.

The authentication service will return a JSON object which includes an access token with which you can to access the API. Currently this token has a validity of one hour, after which you need to use the authentication service again to obtain a new token.

Accessing the API with the bearer token is now simply a matter of adding it to the http authorization header for the required call. For example to request the Risk Rating data for GNAF-ID: GANSW703873411 use the following:

https://rrd.riskfrontiers.com/v1/gnaf_risk_ratings/GANSW703873411

and add an `Authorization` key with the value:
```
Bearer eyJhbGc...54cG2A
```
(where the long actual token value has been abbreviated for clarity here)
