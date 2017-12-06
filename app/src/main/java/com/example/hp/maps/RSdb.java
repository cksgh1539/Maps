package com.example.hp.maps;

import android.net.Uri;
import android.provider.BaseColumns;

import java.net.URI;

public final class RSdb {
    public static final String DB_NAME="GPS2.db";
    public static final int DATABASE_VERSION = 1;
    private static final String TEXT_TYPE = " TEXT";
    private static final String TEXT_TYPE2 = "REAL";
    private static final String COMMA_SEP = ",";

 //식당 정보 받는 테이블
    public static class Restaurant implements BaseColumns {
        public static final String TABLE_NAME="ResisRestaurant";
        public static final String KEY_ImageRS = "RSImage";
        public static final String KEY_name = "RSname";
        public static final String KEY_num = "RSnum";
        public static final String KEY_adrress = "RSadrress";


        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                KEY_ImageRS+TEXT_TYPE+COMMA_SEP +
                KEY_name + TEXT_TYPE + COMMA_SEP +
                KEY_num + TEXT_TYPE + COMMA_SEP +
                KEY_adrress + TEXT_TYPE +  " )";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
//메뉴정보 받는 테이블
    public static class Menu implements BaseColumns {
        public static final String TABLE_NAME2="ResisMENU";
        public static final String KEY_ImageMenu = "MENUImage";
        public static final String KEY_menu = "MENUname";
        public static final String KEY_price = "MENUprice";
        public static final String KEY_comment = "MENUcomment";

        public static final String CREATE_TABLE2 = "CREATE TABLE " + TABLE_NAME2 + " (" +
                _ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                KEY_ImageMenu + TEXT_TYPE + COMMA_SEP +
                KEY_menu + TEXT_TYPE + COMMA_SEP +
                KEY_price + TEXT_TYPE + COMMA_SEP +
                KEY_comment + TEXT_TYPE +  " )";

        public static final String DELETE_TABLE2 = "DROP TABLE IF EXISTS " + TABLE_NAME2;
    }
  //구글 맵 마커 저장 테이블
  public static class Marker implements BaseColumns {
      public static final String TABLE_NAME3="ResisGPS";
      public static final String KEY_Title = "GPSTitle";
      public static final String KEY_Lat = "GPSLat";
      public static final String KEY_Long = "GPSLong";


      public static final String CREATE_TABLE3 = "CREATE TABLE " + TABLE_NAME3 + " (" +
              _ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
              KEY_Title + TEXT_TYPE + COMMA_SEP +
              KEY_Lat + TEXT_TYPE + COMMA_SEP +
              KEY_Long + TEXT_TYPE +  " )";

      public static final String DELETE_TABLE3 = "DROP TABLE IF EXISTS " + TABLE_NAME3;
  }

}