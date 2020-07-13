/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.rdp.googlecloud;

import com.google.api.services.bigquery.model.TableRow;
import lombok.extern.slf4j.Slf4j;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.SimpleFunction;
import org.apache.beam.sdk.values.KV;
import org.rdp.googlecloud.commons.TableSchemaFactory;
import org.rdp.googlecloud.options.BatchPipelineOptions;
import org.rdp.googlecloud.transforms.BatchTransform;

@Slf4j
public class BatchWorkshop {
    public static void main(String[] args) {
        log.info("Batch Workshop is running...");
        log.info("GOOGLE_APPLICATION_CREDENTIALS: " + System.getenv("GOOGLE_APPLICATION_CREDENTIALS"));

        BatchPipelineOptions batchPipelineOptions = PipelineOptionsFactory
                .fromArgs(args)
                .withValidation()
                .as(BatchPipelineOptions.class);

        Pipeline pipeline = Pipeline.create(batchPipelineOptions);


        pipeline
                .apply(
                        String.format("1.) Read file from GCS  %s", batchPipelineOptions.getInputFile()),
                        TextIO.read().from(batchPipelineOptions.getInputFile()))
                .apply(
                        "2.) Process covid19 records by calculating total deaths per state",
                        new BatchTransform())
                .apply("3.) Maps to BigQuery TableRow",
                        MapElements.via(new SimpleFunction<KV<String, Integer>, TableRow>() {
                            @Override
                            public TableRow apply(KV<String, Integer> input) {
                                TableRow row = new TableRow();
                                row.set("state_name", input.getKey());
                                row.set("total_deaths", input.getValue());
                                return row;
                            }
                        }))
                .apply("4.) Write to BigQuery",
                        BigQueryIO.writeTableRows()
                                .to(batchPipelineOptions.getOutputTable())
                                .withSchema(TableSchemaFactory.getDeathsPerStateTableSchema())
                                .withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_IF_NEEDED)
                                .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_TRUNCATE)
                );


        pipeline.run().waitUntilFinish();
    }
}
