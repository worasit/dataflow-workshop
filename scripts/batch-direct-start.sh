#!/usr/bin/env bash

PROJECT="dataflow-demo-283112"
BUCKET="batch_demo_001"
RUNNER="DirectRunner"
REGION="us-central1"


BQ_TABLE="taxi_rides"
SUBSCRIPTION="taxi-test-sub"

echo "Start Streaming Data Pipeline with ${RUNNER} mode."

export GOOGLE_APPLICATION_CREDENTIALS="your/full/path/to/dataflow-demo-service-account.json"

mvn compile exec:java -e \
-Dexec.mainClass=org.rdp.googlecloud.BatchWorkshop \
-Dexec.args="--project=${PROJECT} \
--stagingLocation=gs://${BUCKET}/staging \
--gcpTempLocation=gs://${BUCKET}/df_temp \
--tempLocation=gs://${BUCKET}/bq_temp \
--inputFile=gs://${BUCKET}/covid19_us_states.csv \
--outputFile=gs://${BUCKET}/invaid_records.csv \
--outputTable=${PROJECT}:batch_demo.deaths_per_state \
--region=${REGION} \
--runner=${RUNNER}"