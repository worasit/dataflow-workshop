package org.rdp.googlecloud.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Covid19Record implements Serializable {
    DateTime date;
    String stateName;
    int confirmCases;
    int deaths;
}
