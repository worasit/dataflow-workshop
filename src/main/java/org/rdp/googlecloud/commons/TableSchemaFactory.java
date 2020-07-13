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


    public static TableSchema getTaxiRidesTableSchema() {
        return new TableSchema()
                .setFields(
                        ImmutableList.of(
                                new TableFieldSchema()
                                        .setName("ride_id")
                                        .setType("STRING")
                                        .setMode("NULLABLE"),
                                new TableFieldSchema()
                                        .setName("point_idx")
                                        .setType("INTEGER")
                                        .setMode("NULLABLE"),
                                new TableFieldSchema()
                                        .setName("latitude")
                                        .setType("FLOAT")
                                        .setMode("NULLABLE"),
                                new TableFieldSchema()
                                        .setName("longitude")
                                        .setType("FLOAT")
                                        .setMode("NULLABLE"),
                                new TableFieldSchema()
                                        .setName("timestamp")
                                        .setType("TIMESTAMP")
                                        .setMode("NULLABLE"),
                                new TableFieldSchema()
                                        .setName("meter_reading")
                                        .setType("FLOAT")
                                        .setMode("NULLABLE"),
                                new TableFieldSchema()
                                        .setName("meter_increment")
                                        .setType("FLOAT")
                                        .setMode("NULLABLE"),
                                new TableFieldSchema()
                                        .setName("ride_status")
                                        .setType("STRING")
                                        .setMode("NULLABLE"),
                                new TableFieldSchema()
                                        .setName("passenger_count")
                                        .setType("INTEGER")
                                        .setMode("NULLABLE")
                        )
                );
    }
}
