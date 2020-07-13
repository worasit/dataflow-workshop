#!/usr/bin/env bash
PROJECT="dataflow-demo-283112"
BUCKET="batch_demo_001"
RUNNER_DIRECT="DirectRunner"
RUNNER_DATAFLOW="DataflowRunner"

export GOOGLE_APPLICATION_CREDENTIALS="/Users/worasitdaimongkol/Desktop/refinitiv_docs/dataflow-demo/dataflow-demo-service-account.json"

mvn compile exec:java -e \
-Dexec.mainClass=org.rdp.googlecloud.StarterPipeline \
-Dexec.args="--project=${PROJECT} \
--stagingLocation=gs://${BUCKET}/staging/ \
--tempLocation=gs://${BUCKET}/temp/ \
--runner=${RUNNER_DATAFLOW}"