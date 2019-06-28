import requests
import json

# base url
baseurl = "http://localhost:3000/"

# get all lines of a file as a list
def getlines(filename):
    with open(filename,'r') as file:
        lines = [line.rstrip('\n') for line in file]
    return lines

# post some json data in url
def post(url,jsondata):
    resp = requests.post(url,data=jsondata)
    return resp.json()

# get some json data in url
def get(url,jsondata):
    resp = requests.get(url,data=jsondata)
    return resp.json()

# add keywords
def addkeywords():
    # get list of keywords from file
    keywords = getlines("keywords")

    for keyword in keywords:
        # create json for given keyword
        jsondata = {
            "keyword": keyword,
            "blacklist": "false"
        }
        # make post to add in database
        ans = post(baseurl+"keywords/add/",jsondata)
        print(ans)

# add links
def addlinks():
    # get list of links from file
    links = getlines("seeds")

    for link in links:
        # create json for given keyword
        jsondata = {
            "link": link,
            "isBaseURL": "true"
        }
        # make post to add in database
        ans = post(baseurl+"links/add/",jsondata)
        print(ans)


addkeywords()
addlinks()

# url = 'http://localhost:3000/keywords'

# resp = requests.get(url=url)
# data = resp.json()
# print(json.dumps(data,indent=4))