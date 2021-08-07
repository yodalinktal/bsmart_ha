package bsmart.technology.rru.base.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bsmart.technology.rru.base.utils.BSmartUtil;

public class TableUtils {

    //表名。
    public static final String TABLE_NAME = "p_log";

    public static final String POSITION_i_cmd_ref_id = "i_cmd_ref_id";
    public static final String POSITION_v_bat_id = "v_bat_id";
    public static final String POSITION_i_event_id = "i_event_id";
    public static final String POSITION_d_gps_time_stamp = "d_gps_time_stamp";
    public static final String POSITION_i_gps_time_stamp = "i_gps_time_stamp";
    public static final String POSITION_n_latitude = "n_latitude";
    public static final String POSITION_n_longitude = "n_longitude";
    public static final String POSITION_i_altitude = "i_altitude";
    public static final String POSITION_i_unit_status = "i_unit_status";
    public static final String POSITION_i_gsm_cell_id = "i_gsm_cell_id";
    public static final String POSITION_n_horizontal_velocity = "n_horizontal_velocity";
    public static final String POSITION_n_vertical_velocity = "n_vertical_velocity";
    public static final String POSITION_n_direction = "n_direction";
    public static final String POSITION_i_mode = "i_mode";
    public static final String POSITION_v_gsm_roaming = "v_gsm_roaming";
    public static final String POSITION_i_gsm_bcch = "i_gsm_bcch";
    public static final String POSITION_i_sv = "i_sv";
    public static final String POSITION_i_gsm_lac_id = "i_gsm_lac_id";
    public static final String POSITION_i_download_channel = "i_download_channel";
    public static final String POSITION_v_imei_no = "v_imei_no";
    public static final String POSITION_v_reg_no = "v_reg_no";
    public static final String POSITION_n_int_power = "n_int_power";
    public static final String POSITION_n_ext_power = "n_ext_power";
    public static final String POSITION_n_hdop = "n_hdop";

    //创建数据库表的SQL语句。
    public static String sql_create_position_table = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
            + " ("
            + POSITION_i_cmd_ref_id + " integer primary key autoincrement,"
            + POSITION_v_bat_id + " text,"
            + POSITION_i_event_id + " text,"
            + POSITION_d_gps_time_stamp + " text,"
            + POSITION_i_gps_time_stamp + " long,"
            + POSITION_n_latitude + " double,"
            + POSITION_n_longitude + " double,"
            + POSITION_i_altitude + " integer,"
            + POSITION_i_unit_status + " integer,"
            + POSITION_i_gsm_cell_id + " integer,"
            + POSITION_n_horizontal_velocity + " double,"
            + POSITION_n_vertical_velocity + " double,"
            + POSITION_n_direction + " double,"
            + POSITION_i_mode + " double,"
            + POSITION_v_gsm_roaming + " text,"
            + POSITION_i_gsm_bcch + " integer,"
            + POSITION_i_sv + " integer,"
            + POSITION_i_gsm_lac_id + " integer,"
            + POSITION_i_download_channel + " integer,"
            + POSITION_v_imei_no + " text,"
            + POSITION_v_reg_no + " text,"
            + POSITION_n_int_power + " integer,"
            + POSITION_n_ext_power + " integer,"
            + POSITION_n_hdop + " double"
            +")";

    public static ContentValues getContentPositionValues(String v_bat_id,
                                                         Double latitude,Double longitude,Integer altitude,
                                                         Long gspTimestamp,Float speed,Float vertical,Float degree,
                                                         int cellId,int dbm,int batteryLevel,Float n_hdop,Integer sv,
                                                         String imei,String accountId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(POSITION_v_bat_id,v_bat_id);
        contentValues.put(POSITION_i_event_id,7);
        contentValues.put(POSITION_d_gps_time_stamp, BSmartUtil.getGPSTime(gspTimestamp));
        contentValues.put(POSITION_i_gps_time_stamp, gspTimestamp);
        contentValues.put(POSITION_n_latitude,latitude);
        contentValues.put(POSITION_n_longitude,longitude);
        contentValues.put(POSITION_i_altitude,altitude);
        contentValues.put(POSITION_i_unit_status,2050);
        contentValues.put(POSITION_i_gsm_cell_id,cellId);
        contentValues.put(POSITION_n_horizontal_velocity,speed);
        contentValues.put(POSITION_n_vertical_velocity,vertical);
        contentValues.put(POSITION_n_direction,degree);
        contentValues.put(POSITION_i_mode,4);
        contentValues.put(POSITION_v_gsm_roaming,"Roaming");
        contentValues.put(POSITION_i_gsm_bcch,dbm);
        contentValues.put(POSITION_i_sv,sv);
        contentValues.put(POSITION_i_gsm_lac_id,4056);
        contentValues.put(POSITION_i_download_channel,1);
        contentValues.put(POSITION_v_imei_no,imei);
        contentValues.put(POSITION_v_reg_no,accountId);
        contentValues.put(POSITION_n_int_power,batteryLevel);
        contentValues.put(POSITION_n_ext_power,batteryLevel);
        contentValues.put(POSITION_n_hdop,n_hdop);
        return contentValues;
    }

