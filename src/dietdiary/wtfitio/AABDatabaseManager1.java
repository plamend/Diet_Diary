package dietdiary.wtfitio;



import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;


public class AABDatabaseManager1 {
	
	// the Activity or Application that is creating an object from this class.
		Context context;
		private static Context context1;
		// a reference to the database used by this application/object
		private SQLiteDatabase db;
	 
		// These constants are specific to the database.  They should be 
		// changed to suit your needs.
		private final String DB_NAME = "DietDiary";
		private final int DB_VERSION = 1;
	 
		// These constants are specific to the database table.  They should be
		// changed to suit your needs.
		private final String TABLE_NAME = "foods";
		private final String TABLE_NAME2 = "eating_time";
		private final String TABLE_NAME3 = "tracker";
		private final String TABLE_ROW_ID = "id";
		private final String TABLE_ROW_ONE = "food_name";
		private final String TABLE_ROW_TWO = "points";
		private final String TABLE_ROW_THREE="food_t_id";
		private final String TABLE2_ROW_ONE ="eating";
		private final String TABLE2_ROW_TWO ="ind";
		private final String TABLE3_ROW_1_1 ="date";
		private final String TABLE3_ROW_1_2 ="month";
		private final String TABLE3_ROW_1_3 ="day";
		private final String TABLE3_ROW_2 ="eating_time";
		private final String TABLE3_ROW_3 ="eating_time_ind";
		private final String TABLE3_ROW_4 ="food_q";
		private final String TABLE3_ROW_5 ="food_ID";
		private final String TABLE3_ROW_6 ="points";
		private final String TABLE3_ROW_7 ="food_name";
	 
		public AABDatabaseManager1(Context context)
		{
			this.context = context;
	 
			// create or open the database
			CustomSQLiteOpenHelper helper = new CustomSQLiteOpenHelper(context);
			this.db = helper.getWritableDatabase();
			
		}
	 
	 
	 
	 
		/**********************************************************************
		 * ADDING A ROW TO THE DATABASE TABLE
		 * 
		 * This is an example of how to add a row to a database table
		 * using this class.  You should edit this method to suit your
		 * needs.
		 * 
		 * the key is automatically assigned by the database
		 * @param rowStringOne the value for the row's first column
		 * @param rowStringTwo the value for the row's second column 
		 * 
		 */
		
		 public static void setContext(Context mcontext) {
		        if (context1 == null)
		            context1 = mcontext;
		    }
		public void addRow_foods(String food_t_id, String food_name, String points)
		{
			// this is a key value pair holder used by android's SQLite functions
			// ContentValues values = new ContentValues();
	        //    values.put(TABLE_ROW_ONE, food_name);
			//	values.put(TABLE_ROW_TWO, points);
			//	values.put(TABLE_ROW_THREE, food_t_id);
	 
			// ask the database object to insert the new data 
				
				
			db.beginTransaction();
			
			try{
				SQLiteStatement insert = null;
				
				//db.insert(TABLE_NAME, null, values);
			
			insert = db.compileStatement("INSERT  INTO \"" +TABLE_NAME+"\" ("
		            + "\""+TABLE_ROW_ONE+"\""+ ", \""+TABLE_ROW_TWO+"\", \""+TABLE_ROW_THREE+"\") VALUES (?,?,?)");
			insert.bindString(1, food_name);
			insert.bindString(2, points);
			insert.bindString(3, food_t_id);
	        insert.execute();
	        insert.clearBindings();
	        db.setTransactionSuccessful();
	        insert.close();
			}
			catch(Exception e)
			{
				Log.e("DB ERROR", e.toString());
				e.printStackTrace();
				db.endTransaction();
			}
			finally{
			db.endTransaction();
			
			}
		}
		
		public void addRow_eating_time(String eating, String ind)
		{
			// this is a key value pair holder used by android's SQLite functions
			ContentValues values = new ContentValues();
			values.put(TABLE2_ROW_ONE, eating);
			values.put(TABLE2_ROW_TWO, ind);
	 
			// ask the database object to insert the new data 
			try{db.insert(TABLE_NAME2, null, values);}
			catch(Exception e)
			{
				Log.e("DB ERROR", e.toString());
				e.printStackTrace();
				
			}
		}
		
