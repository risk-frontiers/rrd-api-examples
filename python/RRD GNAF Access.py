import requests
import json

# copy/paste your credentials here
client_id = '<my_client_id>'
client_secret = '<my_client_secret>'

def obtain_token():
    url = "https://auth.riskfrontiers.com/connect/token"

    payload = 'client_id=' + client_id + '&client_secret=' + client_secret + '&scope=rrd1&grant_type=client_credentials'
    headers = {
      'Content-Type': 'application/x-www-form-urlencoded'
              }

    return json.loads(requests.request("POST", url, headers=headers, data = payload).text)


def retrieve_risk_rating_by_gnaf(auth_token, gnaf_to_get):
    url = "https://rrd.riskfrontiers.com/v1/gnaf_risk_ratings/" + gnaf_to_get
    headers = {
      'Authorization': 'Bearer ' + auth_token
    }

    return json.loads(requests.request("GET", url, headers=headers, data={}).text)

# Get a JWT token from the authentication service
my_token = obtain_token()['access_token']
# The token may be re-used multiple times for an hour
my_risk_rating = retrieve_risk_rating_by_gnaf(my_token, "GANSW703873411")
print("GNAF-ID number 1:", my_risk_rating)

my_risk_rating = retrieve_risk_rating_by_gnaf(my_token, "GASA_414890230")
print("GNAF-ID number 2:", my_risk_rating)

my_risk_rating = retrieve_risk_rating_by_gnaf(my_token, "GANT_702910374")
print("GNAF-ID number 3:", my_risk_rating)
