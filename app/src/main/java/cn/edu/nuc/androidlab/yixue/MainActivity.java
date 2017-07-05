package cn.edu.nuc.androidlab.yixue;

import android.os.Bundle;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MyLocationStyle;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private MapView mapView;
    private AMap amap ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        mapView.onCreate(savedInstanceState);

        amap = mapView.getMap();
        initLocationStyle();

        initLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    /**
     * 定位
     */
    private void initLocation() {

        LocationManager manager = LocationManager.INSTANCE;
        LocationManager.LocationListener locationListener = new LocationManager.LocationListener(){

            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if(aMapLocation != null){
                    if(aMapLocation.getErrorCode() == 0){
                        int locationType = aMapLocation.getLocationType();  // 定位类型
                        double latitude = aMapLocation.getLatitude();       // 纬度信息
                        double longitude = aMapLocation.getLongitude();     // 经度信息
                        float accuracy = aMapLocation.getAccuracy();        // 精度信息

                        Log.i(TAG, "onLocationChanged: " + latitude +" " + longitude);
                    }else{
                        Log.i(TAG, "onLocationChanged: " + "ErrorCode: " + aMapLocation.getErrorCode() +"\n" +"ErrorInfo:" + aMapLocation.getErrorInfo());
                    }
                }
            }
        };
        manager.setLocationListener(locationListener);
        manager.setLocationClientOption();
        manager.openLocation();

        /*AMapLocationClient locationClientContinue = new AMapLocationClient(this.getApplicationContext());
        AMapLocationListener locationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {

            }
        };
        AMapLocationClientOption locationClientOption  = new AMapLocationClientOption();
        locationClientOption.setInterval(1000);
        locationClientContinue.setLocationListener(locationListener);
        locationClientContinue.setLocationOption(locationClientOption);
        locationClientContinue.startLocation();*/
    }

    private void initLocationStyle() {
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.interval(2000);
        amap.setMyLocationStyle(myLocationStyle);
        amap.setMyLocationEnabled(true);
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        // Example of a call to a native method
        //TextView tv = (TextView) findViewById(R.id.sample_text);
        //tv.setText(stringFromJNI());

        mapView = (MapView) findViewById(R.id.map);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
