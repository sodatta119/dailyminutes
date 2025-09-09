/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 02/09/25
 */
package com.dailyminutes.laundry.integration.tookan.dto.task;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TookanCreateTaskRequest(
        String api_key,
        String order_id,
        String job_description,
        String job_pickup_address,
        String job_pickup_latitude,
        String job_pickup_longitude,
        String job_delivery_address,
        String job_delivery_latitude,
        String job_delivery_longitude,
        String customer_email,
        String customer_username,
        String customer_phone,
        String custom_field_template,
        List<MetaData> meta_data,
        Long team_id,
        String timezone
) {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record MetaData(String label, String data) {}
}
