package org.rdp.googlecloud.options;

import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.Validation;

public interface BatchPipelineOptions extends PipelineOptions {
    @Description("Input file location`covid19_us_states`")
    @Default.String("gs://your-bucket/covid19_us_states.csv")
    String getInputFile();

    void setInputFile(String value);


    @Description("Input file type")
    @Default.String("gs://your-bucket/invalid_record.csv")
    String getOutputFile();

    void setOutputFile(String value);

    @Validation.Required
    @Description("Bigquery table to save processing result")
    @Default.String("your-project-id:data-set.table")
    String getOutputTable();

    void setOutputTable(String value);

}