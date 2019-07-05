import subprocess
import json

arq = open('places', 'r')
arq_saida = open('places.json', 'w')

texto = arq.readline().split('#')

#print(texto)

# for bairro in set(texto):
# 	print(bairro)
# 	arch = subprocess.check_output("curl -X POST \'https://places-dsn.algolia.net/1/places/query\' --data \'{\"query\": \"Manaus "+bairro+"\",\"hitsPerPage\":\"1\",\"countries\": [\"br\"],\"language\":\"pt\",\"type\": \"address\"}\'", shell=True);
# 	data = json.loads(arch);

# 	js = {
# 		"name":  bairro,
# 		"lat": data["hits"][0]["_geoloc"].get("lat"),
# 		"lng": data["hits"][0]["_geoloc"].get("lng")
# 	}

# 	json.dump(js,arq_saida)
# 	arq_saida.write("\n");

# arq.close()
# arq_saida.close()

arch = subprocess.check_output("curl -X POST \'https://places-dsn.algolia.net/1/places/query\' --data \'{\"query\": \"Manaus zona norte\",\"hitsPerPage\":\"1\",\"countries\": [\"br\"],\"language\":\"pt\",\"type\": \"address\"}\'", shell=True);
print ("[DEBUG]")
print (arch)

data = json.loads(arch);
print (data["hits"][0]["_geoloc"].get("lat"), data["hits"][0]["_geoloc"].get("lng"))