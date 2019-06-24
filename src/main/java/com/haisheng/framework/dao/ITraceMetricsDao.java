package com.haisheng.framework.dao;

import com.haisheng.framework.model.bean.Capture;
import com.haisheng.framework.model.bean.CaptureRatio;
import com.haisheng.framework.model.bean.TraceMetrics;

import java.util.List;

/**
 * Created by yuhaisheng
 */

public interface ITraceMetricsDao {

    List<TraceMetrics> getTraceMetricsByDay(String day, int limit);
}
