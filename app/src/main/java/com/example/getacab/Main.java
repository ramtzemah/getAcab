package com.example.getacab;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class Main {
private String type;
private Context context;
private String uId;

    public Main(Context context, String type,String uId) {
        this.context = context;
        this.type=type;
        this.uId=uId;
        Intent intent = new Intent(context,ThePick.class);
        Bundle bundle = new Bundle();
        bundle.putString(ThePick.TYPE,type);
        bundle.putString(ThePick.UID,uId);
        intent.putExtra("Bundle", bundle);
        context.startActivity(intent);

    }



}

