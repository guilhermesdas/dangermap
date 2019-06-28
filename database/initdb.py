from newssites import *

# open db
dburl = "mongodb://localhost:27017/"
dbname = "newssites"
db = getdb(dburl,dbname)

# create collections
addcollection(db,"keywords","keyword")
addcollection(db,"seeds","seed")
initfrontier(db)