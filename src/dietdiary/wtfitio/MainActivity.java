package dietdiary.wtfitio;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//import com.inmobi.androidsdk.IMAdView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.inmobi.commons.InMobi;
import com.inmobi.monetization.IMBanner;

public class MainActivity extends Activity {
	
	
	public static final String SECURE_SETTINGS = "DietDiary_sett";
    private static String AD_UNIT_ID_ADMOB = "ca-app-pub-7046352820490447/5936744513";
	
	AABDatabaseManager1 db;
	Button aplay,cancel;
	TextView total, day,points;
	TableLayout table;
	Spinner eating,food_t;
	AutoCompleteTextView food;
	EditText qantity;
    FrameLayout add_inmobi;
	WebView wv;
	Context c;
	boolean test;
   // IMAdView imAdView;
    IMBanner imbanner;
    Geocoder geocoder;
    String bestProvider;
    String postcode = null;
    List<Address> user = null;
    double lat;
    double lng;
    private AdView adView_Admob;
  // public test is;
  DecimalFormat df = new DecimalFormat("@@@#");


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

    private RefreshHandler mRedrawHandler = new RefreshHandler();
	//private dbdownload dw= new dbdownload();

	 
	class RefreshHandler extends Handler {
	    @Override
	    public void handleMessage(Message msg) {
	    
	     MainActivity.this.updateUI();
	    }

	    public void sleep(long delayMillis) {
	      this.removeMessages(0);
	      sendMessageDelayed(obtainMessage(0), delayMillis);
	      
	    }
	    public void remove(){
	    	this.removeMessages(0);
	    	
	    }
	  }

	

	  public  void updateUI(){
		float point=0;
		
	      mRedrawHandler.sleep(1000);
	     
	      
	      
		     
	      
	      if ((!food.getText().toString().equals(""))&&(qantity.getText().toString().equals(""))) {

              points.setText(df.format(point) + " точки");
          }
	      
	      else if((!food.getText().toString().equals(""))&&(!qantity.getText().toString().equals(""))){
	    	  
	    	  String grams = qantity.getText().toString();
	    	  String Points = String.valueOf(get_food_points(food.getText().toString()));
	    	  point = (Float.valueOf(grams)/100)*Float.valueOf(Points);
	    	  points.setText(df.format(point)+" точки");
	    	  
	    	 /** Toast.makeText(getBaseContext(), 
		    		  "Въведете храна", 
	                  Toast.LENGTH_SHORT).show();*/
	    	  		
	    	  
	      }
	      
	      else {
	    	  
	      }
	  }
	
	
		
	  

    @Override
    public void onCreate(Bundle savedInstanceState) {
       try{
    	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AABDatabaseManager1.setContext(getApplicationContext());
        db = new AABDatabaseManager1(this);
        SharedPreferences preferences11 = getSharedPreferences(SECURE_SETTINGS, MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences11.edit();
        //is= new test();
           
        
        
       
        
        setupViews();
        
        
       
        
        addButtonListeners();
        
      
       
       lins_setup();
        
        if(!isittoday()){
        	edit.putFloat("used_points", 0).commit();
        	
        	Intent intent = getIntent();
	    	finish();
	    	startActivity(intent);
        	
        }
        
        if ((preferences11.getFloat("wish_weight", 0)==0)||preferences11.getFloat("weight",0)==0){
        	settings();
        }



           if (haveNetworkConnection()){

               adds_load();
           }

        
       }
       catch (Exception e)
   	{
   		Log.e("ERROR", e.toString());
   		e.printStackTrace();
   	}
        
    }
    
    
   
    
    


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
    	
    	return true;
    }
    
   
    
  public boolean onOptionsItemSelected(MenuItem menu){
		switch (menu.getItemId()){
		
       case R.id.add:{
			
			add_food();
			 break;
		}
		
		case R.id.menu_settings :{
			
			settings();
			 break;		    	
	    	
		      }
		case R.id.stats :{
			Intent i = new Intent(getApplicationContext(),StatsActivity.class);
			startActivity(i);
			break;
		}
		
		}
    	
    	
    	return false;
    	
    }
    
    
    // Views setup 
    public void setupViews(){

    	aplay = (Button)findViewById(R.id.button1);
        cancel = (Button)findViewById(R.id.button2);
    	total = (TextView)findViewById(R.id.textView1);
    	//day = (TextView)findViewById(R.id.textView2);
    	//table = (TableLayout)findViewById(R.id.table);
    	food= (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView1);
    	qantity = (EditText)findViewById(R.id.editText1);
    	eating = (Spinner)findViewById(R.id.spinner1);
    	food_t = (Spinner)findViewById(R.id.spinner2);
    	points = (TextView)findViewById(R.id.textViewtochki);
        add_inmobi = (FrameLayout)findViewById(R.id.ad_container);
    	
    	
    	
    	food.setTextColor(Color.BLACK);
    	
    	
    	clear_box();
    	
    	
    	
    	
    }
    
    
    // Add Button Listeners
    
