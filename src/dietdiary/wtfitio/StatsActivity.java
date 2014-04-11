package dietdiary.wtfitio;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.inmobi.commons.InMobi;
import com.inmobi.monetization.IMBanner;


public class StatsActivity extends Activity {
	
	public static final String SECURE_SETTINGS = "DietDiary_sett";
    private static String AD_UNIT_ID_ADMOB = "ca-app-pub-7046352820490447/5797143713";
	
	TableLayout table_stat0,table_stat1,table_stat2,table_stat3;
	TableRow r0_1,r0_2,r1_1,r1_2,r2_1,r2_2,r3_1,r3_2;
	Boolean  i = false;
	String a ="";
	TextView view,table_r_1,P0,P1,P2,P3,allpoints;
	Spinner stat_spinner;
	Button stat_but;
	ScrollView stat_scroll;

	WebView stat_web;
	public static Context ab ;
    DecimalFormat df = new DecimalFormat("@@@#");
    DecimalFormat df1 = new DecimalFormat("@@#");
    IMBanner imbanner;
    Geocoder geocoder;
    String bestProvider;
    String postcode = null;
    List<Address> user = null;
    double lat;
    double lng;
    private AdView adView_Admob;

    @Override
    protected void onResume() {
        super.onResume();
        if (adView_Admob != null) {
            adView_Admob.resume();
        }
    }

    @Override
    protected void onPause() {
        if (adView_Admob != null) {
            adView_Admob.pause();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (adView_Admob != null) {
            adView_Admob.destroy();
        }
        super.onDestroy();
    }
	private class MyWebViewClient extends WebViewClient {
	    @Override
	    
	    public void onLoadResource(WebView webview, String s)
        {if(!s.equals(null)){
            if(s.contains("grabo") &! s.contains("facebook")  &! s.contains("imgrabo") && webview.getHitTestResult().getType() > 0)
            {
                webview.getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(s)));
                webview.stopLoading();
                Log.i("RESLOAD", Uri.parse(s).toString());
            }
        }
        }

