package com.xrwl.owner.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xrwl.owner.bean.Address;

import java.util.ArrayList;
import java.util.List;

public class AreaDao {

	private AssetsDatabaseManager mg;
	private SQLiteDatabase db;
	private static final String DB_NAME = "china_Province_city_zone.db";
	public static final String T_PROVINCE = "T_Province";
	public static final String T_CITY = "T_City";
	public static final String T_ZONE = "T_Zone";

	public AreaDao(Context context) {

		AssetsDatabaseManager.initManager(context);
		mg = AssetsDatabaseManager.getManager();
		db = mg.getDatabase(DB_NAME);


	}

	public List<Address> getAllProvinces() {

		List<Address> provinces = new ArrayList<>();
		Cursor cursor = db.rawQuery("select 'rowid', * from " + T_PROVINCE + " order by 'rowid'", null);
		while (cursor.moveToNext()) {
			Address province = new Address();
			province.setName(cursor.getString(cursor
					.getColumnIndex("ProName")));
			province.setId(cursor.getInt(cursor.getColumnIndex("ProSort")));
			provinces.add(province);
		}
		cursor.close();
		return provinces;

	}

	public List<Address> getAllCityByProId(int proId) {

		List<Address> citys = new ArrayList<>();
		Cursor cursor = db.rawQuery("select * from " + T_CITY
				+ " where ProID=? order by CitySort", new String[] { proId + "" });
		while (cursor.moveToNext()) {
			Address city = new Address();
			city.setName(cursor.getString(cursor.getColumnIndex("CityName")));
			city.setId(cursor.getInt(cursor.getColumnIndex("CitySort")));
			citys.add(city);
		}
		cursor.close();
		return citys;

	}

	public List<Address> getAllZoneByCityId(int cityId) {

		List<Address > zones = new ArrayList<>();
		Cursor cursor = db.rawQuery("select * from " + T_ZONE
				+ " where CityID=?", new String[] { cityId + "" });
		while (cursor.moveToNext()) {
			Address zone = new Address();
			zone.setName(cursor.getString(cursor.getColumnIndex("ZoneName")));
			zone.setId(cursor.getInt(cursor.getColumnIndex("ZoneID")));
			zones.add(zone);
		}
		cursor.close();
		return zones;
	}

}