		public void addRow_tracker(String eating_time, String eating_ind, String grams, String foodname,String food_Id, String Points )
		{
			// this is a key value pair holder used by android's SQLite functions
			Calendar c = Calendar.getInstance();
			SimpleDateFormat dfy = new SimpleDateFormat("yyyy-MM-dd");
			//SimpleDateFormat dfm = new SimpleDateFormat("MM");
			//SimpleDateFormat dfd = new SimpleDateFormat("dd");
	        String year = dfy.format(c.getTime());
	        //String month = dfm.format(c.getTime());
	        //String day = dfd.format(c.getTime());
			
			ContentValues values = new ContentValues();
			values.put(TABLE3_ROW_1_1, year);
			//values.put(TABLE3_ROW_1_2, month);
			//values.put(TABLE3_ROW_1_3, day);
			values.put(TABLE3_ROW_2, eating_time);
			values.put(TABLE3_ROW_3, eating_ind);
			values.put(TABLE3_ROW_4, grams);
			values.put(TABLE3_ROW_7, foodname);
			values.put(TABLE3_ROW_5, food_Id);
			values.put(TABLE3_ROW_6, Points);
	 
			// ask the database object to insert the new data 
			try{db.insert(TABLE_NAME3, null, values);}
			catch(Exception e)
			{
				Log.e("DB ERROR", e.toString());
				e.printStackTrace();
			}
		}
	 
	 
	 
		/**********************************************************************
		 * DELETING A ROW FROM THE DATABASE TABLE
		 * 
		 * This is an example of how to delete a row from a database table
		 * using this class. In most cases, this method probably does
		 * not need to be rewritten.
		 * 
		 * @param rowID the SQLite database identifier for the row to delete.
		 */
		public void deleteRow(long rowID, String Table_n)
		{
			// ask the database manager to delete the row of given id
			try {db.delete(Table_n, TABLE_ROW_ID + "=" + rowID, null);}
			catch (Exception e)
			{
				Log.e("DB ERROR", e.toString());
				e.printStackTrace();
			}
		}
	 
		/**********************************************************************
		 * UPDATING A ROW IN THE DATABASE TABLE
		 * 
		 * This is an example of how to update a row in the database table
		 * using this class.  You should edit this method to suit your needs.
		 * 
		 * @param rowID the SQLite database identifier for the row to update.
		 * @param rowStringOne the new value for the row's first column
		 * @param rowStringTwo the new value for the row's second column 
		 */ 
		public void updateRow_tracker(long rowID, String eating_time, Integer eating_ind, Float grams,String foodname, Integer food_ID, Float Points)
		{
			// this is a key value pair holder used by android's SQLite functions
			Calendar c = Calendar.getInstance();
			SimpleDateFormat dfy = new SimpleDateFormat("yyyy-MM-dd");
			//SimpleDateFormat dfm = new SimpleDateFormat("MM");
			//SimpleDateFormat dfd = new SimpleDateFormat("dd");
	        String year = dfy.format(c.getTime());
	        //String month = dfm.format(c.getTime());
	       // String day = dfd.format(c.getTime());
			
			ContentValues values = new ContentValues();
			values.put(TABLE3_ROW_1_1, year);
			//values.put(TABLE3_ROW_1_2, month);
			//values.put(TABLE3_ROW_1_3, day);
			values.put(TABLE3_ROW_2, eating_time);
			values.put(TABLE3_ROW_3, eating_ind);
			values.put(TABLE3_ROW_4, grams);
			values.put(TABLE3_ROW_7, foodname);
			values.put(TABLE3_ROW_5, food_ID);
			values.put(TABLE3_ROW_6, Points);
	 
			// ask the database object to update the database row of given rowID
			try {db.update(TABLE_NAME3, values, TABLE_ROW_ID + "=" + rowID, null);}
			catch (Exception e)
			{
				Log.e("DB Error", e.toString());
				e.printStackTrace();
			}
		}
	 
