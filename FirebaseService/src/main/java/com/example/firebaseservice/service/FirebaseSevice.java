package com.example.firebaseservice.service;

import com.example.firebaseservice.model.*;
import com.example.firebaseservice.repository.PathOnFBRepo;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * com.example.firebaseservice.service;
 * Created by Phuoc -19127520
 * Date 27/08/2022 - 10:30 CH
 * Description: ...
 */

@Service
public class FirebaseSevice {
    LoadingCache<Integer, Position> cache = CacheBuilder.newBuilder()
            .build(new CacheLoader<>() {

                @Override
                public Position load(Integer integer) throws Exception {
                    return null;
                }
            });


    Jedis jedis=new Jedis("http://localhost:6379");

    @Autowired
    private PathOnFBRepo pathOnFBRepo;

    @PostConstruct
    public void initialization(){
        try {
            FileInputStream serviceAccount = new FileInputStream("./serviceAccountKey.json");
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://uberclone-1707-default-rtdb.asia-southeast1.firebasedatabase.app")
                    .build();

            FirebaseApp.initializeApp(options);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Position trackingDriver(int tripId){

        try {
            return new Gson().fromJson(jedis.get(String.valueOf(tripId)),Position.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public String onlineOrBooking(VehicleType vehicleType, int id, String typeList, Object object, Position position){
        String lat=String.valueOf((double)Math.round(position.getLatitude()*100)/100).replace(".",",");
        String lng=String.valueOf((double)Math.round(position.getLongitude()*100)/100).replace(".",",");
        try{
            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference(vehicleType.toString());

            Map<String, Object> idObject=new HashMap<>();
            idObject.put(String.valueOf(id),object);

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot Snapshot) {
                    DatabaseReference ref2=ref.child(lat).child(lng);
                    if(Snapshot.child(lat).child(lng).hasChild(typeList)){
                        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.hasChild(String.valueOf(id))) return;
                                ref2.child(typeList).updateChildrenAsync(idObject);
                                if(typeList.equals("DriverList")) {
                                    ref2.child(typeList).child(String.valueOf(id))
                                            .child("position").setValueAsync(position);

                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }
                    else{
                        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Map<String, Object> driverAndRequest=new HashMap<>();
                                driverAndRequest.put(typeList, idObject);
                                ref2.updateChildrenAsync(driverAndRequest);
                                if(typeList.equals("DriverList")) {
                                    ref2.child(typeList).child(String.valueOf(id))
                                            .child("position").setValueAsync(position);

                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }


                    PathOnFirebase pathOnFirebase=pathOnFBRepo.findByTypeAndIdRrD(typeList,id);
                    if(pathOnFirebase==null){
                        pathOnFirebase=new PathOnFirebase();
                        pathOnFirebase.setType(typeList);
                        pathOnFirebase.setIdRrD(id);
                    }
                    pathOnFirebase.setPath(ref2.child(typeList).getPath().toString());
                    pathOnFBRepo.save(pathOnFirebase);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        if(typeList.equals("DriverList"))
            return "/"+vehicleType+"/"+lat+"/"+lng+"/"+typeList;
        else return "/"+vehicleType+"/"+lat+"/"+lng+"/"+typeList+"/"+id;
    }

    public String offline(int id){
        try{
            PathOnFirebase pathOnFirebase=pathOnFBRepo.findByTypeAndIdRrD("DriverList",id);
            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference(pathOnFirebase.getPath());
            ref.child(String.valueOf(id)).removeValueAsync();
//            pathOnFBRepo.delete(pathOnFirebase);

        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
        return null;
    }

    public String cancelBooking(int id){
        try{
            String path=pathOnFBRepo.findByTypeAndIdRrD("Booking",id).getPath();
            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference(path);
            ref.child(String.valueOf(id)).removeValueAsync();
        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
        return null;
    }

    public String acceptBooking(Request request, DriverDTO driverDTO){
        try{
            System.out.println(request.getRequestId());
            PathOnFirebase pathInDriver=pathOnFBRepo.findByTypeAndIdRrD("DriverList",driverDTO.getDriverId());
            PathOnFirebase pathInBooking=pathOnFBRepo.findByTypeAndIdRrD("Booking",request.getRequestId());

            DatabaseReference currentRefRequest = FirebaseDatabase.getInstance()
                    .getReference(pathInBooking.getPath()+"/"+request.getRequestId());

            DatabaseReference currentRefDriver=FirebaseDatabase.getInstance()
                    .getReference(pathInDriver.getPath()+"/"+driverDTO.getDriverId());

            DatabaseReference refTrip=FirebaseDatabase.getInstance().getReference()
                    .child("Trip").child(String.valueOf(request.getRequestId()));

            currentRefDriver.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map<String, Object> driver=new HashMap<>();
                    driver.put("DriverAccept",dataSnapshot.getValue());
                    //cache.put(request.getRequestId(), dataSnapshot.child("position").getValue(Position.class));
                    jedis.set(String.valueOf(request.getRequestId()),new Gson().toJson(dataSnapshot.child("position").getValue(Position.class)));
                    refTrip.updateChildrenAsync(driver);
                    currentRefDriver.removeValueAsync();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            currentRefRequest.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    refTrip.updateChildrenAsync((Map<String, Object>) dataSnapshot.getValue());
                    currentRefRequest.removeValueAsync();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            pathInBooking.setPath(refTrip.getPath().toString());
            pathInBooking.setType("Trip");
            pathOnFBRepo.save(pathInBooking);
            pathOnFBRepo.delete(pathInDriver);

            return String.valueOf(refTrip.getPath());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String updatePos(int tripId, Position position){
        try{
            PathOnFirebase pathTrip=pathOnFBRepo.findByTypeAndIdRrD("Trip",tripId);
            DatabaseReference refTrip=FirebaseDatabase.getInstance()
                    .getReference(pathTrip.getPath()).child("DriverAccept");
            Map<String, Object> pos=new HashMap<>();
            pos.put("position",position);

            jedis.set(String.valueOf(tripId),new Gson().toJson(position));
//            cache.refresh(tripId);
//            cache.put(tripId,position);
            refTrip.updateChildrenAsync(pos);
            return "Sucessful";
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String completeTrip(int tripId){
        try{
            PathOnFirebase pathOnFirebase = pathOnFBRepo.findByTypeAndIdRrD("TRIP", tripId);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference(pathOnFirebase.getPath());
            reference.removeValueAsync();
            pathOnFBRepo.delete(pathOnFirebase);

            jedis.del(String.valueOf(tripId));
            //cache.invalidate(tripId);

            return "Successful";
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
