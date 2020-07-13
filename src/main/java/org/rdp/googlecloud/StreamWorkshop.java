package org.rdp.googlecloud;

import com.google.api.services.bigquery.model.TableRow;
import lombok.extern.slf4j.Slf4j;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.coders.Coder;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.io.gcp.bigquery.TableRowJsonCoder;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.rdp.googlecloud.commons.TableSchemaFactory;
import org.rdp.googlecloud.options.StreamPipelineOptions;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

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
                .apply("2.) Convert to BigQuery's row", ParDo.of(new DoFn<String, TableRow>() {
                    @ProcessElement
                    public void processElement(ProcessContext context) {
                        TableRow row = convertJsonToTableRow(context.element());
                        context.output(row);
                    }
                }))
                .apply("3.) Write to BigQuery",
                        BigQueryIO.writeTableRows()
                                .to(streamPipelineOptions.getOutputTable())
                                .withSchema(TableSchemaFactory.getTaxiRidesTableSchema())
                                .withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_IF_NEEDED)
                                .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND)
                );


        pipeline.run();
    }


    private static TableRow convertJsonToTableRow(String json) {
        TableRow row;
        // Parse the JSON into a {@link TableRow} object.
        try (InputStream inputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8))) {
            row = TableRowJsonCoder.of().decode(inputStream, Coder.Context.OUTER);
        } catch (IOException e) {
            throw new RuntimeException("Failed to serialize json to table row: " + json, e);
        }
        return row;
    }
}
