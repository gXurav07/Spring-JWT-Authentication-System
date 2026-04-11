#!/bin/sh
set -e

CLUSTER_ID=$(/opt/kafka/bin/kafka-storage.sh random-uuid)

echo "Using cluster id: $CLUSTER_ID"

/opt/kafka/bin/kafka-storage.sh format \
  --ignore-formatted \
  --standalone \
  -t "$CLUSTER_ID" \
  -c /opt/kafka/config/server.properties

exec /opt/kafka/bin/kafka-server-start.sh /opt/kafka/config/server.properties