	    /**public boolean shouldOverrideUrlLoading(WebView view, String url) {
	       /** if (Uri.parse(url).getHost().equals("www.example.com")) {
	            // This is my web site, so do not override; let my WebView load the page
	            return false;
	        }
	        // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
	        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
	        startActivity(intent);
	        return true;
	    }*/
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stats);
		ab=this;
	     setupViews();
		 spinner_ready();
		 addButtonListeners();


		 if (haveNetworkConnection()==true){

		 adds_load();
		 }
		/**Toast.makeText(getBaseContext(), 
				select_date(), 
                Toast.LENGTH_SHORT).show();*/
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_stats, menu);
		return true;
	}
	
	public void setupViews(){
		
		table_stat0 = (TableLayout)findViewById(R.id.stat_table0);
		table_stat1 = (TableLayout)findViewById(R.id.stat_table1);
		table_stat2 = (TableLayout)findViewById(R.id.stat_table2);
		table_stat3 = (TableLayout)findViewById(R.id.stat_table3);
		//view = (TextView)findViewById(R.id.viewtest1);
		stat_spinner = (Spinner)findViewById(R.id.spinnerdate1);
		stat_but = (Button)findViewById(R.id.statbutton1);
		table_r_1=(TextView)findViewById(R.id.TRdate);
		allpoints=(TextView)findViewById(R.id.all_points);
		P0=(TextView)findViewById(R.id.eat0_t);
		P1=(TextView)findViewById(R.id.eat1_t);
		P2=(TextView)findViewById(R.id.eat2_t);
		P3=(TextView)findViewById(R.id.eat3_t);
		stat_scroll = (ScrollView)findViewById(R.id.scrollView1);
		stat_web = (WebView)findViewById(R.id.webViewstats1);
		r0_1=(TableRow)findViewById(R.id.table0_row1);
		r0_2=(TableRow)findViewById(R.id.table0_row2);
		r1_1=(TableRow)findViewById(R.id.table1_row1);
		r1_2=(TableRow)findViewById(R.id.table1_row2);
		r2_1=(TableRow)findViewById(R.id.table2_row1);
		r2_2=(TableRow)findViewById(R.id.table2_row2);
		r3_1=(TableRow)findViewById(R.id.table3_row1);
		r3_2=(TableRow)findViewById(R.id.table3_row2);
	}
	
	
	
	public void addButtonListeners(){
		stat_but.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				/**while (table_stat.getChildCount() > 1)
		    	{
		    		// while there are at least two rows in the table widget, delete
		    		// the second row.
		    		table_stat.removeViewAt(1);
		    		
		    		
		    		
		    	}*/
				
				
				if(stat_spinner.getCount()>0){
				udate_table(stat_spinner.getSelectedItem().toString());
				}
				else{
					Toast.makeText(getBaseContext(), 
				    		  "Няма въведена информация!", 
			                  Toast.LENGTH_SHORT).show();
					
				}
			}
		});
	}
	
	public void udate_table(String search){
		stat_web.setVisibility(2);
		stat_scroll.setVisibility(0);
		table_r_1.setText(search);
		table_r_1.setTextColor(Color.WHITE);
		table_r_1.setBackgroundColor(Color.BLACK);
		allpoints.setTextSize(20);
		allpoints.setBackgroundColor(Color.GREEN);
		
		r0_1.setBackgroundColor(Color.LTGRAY);
        r0_2.setBackgroundColor(Color.LTGRAY);
		r1_1.setBackgroundColor(Color.GRAY);
        r1_2.setBackgroundColor(Color.GRAY);
        r2_1.setBackgroundColor(Color.LTGRAY);
        r2_2.setBackgroundColor(Color.LTGRAY);
        r3_1.setBackgroundColor(Color.GRAY);
        r3_2.setBackgroundColor(Color.GRAY);

		
		AABDatabaseManager1 db = new AABDatabaseManager1(this);
		Float eat0=Float.valueOf("0"),eat1=Float.valueOf("0"),eat2=Float.valueOf("0"),eat3=Float.valueOf("0"),total =Float.valueOf("0");
		
		ArrayList<ArrayList<Object>> data = db.getStatsRowsAsArrays_tracker(search);
		
		
		
		
		while (table_stat0.getChildCount() >  2)
    	{
    		// while there are at least two rows in the table widget, delete
    		// the second row.
			table_stat0.removeViewAt(2);
    	}
		
		while (table_stat1.getChildCount() >  2)
    	{
    		// while there are at least two rows in the table widget, delete
    		// the second row.
			table_stat1.removeViewAt(2);
    	}
		while (table_stat2.getChildCount() >  2)
    	{
    		// while there are at least two rows in the table widget, delete
    		// the second row.
			table_stat2.removeViewAt(2);
    	}
		
		while (table_stat3.getChildCount() >  2)
    	{
    		// while there are at least two rows in the table widget, delete
    		// the second row.
			table_stat3.removeViewAt(2);
    	}
		
				
						
						
		
		for (int position=0; position < data.size(); position++){
			ArrayList<Object> row = data.get(position);
			int test =Integer.valueOf(row.get(0).toString()); 
			switch(test){
			case 0:{
				eat0 =eat0 +  Float.valueOf(row.get(1).toString());
				
				TableRow tableRow= new TableRow(this);
				tableRow.setBackgroundColor(Color.LTGRAY);
				 
	    		 
	    		TextView NameText = new TextView(this);
	    		NameText.setMaxWidth(150);
	    		NameText.setText(row.get(2).toString());
	    		tableRow.addView(NameText);
	 
	    		TextView Points = new TextView(this);
	    		Points.setText(df.format(row.get(1)));
	    		tableRow.addView(Points);
	    		table_stat0.addView(tableRow);
				break;
			}
			case 1:{
				eat1 = eat1 + Float.valueOf(row.get(1).toString());
				
				TableRow tableRow= new TableRow(this);
				tableRow.setBackgroundColor(Color.GRAY);
				 
	    		 
	    		TextView NameText = new TextView(this);
	    		NameText.setMaxWidth(150);
	    		NameText.setText(row.get(2).toString());
	    		tableRow.addView(NameText);
	 
	    		TextView Points = new TextView(this);
	    		Points.setText(df.format(row.get(1)));
	    		tableRow.addView(Points);
	    		table_stat1.addView(tableRow);
	 
				break;
			}
			case 2:{
				eat2 =eat2 +  Float.valueOf(row.get(1).toString());
				
				TableRow tableRow= new TableRow(this);
				tableRow.setBackgroundColor(Color.LTGRAY);
				 
	    		 
	    		TextView NameText = new TextView(this);
	    		NameText.setMaxWidth(150);
	    		NameText.setText(row.get(2).toString());
	    		tableRow.addView(NameText);
	 
	    		TextView Points = new TextView(this);
	    		Points.setText(df.format(row.get(1)));
	    		tableRow.addView(Points);
	    		table_stat2.addView(tableRow);
				break;
			}
			case 3:{
				eat3 =eat3 + Float.valueOf(row.get(1).toString());
				
				TableRow tableRow= new TableRow(this);
				tableRow.setBackgroundColor(Color.GRAY);
				 
	    		 
	    		TextView NameText = new TextView(this);
	    		NameText.setMaxWidth(150);
	    		NameText.setText(row.get(2).toString());
	    		tableRow.addView(NameText);
	 
	    		TextView Points = new TextView(this);
	    		Points.setText(df.format(row.get(1)));
	    		tableRow.addView(Points);
	    		table_stat3.addView(tableRow);
				
				break;
			}
			}
			
		}
		
		total = eat0+eat1+eat2+eat3;
        String nulli =df1.format(Float.valueOf("0"));
        String test = df1.format(eat0);
		
		P0.setText(df.format(eat0));
        if (df1.format(eat0).equals(nulli)) {
            r0_2.setVisibility(view.GONE);
        }
		P1.setText(df.format(eat1));
        if (df1.format(eat1).equals(nulli)) {
            r1_2.setVisibility(view.GONE);
        }
		P2.setText(df.format(eat2));
        if (df1.format(eat2).equals(nulli)) {
            r2_2.setVisibility(view.GONE);
        }
		P3.setText(df.format(eat3));
        if (df1.format(eat3).equals(nulli)) {
            r3_2.setVisibility(view.GONE);
        }

		
		SharedPreferences preferences = getSharedPreferences(SECURE_SETTINGS, MODE_PRIVATE);
    	if(total>=preferences.getFloat("points_limits", 0)){
    		allpoints.setBackgroundColor(Color.RED);
    	}
    	allpoints.setText("Общо точки за деня: "+String.valueOf(df.format(total)));

	}
	
	public void spinner_ready(){
		AABDatabaseManager1 db = new AABDatabaseManager1(this);
		final ArrayAdapter<Object> spinerAdapter1 = new ArrayAdapter<Object> (this,android.R.layout.simple_spinner_item,db.getdates_tracker());
    	spinerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
		stat_spinner.setAdapter(spinerAdapter1);
	}
	
	public void adds_load(){
		//stat_web.setVisibility(2);
		//stat_web.getSettings().setJavaScriptEnabled(true);
		//stat_web.setBackgroundColor(Color.TRANSPARENT);
		//stat_web.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		//stat_web.setWebViewClient(new MyWebViewClient());
		//stat_web.loadUrl("http://adds.web44.net/");
		//String html = "%3Chtml%3E%3Cbody%20style='margin:0;padding:0;'%3E%3Ciframe%20src='http://b.grabo.bg/?city=&affid=12590&size=300x250&output=iframe'%20width='300'%20height='250'%20style='width:300px;%20height:250px;%20border:0px%20solid;%20overflow:hidden;'%20border='0'%20frameborder='0'%20scrolling='no'%3E%3C/iframe%3E%3C/body%3E%3C/html%3E";
		//stat_web.loadData(html, "text/html", "utf-8");
        //IMAdView imAdView = new IMAdView(this, IMAdView.INMOBI_AD_UNIT_320X50,"a8b53472ce764ecb8ed7bd28bb1b3053");
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Location location;
        Criteria criteria = new Criteria();
        bestProvider = lm.getBestProvider(criteria, false);
        if(bestProvider!=null) {
            location = lm.getLastKnownLocation(bestProvider);
        }
        else{
            location=null;
        }
        if (location == null){
            Log.v("Location_traking","Location Not found");
            //Toast.makeText(this, "Location Not found", Toast.LENGTH_LONG).show();
        }else{
            /*geocoder = new Geocoder(this);
            try {

                user = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                postcode=user.get(0).getPostalCode();


                System.out.println(postcode);

            }catch (Exception e) {
                e.printStackTrace();
            }*/
        }

        //InMobi.setCurrentLocation(location);
        //InMobi.setLogLevel(InMobi.LOG_LEVEL.DEBUG);
        //InMobi.initialize(this, "a8b53472ce764ecb8ed7bd28bb1b3053");
        //imbanner = new IMBanner(this,"a8b53472ce764ecb8ed7bd28bb1b3053",getOptimalSlotSize(this));
        //imbanner.setRefreshInterval(30);
        //IMAdView imAdView = new IMAdView(this, IMAdView.INMOBI_AD_UNIT_320X50,"a8b53472ce764ecb8ed7bd28bb1b3053");
        final float scale = getResources().getDisplayMetrics().density;
        int width = (int) (320 * scale + 0.5f);
        int height = (int) (50 * scale + 0.5f);
        //imAdView.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        LinearLayout parent = (LinearLayout)findViewById(R.id.LinLayout1);
        adView_Admob = new AdView(this);
        adView_Admob.setAdSize(AdSize.BANNER);
        adView_Admob.setAdUnitId(AD_UNIT_ID_ADMOB);
        AdRequest adRequest1 = new AdRequest.Builder().setLocation(location).build();
        parent.addView(adView_Admob);
        adView_Admob.loadAd(adRequest1);
        //imAdView.loadNewAd();
        //add_inmobi.addView(imbanner);
	}
	
	
	private boolean haveNetworkConnection() {
	    boolean haveConnectedWifi = false;
	    boolean haveConnectedMobile = false;

	    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        assert netInfo != null;
        for (NetworkInfo ni : netInfo) {
	        if (ni.getTypeName().equalsIgnoreCase("WIFI"))
	            if (ni.isConnected())
	                haveConnectedWifi = true;
	        if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
	            if (ni.isConnected())
	                haveConnectedMobile = true;
	    }
	    return haveConnectedWifi || haveConnectedMobile;
	}

    public static Integer getOptimalSlotSize(Activity ctxt) {
        int toReturn = IMBanner.INMOBI_AD_UNIT_320X50;
        Display display = ((WindowManager) ctxt
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        double density = displayMetrics.density;
        double width = displayMetrics.widthPixels;
        double height = displayMetrics.heightPixels;
        int[][] maparray = { { IMBanner.INMOBI_AD_UNIT_728X90, 728, 90 }, {
                IMBanner.INMOBI_AD_UNIT_468X60, 468, 60 }, {
                IMBanner.INMOBI_AD_UNIT_320X50, 320, 50 } };
        for (int i = 0; i < maparray.length; i++) {
            if (maparray[i][1] * density <= width
                    && maparray[i][2] * density <= height) {
                toReturn = maparray[i][0];
            }
        }
        return toReturn;
    }

}
