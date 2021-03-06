#!/usr/bin/env bash

PROJECT="dataflow-demo-283112"
BUCKET="stream_demo_001"
RUNNER="DataflowRunner"
REGION="us-central1"


BQ_TABLE="taxi_rides"
SUBSCRIPTION="taxi-test-sub"

echo "Start Streaming Data Pipeline with ${RUNNER} mode."

export GOOGLE_APPLICATION_CREDENTIALS="/Users/worasitdaimongkol/Desktop/refinitiv_docs/dataflow-demo/dataflow-demo-service-account.json"

mvn compile exec:java -e -P direct-runner,dataflow-runner \
-Dexec.mainClass=org.rdp.googlecloud.StreamWorkshop \
-Dexec.args="--project=${PROJECT} \
--stagingLocation=gs://${BUCKET}/staging \
--gcpTempLocation=gs://${BUCKET}/df_temp \
--tempLocation=gs://${BUCKET}/bq_temp \
--outputTable=${PROJECT}:stream_demo.${BQ_TABLE} \
--inputSubscription=projects/${PROJECT}/subscriptions/${SUBSCRIPTION} \
--region=${REGION} \
--runner=${RUNNER}"