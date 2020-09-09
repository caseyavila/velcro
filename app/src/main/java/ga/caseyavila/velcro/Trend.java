package ga.caseyavila.velcro;

import org.json.JSONArray;
import org.json.JSONException;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import lecho.lib.hellocharts.model.AxisValue;

public class Trend {

    private JSONArray trendJSON;

    public Trend(JSONArray trendJSON) {
        this.trendJSON = trendJSON;
    }

    public ArrayList<Long> xTrendValues() {
        ArrayList<Long> values = new ArrayList<>();
        try {
            for (int i = 0; i < trendJSON.length(); i++) {
                //Convert milliseconds to days by dividing by a large number
                values.add(trendJSON.getJSONObject(i).getLong("dayID") / 86400000);
            }
            return values;
        } catch (JSONException e) {
            return null;
        }
    }

    public ArrayList<Float> yTrendValues() {
        ArrayList<Float> values = new ArrayList<>();
        try {
            for (int i = 0; i < trendJSON.length(); i++) {
                values.add(BigDecimal.valueOf(trendJSON.getJSONObject(i).getDouble("score") * 100).floatValue());
            }
            return values;
        } catch (JSONException e) {
            return null;
        }
    }

    public ArrayList<AxisValue> dateLabels() {
        ArrayList<AxisValue> arrayList = new ArrayList<>();
        long difference = getTrendLastDate() - getTrendFirstDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("M/d", Locale.US);
        Date date;
        for (int i = 5; i > 0; i--) {
            long value = (getTrendFirstDate() + (difference * i/5));
            date = new Date(value * 86400000);  //Convert days back to milliseconds for date conversion
            arrayList.add(new AxisValue(value).setLabel(dateFormat.format(date)));
        }
        return arrayList;
    }

    private long getTrendFirstDate() {
        return xTrendValues().get(xTrendValues().size() - 1);
    }

    private long getTrendLastDate() {
        return xTrendValues().get(0);
    }

    public float getTrendMin() {
        return Collections.min(yTrendValues());
    }

    public float getTrendMax() {
        return Collections.max(yTrendValues());
    }

    public float getTrendRange() {
        return Collections.max(yTrendValues()) - Collections.min(yTrendValues());
    }
}
