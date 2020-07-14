package org.rdp.googlecloud.transforms;

import lombok.extern.slf4j.Slf4j;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.metrics.Counter;
import org.apache.beam.sdk.metrics.Metrics;
import org.apache.beam.sdk.transforms.*;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.rdp.googlecloud.functions.BatchParseRecordFn;
import org.rdp.googlecloud.functions.BatchPrintRecordFn;
import org.rdp.googlecloud.models.Covid19Record;
import org.rdp.googlecloud.options.BatchPipelineOptions;

@Slf4j
public class BatchTransform extends PTransform<PCollection<String>, PCollection<KV<String, Integer>>> {

    private final Counter zeroDeaths = Metrics.counter(BatchTransform.class, "zero_deaths_records");
    private final Counter totalRecords = Metrics.counter(BatchTransform.class, "total_records");

    @Override
    public PCollection<KV<String, Integer>> expand(PCollection<String> input) {
        PCollection<String> records = input
                .apply("Print a Record", ParDo.of(new BatchPrintRecordFn()));


        PCollection<Covid19Record> covid19Records = records
                .apply("Convert to Covid19Record object", ParDo.of(new BatchParseRecordFn()));


        String outputFile = input.getPipeline().getOptions().as(BatchPipelineOptions.class).getOutputFile();
        covid19Records
                .apply("Filter 0 deaths record", Filter.by((Covid19Record covid19Record) -> {
                    if (covid19Record.getDeaths() == 0) {
                        log.info("invalid record: " + covid19Record);
                        zeroDeaths.inc();
                    }
                    return covid19Record.getDeaths() == 0;
                }))
                .apply(MapElements.via(new SimpleFunction<Covid19Record, String>() {
                    @Override
                    public String apply(Covid19Record input) {
                        return input.toString();
                    }
                }))
                .apply("Write to file in GCS", TextIO.write().to(outputFile));


        return covid19Records
                .apply("Filter deaths records",
                        Filter.by((Covid19Record covid19Record) -> {
                            totalRecords.inc();
                            if (covid19Record.getDeaths() > 0) log.info("valid record: " + covid19Record);
                            return covid19Record.getDeaths() > 0;
                        }))
                .apply("Map to key-value object",
                        ParDo.of(new DoFn<Covid19Record, KV<String, Integer>>() {
                            @ProcessElement
                            public void processElement(ProcessContext context) {
                                Covid19Record element = context.element();
                                context.output(KV.of(element.getStateName(), element.getDeaths()));
                            }
                        }))
                .apply("Aggregate results and sum total deaths",
                        Sum.integersPerKey())
                .apply("Print total deaths records", ParDo.of(new DoFn<KV<String, Integer>, KV<String, Integer>>() {
                    @ProcessElement
                    public void processElement(ProcessContext context) {
                        KV<String, Integer> element = context.element();
                        log.info(String.format("total death record K: %s V: %d", element.getKey(), element.getValue()));
                        context.output(element);
                    }
                }));
    }
}
