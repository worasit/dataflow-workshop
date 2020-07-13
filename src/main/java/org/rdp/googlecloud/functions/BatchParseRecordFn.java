package org.rdp.googlecloud.functions;

import lombok.extern.slf4j.Slf4j;
import org.apache.beam.sdk.transforms.DoFn;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.rdp.googlecloud.models.Covid19Record;

@Slf4j
public class BatchParseRecordFn extends DoFn<String, Covid19Record> {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd");
    private static final String DELIMITOR = ",";
    private static final int LIMIT = -1;

    @ProcessElement
    public void processElement(ProcessContext context) {
        String[] columns = context.element().split(DELIMITOR, LIMIT);
        DateTime date = DateTime.parse(columns[0].trim(), DATE_TIME_FORMATTER);
        String stateName = columns[1].trim();
        int confirmCases = Integer.parseInt(columns[2].trim());
        int deaths = Integer.parseInt(columns[4].trim());

        Covid19Record covid19Record = new Covid19Record(date, stateName, confirmCases, deaths);

        context.output(covid19Record);
    }
}
