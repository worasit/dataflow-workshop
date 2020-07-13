#!/usr/bin/env bash

PROJECT="dataflow-demo-283112"
BUCKET="stream_demo_001"
RUNNER="DataflowRunner"
REGION="us-central1"


BQ_TABLE="taxi_rides"
SUBSCRIPTION="taxi-test-sub"

echo "Start Streaming Data Pipeline with ${RUNNER} mode."

export GOOGLE_APPLICATION_CREDENTIALS="your/full/path/to/dataflow-demo-service-account.json"

mvn compile exec:java -e -P direct-runner,dataflow-runner \
-Dexec.mainClass=org.rdp.googlecloud.StreamWorkshop \
-Dexec.args="--project=${PROJECT} \
--stagingLocation=gs://${PROJECT}/staging \
--gcpTempLocation=gs://${PROJECT}/df_temp \
--tempLocation=gs://${PROJECT}/bq_temp \
--outputTable=${PROJECT}:stream_demo.${BQ_TABLE} \
--inputSubscription=projects/${PROJECT}/subscriptions/${SUBSCRIPTION} \
--region=${REGION} \
--runner=${RUNNER}"