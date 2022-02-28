package navigationtiming;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Pong;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.JavascriptExecutor;


import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static util.Constants.*;
import static util.WebDriverHolder.getDriver;

@Log4j2
public class PerfNavigationTiming {
    Map<String, Object> timings = null;

    private static FileWriter fileWriter;

    private final String javascriptForPerformance = "var timings = performance.timing || {}; return timings;";

    private final String javaScriptForPerformanceInternetExplorer = "return {performance:JSON.stringify(performance.timing)}";

    InfluxDB influxDB = InfluxDBFactory.connect(DATABASE_URL);

    BatchPoints batchPoints = BatchPoints
            .database(DATABASE_NAME)
            .retentionPolicy("autogen")
            .build();

    public boolean checkDBConnection() {
        influxDB.setLogLevel(InfluxDB.LogLevel.BASIC);
        Pong response = this.influxDB.ping();
        if(response.getVersion().equalsIgnoreCase("unknown")) {
            log.debug("Error pinging server.");
            return false;
        } else {
            log.debug("Connection is established");
            return true;
        }
    }

    public void writeToInflux(String pageName) {
        getAllTiming();
            writeMetricsToJsonFile(pageName, this.getLatency(), this.getTimeToInteract(),
                    this.getTimeToLoad(), this.getOnLoad(), this.getTotal_time());
            return;


//        if (IS_INFLUX_DISABLED) {
//            writeMetricsToJsonFile(pageName, this.getLatency(), this.getTimeToInteract(),
//                    this.getTimeToLoad(), this.getOnLoad(), this.getTotal_time());
//            return;
//        }

//        log.debug("This is Navigation Timing Case");
//
//        influxDB.setLogLevel(InfluxDB.LogLevel.BASIC);
//        org.influxdb.dto.Point point = Point.measurement("clientSide")
//                .time(System.currentTimeMillis(), TimeUnit.MICROSECONDS)
//                .tag("projectName", PROJECT_NAME)
//                .tag("scenario", SCENARIO_NAME)
//                .tag("env", ENV_NAME)
//                .tag("browser", BROWSER_NAME)
//                .tag("page", pageName)
//                .tag("periodicity", PERIODICITY)
//                .tag("periodicity_comment", VERSION)
//                .tag("buildID", BUILD_ID)
//                .addField("latency", this.getLatency())
//                .addField("backend_response", this.getBackend_response())
//                .addField("tti", this.getTimeToInteract())
//                .addField("ttl", this.getTimeToLoad())
//                .addField("onLoad", this.getOnLoad())
//                .addField("total_time", this.getTotal_time())
//                .build();
//        batchPoints.point(point);
//        influxDB.write(batchPoints);
    }

    public Map<String, Object> getAllTiming() {
        JavascriptExecutor jsrunner = (JavascriptExecutor) getDriver();
//        if ("InternetExplorer".equalsIgnoreCase(BROWSER_NAME)) {
//            Map<String, Object> ieTimings = (Map<String, Object>) jsrunner.executeScript(javaScriptForPerformanceInternetExplorer);
//            timings = parseNavigationTimingDataFromIe(ieTimings);
//        } else {
            timings = (Map<String, Object>) jsrunner.executeScript(javascriptForPerformance);
//        }
        return timings;
    }

    private Map<String, Object> parseNavigationTimingDataFromIe(Map<String, Object> ieTimings) {
        Map<String, Object> parsedTimings = new HashMap<>();
        String mapValue = (String) ieTimings.get("performance");
        mapValue = mapValue.substring(1, mapValue.length()-1).replace("\"", "");
        String[] keyValuePairs = mapValue.split(",");

        for (String pair : keyValuePairs) {
            String[] entry = pair.split(":");
            parsedTimings.put(entry[0].trim(), Long.valueOf(entry[1]));
        }

        return parsedTimings;
    }

    @SneakyThrows
    private void writeMetricsToJsonFile(String pageName, long latency, long tti, long ttl, long onload, long totalTime) {
        log.info("Writing data to the new JSON file, dedicated to store performance results data");
        ObjectMapper mapper = new ObjectMapper();
        JSONObject jsonObject;
        jsonObject = PERF_METRICS_JSON.exists() ? mapper.readValue(PERF_METRICS_JSON, JSONObject.class) : new JSONObject();
        JSONArray entityArray = new JSONArray();
        JSONObject innerJsonObject = new JSONObject();
        innerJsonObject.put("latency", latency);
        innerJsonObject.put("tti", tti);
        innerJsonObject.put("ttl", ttl);
        innerJsonObject.put("onload", onload);
        innerJsonObject.put("total_time", totalTime);

        entityArray.add(innerJsonObject);
        jsonObject.put(pageName, entityArray);

        log.info("JSON Object: " + jsonObject);

        try {
            fileWriter = new FileWriter(PERF_METRICS_JSON);
            fileWriter.write(jsonObject.toJSONString());
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public Long getAnTime(String name) {
        return (Long) timings.get((Object) name);
    }

    public Long getnavigationStart() {
        return getAnTime("navigationStart");
    }

    public Long getunloadEventStart() {
        return getAnTime("unloadEventStart");
    }

    public Long getunloadEventEnd() {
        return getAnTime("unloadEventEnd");
    }

    public Long getredirectStart() {
        return getAnTime("redirectStart");
    }

    public Long getredirectEnd() {
        return getAnTime("redirectEnd");
    }

    public Long getfetchStart() {
        return getAnTime("fetchStart");
    }

    public Long getdomInLookupStart() {
        return getAnTime("domInLookupStart");
    }

    public Long getdomInLookupEnd() {
        return getAnTime("domInLookupEnd");
    }

    public Long getconnectStart() {
        return getAnTime("connectStart");
    }

    public Long getconnectEnd() {
        return getAnTime("connectEnd");
    }

    public Long getsecureConnectionStart() {
        return getAnTime("secureConnectionStart");
    }

    public Long getrequestStart() {
        return getAnTime("requestStart");
    }

    public Long getresponseStart() {
        return getAnTime("responseStart");
    }

    public Long getresponseEnd() {
        return getAnTime("responseEnd");
    }

    public Long getdomLoading() {
        return getAnTime("domLoading");
    }

    public Long getdomInteractive() {
        return getAnTime("domInteractive");
    }

    public Long getdomContentLoadedEventStart() {
        return getAnTime("domContentLoadedEventStart");
    }

    public Long getdomContentLoadedEventEnd() {
        return getAnTime("domContentLoadedEventEnd");
    }

    public Long getdomComplete() {
        return getAnTime("domComplete");
    }

    public Long getloadEventStart() {
        return getAnTime("loadEventStart");
    }

    public Long getloadEventEnd() {
        return getAnTime("loadEventEnd");
    }

    // results
    public long getLatency() {
        return getresponseStart() - getnavigationStart();
    }

    public long getBackend_response() {
        return getresponseEnd() - getresponseStart();
    }

    public long getTimeToInteract() {
        return getdomInteractive() - getdomLoading();
    }

    public long getTimeToLoad() {
        return getdomComplete() - getdomInteractive();
    }

    public long getOnLoad() {
        return getloadEventEnd() - getloadEventStart();
    }

    public long getTotal_time() {
        return getloadEventEnd() - getnavigationStart();
    }
}
