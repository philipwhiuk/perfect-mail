package com.whiuk.philip.perfect.mail.oauth;


import com.whiuk.philip.perfect.mail.MailLibrary;
import com.whiuk.philip.perfect.mail.filter.Base64;
import org.json.JSONException;
import org.json.JSONObject;
import timber.log.Timber;


/**
 * Parses Google's Error/Challenge responses
 * See: https://developers.google.com/gmail/xoauth2_protocol#error_response
 */
public class XOAuth2ChallengeParser {
    public static final String BAD_RESPONSE = "400";


    public static boolean shouldRetry(String response, String host) {
        String decodedResponse = Base64.decode(response);

        if (MailLibrary.isDebug()) {
            Timber.v("Challenge response: %s", decodedResponse);
        }

        try {
            JSONObject json = new JSONObject(decodedResponse);
            String status = json.getString("status");
            if (!BAD_RESPONSE.equals(status)) {
                return false;
            }
        } catch (JSONException jsonException) {
            Timber.e("Error decoding JSON response from: %s. Response was: %s", host, decodedResponse);
        }

        return true;
    }
}
