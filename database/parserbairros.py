with open("neighborhood", "r") as file:
    lines = [line.rstrip('\n') for line in file]

#f = open("neighborhood", "r")
#lines = f.read()
#f.close()

bairros = ''
for line in lines:
    bairros += line[60:-18] + '\n'

for bairro in bairros:
    print(bairro)

f = open("neighborhood2","w")
f.write(bairros)
f.close()

#print(bairros)//