		/**********************************************************************
		 * RETRIEVING A ROW FROM THE DATABASE TABLE
		 * 
		 * This is an example of how to retrieve a row from a database table
		 * using this class.  You should edit this method to suit your needs.
		 * 
		 * @param rowID the id of the row to retrieve
		 * @return an array containing the data from the row
		 */
		public ArrayList<Object> getRowAsArray_food(long rowID)
		{
			// create an array list to store data from the database row.
			// I would recommend creating a JavaBean compliant object 
			// to store this data instead.  That way you can ensure
			// data types are correct.
			ArrayList<Object> rowArray = new ArrayList<Object>();
			Cursor cursor;
	 
			try
			{
				// this is a database call that creates a "cursor" object.
				// the cursor object store the information collected from the
				// database and is used to iterate through the data.
				cursor = db.query
				(
						TABLE_NAME,
						new String[] { TABLE_ROW_ID, TABLE_ROW_ONE, TABLE_ROW_TWO },
						TABLE_ROW_ID + "=" + rowID,
						null, null, null, null, null
				);
	 
				// move the pointer to position zero in the cursor.
				cursor.moveToFirst();
	 
				// if there is data available after the cursor's pointer, add
				// it to the ArrayList that will be returned by the method.
				if (!cursor.isAfterLast())
				{
					do
					{
						rowArray.add(cursor.getLong(0));
						rowArray.add(cursor.getString(1));
						rowArray.add(cursor.getString(2));
					}
					while (cursor.moveToNext());
				}
	 
				// let java know that you are through with the cursor.
				cursor.close();
			}
			catch (SQLException e) 
			{
				Log.e("DB ERROR", e.toString());
				e.printStackTrace();
			}
	 
			// return the ArrayList containing the given row from the database.
			return rowArray;
		}
	 
		
		
		
		public ArrayList<Object> getRowAsArray_eating(long rowID)
		{
			// create an array list to store data from the database row.
			// I would recommend creating a JavaBean compliant object 
			// to store this data instead.  That way you can ensure
			// data types are correct.
			ArrayList<Object> rowArray = new ArrayList<Object>();
			Cursor cursor;
	 
			try
			{
				// this is a database call that creates a "cursor" object.
				// the cursor object store the information collected from the
				// database and is used to iterate through the data.
				cursor = db.query
				(
						TABLE_NAME2,
						new String[] { TABLE_ROW_ID, TABLE2_ROW_ONE, TABLE2_ROW_TWO },
						TABLE_ROW_ID + "=" + rowID,
						null, null, null, null, null
				);
	 
				// move the pointer to position zero in the cursor.
				cursor.moveToFirst();
	 
				// if there is data available after the cursor's pointer, add
				// it to the ArrayList that will be returned by the method.
				if (!cursor.isAfterLast())
				{
					do
					{
						rowArray.add(cursor.getLong(0));
						rowArray.add(cursor.getString(1));
						rowArray.add(cursor.getString(2));
					}
					while (cursor.moveToNext());
				}
	 
				// let java know that you are through with the cursor.
				cursor.close();
			}
			catch (SQLException e) 
			{
				Log.e("DB ERROR", e.toString());
				e.printStackTrace();
			}
	 
			// return the ArrayList containing the given row from the database.
			return rowArray;
		}
	 
	 
		public ArrayList<Object> getRowAsArray_tracker(long rowID)
		{
			// create an array list to store data from the database row.
			// I would recommend creating a JavaBean compliant object 
			// to store this data instead.  That way you can ensure
			// data types are correct.
			ArrayList<Object> rowArray = new ArrayList<Object>();
			Cursor cursor;
	 
			try
			{
				// this is a database call that creates a "cursor" object.
				// the cursor object store the information collected from the
				// database and is used to iterate through the data.
				cursor = db.query
				(
						TABLE_NAME3,
						new String[] { TABLE_ROW_ID, TABLE3_ROW_1_1,TABLE3_ROW_2,TABLE3_ROW_3,TABLE3_ROW_4,TABLE3_ROW_7,TABLE3_ROW_5,TABLE3_ROW_6 },
						TABLE_ROW_ID + "=" + rowID,
						null, null, null, null, null
				);
	 
				// move the pointer to position zero in the cursor.
				cursor.moveToFirst();
	 
				// if there is data available after the cursor's pointer, add
				// it to the ArrayList that will be returned by the method.
				if (!cursor.isAfterLast())
				{
					do
					{
						rowArray.add(cursor.getLong(0));
						rowArray.add(cursor.getString(1));
						rowArray.add(cursor.getString(2));
						rowArray.add(cursor.getString(3));
						rowArray.add(cursor.getString(4));
						rowArray.add(cursor.getString(5));
						rowArray.add(cursor.getString(6));
						rowArray.add(cursor.getString(7));
						
					}
					while (cursor.moveToNext());
				}
	 
				// let java know that you are through with the cursor.
				cursor.close();
			}
			catch (SQLException e) 
			{
				Log.e("DB ERROR", e.toString());
				e.printStackTrace();
			}
	 
			// return the ArrayList containing the given row from the database.
			return rowArray;
		}
		/**retreaving columns from food table as Array*/
		
		
		public ArrayList<Object> getRowAsArrayByName_food(String food_name)
		{
			// create an array list to store data from the database row.
			// I would recommend creating a JavaBean compliant object 
			// to store this data instead.  That way you can ensure
			// data types are correct.
			ArrayList<Object> rowArray = new ArrayList<Object>();
			Cursor cursor;
	 
			try
			{
				// this is a database call that creates a "cursor" object.
				// the cursor object store the information collected from the
				// database and is used to iterate through the data.
				cursor = db.query
				(
						TABLE_NAME,
						new String[] { TABLE_ROW_ID, TABLE_ROW_ONE, TABLE_ROW_TWO },
						TABLE_ROW_ONE + "='" + food_name+"'",
						null, null, null, null, null
				);
	 
				// move the pointer to position zero in the cursor.
				cursor.moveToFirst();
	 
				// if there is data available after the cursor's pointer, add
				// it to the ArrayList that will be returned by the method.
				if (!cursor.isAfterLast())
				{
					do
					{
						rowArray.add(cursor.getLong(0));
						rowArray.add(cursor.getString(1));
						rowArray.add(cursor.getString(2));
					}
					while (cursor.moveToNext());
				}
	 
				// let java know that you are through with the cursor.
				cursor.close();
			
			
			
			}
			catch (SQLException e) 
			{
				Log.e("DB ERROR", e.toString());
				e.printStackTrace();
			}
	 
			// return the ArrayList containing the given row from the database.
			return rowArray;
		}
		
		
		public ArrayList<Object> getcolumIDAsArrays_food()
		{
			// create an ArrayList that will hold all of the data collected from
			// the database.
			ArrayList<Object> dataArrays = new ArrayList<Object>();
	 
			// this is a database call that creates a "cursor" object.
			// the cursor object store the information collected from the
			// database and is used to iterate through the data.
			Cursor cursor;
	 
			try
			{
				// ask the database object to create the cursor.
				cursor = db.query(
						TABLE_NAME,
						new String[]{ TABLE_ROW_ID},
						null, null, null, null, null
				);
	 
				// move the cursor's pointer to position zero.
				cursor.moveToFirst();
	 
				// if there is data after the current cursor position, add it
				// to the ArrayList.
				if (!cursor.isAfterLast())
				{
					do
					{
						
	 
						
						dataArrays.add(cursor.getLong(0));
						
	 
						
					}
					// move the cursor's pointer up one position.
					while (cursor.moveToNext());
				}
			
				cursor.close();
			}
			catch (SQLException e)
			{
				Log.e("DB Error", e.toString());
				e.printStackTrace();
			}
	 
			// return the ArrayList that holds the data collected from
			// the database.
			return dataArrays;
		}
		public ArrayList<Object> getcolumfood_namesArrays_foods( int food_t_id)
		{
			// create an ArrayList that will hold all of the data collected from
			// the database.
			ArrayList<Object> dataArrays = new ArrayList<Object>();
	 
			// this is a database call that creates a "cursor" object.
			// the cursor object store the information collected from the
			// database and is used to iterate through the data.
			Cursor cursor;
	 
			try
			{
				// ask the database object to create the cursor.
				cursor = db.query(
						TABLE_NAME,
						new String[]{ TABLE_ROW_ONE},
						TABLE_ROW_THREE + "='" + food_t_id+"'",null, null, null, null, null
				);
	 
				// move the cursor's pointer to position zero.
				cursor.moveToFirst();
	 
				// if there is data after the current cursor position, add it
				// to the ArrayList.
				if (!cursor.isAfterLast())
				{
					do
					{
						
	 
						
						dataArrays.add(cursor.getString(0));
						
	 
						
					}
					// move the cursor's pointer up one position.
					while (cursor.moveToNext());
				}
			
				cursor.close();
			}
			catch (SQLException e)
			{
				Log.e("DB Error", e.toString());
				e.printStackTrace();
			}
	 
			// return the ArrayList that holds the data collected from
			// the database.
			return dataArrays;
		}
		/** Retrеavинг column from Eating tabel as Array*/
		
		
		public ArrayList<Object> getcolumEATINGAsArrays_eating()
		{
			// create an ArrayList that will hold all of the data collected from
			// the database.
			ArrayList<Object> dataArrays = new ArrayList<Object>();
	 
			// this is a database call that creates a "cursor" object.
			// the cursor object store the information collected from the
			// database and is used to iterate through the data.
			Cursor cursor;
	 
			try
			{
				// ask the database object to create the cursor.
				cursor = db.query(
						TABLE_NAME2,
						new String[]{ TABLE2_ROW_ONE},
						null, null, null, null, null
				);
	 
				// move the cursor's pointer to position zero.
				cursor.moveToFirst();
	 
				// if there is data after the current cursor position, add it
				// to the ArrayList.
				if (!cursor.isAfterLast())
				{
					do
					{
						
	 
						
						dataArrays.add(cursor.getString(0));
						
	 
						
					}
					// move the cursor's pointer up one position.
					while (cursor.moveToNext());
				}
			
				cursor.close();
			}
			catch (SQLException e)
			{
				Log.e("DB Error", e.toString());
				e.printStackTrace();
			}
	 
			// return the ArrayList that holds the data collected from
			// the database.
			return dataArrays;
		}
		
		
		public ArrayList<Object> getdates_tracker(){
			ArrayList<Object> dataArrays = new ArrayList<Object>();
			
			Cursor cursor;
			 
			try
			{
				// ask the database object to create the cursor.
				/**cursor = db.query(
						TABLE_NAME3,
						new String[]{ TABLE3_ROW_1_1},
						null, null, null, null, null
				);*/
				
				
				cursor =	db.query(true, TABLE_NAME3, new String[]{"date"}, null, null, null, null, null, null);	
	//cursor =  db.query(true, TABLE_NAME3, new String[]{"year"},null, null, null, null, null, null, null);
				// move the cursor's pointer to position zero.
				cursor.moveToFirst();
	 
				// if there is data after the current cursor position, add it
				// to the ArrayList.
				if (!cursor.isAfterLast())
				{
					do
					{
						
	 
						
						dataArrays.add(cursor.getString(0));
						
	 
						
					}
					// move the cursor's pointer up one position.
					while (cursor.moveToNext());
					
				}
			
				cursor.close();
			}
			catch (SQLException e)
			{
				Log.e("DB Error", e.toString());
				e.printStackTrace();
			}

            Collections.sort(dataArrays,Collections.reverseOrder());
			return dataArrays;
		}
		
