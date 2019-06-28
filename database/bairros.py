from urllib.parse import urlencode
from urllib.request import Request, urlopen
import json

url = 'https://places-dsn.algolia.net/1/places/query'
post = {}
post["@query"] = "Manaus ufam"
header = {
"User-Agent": "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.75 Safari/537.36",
"X-Requested-With": "XMLHttpRequest"
}

request = Request(url, urlencode(post).encode())
json = urlopen(request).read().decode()
print(json)

#with open("bairros", "r") as json_file:
#    data = json.load(json_file)

#print(data["hits"][0]["_geoloc"])