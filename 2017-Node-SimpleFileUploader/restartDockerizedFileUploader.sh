#!/bin/bash

docker rm -f file-server-test

docker run -d \
  -u 1000 \
  --name=file-server-test \
  -v /home/pi/fileserver/uploads:/usr/src/app/uploads \
  -p 3000:3000 \
  file-server:1.0

kill -15 `sudo ps -fea | grep '[d]ocker logs -f file-server-test' | awk '{print $2}'` 2>/dev/null
sleep 3
nohup sudo docker logs -f file-server-test >> /var/log/file-server.log &