		public ArrayList<Object> getcolumIDAsArrays_eating()
		{
			// create an ArrayList that will hold all of the data collected from
			// the database.
			ArrayList<Object> dataArrays = new ArrayList<Object>();
	 
			// this is a database call that creates a "cursor" object.
			// the cursor object store the information collected from the
			// database and is used to iterate through the data.
			Cursor cursor;
	 
			try
			{
				// ask the database object to create the cursor.
				cursor = db.query(
						TABLE_NAME2,
						new String[]{ TABLE_ROW_ID},
						null, null, null, null, null
				);
	 
				// move the cursor's pointer to position zero.
				cursor.moveToFirst();
	 
				// if there is data after the current cursor position, add it
				// to the ArrayList.
				if (!cursor.isAfterLast())
				{
					do
					{
						
	 
						
						dataArrays.add(cursor.getLong(0));
						
	 
						
					}
					// move the cursor's pointer up one position.
					while (cursor.moveToNext());
				}
			
				cursor.close();
			}
			catch (SQLException e)
			{
				Log.e("DB Error", e.toString());
				e.printStackTrace();
			}
	 
			// return the ArrayList that holds the data collected from
			// the database.
			return dataArrays;
		}
		
		
	 
		/**********************************************************************
		 * RETRIEVING ALL ROWS FROM THE DATABASE TABLE
		 * 
		 * This is an example of how to retrieve all data from a database
		 * table using this class.  You should edit this method to suit your
		 * needs.
		 * 
		 * the key is automatically assigned by the database
		 */
	 
