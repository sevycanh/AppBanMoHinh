package com.example.shopmohinh.zalo;

import com.example.shopmohinh.zalo.AppInfo;
import com.example.shopmohinh.zalo.Helpers;
import com.example.shopmohinh.zalo.HttpProvider;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.TimeZone;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class RefundOrder {
    private class RefundOrderData {
        String AppId;
        String MRefundId;
        String ZpTransId;
        String Amount;
        String Timestamp;
        String Description;
        String Mac;

        private String getCurrentTimeString(String format) {
            Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
            SimpleDateFormat fmt = new SimpleDateFormat(format);
            fmt.setCalendar(cal);
            return fmt.format(cal.getTimeInMillis());
        }

        private RefundOrderData(long zpTransId, int amount) throws Exception {
            long timestamp = System.currentTimeMillis();

            AppId = String.valueOf(AppInfo.APP_ID);;
            MRefundId = getCurrentTimeString("yyMMdd") +"_"+ String.valueOf(AppInfo.APP_ID) +"_"+ timestamp;
            ZpTransId = String.valueOf(zpTransId);
            Amount = String.valueOf(amount);
            Timestamp = String.valueOf(timestamp);
            Description = "ZaloPay Refund " + zpTransId;

            String inputHMac = String.format("%s|%s|%s|%s|%s",
                    this.AppId,
                    this.ZpTransId,
                    this.Amount,
                    this.Description,
                    this.Timestamp);
            Mac = Helpers.getMac(AppInfo.MAC_KEY, inputHMac);
        }
    }

    public JSONObject CreateRefundOrder(long zpTransId, int amount) throws Exception {
        RefundOrderData input = new RefundOrderData(zpTransId, amount);
        System.out.println(input.AppId);
        System.out.println(input.MRefundId);
        System.out.println(input.ZpTransId);
        System.out.println(input.Amount);
        System.out.println(input.Timestamp);
        System.out.println(input.Description);
        System.out.println(input.Mac);

        RequestBody formBody = new FormBody.Builder()
                .add("app_id", input.AppId)
                .add("m_refund_id", input.MRefundId)
                .add("zp_trans_id", input.ZpTransId)
                .add("amount", input.Amount)
                .add("timestamp", input.Timestamp)
                .add("description", input.Description)
                .add("mac", input.Mac)
                .build();

        JSONObject data = HttpProvider.sendPost(AppInfo.URL_REFUND_ORDER, formBody);
        return data;
    }
}