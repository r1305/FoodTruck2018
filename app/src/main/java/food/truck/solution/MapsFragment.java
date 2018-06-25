package food.truck.solution;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private String result="";
    private LocationManager locationManager;
    private GoogleMap mMap;
    double latitud=0.0;
    double longitud=0.0;
    List<DataSnapshot> l=new ArrayList<>();
    DatabaseReference myRef;
    Query query;

    private String KEY="LFpbesokpq6vaRS-7xg";

    int level;

    public MapsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MapsFragment newInstance() {
        MapsFragment fragment = new MapsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_maps, container, false);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                l.clear();
                for (DataSnapshot values: dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    l.add(values);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(getActivity(), "Failed to read value."+ error.toException(), Toast.LENGTH_SHORT).show();
            }
        });

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        mapFragment.getMapAsync(this);
        return v;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //OBTENEMOS LA UBICACION DEL USUARIO Y MOVEMOS LA CAMARA HACIA SU POSICION

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

            }
        });

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 400, 1000, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Toast.makeText(getContext(), "Ubicando...", Toast.LENGTH_LONG).show();
                latitud=location.getLatitude();
                longitud=location.getLongitude();
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
                mMap.animateCamera(cameraUpdate);
                mMap.addMarker(new MarkerOptions().position(latLng));
                updateLocation("",latitud,longitud);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
                Toast.makeText(getContext(), "Active su GPS porfavor", Toast.LENGTH_SHORT).show();
            }
        });

        mMap.setMyLocationEnabled(true);
        UiSettings u=mMap.getUiSettings();
        u.setZoomControlsEnabled(true);
        u.setMyLocationButtonEnabled(true);
        u.setRotateGesturesEnabled(true);
    }

    public void updateLocation(String key,double lat,double lng){
        myRef.child(KEY).child("lat").setValue(lat);
        myRef.child(KEY).child("lng").setValue(lng);
    }
}
