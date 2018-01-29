package com.example.sebastian.smb_geolocalization;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class LocationRepository {
    private final SQLiteDatabase database;

    LocationRepository(Context context){
        File mDatabaseFile = context.getDatabasePath("smb_location.db").getAbsoluteFile();
        database = SQLiteDatabase.openOrCreateDatabase(mDatabaseFile, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS Locations(Id VARCHAR PRIMARY KEY, Name VARCHAR, Description VARCHAR, Radius NUMERIC, Lat NUMERIC, Lng NUMERIC);");
    }

    public void addLocation(Position position) {
        ContentValues insertValues = new ContentValues();
        insertValues.put("Id", position.getId().toString());
        insertValues.put("Name", position.getName());
        insertValues.put("Description", position.getDesc());
        insertValues.put("Radius", position.getRadius());
        insertValues.put("Lat", position.getLatitude());
        insertValues.put("Lng", position.getLongitude());
        database.insert("Locations", null, insertValues);
    }

    public ArrayList<Position> getAllPositions(){
        Cursor cursor = database.rawQuery("select * from Locations", null);
        ArrayList<Position> listResult = new ArrayList<>();

        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                String id = cursor.getString(cursor.getColumnIndex("Id"));
                String name = cursor.getString(cursor.getColumnIndex("Name"));
                String desc = cursor.getString(cursor.getColumnIndex("Description"));
                double radius = cursor.getDouble(cursor.getColumnIndex("Radius"));
                double lat = cursor.getDouble(cursor.getColumnIndex("Lat"));
                double lng = cursor.getDouble(cursor.getColumnIndex("Lng"));
                listResult.add(new Position(UUID.fromString(id),name, desc, radius, lat, lng));
                cursor.moveToNext();
            }
        }

        return listResult;
    }

    public Position getPositionsById(String positionId){
        String[] columns = {"Id","Name","Description", "Radius", "Lat","Lng"};
        String[] whereArgs = {positionId};
        Cursor cursor = database.query("Locations",columns,"Id=?",whereArgs,null,null,null);
        Position result = null;

        if (cursor.moveToFirst()) {
                String id = cursor.getString(cursor.getColumnIndex("Id"));
                String name = cursor.getString(cursor.getColumnIndex("Name"));
                String desc = cursor.getString(cursor.getColumnIndex("Description"));
                double radius = cursor.getDouble(cursor.getColumnIndex("Radius"));
                double lat = cursor.getDouble(cursor.getColumnIndex("Lat"));
                double lng = cursor.getDouble(cursor.getColumnIndex("Lng"));
                result = new Position(UUID.fromString(id),name, desc, radius, lat, lng);
        }

        return result;
    }


}
