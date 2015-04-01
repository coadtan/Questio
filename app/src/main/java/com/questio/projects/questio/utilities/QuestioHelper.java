package com.questio.projects.questio.utilities;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QuestioHelper {


    private static final String LOG_TAG = QuestioHelper.class.getSimpleName();

    // passing "questio:zone:123:questio" to paremater qrCode will return "123"
    public static String getDeQRCode(String qrCode) {
        String deCodeForReturn = "failed";
        final String BEGIN_TYPE_1 = "questio:zone:";
        final String BEGIN_TYPE_2 = "questio:place:";
        final String END = ":questio";
        // 1 Step: check size
        if (qrCode.length() < (BEGIN_TYPE_2.length()+END.length())){
            return deCodeForReturn;
        }
        // 2 Step: removeBegin
        String type = getQRType(qrCode);

        if(type.equalsIgnoreCase("place")){
            qrCode = qrCode.replace(BEGIN_TYPE_2,"");
        }else if(type.equalsIgnoreCase("zone")){
            qrCode = qrCode.replace(BEGIN_TYPE_1,"");
        }

        // 3 Step: removeEnd
        qrCode = qrCode.replace(END,"");
        deCodeForReturn = qrCode;
        return deCodeForReturn;
    }

    public static String getQRType(String qrCode){
        String type = qrCode.substring(8,9);
        if(type.equalsIgnoreCase("p")){
            type="place";
        }else if(type.equalsIgnoreCase("z")){
            type="zone";
        }else{
            type="N/A";
        }
        return type;
    }

//    public static int checkValidQRCode(String qrCode,ArrayList<Zone> zoneList){
//        int count = -1;
//        String tempQR;
//        for(int i = 0; i < zoneList.size(); i++){
//            tempQR = zoneList.get(i).getQrCode();
//            if(tempQR.equalsIgnoreCase(qrCode)){
//                count = i;
//                return count;
//            }
//        }
//        Log.d(LOG_TAG, "checkValidQRCode: QRCode Invalid: Not found in list or database.");
//        return count;
//    }
     public static long getPlaceCountFromJson(String response){
         JSONArray arr = null;
         Log.d(LOG_TAG, "getPlaceCountFromJson: " + response);
         long result = 0;
         try {
             arr = new JSONArray(response);
             if (arr.length() != 0) {
                 for (int i = 0; i < arr.length(); i++) {
                     JSONObject obj = (JSONObject) arr.get(i);
                     result = Long.parseLong(obj.get("count").toString());

                 }
             }
         } catch (JSONException e) {
             e.printStackTrace();
         }
         return result;
     }
}
