#!/bin/bash

#bairros=`cat neighborhood`
#echo "$bairros"

while IFS= read -r line; do
  post='{"query": "'$line'"`,"hitsPerPage":"1","countries": ["br"],"language":"pt","type": "address"}'
  curl -X POST 'https://places-dsn.algolia.net/1/places/query' \
    --data $post >> bairros
done < "$1"

#python bairros.py