    public void addButtonListeners(){
    	
    	aplay.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SharedPreferences preferences2 = getSharedPreferences(SECURE_SETTINGS, MODE_PRIVATE);
				SharedPreferences.Editor edit = preferences2.edit();
				float temp_point;
				float point;
				
				
				if ((food.getText().toString().equals("")!=true)&&(qantity.getText().toString().equals("")!=true)){
					String eating_time = eating.getSelectedItem().toString();
					String eating_ind =String.valueOf(get_eating_ind());
					String grams = qantity.getText().toString();
					String foodname = food.getText().toString();
					String food_Id = String.valueOf(get_food_ID(food.getText().toString()));
					String Points = String.valueOf(get_food_points(food.getText().toString()));
                    point =( Float.valueOf(grams)/100)*Float.valueOf(Points);
					temp_point =preferences2.getFloat("used_points", 0);
					temp_point=temp_point+point;
					db.addRow_tracker(eating_time, eating_ind, grams, foodname, food_Id, String.valueOf(point));
					edit.putFloat("used_points", temp_point).commit();
					// TODO Auto-generated method stub
					
					Toast.makeText(getBaseContext(), 
	                        "Запаметено", 
	                        Toast.LENGTH_SHORT).show();
					clear_box();	
				}
				
				else{
					
					Toast.makeText(getBaseContext(), 
	                        "Моля въведете име на храна и количество!!", 
	                        Toast.LENGTH_SHORT).show();
				}
				
			}
		});

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clear_box();
            }
        });
    	
    	
    	
    	food.setOnItemClickListener(new OnItemClickListener()
        {  
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
            	Toast.makeText(getBaseContext(), 
                        "Моля въведете количество!!", 
                        Toast.LENGTH_SHORT).show();
            	updateUI();
        }
        });
    	
    	food_t.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				food.setText(null);
				autotext_fill();
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}});
    	
    	/**food.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				mRedrawHandler.remove();
				food.setText(null);
				qantity.setText(null);
				
			}
		});*/
    
    	qantity.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				qantity.setText(null);
			}
		});
    
    	food.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				mRedrawHandler.remove();
				food.setText(null);
				qantity.setText(null);
				points.setText("0");
				return false;
			}
		});
    
    }
    
    public void clear_box(){
    	
    	SharedPreferences preferences = getSharedPreferences(SECURE_SETTINGS, MODE_PRIVATE);
    	Float calc=preferences.getFloat("points_limits", 0)-preferences.getFloat("used_points", 0);
    	if(calc<0){
    		total.setTextColor(Color.RED);
    		total.setText("Вие надхвърлихте лимита си с "+df.format(preferences.getFloat("used_points", 0)-preferences.getFloat("points_limits", 0))+" точки!!!");
    	}
    	else{
    	total.setText("Разполагате с " +df.format(calc)+ " от общо " + df.format(preferences.getFloat("points_limits", 0)) +" точки за деня");
    	}

    	food.setText(null);
    	qantity.setText(null);
    	points.setText("0");
    	
    } 
    
    
    public String curent_date(){
    	
    Calendar c = Calendar.getInstance();
	SimpleDateFormat df = new SimpleDateFormat("HH");
    String formattedDate = df.format(c.getTime());
   
   
    return formattedDate;
    
    }
    
    
    public boolean isittoday(){
    	
    	SharedPreferences preferences = getSharedPreferences(SECURE_SETTINGS, MODE_PRIVATE);
    	Calendar c = Calendar.getInstance();
    	Calendar c1 = Calendar.getInstance();
    	int thisDay = c.get(Calendar.DAY_OF_YEAR); //You can chose something else to compare too, such as DATE..
    	long todayMillis = c.getTimeInMillis(); //We might need this a bit later.
    	long last=preferences.getLong("date",0);
    	
    	c1.setTimeInMillis(last);
    	int lastDay = c1.get(Calendar.DAY_OF_YEAR);
    	
    	
    	if( lastDay != thisDay ){
    	    //New day, update TextView and preference:
    	    SharedPreferences.Editor edit = preferences.edit();
    	    edit.putLong("date", todayMillis);
    	    edit.commit();
    	    return false;
    	}
    	
		return true;
    	
    }
   
    public int get_eating_ind(){
    	ArrayList<?> line;  
    	ArrayList<?> row;
    	int ind=0;
    	line=db.getcolumIDAsArrays_eating();
    	row=db.getRowAsArray_eating((Long) line.get( (int) eating.getSelectedItemId()));
    	ind=Integer.valueOf(row.get(2).toString());
		return ind;
    	
    }
    
    public float get_food_points(String food_name)
    {	  
    	ArrayList<?> row;
    	float points=0;
    	
    	row=db.getRowAsArrayByName_food(food_name);
        if (row.size()==0) points = 0;
        else points = Float.valueOf(row.get(2).toString());
		return points;
    }
    
    public int get_food_ID(String food_name)
    {	  
    	ArrayList<?> row;
    	int points;
        points = 0;
        row=db.getRowAsArrayByName_food(food_name);
        if (row.size()==0){
            points=0;
        }
        else{
    	points=Integer.valueOf(row.get(0).toString());
        }
		return points;
    }
    
    public int spinner_fill(int selection){
    	
    	
    	
    	ArrayList<Object> eating_eating;
    	
    	eating_eating=db.getcolumEATINGAsArrays_eating();
    	
    	ArrayAdapter<Object> spinerAdapter = new ArrayAdapter<Object> (this,android.R.layout.simple_spinner_item,eating_eating);
    	spinerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	eating.setAdapter(spinerAdapter);
    	eating.setSelection(selection);
    	int select = eating.getSelectedItemPosition();
    	return select;
    }
    
  
    public void autotext_fill(){
    	
    	ArrayList<Object> foods;
    	foods=db.getcolumfood_namesArrays_foods(food_t.getSelectedItemPosition());
    	ArrayAdapter<Object> arrayAdapter = new ArrayAdapter<Object> (this,R.layout.my_custom_dropdown,foods);
		ArrayAdapter<Object> autotextAdapter = arrayAdapter;
    	food.setAdapter(autotextAdapter);
    	
    }
    
    public void point_limit(){
    	
    	SharedPreferences preferences = getSharedPreferences(SECURE_SETTINGS, MODE_PRIVATE);
    	SharedPreferences.Editor edit = preferences.edit();
    	if (preferences.getFloat("weight", 0)>=preferences.getFloat("wish_weight", 0) ){
    		
    		edit.putFloat("points_limits", 40).commit();
    		
    	}
    	else {
    		edit.putFloat("points_limits", 70).commit();
    		
    	}
    	
    }
    
    
    public void lins_setup(){
    	
    	String food_tt [] = {"0 Агнешки продукти","1 Билки и подправки","2 Бобови култури","3 Говежди продукти","4 Дивеч","5 Заместители на храна","6 Захарни изделия","7 Зеленчуци","8 Зърнени култури","9 Колбаси","10 Консервирани храни","11 Масла и олия","12Млечни и яйчни продукти","13 Напитки","14 Печени продукти","15 Плодове","16 Продукти за бързо хранене","17 Птичи продукти","18 Рибни продукти","19 Свински продукти","20 Снаксове","21 Супи, сосове, заливки","22 Телешки продукти","23 Тестени изделия","24 Ядки и семена"};
    	
    	ArrayAdapter<Object> spinerAdapter = new ArrayAdapter<Object> (this,android.R.layout.simple_spinner_item,food_tt);
    	spinerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	food_t.setAdapter(spinerAdapter);
    	
    	if ((Integer.valueOf(curent_date())>=6)&&(Integer.valueOf(curent_date())<=9))
    		
    			{
    			spinner_fill(0);
    		
    			}
    	else if ((Integer.valueOf(curent_date())>=12)&&(Integer.valueOf(curent_date())<=14))
    	{
    		spinner_fill(1);
    	}
    	else if ((Integer.valueOf(curent_date())>=18)&&(Integer.valueOf(curent_date())<=20))
    	{
    		spinner_fill(3);
    	}
    	
    	else{
    		spinner_fill(2);
    	}
    	
    	autotext_fill();
    	
    	
    }
    
    
    //setup dialog
    
   @SuppressWarnings("deprecation")
