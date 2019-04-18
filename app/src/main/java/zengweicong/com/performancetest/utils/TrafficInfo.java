/*
 * Copyright (c) 2012-2013 NetEase, Inc. and other contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package zengweicong.com.performancetest.utils;

import android.content.Context;
import android.net.TrafficStats;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * information of network traffic
 *
 */
public class TrafficInfo {

	private static final String LOG_TAG = "Performance-" + TrafficInfo.class.getSimpleName();
	private static final int UNSUPPORTED = -1;

	private String uid;
	private Context mContext;

	public TrafficInfo(Context context,String uid) {
		this.uid = uid;
		this.mContext = context;
	}

	/**
	 * get total network traffic, which is the sum of upload and download
	 * traffic.
	 * 
	 * @return total traffic include received and send traffic
	 */
	public long getTrafficInfo() {
		Log.i(LOG_TAG, "get traffic information");
		Log.d(LOG_TAG, "uid = " + uid);
		long traffic = trafficFromApi();
		return traffic <= 0 ? trafficFromFiles() : traffic;
	}

	/**
	 * Use TrafficStats getUidRxBytes and getUidTxBytes to get network
	 * traffic,these API return both tcp and udp usage
	 * 
	 * @return
	 */
	private long trafficFromApi() {
		long rcvTraffic = UNSUPPORTED, sndTraffic = UNSUPPORTED;
		rcvTraffic = TrafficStats.getUidRxBytes(Integer.parseInt(uid));
		sndTraffic = TrafficStats.getUidTxBytes(Integer.parseInt(uid));
		return rcvTraffic + sndTraffic < 0 ? UNSUPPORTED : rcvTraffic + sndTraffic;
	}
//	NetworkStatsManager networkStatsManager = (NetworkStatsManager) mContext.getSystemService(NETWORK_STATS_SERVICE) ;
//	public long getPackageTxDayBytesWifi(int packageUid) {
//		NetworkStats networkStats = null;
//		try {
//			networkStats = networkStatsManager.queryDetailsForUid(
//					ConnectivityManager.TYPE_WIFI,
//					"",
//					getTimesmorning(),
//					System.currentTimeMillis(),
//					packageUid);
//		} catch (RemoteException e) {
//			return -1;
//		}
//		NetworkStats.Bucket bucket = new NetworkStats.Bucket();
//		networkStats.getNextBucket(bucket);
//		return bucket.getTxBytes();
//	}
	/**
	 * 获取当天的零点时间
	 *
	 * @return
	 */
//	public static long getTimesmorning() {
//		Calendar cal = Calendar.getInstance();
//		cal.set(Calendar.HOUR_OF_DAY, 0);
//		cal.set(Calendar.SECOND, 0);
//		cal.set(Calendar.MINUTE, 0);
//		cal.set(Calendar.MILLISECOND, 0);
//		return (cal.getTimeInMillis());
//	}
	/**
	 * read files in uid_stat to get traffic info
	 * 
	 * @return
	 */
	private long trafficFromFiles() {
		RandomAccessFile rafRcv = null, rafSnd = null;
		long rcvTraffic = UNSUPPORTED, sndTraffic = UNSUPPORTED;
		String rcvPath,sndPath;
		try {

			if (Build.VERSION.SDK_INT >= 26)
			{
				FlowInfo flowInfo = new FlowInfo();
				//需要申请为系统应用才能获取流量
				flowInfo.setUpKb(TrafficStats.getUidRxBytes(Integer.parseInt(uid)));
				//下载的流量byte
				flowInfo.setDownKb(TrafficStats.getUidTxBytes(Integer.parseInt(uid)));
//				rcvPath = "/proc/net/xt_qtaguid" + uid ;
//				sndPath  = "/proc/net/xt_qtaguid" + uid ;
//				rafRcv = new RandomAccessFile(rcvPath, "r");
//				rafSnd = new RandomAccessFile(sndPath, "r");
				rcvTraffic = flowInfo.getDownKb();
				sndTraffic =flowInfo.getUpKb();
				Log.d(LOG_TAG, String.format("rcvTraffic, sndTraffic = %s, %s", rcvTraffic, sndTraffic));
			}
			else
			{
				rcvPath = "/proc/uid_stat/" + uid + "/tcp_rcv";
				sndPath  = "/proc/uid_stat/" + uid + "/tcp_snd";
				rafRcv = new RandomAccessFile(rcvPath, "r");
				rafSnd = new RandomAccessFile(sndPath, "r");
				rcvTraffic = Long.parseLong(rafRcv.readLine());
				sndTraffic = Long.parseLong(rafSnd.readLine());
				Log.d(LOG_TAG, String.format("rcvTraffic, sndTraffic = %s, %s", rcvTraffic, sndTraffic));
			}

		} catch (Exception e) {
		} 
		finally {
			try {
				if (rafRcv != null) {
					rafRcv.close();
				}
				if (rafSnd != null)
					rafSnd.close();
			} catch (IOException e) {}
		}
		Log.d("rcvTraffic",rcvTraffic+sndTraffic+"");
		return rcvTraffic + sndTraffic < 0 ? UNSUPPORTED : rcvTraffic + sndTraffic;
	}
}
