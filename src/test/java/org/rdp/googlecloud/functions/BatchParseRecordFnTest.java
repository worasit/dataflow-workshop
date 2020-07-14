package org.rdp.googlecloud.functions;

import org.apache.beam.sdk.testing.PAssert;
import org.apache.beam.sdk.testing.TestPipeline;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.joda.time.DateTime;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.rdp.googlecloud.models.Covid19Record;

import java.util.Collections;

@RunWith(JUnit4.class)
public class BatchParseRecordFnTest {

    @Rule
    public TestPipeline pipeline = TestPipeline.create();

    @Test
    public void shouldParseToCovid19() {
        // Arrange
        Iterable<String> rawCovidRecords = Collections.singletonList("2020-03-15,Guam,66,3,3");
        PCollection<String> inputCollection = pipeline.apply(Create.of(rawCovidRecords));
        Covid19Record expectedRecord = Covid19Record
                .builder()
                .date(DateTime.parse("2020-03-15T00:00:00.000+07:00"))
                .stateName("Guam")
                .confirmCases(66)
                .deaths(3)
                .build();

        // Act
        PCollection<Covid19Record> actualPCollection = inputCollection
                .apply(ParDo.of(new BatchParseRecordFn()));

        // Assert
        PAssert.that(actualPCollection)
                .containsInAnyOrder(expectedRecord);

        pipeline.run().waitUntilFinish();
    }
}