    public static void remove5kData(SQLiteDatabase db){
        db.delete(TABLE_NAME,null,null);
    }

    public static JSONArray get5KData(SQLiteDatabase db){

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME +" ORDER BY "+POSITION_i_gps_time_stamp+" DESC LIMIT 5000", null);
        if (cursor != null){
            Log.d("CheckCaseService","cursor.size:"+cursor.getCount());
        }
        if (cursor != null && cursor.getCount() > 0) {
            JSONArray array = new JSONArray();
            while (cursor.moveToNext()) {
                try{

                    JSONObject json = new JSONObject();
                    json.put(POSITION_i_cmd_ref_id,cursor.getInt(cursor.getColumnIndex(POSITION_i_cmd_ref_id)));
                    json.put(POSITION_v_bat_id,cursor.getString(cursor.getColumnIndex(POSITION_v_bat_id)));
                    json.put(POSITION_i_event_id,cursor.getInt(cursor.getColumnIndex(POSITION_i_event_id)));
                    json.put(POSITION_d_gps_time_stamp,cursor.getString(cursor.getColumnIndex(POSITION_d_gps_time_stamp)));
                    json.put(POSITION_i_gps_time_stamp,cursor.getLong(cursor.getColumnIndex(POSITION_i_gps_time_stamp)));
                    json.put(POSITION_n_latitude,cursor.getString(cursor.getColumnIndex(POSITION_n_latitude)));
                    json.put(POSITION_n_longitude,cursor.getString(cursor.getColumnIndex(POSITION_n_longitude)));
                    json.put(POSITION_i_altitude,cursor.getString(cursor.getColumnIndex(POSITION_i_altitude)));
                    json.put(POSITION_i_unit_status,cursor.getString(cursor.getColumnIndex(POSITION_i_unit_status)));
                    json.put(POSITION_i_gsm_cell_id,cursor.getString(cursor.getColumnIndex(POSITION_i_gsm_cell_id)));
                    json.put(POSITION_n_horizontal_velocity,cursor.getString(cursor.getColumnIndex(POSITION_n_horizontal_velocity)));
                    json.put(POSITION_n_vertical_velocity,cursor.getString(cursor.getColumnIndex(POSITION_n_vertical_velocity)));
                    json.put(POSITION_n_direction,cursor.getString(cursor.getColumnIndex(POSITION_n_direction)));
                    json.put(POSITION_i_mode,cursor.getString(cursor.getColumnIndex(POSITION_i_mode)));
                    json.put(POSITION_v_gsm_roaming,cursor.getString(cursor.getColumnIndex(POSITION_v_gsm_roaming)));
                    json.put(POSITION_i_gsm_bcch,cursor.getString(cursor.getColumnIndex(POSITION_i_gsm_bcch)));
                    json.put(POSITION_i_sv,cursor.getString(cursor.getColumnIndex(POSITION_i_sv)));
                    json.put(POSITION_i_gsm_lac_id,cursor.getString(cursor.getColumnIndex(POSITION_i_gsm_lac_id)));
                    json.put(POSITION_i_download_channel,cursor.getString(cursor.getColumnIndex(POSITION_i_download_channel)));
                    json.put(POSITION_v_imei_no,cursor.getString(cursor.getColumnIndex(POSITION_v_imei_no)));
                    json.put(POSITION_v_reg_no,cursor.getString(cursor.getColumnIndex(POSITION_v_reg_no)));
                    json.put(POSITION_n_int_power,cursor.getString(cursor.getColumnIndex(POSITION_n_int_power)));
                    json.put(POSITION_n_ext_power,cursor.getString(cursor.getColumnIndex(POSITION_n_ext_power)));
                    json.put(POSITION_n_hdop,cursor.getString(cursor.getColumnIndex(POSITION_n_hdop)));
                    array.put(json);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            cursor.close();
            return array;
        }else{
            return null;
        }
    }


    public static List<Map<String,Object>> get5KListData(SQLiteDatabase db){

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME +" ORDER BY "+POSITION_i_gps_time_stamp+" DESC LIMIT 5000", null);
        if (cursor != null){
            Log.d("CheckCaseService","cursor.size:"+cursor.getCount());
        }
        if (cursor != null && cursor.getCount() > 0) {
            List<Map<String,Object>> array = new ArrayList<>();
            while (cursor.moveToNext()) {
                try{

                    Map<String,Object> json = new HashMap<>();
                    json.put(POSITION_i_cmd_ref_id,cursor.getInt(cursor.getColumnIndex(POSITION_i_cmd_ref_id)));
                    json.put(POSITION_v_bat_id,cursor.getString(cursor.getColumnIndex(POSITION_v_bat_id)));
                    json.put(POSITION_i_event_id,cursor.getInt(cursor.getColumnIndex(POSITION_i_event_id)));
                    json.put(POSITION_d_gps_time_stamp,cursor.getString(cursor.getColumnIndex(POSITION_d_gps_time_stamp)));
                    json.put(POSITION_i_gps_time_stamp,cursor.getLong(cursor.getColumnIndex(POSITION_i_gps_time_stamp)));
                    json.put(POSITION_n_latitude,cursor.getString(cursor.getColumnIndex(POSITION_n_latitude)));
                    json.put(POSITION_n_longitude,cursor.getString(cursor.getColumnIndex(POSITION_n_longitude)));
                    json.put(POSITION_i_altitude,cursor.getString(cursor.getColumnIndex(POSITION_i_altitude)));
                    json.put(POSITION_i_unit_status,cursor.getString(cursor.getColumnIndex(POSITION_i_unit_status)));
                    json.put(POSITION_i_gsm_cell_id,cursor.getString(cursor.getColumnIndex(POSITION_i_gsm_cell_id)));
                    json.put(POSITION_n_horizontal_velocity,cursor.getString(cursor.getColumnIndex(POSITION_n_horizontal_velocity)));
                    json.put(POSITION_n_vertical_velocity,cursor.getString(cursor.getColumnIndex(POSITION_n_vertical_velocity)));
                    json.put(POSITION_n_direction,cursor.getString(cursor.getColumnIndex(POSITION_n_direction)));
                    json.put(POSITION_i_mode,cursor.getString(cursor.getColumnIndex(POSITION_i_mode)));
                    json.put(POSITION_v_gsm_roaming,cursor.getString(cursor.getColumnIndex(POSITION_v_gsm_roaming)));
                    json.put(POSITION_i_gsm_bcch,cursor.getString(cursor.getColumnIndex(POSITION_i_gsm_bcch)));
                    json.put(POSITION_i_sv,cursor.getString(cursor.getColumnIndex(POSITION_i_sv)));
                    json.put(POSITION_i_gsm_lac_id,cursor.getString(cursor.getColumnIndex(POSITION_i_gsm_lac_id)));
                    json.put(POSITION_i_download_channel,cursor.getString(cursor.getColumnIndex(POSITION_i_download_channel)));
                    json.put(POSITION_v_imei_no,cursor.getString(cursor.getColumnIndex(POSITION_v_imei_no)));
                    json.put(POSITION_v_reg_no,cursor.getString(cursor.getColumnIndex(POSITION_v_reg_no)));
                    json.put(POSITION_n_int_power,cursor.getString(cursor.getColumnIndex(POSITION_n_int_power)));
                    json.put(POSITION_n_ext_power,cursor.getString(cursor.getColumnIndex(POSITION_n_ext_power)));
                    json.put(POSITION_n_hdop,cursor.getString(cursor.getColumnIndex(POSITION_n_hdop)));
                    array.add(json);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            cursor.close();
            return array;
        }else{
            return null;
        }
    }

}
