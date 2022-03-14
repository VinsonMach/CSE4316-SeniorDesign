package vib.track.cerberus.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import vib.track.cerberus.data.EventsData;

public interface HistoryPageAPI {
    @GET("/ring/event")
    Call<List<EventsData>> showEvents();
}
