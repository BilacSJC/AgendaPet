package com.converter.cambio.app_petshop.Controller;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FireBaseQuery {

    public void InsertObjectDb(Object obj, String tableName, String idObj, DatabaseReference databaseReference){

        databaseReference.child(tableName).child(idObj).setValue(obj);
    }

//    public void UpdateObjetcDb(Object obj, String tableName, String idObj, DatabaseReference databaseReference){
//        Map<String, Object> map = new HashMap<>();
//
//        databaseReference.child(tableName).child(idObj).updateChildren(map, new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//
//            }
//        });
//    }
//
//    private Map<String, Object> setMap() {
//        Map<String, Object> map = new HashMap<>();
//
//        map.put("", )
//
//    }

    public void DeleteObjectDb(){

    }

    public ArrayList SelectObjectDb(String tableName, final ArrayList lstObj, final Object obj, DatabaseReference databaseReference){

        databaseReference.child(tableName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lstObj.clear();
                for(DataSnapshot objSaSnapshot:dataSnapshot.getChildren()){
                    Object objPopulado = objSaSnapshot.getValue(obj.getClass());

                    lstObj.add(objPopulado);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return lstObj;
    }

    public void SelectObjectById(){

    }
}
