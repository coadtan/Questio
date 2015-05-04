package com.questio.projects.questio;

import android.test.InstrumentationTestCase;

import com.questio.projects.questio.models.Place;

import java.util.ArrayList;

/**
 * Created by ning jittima on 2/5/2558.
 */
public class PlaceTest extends InstrumentationTestCase {
    public void testGetAllPlaceArrayList(){
        Place place = new Place();
        ArrayList<Place> list = place.getAllPlaceArrayList();
        assertEquals(list.get(1).toString(), "Place{" +
                "placeId=" + 1 +
                ", placeName='" + "KMUTT" + '\'' +
                ", placeFullName='" + "King Mongkut's University of Technology Thonburi" + '\'' +
                ", qrCode=" + 65001 +
                ", sensorId=" + 65001 +
                ", latitude=" + 13.651054 +
                ", longitude=" + 100.494087 +
                ", radius=" + 248.16 +
                ", placetype='" + "University" + '\'' +
                ", imageurl='" + "/main_placepic/placekmutt.png" + '\'' +
                '}');
    }
}
