import pymongo

############### Database API ###############

def getdb(url,name):
    client = pymongo.MongoClient(url)
    db = client[name]
    return db

# add some collection from file
def addcollection(db, colname, fieldname):
    # get all data from file
    with open(colname, "r") as file:
        lines = [line.rstrip('\n') for line in file]

    # add each line to database
    for line in lines:
        js = { fieldname: line }
        db[colname].insert_one(js)

# reset given collection
def resetcollection(db,colname):
    db[colname].delete_many({})

# get data from database
# colname: collection name
# fieldname: field name from data to be addded in list
def getdata(db,colname,fieldname):
    data = []
    cursor = db[colname].find({}).sort(fieldname,1)
    for doc in cursor:
        data.append(doc[fieldname])
    return data

# get only one frontier page from index
def getfrontier(db,index):
    
    # if there's no frontier, somethings wrong with database
    nfrontier = db["frontier"].count_documents({})
    if ( nfrontier == 0 or index >= nfrontier  ):
        return None

    # return url frontier from given index
    cursor = db["frontier"].find({})
    return cursor[index]["url"]

# initialize frontier with seeds
def initfrontier(db):
    resetcollection(db,"frontier")
    # get all links from seeds
    cursor = db["seeds"].find({}).sort("seed",1)
    for doc in cursor:
        # add to frontier
        js = { "url": doc["seed"] }
        db["frontier"].insert_one(js)

# insert urls to frontier, without repetition
def addfrontiers(db,urls):
    if not(db is None):
        for url in urls:
            js = {"url": url}
            if db["frontier"].count_documents(js) == 0:
                db["frontier"].insert_one(js)
    else:
        print("Database error: null object")

# insert links into repository
# js has baseurl, url, and list of keywords fields
def addrepository(db,baseUrl,url,foundedkeywords):
    if not(db is None):
        js = { "baseurl": baseUrl, "url": url, "keywords": foundedkeywords }
        url = { "url": js["url"] }
        # insert only if there's no repetited urls
        if db["repository"].count_documents(url) == 0:
            db["repository"].insert_one(js)
    else:
        print("Database error: null object")

# get all frontier links
def getkeywords(db):
    # add all frontier to an list
    return getdata(db,"keywords","keyword")

# get all repository
def getrepository(db):
    # add all frontier to an list
    repository = []
    cursor = db["repository"].find({}).sort("baseurl",1)
    for doc in cursor:
        repository.append(doc)
    return repository

# get all frontier links
def getfrontiers(db):
    # add all frontier to an list
    return getdata(db,"frontier","url")

#
def getseeds(db):
    return getdata(db,"seeds","seed")