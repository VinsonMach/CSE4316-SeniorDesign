package vib.track.cerberus.network;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import vib.track.cerberus.data.model.HistoryPageEvent;

public interface HistoryPageAPI {
    @GET("/history/event")
    Call<List<HistoryPageEvent>> getEvent();
}
