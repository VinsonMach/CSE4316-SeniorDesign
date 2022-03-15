package vib.track.cerberus.data;

import com.google.gson.annotations.SerializedName;

public class RingLoginResponse {
    @SerializedName("restClient")
    private String restClient;

    public String getRestClient() {
        return restClient;
    }
}
