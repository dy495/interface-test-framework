package com.haisheng.framework.dao;

import com.haisheng.framework.model.bean.Capture;
import com.haisheng.framework.model.bean.CaptureRatio;
import com.haisheng.framework.model.bean.PVUV;
import com.haisheng.framework.model.bean.PVUVAccuracy;

import java.util.List;

/**
 * Created by yuhaisheng
 */

public interface ICaptureDao {

    int insert(Capture pvuv);

    List<CaptureRatio> getCaptureRatioByDay(String day);
}
