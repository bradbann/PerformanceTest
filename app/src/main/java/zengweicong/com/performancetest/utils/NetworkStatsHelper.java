package zengweicong.com.performancetest.utils;

import android.app.usage.NetworkStatsManager;

/**
 * Created by zengweicong on 2019-3-26.
 */

public class NetworkStatsHelper {
    NetworkStatsManager networkStatsManager;
    int packageUid;

    public NetworkStatsHelper(NetworkStatsManager networkStatsManager) {
        this.networkStatsManager = networkStatsManager;
    }
}