		public ArrayList<ArrayList<Object>> getAllRowsAsArrays_food()
		{
			// create an ArrayList that will hold all of the data collected from
			// the database.
			ArrayList<ArrayList<Object>> dataArrays = new ArrayList<ArrayList<Object>>();
	 
			// this is a database call that creates a "cursor" object.
			// the cursor object store the information collected from the
			// database and is used to iterate through the data.
			Cursor cursor;
	 
			try
			{
				// ask the database object to create the cursor.
				cursor = db.query(
						TABLE_NAME,
						new String[]{TABLE_ROW_ID, TABLE_ROW_ONE, TABLE_ROW_TWO},
						null, null, null, null, null
				);
	 
				// move the cursor's pointer to position zero.
				cursor.moveToFirst();
	 
				// if there is data after the current cursor position, add it
				// to the ArrayList.
				if (!cursor.isAfterLast())
				{
					do
					{
						ArrayList<Object> dataList = new ArrayList<Object>();
	 
						dataList.add(cursor.getLong(0));
						dataList.add(cursor.getString(1));
						dataList.add(cursor.getString(2));
	 
						dataArrays.add(dataList);
					}
					// move the cursor's pointer up one position.
					while (cursor.moveToNext());
				}
				cursor.close();
			
			}
			catch (SQLException e)
			{
				Log.e("DB Error", e.toString());
				e.printStackTrace();
			}
	 
			// return the ArrayList that holds the data collected from
			// the database.
			return dataArrays;
		}
	 
