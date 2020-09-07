package ga.caseyavila.velcro;

import org.json.JSONArray;
import org.json.JSONException;

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

    public ArrayList<Float> xTrendValues() {
        ArrayList<Float> values = new ArrayList<>();
        try {
            for (int i = 0; i < trendJSON.length(); i++) {
                //Convert milliseconds to days by multiplying by a large number
                values.add(Float.parseFloat(trendJSON.getJSONObject(i).getString("dayID")) / 86400000);
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
                values.add(Float.parseFloat(trendJSON.getJSONObject(i).getString("score")) * 100);
            }
            return values;
        } catch (JSONException e) {
            return null;
        }
    }

    public ArrayList<AxisValue> dateLabels() {
        ArrayList<AxisValue> arrayList = new ArrayList<>();
        float difference = getTrendLastDate() - getTrendFirstDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("M/d", Locale.US);
        Date date;
        for (int i = 5; i > 0; i--) {
            Float value = (getTrendFirstDate() + (difference * i/5));
            date = new Date(value.longValue() * 86400000);  //Convert days back to milliseconds for date conversion
            arrayList.add(new AxisValue(value).setLabel(dateFormat.format(date)));
        }
        return arrayList;
    }

    private Float getTrendFirstDate() {
        return xTrendValues().get(xTrendValues().size() - 1);
    }

    private Float getTrendLastDate() {
        return xTrendValues().get(0);
    }

    public Float getTrendMin() {
        return Collections.min(yTrendValues());
    }

    public Float getTrendMax() {
        return Collections.max(yTrendValues());
    }

    public Float getTrendRange() {
        Float maxValue = Collections.max(yTrendValues());
        Float minValue = Collections.min(yTrendValues());
        return maxValue - minValue;
    }
}
