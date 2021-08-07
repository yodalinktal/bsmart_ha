package bsmart.technology.rru.base.utils;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Health Data
 */
public class HealthUtil {

    public static String getHealthData(JsonObject jsonObject){
        if (null == jsonObject){
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append(jsonObject.get("i_body_temp").getAsFloat()+";");
        stringBuffer.append(jsonObject.get("i_other_cough").getAsString()+";");
        stringBuffer.append(jsonObject.get("i_other_shortness_of_breath").getAsString()+";");
        stringBuffer.append(jsonObject.get("i_other_sorethroat").getAsString()+";");
        stringBuffer.append(jsonObject.get("i_other_running_nose").getAsString()+";");
        stringBuffer.append(jsonObject.get("i_other_hoarse_voice").getAsString()+";");
        stringBuffer.append(jsonObject.get("i_other_swallowing").getAsString()+";");
        stringBuffer.append(jsonObject.get("i_other_smell_disorder").getAsString()+";");
        stringBuffer.append(jsonObject.get("i_other_diarrhea").getAsString()+";");
        stringBuffer.append(jsonObject.get("i_other_tiredness").getAsString()+";");
        stringBuffer.append(jsonObject.get("i_other_chills").getAsString()+";");
        stringBuffer.append(jsonObject.get("i_other_headache").getAsString()+";");
        stringBuffer.append(jsonObject.get("i_other_travelled_covid_country").getAsString()+";");
        stringBuffer.append(jsonObject.get("i_other_fever").getAsString()+";");
        stringBuffer.append(jsonObject.get("i_other_close_contact_covid").getAsString()+";");
        stringBuffer.append(jsonObject.get("i_other_wear_personal_protective").getAsString()+";");
        stringBuffer.append("1;");//health declared
        String remark = " ";
        if (jsonObject.has("v_other_remark") && !jsonObject.get("v_other_remark").isJsonNull()){
            remark = jsonObject.get("v_other_remark").getAsString();
        }
        stringBuffer.append(remark+";");
        String updatedDateTime = jsonObject.get("d_covid_specimen_collect").getAsString();
        stringBuffer.append(updatedDateTime+";");

        return stringBuffer.toString();
    }

    public static Map<String,Object> healthStringMetaData(
            String smart_driver_id,String c_people_rid,
            String test1Result,String test2Result,String test3Result,String test4Result,
            String test5Result,String test6Result,String test7Result,String test8Result,
            String test9Result,String test10Result,String test11Result,String test12Result,
            String test13Result,String test14Result,String test15Result,Float temperature,
            String c_user_rid,String remark
    ){

        Map<String,Object> map = new HashMap<>();
        map.put("smart_driver_id",smart_driver_id);
        map.put("c_people_rid",c_people_rid);
        map.put("i_other_cough",test1Result);
        map.put("i_other_shortness_of_breath",test2Result);
        map.put("i_other_sorethroat",test3Result);
        map.put("i_other_running_nose",test4Result);
        map.put("i_other_hoarse_voice",test5Result);
        map.put("i_other_swallowing",test6Result);
        map.put("i_other_smell_disorder",test7Result);
        map.put("i_other_diarrhea",test8Result);
        map.put("i_other_tiredness",test9Result);
        map.put("i_other_chills",test10Result);
        map.put("i_other_headache",test11Result);
        map.put("i_other_travelled_covid_country",test12Result);
        map.put("i_other_fever",test13Result);
        map.put("i_other_close_contact_covid",test14Result);
        map.put("i_other_wear_personal_protective",test15Result);

        map.put("i_body_temp",temperature);

        map.put("v_other_remark",remark);
        map.put("v_clinic_rid",remark);
        map.put("v_clinic_facility_code",remark);
        map.put("n_latitude",3.020321);
        map.put("n_longitude",2.000012);

        return map;
    }


}
