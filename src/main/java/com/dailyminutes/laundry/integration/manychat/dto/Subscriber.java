/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 31/08/25
 */
package com.dailyminutes.laundry.integration.manychat.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * The type Subscriber.
 */
public record Subscriber(
        String id,
        @JsonProperty("page_id") String pageId,
        @JsonProperty("user_refs") List<String> userRefs,
        String status,
        @JsonProperty("first_name") String firstName,
        @JsonProperty("last_name") String lastName,
        String name,
        String gender,
        @JsonProperty("profile_pic") String profilePic,
        String locale,
        String language,
        String timezone,
        @JsonProperty("live_chat_url") String liveChatUrl,
        @JsonProperty("last_input_text") String lastInputText,
        @JsonProperty("optin_phone") Boolean optinPhone,
        String phone,
        @JsonProperty("optin_email") Boolean optinEmail,
        String email,
        String subscribed,
        @JsonProperty("last_interaction") String lastInteraction,
        @JsonProperty("ig_last_interaction") String igLastInteraction,
        @JsonProperty("last_seen") String lastSeen,
        @JsonProperty("ig_last_seen") String igLastSeen,
        @JsonProperty("is_followup_enabled") Boolean isFollowupEnabled,
        @JsonProperty("ig_username") String igUsername,
        @JsonProperty("ig_id") String igId,
        @JsonProperty("whatsapp_phone") String whatsappPhone,
        @JsonProperty("optin_whatsapp") Boolean optinWhatsapp,
        @JsonProperty("custom_fields") List<CustomField> customFields,
        List<Tag> tags
) {}


