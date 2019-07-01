from newscollector import *

# OBS::
# IF YOU WANT TO REMOVE DROP PREVIOUS DATABASE, RUN IN SHELL
#             mongo
# in mongo shell:
#             use newscollector
#             db.dropDatabase()
# database is now dropped.
# you can check running getkeywords()

addkeywords()
addlinks()
addneighborhoods()
#initrepository()
#print(getrepository())

# url = 'http://localhost:3000/keywords'

# resp = requests.get(url=url)
# data = resp.json()
# print(json.dumps(data,indent=4))
