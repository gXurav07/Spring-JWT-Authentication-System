podman run -d   --name mysql-demo   -e MYSQL_ROOT_PASSWORD=rootpass123  \
  -e MYSQL_DATABASE=testdb   \
  -e MYSQL_USER=testuser   -e MYSQL_PASSWORD=testpass123  \
  -p 3306:3306   \
  docker.io/library/mysql:8.4


podman run -d --name kafka \
  -p 9092:9092 \
  -v "$(pwd)/start-kafka.sh:/start-kafka.sh:Z" \
  docker.io/apache/kafka:latest \
  /bin/sh /start-kafka.sh


/opt/kafka/bin/kafka-topics.sh \
  --bootstrap-server localhost:9092 \
  --list

podman run -d -p 1025:1025 -p 8025:8025 mailhog/mailhog
# open localhost:8025 after this to see the mails

