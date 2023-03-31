package com.example.testmaps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.testmaps.env.ImageUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.example.testmaps.R;

import com.example.testmaps.database.ItemDatabase;
import com.example.testmaps.models.Item;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Location currentLocation;
    FusedLocationProviderClient fusedClient;
    private static final int REQUEST_CODE = 101;
    //private ActivityMapsBinding binding;
    FrameLayout map;
    CircleOptions circleOptions = new CircleOptions();


    Spinner distanceSpinner;
    ArrayList<String> distances;
    List<Marker> marker_list = new ArrayList<>();
    Circle mCircle;
    boolean isMarkerClicked = false;
    LatLng defaultPosition = new LatLng(0, 0); // position par défaut à (0, 0)
    String defaultTitle = "Default Marker"; // titre par défaut
    MarkerOptions clickedMarker = new MarkerOptions()
            .position(defaultPosition)
            .title(defaultTitle);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        map = findViewById(R.id.map);

        fusedClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation();

       //helper = new SQLiteOpenHelper(MapsActivity.this, "Database1.db", null, 1) {
       //    @Override
       //    public void onCreate(SQLiteDatabase db) {
       //        db.execSQL("CREATE TABLE Photos (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, bitmap TEXT, latitude REAL, longitude REAL)");
       //    }

       //    @Override
       //    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
       //        db.execSQL("DROP TABLE Photos");
       //        onCreate(db);

       //    }
       //};


    }
    private void getLocation(){
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }

        Task<Location> task = fusedClient.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    currentLocation = location;
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(MapsActivity.this);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Votre position"); // marker de localisation du tel
        //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bluelocation);
        int imageWidth = 100;
        int imageHeight = 100;
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, imageWidth, imageHeight, false);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(scaledBitmap);
        markerOptions.icon(bitmapDescriptor);
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng)); // déplacement
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15)); // zoom
        mMap.addMarker(markerOptions);

        Button photoButton = findViewById(R.id.b2);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, DetectorActivity.class);
                startActivity(intent);
            }
        });


        //ItemDatabase itemDatabase=new ItemDatabase(MapsActivity.this);
        //Item item = new Item();
        //item.image_name = new Random().nextInt()+".png";
        //item.object_name= "";
        //item.lat = 48.907029;
        //item.lng = 2.286115;

        //Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.id1);

        //ImageUtils.saveBitmap(MapsActivity.this, bitmap1, item.image_name);
        //itemDatabase.insertItem(item);

        ItemDatabase itemDatabase=new ItemDatabase(MapsActivity.this);
        Item item = new Item();
        item.image_name = new Random().nextInt()+".png";
        item.object_name= "canapé + fauteuil";
        item.lat = 48.910097;
        item.lng = 2.288557;
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.id2);
        ImageUtils.saveBitmap(MapsActivity.this, bitmap1, item.image_name);
        itemDatabase.insertItem(item);

        Item item2 = new Item();
        item2.image_name = new Random().nextInt()+".png";
        item2.object_name= "fauteuil + fauteuil";
        item2.lat = 48.919097;
        item2.lng = 2.281557;
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.id3);
        ImageUtils.saveBitmap(MapsActivity.this, bitmap2, item2.image_name);
        itemDatabase.insertItem(item2);

        Item item3 = new Item();
        item3.image_name = new Random().nextInt()+".png";
        item3.object_name= "canapé";
        item3.lat = 48.907029;
        item3.lng = 2.286115;
        Bitmap bitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.id4);
        ImageUtils.saveBitmap(MapsActivity.this, bitmap3, item3.image_name);
        itemDatabase.insertItem(item3);

        // création des icones boites fermées et ouvertes
        List<Marker> markerList = new ArrayList<Marker>();
        Bitmap iconencomb = BitmapFactory.decodeResource(getResources(), R.drawable.boiteferme);
        Bitmap iconencombscale = Bitmap.createScaledBitmap(iconencomb, 100, 84, false);
        BitmapDescriptor boiteferme = BitmapDescriptorFactory.fromBitmap(iconencombscale);
        Bitmap iconencombouvre = BitmapFactory.decodeResource(getResources(), R.drawable.boiteouverte);
        Bitmap iconencombouvrescale = Bitmap.createScaledBitmap(iconencombouvre, 100, 84, false);
        BitmapDescriptor boiteouverte = BitmapDescriptorFactory.fromBitmap(iconencombouvrescale);
        ////


        ItemDatabase database1=new ItemDatabase(MapsActivity.this);
        List<Item> allItems = database1.getAllItems();


        allItems.forEach(new Consumer<Item>() {
            @Override
            public void accept(Item item) {
                LatLng latLng2 = new LatLng(item.lat,item.lng);
                MarkerOptions markerOptions2 = new MarkerOptions()
                        .position(latLng2)
                        .icon(boiteferme)
                        .title(item.object_name)
                        .snippet(item.image_name);
                Marker marker = mMap.addMarker(markerOptions2);
                markerList.add(marker);
            }
        });




        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                if ((!isMarkerClicked) || !clickedMarker.getTitle().equals(marker.getTitle())) {
                    // Afficher une image au-dessus du marqueur
                    mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                        @Override
                        public View getInfoWindow(Marker marker) {
                            return null;
                        }

                        @Override
                        public View getInfoContents(Marker marker) {

                            String image = marker.getSnippet();
                            System.out.println("000000000000000000000000000000000000");
                            //Bitmap bitmapFromFile = BitmapFactory.decodeFile(file.getAbsolutePath());
                            Bitmap bitmapFromFile = loadBitmapFromInternalStorage(image);

                            if(bitmapFromFile==null)
                                return null;
                            int imageWidth = 301;
                            int imageHeight = 410;
                            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmapFromFile, imageWidth, imageHeight, false);
                            // Créer une nouvelle vue pour l'image redimensionnée
                            View markerView = getLayoutInflater().inflate(R.layout.activity_custom_marker_layout, null);
                            ImageView imageView = markerView.findViewById(R.id.marker_image);
                            TextView txtView = markerView.findViewById(R.id.txtView);
                            txtView.setText(marker.getTitle());
                            imageView.setImageBitmap(scaledBitmap);
                            return markerView;

                        }
                    });

                    for (Marker m : markerList) {
                        m.setIcon(boiteferme);
                    }
                    // Changer l'icône du marqueur cliqué
                    marker.setIcon(boiteouverte);
                    isMarkerClicked = true;
                    clickedMarker.title(marker.getTitle());;

                } else {
                    Intent intent = new Intent(MapsActivity.this, photo_printer.class);
                    intent.putExtra("description", marker.getTitle());
                    intent.putExtra("id_image", marker.getSnippet());
                    startActivity(intent);
                }
                // Toujours retourner false pour que la bulle d'info de base ne soit pas affichée
                return false;
            }
        });



        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                // Réinitialiser isMarkerClicked lorsque vous cliquez sur la carte
                isMarkerClicked = false;
                for (Marker marker : markerList) {
                    marker.setIcon(boiteferme);
                }
            }
        });



        Spinner distanceSpinner = findViewById(R.id.distance_spinner);
        ArrayList<String> distances = new ArrayList<>();
        //distances.add("0 km");
        distances.add("1 km");
        distances.add("2 km");
        distances.add("4 km");
        distances.add("8 km");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, distances);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distanceSpinner.setAdapter(adapter);

        distanceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDistance = parent.getItemAtPosition(position).toString();
                int distance = Integer.parseInt(selectedDistance.split(" ")[0]);

                if (mCircle != null) {
                    removeCircleFromMap(mCircle);
                }

                mCircle = drawCircleOnMap(latLng, distance);
                filterMarkers(distance,latLng,markerList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // rien à faire ici
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLocation();
            }
        }
    }

    Circle drawCircleOnMap(LatLng center, int radius) {
        CircleOptions circleOptions = new CircleOptions()
                .center(center)
                .radius(radius*1000)
                .fillColor(0x30488ef0)
                .strokeColor(R.color.bluedark)
                .strokeWidth(4);

        Circle circle = mMap.addCircle(circleOptions);

        return circle;
    }

    private void removeCircleFromMap(Circle circle) {
        circle.remove();
    }

    private void filterMarkers(int distance, LatLng currentLocation, List<Marker> markers_list) {
        for (Marker marker : markers_list) {
            LatLng markerLocation = marker.getPosition();
            float[] results = new float[1];
            Location.distanceBetween(currentLocation.latitude, currentLocation.longitude,
                    markerLocation.latitude, markerLocation.longitude, results);
            float markerDistance = results[0] / 1000;
            if (markerDistance > distance) {
                marker.setVisible(false);
            } else {
                marker.setVisible(true);
            }
        }
    }

    //public void insertImage(SQLiteOpenHelper helper,String description, double latitude, double longitude, Bitmap image) {
    //    SQLiteDatabase db = helper.getWritableDatabase();
    //    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    //    image.compress(Bitmap.CompressFormat.PNG, 100, stream);
    //    byte[] byteArray = stream.toByteArray();
    //    ContentValues contentValues = new ContentValues();
    //    contentValues.put("description",description);
    //    contentValues.put("latitude",latitude);
    //    contentValues.put("longitude",longitude);
    //    contentValues.put("image", byteArray);
    //    db.insert("table_main", null, contentValues);
    //    //db.close();
    //}

//    public void insertImage(SQLiteOpenHelper helper,String description, double latitude, double longitude, String id_image) {
//        SQLiteDatabase db = helper.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("description",description);
//        contentValues.put("latitude",latitude);
//        contentValues.put("longitude",longitude);
//        contentValues.put("id_image",id_image);
//        db.insert("table_main", null, contentValues);
//        db.close();
//    }

    public Bitmap loadBitmapFromInternalStorage(String filename) {
        Bitmap bitmap = null;
        FileInputStream fis = null;

        try {

            File file = new File(getFilesDir(), filename);
            fis = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bitmap;
    }






}