		public ArrayList<ArrayList<Object>> getAllRowsAsArrays_eating()
		{
			// create an ArrayList that will hold all of the data collected from
			// the database.
			ArrayList<ArrayList<Object>> dataArrays = new ArrayList<ArrayList<Object>>();
	 
			// this is a database call that creates a "cursor" object.
			// the cursor object store the information collected from the
			// database and is used to iterate through the data.
			Cursor cursor;
	 
			try
			{
				// ask the database object to create the cursor.
				cursor = db.query(
						TABLE_NAME2,
						new String[]{TABLE_ROW_ID, TABLE2_ROW_ONE, TABLE2_ROW_TWO},
						null, null, null, null, null
				);
	 
				// move the cursor's pointer to position zero.
				cursor.moveToFirst();
	 
				// if there is data after the current cursor position, add it
				// to the ArrayList.
				if (!cursor.isAfterLast())
				{
					do
					{
						ArrayList<Object> dataList = new ArrayList<Object>();
	 
						dataList.add(cursor.getLong(0));
						dataList.add(cursor.getString(1));
						dataList.add(cursor.getString(2));
	 
						dataArrays.add(dataList);
					}
					// move the cursor's pointer up one position.
					while (cursor.moveToNext());
				}
				cursor.close();
				 
			}
			catch (SQLException e)
			{
				Log.e("DB Error", e.toString());
				e.printStackTrace();
			}
			
			// return the ArrayList that holds the data collected from
			// the database.
			return dataArrays;
		}
		
		
		public ArrayList<ArrayList<Object>> getAllRowsAsArrays_tracker()
		{
			// create an ArrayList that will hold all of the data collected from
			// the database.
			ArrayList<ArrayList<Object>> dataArrays = new ArrayList<ArrayList<Object>>();
	 
			// this is a database call that creates a "cursor" object.
			// the cursor object store the information collected from the
			// database and is used to iterate through the data.
			Cursor cursor;
	 
			try
			{
				// ask the database object to create the cursor.
				cursor = db.query(
						TABLE_NAME3,
						new String[]{ TABLE_ROW_ID, TABLE3_ROW_1_1,TABLE3_ROW_2,TABLE3_ROW_3,TABLE3_ROW_4,TABLE3_ROW_5,TABLE3_ROW_6},
						null, null, null, null, null
				);
	 
				// move the cursor's pointer to position zero.
				cursor.moveToFirst();
	 
				// if there is data after the current cursor position, add it
				// to the ArrayList.
				if (!cursor.isAfterLast())
				{
					do
					{
						ArrayList<Object> dataList = new ArrayList<Object>();
	 
						dataList.add(cursor.getLong(0));
						dataList.add(cursor.getString(1));
						dataList.add(cursor.getString(2));
						dataList.add(cursor.getString(3));
						dataList.add(cursor.getString(4));
						dataList.add(cursor.getString(5));
						dataList.add(cursor.getString(6));
						
	 
						dataArrays.add(dataList);
					}
					// move the cursor's pointer up one position.
					while (cursor.moveToNext());
				}
				cursor.close();
			
			}
			
			catch (SQLException e)
			{
				Log.e("DB Error", e.toString());
				e.printStackTrace();
			}
	 
			// return the ArrayList that holds the data collected from
			// the database.
			return dataArrays;
		}
		public ArrayList<ArrayList<Object>> getStatsRowsAsArrays_tracker(String food_r_date)
		{
			// create an ArrayList that will hold all of the data collected from
			// the database.
			ArrayList<ArrayList<Object>> dataArrays = new ArrayList<ArrayList<Object>>();
	 
			// this is a database call that creates a "cursor" object.
			// the cursor object store the information collected from the
			// database and is used to iterate through the data.
			Cursor cursor;
	 
			try
			{
				// ask the database object to create the cursor.
				cursor = db.query(
						TABLE_NAME3,
						new String[]{ TABLE3_ROW_3,TABLE3_ROW_6,TABLE3_ROW_7},
						TABLE3_ROW_1_1 + "='" + food_r_date+"'",null, null, null, null, null
				);
				
				
				// move the cursor's pointer to position zero.
				cursor.moveToFirst();
	 
				// if there is data after the current cursor position, add it
				// to the ArrayList.
				if (!cursor.isAfterLast())
				{
					do
					{
						ArrayList<Object> dataList = new ArrayList<Object>();
	 
						dataList.add(cursor.getInt(0));
						dataList.add(cursor.getFloat(1));
						dataList.add(cursor.getString(2));
						
						
	 
						dataArrays.add(dataList);
					}
					// move the cursor's pointer up one position.
					while (cursor.moveToNext());
				}
				cursor.close();
			
			}
			
			catch (SQLException e)
			{
				Log.e("DB Error", e.toString());
				e.printStackTrace();
			}
	 
			// return the ArrayList that holds the data collected from
			// the database.
			return dataArrays;
		}
	 
