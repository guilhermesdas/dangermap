import requests
import json
import pymongo
# base url
baseurl = "http://localhost:3000/"
dburl = "mongodb://localhost:27017/"
dbname = "newscollector"

def initdb():
	db = pymongo.MongoClient(dburl)
	db.drop_database(dbname)

#################### BASE FUNCTIONS ####################

# get all lines of a file as a list
def getlines(filename):
    with open(filename,'r') as file:
        lines = [line.rstrip('\n') for line in file]
    return lines

# post some json data in url
def post(url,jsondata):
    print(jsondata)
    resp = requests.post(url,json=jsondata)
    return resp

# get some json data in url
def get(url,jsondata):
    resp = requests.get(url,data=jsondata)
    return resp.json()

#################### KEYWORDS ####################

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

# get all keywords
def getkeywords():
    # https get in server
    return get(baseurl+"keywords/",{})

# add keywords
def addblacklist():
    # get list of keywords from file
    keywords = getlines("blacklist")

    for keyword in keywords:
        # create json for given keyword
        jsondata = {
            "keyword": keyword,
            "blacklist": "true"
        }
        # make post to add in database
        ans = post(baseurl+"keywords/add/",jsondata)
        print(ans)
#################### LINKS ####################

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
        
        #print(jsondata)
        
        # make post to add in database
        ans = post(baseurl+"links/add/",jsondata)
        print(ans)

# get all keywords
def getlinks():
    # https get in server
    return get(baseurl+"links/",{})

#################### NEIGHBORHOODS ####################

# add neighborhoods
def addneighborhoods():
    # get list of links from file
    neighborhood = getlines("bairro_latlng")

    for bairro in neighborhood:
        # create json for given keyword
        jsondata = json.loads(bairro)

        # make post to add in database
        ans = post(baseurl+"neighborhood/add/",jsondata)
        print(ans)

# get all keywords
def getneighborhoods():
    # https get in server
    return get(baseurl+"neighborhood/",{})

#################### REPOSITORY ####################
def addrepository(jsondata):
    return post(baseurl+"repository/add",jsondata)

def getrepository():
    return get(baseurl+"repository/",{})

def initrepository():
    # get keywords ids
    keywords = getkeywords()[:2]
    keywordsids = [ keyword["_id"] for keyword in keywords ]

    # get one neighborhood
    neighborhoodid = getneighborhoods()[3]["_id"]

    # get one link id
    linkid1 = getlinks()[0]["_id"]
    linkid2 = getlinks()[2]["_id"]
    linkid3 = getlinks()[5]["_id"]

    # creates repository json example
    repositoryjson = {}
    repositoryjson["link"] = linkid1
    repositoryjson["neighborhood"] = neighborhoodid
    repositoryjson["keywords"] = keywordsids
    addrepository(repositoryjson)

	# add other fake repositories
    repositoryjson["link"] = linkid2
    addrepository(repositoryjson)
    repositoryjson["link"] = linkid3
    addrepository(repositoryjson)