public void settings(){
    	LayoutInflater sett = LayoutInflater.from(this);
    	View textEntryView =sett.inflate(R.layout.settings_menu, null);
    	final SharedPreferences preferences1 = getSharedPreferences(SECURE_SETTINGS, MODE_PRIVATE);
    	final AlertDialog settings = new AlertDialog.Builder(this).create();
    	
    	
    	settings.setTitle("Настройки");
    	settings.setMessage("Въведете настройки за теглото.");
    	settings.setView(textEntryView);
    	
    	
    	
    	
    	settings.setButton2("Откажи.", new DialogInterface.OnClickListener() {
    		
	    	
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent intent = getIntent();
		    	finish();
		    	startActivity(intent);	
			}
		});
    	settings.setButton3("Запиши.", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				EditText teg = (EditText)settings.findViewById(R.id.settings_text2);
				EditText wish_teg = (EditText)settings.findViewById(R.id.settings_text1);
				SharedPreferences.Editor editora = preferences1.edit();
				
				float wight = preferences1.getFloat("weight", 0);
				float w_wight = preferences1.getFloat("wish_weight", 0);
				
				if ((!teg.getText().toString().equals(""))&&(!wish_teg.getText().toString().equals(""))){
					
					editora.putFloat("weight", Float.valueOf(teg.getText().toString())).commit();
			    	editora.putFloat("wish_weight", Float.valueOf(wish_teg.getText().toString())).commit();
			    	
			    	point_limit();
					
					
					Intent intent = getIntent();
			    	finish();
			    	startActivity(intent);
				}
				
				
				else if ((teg.getText().toString().equals(""))&&(wish_teg.getText().toString().equals("")))
					
					{Toast.makeText(getBaseContext(), 
				    		  "Въведете данни за теглото!", 
			                  Toast.LENGTH_SHORT).show();
						
					}		
					
				else {
					 if((!teg.getText().toString().equals(""))&&(wish_teg.getText().toString().equals(""))) {
						
						editora.putFloat("weight", Float.valueOf(teg.getText().toString())).commit();
						point_limit();
						
						
						Intent intent = getIntent();
				    	finish();
				    	startActivity(intent);
					}
					else {
						editora.putFloat("wish_weight", Float.valueOf(wish_teg.getText().toString())).commit();
						
						point_limit();
						
						
						Intent intent = getIntent();
				    	finish();
				    	startActivity(intent);
						
					}
					}
					
					
					
				

				
			}
		});
    	
    	settings.show();
    
    	EditText wish=(EditText) settings.findViewById(R.id.settings_text1);
    	if (preferences1.getFloat("wish_weight", 0)!=0){
    		
    		
    		wish.setHint("Вашето желано тегло  е  " + String.valueOf(preferences1.getFloat("wish_weight", 0)+" Кг."));
    		
    	}
    	
    }
    
   
   public void add_food(){
	   

	   LayoutInflater sett1 = LayoutInflater.from(this);
   	   View textEntryView =sett1.inflate(R.layout.add_food, null);
   	final AlertDialog settings1 = new AlertDialog.Builder(this).create();
   	
   	String food_tt [] = {"0 Агнешки продукти","1 Билки и подправки","2 Бобови култури","3 Говежди продукти","4 Дивеч","5 Заместители на храна","6 Захарни изделия","7 Зеленчуци","8 Зърнени култури","9 Колбаси","10 Консервирани храни","11 Масла и олия","12Млечни и яйчни продукти","13 Напитки","14 Печени продукти","15 Плодове","16 Продукти за бързо хранене","17 Птичи продукти","18 Рибни продукти","19 Свински продукти","20 Снаксове","21 Супи, сосове, заливки","22 Телешки продукти","23 Тестени изделия","24 Ядки и семена"};
	final ArrayAdapter<Object> spinerAdapter1 = new ArrayAdapter<Object> (this,android.R.layout.simple_spinner_item,food_tt);
	spinerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
	
   
	
	settings1.setTitle("Нова храна");
	settings1.setView(textEntryView);

	
	settings1.setButton2("Откажи", new DialogInterface.OnClickListener() {
		
    	
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			Intent intent = getIntent();
	    	finish();
	    	startActivity(intent);	
		}
	});
	
	settings1.setButton3("Запиши", new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			
			
			EditText food_name = (EditText)settings1.findViewById(R.id.addeditText1);
			EditText food_p = (EditText)settings1.findViewById(R.id.addeditText2);
			Spinner food_t1 = (Spinner)settings1.findViewById(R.id.addspinner1);
			
			
			
			if ((!food_name.getText().toString().equals(""))&&(!food_p.getText().toString().equals(""))){
				
				db.addRow_foods(String.valueOf(food_t1.getSelectedItemId()), food_name.getText().toString(), food_p.getText().toString());
				
				Intent intent = getIntent();
		    	finish();
		    	startActivity(intent);
			}
			
			
			
				else {
					
					Toast.makeText(getBaseContext(), 
				    		  "Въведете всички дани за хранта!", 
			                  Toast.LENGTH_SHORT).show();
					
					//Intent intent = getIntent();
			    	//finish();
			    	//startActivity(intent);
					
				}
				
				
				
				
			

			
		}
	});
	
	settings1.show();
	
	 Spinner food_t1 = (Spinner)settings1.findViewById(R.id.addspinner1);
	 food_t1.setAdapter(spinerAdapter1);
	
	
	
	   
   }
   
   //Initial database input
    
    public void init(){
    	
    	SharedPreferences preferences = getSharedPreferences(SECURE_SETTINGS, MODE_PRIVATE);
        if (!preferences.getBoolean("first_boot", false)){
        	
        	firstfilltable2 ();
           // firstfilltable3 ();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("first_boot", true).commit();
           
        }
    	
    }
   
    //Input eating time into database
    