		/**********************************************************************
		 * THIS IS THE BEGINNING OF THE INTERNAL SQLiteOpenHelper SUBCLASS.
		 * 
		 * I MADE THIS CLASS INTERNAL SO I CAN COPY A SINGLE FILE TO NEW APPS 
		 * AND MODIFYING IT - ACHIEVING DATABASE FUNCTIONALITY.  ALSO, THIS WAY 
		 * I DO NOT HAVE TO SHARE CONSTANTS BETWEEN TWO FILES AND CAN
		 * INSTEAD MAKE THEM PRIVATE AND/OR NON-STATIC.  HOWEVER, I THINK THE
		 * INDUSTRY STANDARD IS TO KEEP THIS CLASS IN A SEPARATE FILE.
		 *********************************************************************/
	 
		/**
		 * This class is designed to check if there is a database that currently
		 * exists for the given program.  If the database does not exist, it creates
		 * one.  After the class ensures that the database exists, this class
		 * will open the database for use.  Most of this functionality will be
		 * handled by the SQLiteOpenHelper parent class.  The purpose of extending
		 * this class is to tell the class how to create (or update) the database.
		 * 
		 * @author Randall Mitchell
		 *
		 */
		private class CustomSQLiteOpenHelper extends SQLiteOpenHelper
		{
			public CustomSQLiteOpenHelper(Context context)
			{
				super(context, DB_NAME, null, DB_VERSION);
			}
	 
