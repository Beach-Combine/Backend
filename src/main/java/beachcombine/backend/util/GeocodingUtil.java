package beachcombine.backend.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Component
public class GeocodingUtil {

    @Value("${application.spring.cloud.gcp.geocodingAPI}")
    private String API_KEY;

    // 지오코딩
    public Map<String, String> getCoordsByAddress(String completeAddress) {

        try {
            String surl = "https://maps.googleapis.com/maps/api/geocode/json?address="+ URLEncoder.encode(completeAddress, "UTF-8")+
                    "&key="+URLEncoder.encode(API_KEY, "UTF-8");
            URL url = new URL(surl);
            InputStream is = url.openConnection().getInputStream();

            BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;
            //System.out.println(">>>>>>>>>> >>>>>>>>>> InputStream Start <<<<<<<<<< <<<<<<<<<<");
            while ((inputStr = streamReader.readLine()) != null) {
                //System.out.println(">>>>>>>>>>     "+inputStr);
                responseStrBuilder.append(inputStr);
            }
            //System.out.println(">>>>>>>>>> >>>>>>>>>> InputStream End <<<<<<<<<< <<<<<<<<<<");

            JSONObject jo = new JSONObject(responseStrBuilder.toString());
            JSONArray results = jo.getJSONArray("results");
            Map<String, String> ret = new HashMap<String, String>();
            if(results.length() > 0) {
                JSONObject jsonObject = results.getJSONObject(0);
                Double lat = jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                Double lng = jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                ret.put("lat", lat.toString());
                ret.put("lng", lng.toString());
                //System.out.println("LAT:\t\t"+lat);
                //System.out.println("LNG:\t\t"+lng);

                return ret;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 역 지오코딩
    public String getAddressByCoords(BigDecimal lat, BigDecimal lng) {

        String language = "ko"; // 영어:en, 한국어:ko
        try {
            String surl = "https://maps.googleapis.com/maps/api/geocode/json?latlng="+lat+","+lng+
                    "&key="+URLEncoder.encode(API_KEY, "UTF-8")+
                    "&language="+language+
                    "&result_type=street_address";
            URL url = new URL(surl);
            InputStream is = url.openConnection().getInputStream();

            BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;
            //System.out.println(">>>>>>>>>> >>>>>>>>>> InputStream Start <<<<<<<<<< <<<<<<<<<<");
            while ((inputStr = streamReader.readLine()) != null) {
                //System.out.println(">>>>>>>>>>     "+inputStr);
                responseStrBuilder.append(inputStr);
            }
            //System.out.println(">>>>>>>>>> >>>>>>>>>> InputStream End <<<<<<<<<< <<<<<<<<<<");

            JSONObject jo = new JSONObject(responseStrBuilder.toString());
            JSONArray results = jo.getJSONArray("results");
            if(results.length() > 0) {
                JSONObject jsonObject = results.getJSONObject(0);
                String address = jsonObject.getString("formatted_address");
                //System.out.println("ADDRESS:\t\t"+address);

                return address;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
