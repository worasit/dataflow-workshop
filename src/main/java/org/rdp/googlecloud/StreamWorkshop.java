package org.rdp.googlecloud;

import lombok.extern.slf4j.Slf4j;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.ParDo;
import org.rdp.googlecloud.commons.TableSchemaFactory;
import org.rdp.googlecloud.functions.StreamParseMessageFn;
import org.rdp.googlecloud.options.StreamPipelineOptions;

@Slf4j
public class StreamWorkshop {


    public static void main(String[] args) {
        log.info("Batch Workshop is running...");
        log.info("GOOGLE_APPLICATION_CREDENTIALS: " + System.getenv("GOOGLE_APPLICATION_CREDENTIALS"));

        StreamPipelineOptions streamPipelineOptions = PipelineOptionsFactory
                .fromArgs(args)
                .withValidation()
                .as(StreamPipelineOptions.class);

        Pipeline pipeline = Pipeline.create(streamPipelineOptions);

        pipeline
                .apply("1.) Consume messages from PubSub",
                        PubsubIO.readStrings().fromSubscription(streamPipelineOptions.getInputSubscription()))
                .apply("2.) Convert to BigQuery's row", ParDo.of(new StreamParseMessageFn()))
                .apply("3.) Write to BigQuery",
                        BigQueryIO.writeTableRows()
                                .to(streamPipelineOptions.getOutputTable())
                                .withSchema(TableSchemaFactory.getTaxiRidesTableSchema())
                                .withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_IF_NEEDED)
                                .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND)
                );

        pipeline.run();
    }
}