			@Override
			public void onCreate(SQLiteDatabase db)
			{
				// This string is used to create the database.  It should
				// be changed to suit your needs.
				
				String reader = "";
				String date = null;
				String value;
				String f_id;
				
				
				String newTableQueryString = "create table " +
											TABLE_NAME +
											" (" +
											TABLE_ROW_ID + " integer primary key autoincrement not null," +
											TABLE_ROW_ONE + " text," +
											TABLE_ROW_TWO + " float," +
											TABLE_ROW_THREE + " integer"+
											");";
				String newTableQueryString2 = "create table " +
						TABLE_NAME2 +
						" (" +
						TABLE_ROW_ID + " integer primary key autoincrement not null," +
						TABLE2_ROW_ONE + " text," +
						TABLE2_ROW_TWO + " integer unique" +
						");";
				
				String newTableQueryString3 = "create table " +
						TABLE_NAME3 +
						" (" +
						TABLE_ROW_ID + " integer primary key autoincrement not null," +
						TABLE3_ROW_1_1 + " text," +
						TABLE3_ROW_2 + " text," +
						TABLE3_ROW_3 + " integer,"+
						TABLE3_ROW_4 + " float," +
						TABLE3_ROW_7 + " text,"+
						TABLE3_ROW_5 + " integer,"+
						TABLE3_ROW_6 + " float"+
						");";
				// execute the query string to the database.
				db.execSQL(newTableQueryString);
				db.execSQL(newTableQueryString2);
				db.execSQL(newTableQueryString3);
				
				InputStream csvStream = context1.getResources().openRawResource(R.raw.food);
				DataInputStream data = new DataInputStream(csvStream);
				BufferedReader in = new BufferedReader(new InputStreamReader(data));
				
				db.beginTransaction();
				
				try{
					SQLiteStatement insert = null;
					
					//db.insert(TABLE_NAME, null, values);
				
				insert = db.compileStatement("INSERT  INTO \"" +TABLE_NAME+"\" ("
			            + "\""+TABLE_ROW_ONE+"\""+ ", \""+TABLE_ROW_TWO+"\", \""+TABLE_ROW_THREE+"\") VALUES (?,?,?)");
				
				
				while ((reader = in.readLine())!=null){
					String[] RowData = reader.split(",");
					
					f_id=RowData[0];
					
				   date =RowData[1];
				    
				   value = RowData[2];
				   
				  
					
				    
				
				insert.bindString(1, date);
				insert.bindString(2, value);
				insert.bindString(3, f_id);
		        insert.execute();
		        insert.clearBindings();
				}
		        
		        db.setTransactionSuccessful();
		        insert.close();
				}
				catch(Exception e)
				{
					Log.e("DB ERROR", e.toString());
					e.printStackTrace();
					db.endTransaction();
				}
				finally{
				db.endTransaction();
				
				}
				
				 reader = "";
				 date = null;
				 value=null;
				 f_id=null;
				
				 InputStream csvStream1 = context1.getResources().openRawResource(R.raw.eating);
					DataInputStream data1 = new DataInputStream(csvStream1);
					BufferedReader in1 = new BufferedReader(new InputStreamReader(data1));
					
					db.beginTransaction();
					
					try{
						SQLiteStatement insert = null;
						
						//db.insert(TABLE_NAME, null, values);
					
					insert = db.compileStatement("INSERT  INTO \"" +TABLE_NAME2+"\" ("
				            + "\""+TABLE2_ROW_ONE+"\""+ ", \""+TABLE2_ROW_TWO+"\") VALUES (?,?)");
			
					
					while ((reader = in1.readLine())!=null){
						String[] RowData = reader.split(",");
						
						
						
					   date =RowData[0];
					    
					   value = RowData[1];
					   
					  
						
					    
					
					insert.bindString(1, date);
					insert.bindString(2, value);
					
			        insert.execute();
			        insert.clearBindings();
					}
			        
			        db.setTransactionSuccessful();
			        insert.close();
					}
					catch(Exception e)
					{
						Log.e("DB ERROR", e.toString());
						e.printStackTrace();
						db.endTransaction();
					}
					finally{
					db.endTransaction();
					
					}
			
			}
	 
	 
			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
			{
				// NOTHING TO DO HERE. THIS IS THE ORIGINAL DATABASE VERSION.
				// OTHERWISE, YOU WOULD SPECIFIY HOW TO UPGRADE THE DATABASE.
			}
		}

}
