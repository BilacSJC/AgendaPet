package com.converter.cambio.app_petshop.Controller;

import com.converter.cambio.app_petshop.Model.ClienteModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class CustomValueEventListener implements ValueEventListener {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        for(DataSnapshot d : dataSnapshot.getChildren()){
            ClienteModel c = d.getValue(ClienteModel.class);
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
