package cc.inflite.typeone.karoo.formatters;

import android.content.Context;

import androidx.annotation.NonNull;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import cc.inflite.typeone.data.SGVServiceData;
import cc.inflite.typeone.service.BackgroundServiceClient;
import io.hammerhead.sdk.v0.SdkContext;
import io.hammerhead.sdk.v0.datatype.formatter.SdkFormatter;

public class DataTimesinceFormatter extends SdkFormatter {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.systemDefault());



    private final SdkContext context;

    public DataTimesinceFormatter(SdkContext context) {
        this.context = context;
    }

    @NonNull
    @Override
    public String formatValue(double v) {
        SGVServiceData data = BackgroundServiceClient.DEFAULT.getValue().getSGVData(context);

        if (data.isError()) {
            if (data.getErrorMessage() != null &&
                    data.getErrorMessage().contains("Unable to resolve host")){
                return "NO INTERNET";
            } else {
                return "ERROR";
            }
        }

        if (data.getData() == null) {
            return "N/A";
        }

        // GET CURRENT TIME - TIMESTAMP OF DATA - 1 HOUR
        return DATE_TIME_FORMATTER.format(Instant.ofEpochMilli(System.currentTimeMillis()-data.getData().getDate()-3600000));
    }
}