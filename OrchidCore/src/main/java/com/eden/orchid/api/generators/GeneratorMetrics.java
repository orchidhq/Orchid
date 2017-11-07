package com.eden.orchid.api.generators;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.LongConsumer;

@Getter @Setter
public class GeneratorMetrics {

    private String key;

    private long indexingStartTime;
    private long generatingStartTime;

    private long indexingEndTime;
    private long generatingEndTime;

    private List<Long> pageGenerationTimes;

    public GeneratorMetrics(String key) {
        this.key = key;
        pageGenerationTimes = new ArrayList<>();
    }

    void startIndexing() {
        indexingStartTime = System.currentTimeMillis();
    }

    void startGenerating() {
        generatingStartTime = System.currentTimeMillis();
    }

    void stopIndexing() {
        indexingEndTime = System.currentTimeMillis();
    }

    void stopGenerating() {
        generatingEndTime = System.currentTimeMillis();
    }

    void addPageGenerationTime(long millis) {
        pageGenerationTimes.add(millis);
    }

    void addAllGenerationTimes(List<Long> millis) {
        pageGenerationTimes.addAll(millis);
    }

// Get formatted values
//----------------------------------------------------------------------------------------------------------------------

    private String makeMillisReadable(double lMillis) {
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        int millis = 0;

        String sTime;

        seconds = (int)(lMillis / 1000) % 60;
        millis = (int)(lMillis % 1000);

        if (seconds > 0) {
            minutes = (int)(lMillis / 1000 / 60) % 60;
            if (minutes > 0) {
                hours = (int)(lMillis / 1000 / 60 / 60) % 24;
                if (hours > 0) {
                    sTime = hours + "h " + minutes + "m " + seconds + "s " + millis + "ms";
                } else {
                    sTime = minutes + "m " + seconds + "s " + millis + "ms";
                }
            } else {
                sTime = seconds + "s " + millis + "ms";
            }
        } else {
            sTime = millis + "ms";
        }

        return sTime;
    }

    String getIndexingTime() {
        return makeMillisReadable(indexingEndTime - indexingStartTime);
    }

    String getGeneratingTime() {
        return makeMillisReadable(generatingEndTime - generatingStartTime);
    }

    String getMeanPageTime() {
        if(pageGenerationTimes.size() > 0) {
            return makeMillisReadable(pageGenerationTimes
                    .stream()
                    .collect(Averager::new, Averager::accept, Averager::combine)
                    .average());
        }
        else {
            return "N/A";
        }
    }

    String getMedianPageTime() {
        if(pageGenerationTimes.size() > 0) {
            pageGenerationTimes.sort(Comparator.naturalOrder());
            return makeMillisReadable(pageGenerationTimes.get((int) Math.floor(pageGenerationTimes.size()/2)));
        }
        else {
            return "N/A";
        }
    }

    static class Averager implements LongConsumer {
        private long total = 0;
        private long count = 0;

        public double average() {
            return count > 0 ? ((double) total)/count : 0;
        }
        public void accept(long i) { total += i; count++; }
        public void combine(Averager other) {
            total += other.total;
            count += other.count;
        }
    }


}