public void firstfilltable2 (){
		
	Context mContext = getApplicationContext();
		
	    
		//InputStream csvStream;
		
    	  String reader = "";
  		String date = null;
  		String value;
  		
  		
  		
  		try {


            assert mContext != null;
            InputStream	 csvStream = mContext.getResources().openRawResource(R.raw.eating);
  			
  			DataInputStream data = new DataInputStream(csvStream);
  			BufferedReader in = new BufferedReader(new InputStreamReader(data));
  			while ((reader = in.readLine())!=null){
  				String[] RowData = reader.split(",");
  				
  			    date =RowData[0];
  			    
  			    value = RowData[1];
  			    db.addRow_eating_time(date, value);
  			   
  			}
  			in.close();
  		} catch (IOException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
          
				
			
		
	}

 // Input Foods into database
public void firstfilltable3 (){
	
Context mContext = getApplicationContext();
	
    
	//InputStream csvStream;
	
	  String reader = "";
		String date = null;
		String value;
		String f_id;
		
		
		
		try {


            assert mContext != null;
            InputStream csvStream = mContext.getResources().openRawResource(R.raw.food);
			
			DataInputStream data = new DataInputStream(csvStream);
			BufferedReader in = new BufferedReader(new InputStreamReader(data));
			
			
			
			while ((reader = in.readLine())!=null){
				String[] RowData = reader.split(",");
				
				f_id=RowData[0];
				
			   date =RowData[1];
			    
			   value = RowData[2];
			   
			  
				db.addRow_foods(f_id,date,value);
			    
			}
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      
	
			
}


    public void adds_load(){

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
            geocoder = new Geocoder(this);
            try {

                user = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                postcode=user.get(0).getPostalCode();


                System.out.println(postcode);

            }catch (Exception e) {
                e.printStackTrace();
            }
        }

       // InMobi.setCurrentLocation(location);
        //InMobi.setLogLevel(InMobi.LOG_LEVEL.DEBUG);
       // InMobi.initialize(this, "a8b53472ce764ecb8ed7bd28bb1b3053");
        //imbanner = new IMBanner(this,"a8b53472ce764ecb8ed7bd28bb1b3053",getOptimalSlotSize(this));
       // imbanner.setRefreshInterval(30);
        adView_Admob = new AdView(this);
        adView_Admob.setAdSize(AdSize.BANNER);
        adView_Admob.setAdUnitId(AD_UNIT_ID_ADMOB);
        AdRequest adRequest1 = new AdRequest.Builder().setLocation(location).build();
        //IMAdView imAdView = new IMAdView(this, IMAdView.INMOBI_AD_UNIT_320X50,"a8b53472ce764ecb8ed7bd28bb1b3053");
        /*final float scale = getResources().getDisplayMetrics().density;
        int width = (int) (320 * scale + 0.5f);
        int height = (int) (50 * scale + 0.5f);
        imAdView.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        LinearLayout parent = (LinearLayout)findViewById(R.id.LinLayout1);
        parent.addView(imAdView);
        imAdView.loadNewAd();*/
        add_inmobi.addView(adView_Admob);
        adView_Admob.loadAd(adRequest1);
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
