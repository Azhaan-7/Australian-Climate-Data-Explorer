package app;

// Class for subtask general search query results
public class SearchResult {
    // Create String variables for search results
    public String stationId;
    public String date;
    public String metric;
    public String quality;
    public String name;
    public String region;
    public String lat;
    public String avgPeriod1;
    public String avgPeriod2;
   
    public String diffFromRef;
    public String sumEarly;
    public String sumLate;
    public String percentChange;

    // Empty constructor
    public SearchResult() {

    }

    // Constructor with fields
    public SearchResult(String stationId, String date, String metric, String quality) {
        this.stationId = stationId;
        this.date = date;
        this.metric = metric;
        this.quality = quality;
    }
    //second constructor
    public SearchResult(String stationId, String name, String region, String lat, String metric) {
        this.stationId = stationId;
        this.name = name;
        this.metric = metric;
        this.lat = lat;
        this.region = region;
    }
    //third constructor
    public SearchResult(String stationId, String name, String avgPeriod1, String avgPeriod2, String percentChange, String diffFromRef) {
        this.stationId = stationId;
        this.name = name;
        this.avgPeriod1 = avgPeriod1;
        this.avgPeriod2 = avgPeriod2;
        this.percentChange = percentChange;
        this.diffFromRef = diffFromRef;
    }

    // Getter for stationId
    public String getStationId() {
        return stationId;
    }

    // Getter for date
    public String getDate() {
        return date;
    }

    // Getter for metric
    public String getMetric() {
        return metric;
    }

    // Getter for quality
    public String getQuality() {
        return quality;
    }
    public String getRegion() {
        return region;
    }
    public String getName() {
        return name;
    }
    public String getLat() {
        return lat;
    }
        public String getAvgPeriod1() {
        return avgPeriod1;
    }

    public String getAvgPeriod2() {
        return avgPeriod2;
    }

    public String getPercentChange() {
        return percentChange;
    }

    public String getDiffFromRef() {
        return diffFromRef;
    }

    public String getSumEarly() {
        return sumEarly;
    }
    public String getSumLate() {
        return sumLate;
    }
    
}
