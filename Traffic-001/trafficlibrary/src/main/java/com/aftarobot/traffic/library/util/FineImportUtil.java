package com.aftarobot.traffic.library.util;

import android.util.Log;

import com.aftarobot.traffic.library.api.DataAPI;
import com.aftarobot.traffic.library.data.FineDTO;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by aubreymalabie on 3/11/17.
 */

public class FineImportUtil {

    private static String TAG = FineImportUtil.class.getCanonicalName();

    public static void importFines() {
        DataAPI api = new DataAPI();
        List<FineDTO> list = getFines();
        for (FineDTO f: list) {
            api.addFine(f, new DataAPI.DataListener() {
                @Override
                public void onResponse(String key) {
                    Log.i(TAG, "onResponse: ".concat(key));
                }

                @Override
                public void onError(String message) {
                    Log.e(TAG, "onError: ".concat(message) );
                }
            });
        }
    }
    public static List<FineDTO> getFines() {
        List<FineDTO> list = new ArrayList<>();
        FineDTO f1 = new FineDTO();
        f1.setFine(1000.00);
        f1.setDepartmentID("-KetZcLP57Iq1OOgUuKi");
        f1.setDepartmentName("Pecanwood Traffic Department");
        f1.setCode("78011");
        f1.setCharge("owner of motor vehicle failed to licence motor vehicle");
        f1.setRegulation("18");
        f1.setSection("");
        list.add(f1);
        //
        FineDTO f2 = new FineDTO();
        f2.setFine(300.00);
        f2.setDepartmentID("-KetZcLP57Iq1OOgUuKi");
        f2.setDepartmentName("Pecanwood Traffic Department");
        f2.setCode("78299");
        f2.setCharge("owner failed to display licence disc on a transparent window");
        f2.setSection("");
        f2.setRegulation("36 (1)");
        list.add(f2);
        //
        FineDTO f3 = new FineDTO();
        f3.setFine(300.00);
        f3.setDepartmentID("-KetZcLP57Iq1OOgUuKi");
        f3.setDepartmentName("Pecanwood Traffic Department");
        f3.setCode("78396");
        f3.setCharge("display licence disc which in any way was odscured or had become illegible");
        f3.setRegulation("36 (2) (b)");
        f3.setSection("");
        list.add(f3);
        //
        FineDTO f4 = new FineDTO();
        f4.setFine(1000.00);
        f4.setDepartmentID("-KetZcLP57Iq1OOgUuKi");
        f4.setDepartmentName("Pecanwood Traffic Department");
        f4.setCode("78100");
        f4.setCharge("fail to affix one numberplate to the motor vehicle");
        f4.setRegulation("35 (5)");
        f4.setSection("");
        list.add(f4);
        //
        FineDTO f5 = new FineDTO();
        f5.setFine(2000.00);
        f5.setDepartmentID("-KetZcLP57Iq1OOgUuKi");
        f5.setDepartmentName("Pecanwood Traffic Department");
        f5.setCode("78118");
        f5.setCharge("fail to affix two numberplate to the motor vehicle");
        f5.setSection("");
        f5.setRegulation("35 (5)");
        list.add(f5);
        //
        FineDTO f6 = new FineDTO();
        f6.setFine(250.00);
        f6.setDepartmentID("-KetZcLP57Iq1OOgUuKi");
        f6.setDepartmentName("Pecanwood Traffic Department");
        f6.setCode("79279");
        f6.setCharge("operate a motor vehicle fitted with defective lamps");
        f6.setSection("");
        f6.setRegulation("157 (1) (a) ");
        list.add(f6);
        //
        FineDTO f8 = new FineDTO();
        f8.setFine(500.00);
        f8.setDepartmentID("-KetZcLP57Iq1OOgUuKi");
        f8.setDepartmentName("Pecanwood Traffic Department");
        f8.setCode("81527");
        f8.setCharge("operate a motor vehicle fitted with a pneumatic tyre whilst such tyre did not display throughout a patern or did not have a tread of at least one millimetre in depth");
        f8.setSection("");
        f8.setRegulation("212 (j) (i)");
        list.add(f8);

        //
        FineDTO f9 = new FineDTO();
        f9.setFine(500);
        f9.setDepartmentID("-KetZcLP57Iq1OOgUuKi");
        f9.setDepartmentName("Pecanwood Traffic Department");
        f9.setCode("91263");
        f9.setCharge("operate an unlicensed motor vehicle on a public road");
        f9.setSection("4 (2)");
        f9.setRegulation("");
        list.add(f9);
        //
        FineDTO f10 = new FineDTO();
        f10.setFine(1000.00);
        f10.setDepartmentID("-KetZcLP57Iq1OOgUuKi");
        f10.setDepartmentName("Pecanwood Traffic Department");
        f10.setCode("10047");
        f10.setCharge("drove a motor vehicle without a valid driving licence to wit Code B or EB");
        f10.setSection("12 (a)");
        f10.setRegulation("");
        list.add(f10);
//
        FineDTO f11 = new FineDTO();
        f11.setFine(1000.00);
        f11.setDepartmentID("-KetZcLP57Iq1OOgUuKi");
        f11.setDepartmentName("Pecanwood Traffic Department");
        f11.setCode("10097");
        f11.setCharge("drove a motor vehicle whilst he or she did not keep a driving licence in the said vehicle");
        f11.setSection("12 (b)");
        f11.setRegulation("");
        list.add(f11);
        //
        FineDTO f12 = new FineDTO();
        f12.setFine(300.00);
        f12.setDepartmentID("-KetZcLP57Iq1OOgUuKi");
        f12.setDepartmentName("Pecanwood Traffic Department");
        f12.setCode("17502");
        f12.setCharge("Failed to comply with the direction of a road traffic sign to wit - Stop sign");
        f12.setSection("58 (1)");
        f12.setRegulation("");
        list.add(f12);
        //
        FineDTO f13 = new FineDTO();
        f13.setFine(1500.00);
        f13.setDepartmentID("-KetZcLP57Iq1OOgUuKi");
        f13.setDepartmentName("Pecanwood Traffic Department");
        f13.setCode("2000");
        f13.setCharge("Failed to comply with the direction of a road traffic mark to wit - No overtaking / barrier line");
        f13.setSection("58 (1)");
        f13.setRegulation("");
        list.add(f13);
        //
        FineDTO f14 = new FineDTO();
        f14.setFine(1500.00);
        f14.setDepartmentID("-KetZcLP57Iq1OOgUuKi");
        f14.setDepartmentName("Pecanwood Traffic Department");
        f14.setCode("20393");
        f14.setCharge("Failed to comply with the direction of a road traffic signal to wit - steady red disc light signal");
        f14.setSection("58 (1)");
        f14.setRegulation("");
        list.add(f14);




        return list;
    }
}
