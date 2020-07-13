package org.rdp.googlecloud.functions;


import com.google.api.services.bigquery.model.TableRow;
import org.apache.beam.sdk.coders.Coder.Context;
import org.apache.beam.sdk.io.gcp.bigquery.TableRowJsonCoder;
import org.apache.beam.sdk.transforms.DoFn;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class StreamParseMessageFn extends DoFn<String, TableRow> {
    @ProcessElement
    public void processElement(ProcessContext context) {
        TableRow row = convertJsonToTableRow(context.element());
        context.output(row);
    }

    private static TableRow convertJsonToTableRow(String json) {
        TableRow row;
        try (InputStream inputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8))) {
            row = TableRowJsonCoder.of().decode(inputStream, Context.OUTER);
        } catch (IOException e) {
            throw new RuntimeException("Failed to serialize json to table row: " + json, e);
        }
        return row;
    }
}
