package org.rdp.googlecloud.models;

import lombok.*;
import org.joda.time.DateTime;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class Covid19Record implements Serializable {
    DateTime date;
    String stateName;
    int confirmCases;
    int deaths;
}
