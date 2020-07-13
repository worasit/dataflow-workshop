package org.rdp.googlecloud.functions;

import lombok.extern.slf4j.Slf4j;
import org.apache.beam.sdk.transforms.DoFn;

/**
 * DoFn LifeCycle https://beam.apache.org/documentation/programming-guide/#pcollections
 */
@Slf4j
public class BatchPrintRecordFn extends DoFn<String, String> {
    @Setup
    public void setUp() {
        log.info("Setup was called");
    }

    @StartBundle
    public void startBundle() {
        log.info("StartBundle was called");
    }

    @ProcessElement
    public void processElement(ProcessContext context) {
        log.info("record value: " + context.element());
        context.output(context.element());
    }

    @FinishBundle
    public void finishBundle() {
        log.info("finishBundle was called");
    }

    @Teardown
    public void tearDown() {
        log.info("TearDown was called");
    }
}
