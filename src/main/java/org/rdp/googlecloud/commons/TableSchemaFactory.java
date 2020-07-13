package org.rdp.googlecloud.commons;

import com.google.api.services.bigquery.model.TableFieldSchema;
import com.google.api.services.bigquery.model.TableSchema;
import org.apache.beam.vendor.grpc.v1p26p0.com.google.common.collect.ImmutableList;

public class TableSchemaFactory {
    public static TableSchema getDeathsPerStateTableSchema() {
        return new TableSchema()
                .setFields(
                        ImmutableList.of(
                                new TableFieldSchema()
                                        .setName("state_name")
                                        .setType("STRING")
                                        .setMode("NULLABLE"),
                                new TableFieldSchema()
                                        .setName("total_deaths")
                                        .setType("INTEGER")
                                        .setMode("NULLABLE")
                        )
                );
    }
}
