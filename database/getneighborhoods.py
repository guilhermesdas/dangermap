import requests
import json

url = 'https://places-dsn.algolia.net/1/places/query'
jsondata = {
    "query": "Manausufam"
}

# header = {
# "User-Agent": "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.75 Safari/537.36",
# "X-Requested-With": "XMLHttpRequest"
# }

request = requests.post(url,data=jsondata)
print(request.json())