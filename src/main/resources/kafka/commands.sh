podman run -d --name kafka \
  -p 9092:9092 \
  -v "$(pwd)/start-kafka.sh:/start-kafka.sh:Z" \
  docker.io/apache/kafka:latest \
  /bin/sh /start-kafka.sh


/opt/kafka/bin/kafka-topics.sh \
  --bootstrap-server localhost:9092 \
  --list