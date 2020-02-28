# GNSSTest  [![Build Status](https://travis-ci.org/embest/GNSSTest.svg?branch=master)](https://travis-ci.org/embest/GNSSTest)

<img src="app/src/main/ic_launcher-web.png" alt="LOGO" width="200px"/>

GNSSTest is a professional app for the tester, written in Kotlin. It can do most of GPS/GNSS functional and performance test. 

## Features:

### 1. Support Constellation

- GPS (L1 ![](app/src/main/res/drawable-mdpi/gps.png) L5 ![](app/src/main/res/drawable-mdpi/gps_df.png))
- GLONASS ![](app/src/main/res/drawable-mdpi/glonass.png)
- QZSS (L1 ![](app/src/main/res/drawable-mdpi/qzss.png) L5 ![](app/src/main/res/drawable-mdpi/qzss_df.png))
- BeiDou (BeiDou-2 ![](app/src/main/res/drawable-mdpi/beidou.png) BeiDou-3(B2A) ![](app/src/main/res/drawable-mdpi/beidou_df.png))
- Galileo (E1 ![](app/src/main/res/drawable-mdpi/galileo.png) E5 ![](app/src/main/res/drawable-mdpi/galileo_df.png))
- SBAS ![](app/src/main/res/drawable-mdpi/sbas.png)
- NavIC ![](app/src/main/res/drawable-mdpi/irnss_df.png)
- Unknown ![](app/src/main/res/drawable-mdpi/un.png)

### 2. Support Task

- SEND_ EXTRA_COMMAND

	```
		<task>
		    <name>SEND_EXTRA_COMMAND</name>
		    <long>65535</long>
		    <string>delete_aiding_data</string>
		</task>
	```
	long is only for `delete_aiding_data`, 65535 is 0XFFFF for delete all.
	string can be any cmd(`delete_aiding_data`, `force_xtra_injection`, `force_time_injection`) which Android supported. Android API `public boolean sendExtraCommand(String provider, String command, Bundle extras)`
	
	
	Delete Aiding:
	
	```
		GPS_DELETE_EPHEMERIS = 0x0001
		GPS_DELETE_ALMANAC = 0x0002
		GPS_DELETE_POSITION = 0x0004
		GPS_DELETE_TIME = 0x0008
		GPS_DELETE_IONO = 0x0010
		GPS_DELETE_UTC = 0x0020
		GPS_DELETE_HEALTH = 0x0040
		GPS_DELETE_SVDIR = 0x0080
		GPS_DELETE_SVSTEER = 0x0100
		GPS_DELETE_SADATA = 0x0200
		GPS_DELETE_RTI = 0x0400
		GPS_DELETE_CELLDB_INFO = 0x8000
		GPS_DELETE_ALL = 0xFFFF
	```

- REQUEST_ LOCATION_UPDATES

	```
		<task>
		    <name>REQUEST_LOCATION_UPDATES</name>
		    <long>1000</long>
		    <float>0.0</float>
		    <timeout>180000</timeout>
		    <break>true</break>
		</task>
	```
	long is for minTime and float is for minDis, Android API is 
	`public void requestLocationUpdates(String provider, long minTime, float minDistance, LocationListener listener)`

- STOP_REQUEST

	```
	<task>
	    <name>STOP_REQUEST</name>
	</task>
	```
- STANDBY

	```
	<task>
	    <name>STANDBY</name>
	    <timeout>99999999</timeout>
	    <break>true</break>
	</task>
	```
	timeout is ms, 99999999 is for 0~60s randomly.
    
- REQUEST_SINGLE _UPDATES

	```
	<task>
	    <name>REQUEST_SINGLE_UPDATES</name>
	    <timeout>180000</timeout>
	    <break>false</break>
	</task>
	```
	Android API `public void requestSingleUpdate(String provider, LocationListener listener, Looper looper)`

### 3. NMEA Record 

It can record NMEA to /sdcard/GNSSTest/, for example: `MI_8_GNSS_00Tracking_180731_153412_nmea.txt`

### 4. Measurements Record
It can record RAW Measurements to /sdcard/GNSSTest/, for example:`MI_8_GNSS_00Tracking_180731_153651_raw.txt`
This file can opened by Google `GNSS Analysis app`. Link `https://developer.android.com/guide/topics/sensors/gnss`
<img src="https://developer.android.com/images/sensors/gnss_figure_3.png" alt="LOGO" width="600px"/>

### 5. Circular Error Probability
TO BE DO


---

## User Manual

[WIKI](https://github.com/embest/GNSSTest/wiki)
