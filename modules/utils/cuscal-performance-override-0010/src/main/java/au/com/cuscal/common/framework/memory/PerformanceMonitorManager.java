package au.com.cuscal.common.framework.memory;

import au.com.cuscal.common.framework.performance.PerformanceMonitor;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import org.apache.commons.lang3.time.DurationFormatUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedHashMap;

public class PerformanceMonitorManager implements Serializable {

    public static PerformanceMonitorManager getInstance() {
        if (_instance == null) {
            _instance = new PerformanceMonitorManager();
        }

        return _instance;
    }

    public PerformanceMonitor getMonitor(String key) {
        return this.getMonitor(key, false);
    }

    private PerformanceMonitor getMonitor(String key, boolean withCreate) {
        PerformanceMonitor monitor = this.monitorMap.get(key);

        if (monitor == null && withCreate) {
            monitor = new PerformanceMonitor();

            synchronized(this.monitorMap) {
                this.monitorMap.put(key, monitor);
            }
        }

        return monitor;
    }

    public Priority getDefaultLoggerPriority() {
        return this.defaultLoggerPriority;
    }

    public void setDefaultLoggerPriority(Priority defaultLoggerPriority) {
        this.defaultLoggerPriority = defaultLoggerPriority;
    }

    public void start() {
        this.start("TOTAL");
    }

    public void stop() {
        this.stop("TOTAL");
    }

    public void start(String key) {
        this.getMonitor(key, true).start();
    }

    public void registerLapCount(String key) {
        this.getMonitor(key, true).registerLapCount();
    }

    public void stop(String key) {
        this.getMonitor(key, true).stop();
    }

    public void stopAll() {
        for(String key : this.monitorMap.keySet()) {
            this.getMonitor(key, true).stop();
        }

        this.stop("TOTAL");
    }

    public PerformanceMonitor getTotal() {
        return this.getMonitor("TOTAL", true);
    }

    public void reset() {
        synchronized(this.monitorMap) {
            this.monitorMap.clear();
        }
    }

    public void log() {
        this.log(this.defaultLoggerPriority);
    }

    public void log(Priority loggerPriority) {
        Level level = this.logger.getLevel();

        if (level == null || loggerPriority.isGreaterOrEqual(level)) {
            for(String key : this.monitorMap.keySet()) {
                if (!"TOTAL".equals(key)) {
                    PerformanceMonitor monitor = this.monitorMap.get(key);
                    String msg = this.formatMessage(key, monitor);
                    this.logger.log(loggerPriority, msg);
                }
            }

            PerformanceMonitor monitor = this.monitorMap.get("TOTAL");

            if (monitor != null) {
                String msg = this.formatMessage("TOTAL", monitor);
                this.logger.log(loggerPriority, msg);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        boolean isFirst = true;

        for(String key : this.monitorMap.keySet()) {
            PerformanceMonitor monitor = this.monitorMap.get(key);

            if (isFirst) {
                isFirst = false;
            }
            else {
                sb.append(", ");
            }

            sb.append(key);
            sb.append(" = ");
            sb.append(DurationFormatUtils.formatDurationHMS(monitor.getTotalTime()));
        }

        return sb.toString();
    }

    private PerformanceMonitorManager() {
        this.defaultLoggerPriority = Level.DEBUG;
        this.monitorMap = new LinkedHashMap<>();
    }

    private PerformanceMonitorManager(Logger logger) {
        this.defaultLoggerPriority = Level.DEBUG;
        this.monitorMap = new LinkedHashMap<>();
        this.logger = logger;
    }

    private String formatMessage(String key, PerformanceMonitor monitor) {
        long elapsedTimeMs = monitor.getTotalTime();
        long lapCount = monitor.getLapCount();

        String msg = key + " = " + elapsedTimeMs + "ms (" + DurationFormatUtils.formatDurationHMS(elapsedTimeMs) + ")";

        if (lapCount > 0L) {
            long averageLapTime = elapsedTimeMs / lapCount;
            msg = msg + ", lapCount = " + lapCount + ", averageLapTime = " + averageLapTime + " (" + DurationFormatUtils.formatDurationHMS(averageLapTime) + ")";
        }

        return msg;
    }

    @Serial
    private static final long serialVersionUID = -4798917040491312801L;
    private Logger logger = Logger.getLogger(PerformanceMonitorManager.class);
    private Priority defaultLoggerPriority;
    private final LinkedHashMap<String, PerformanceMonitor> monitorMap;

    private static PerformanceMonitorManager _instance = null;

}
