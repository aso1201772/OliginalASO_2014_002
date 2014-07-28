package jp.ac.st.asojuku.original2014002;

import android.app.Activity;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

/**
 * @author student
 *
 */
public class MaintenanceActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener{


	//
    SQLiteDatabase sdb = null;
    //
    MySQLiteOpenHelper helper = null;

    //
    int selectedID = -1;
    //
    int lastPosition = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	//
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.maintenanceactivity);
    }

    @Override
    protected void onResume() {
    	//TODO
    	super.onResume();

    	//
    	Button btnDelete = (Button)findViewById(R.id.btnDELETE);
    	Button btnMainte_Back = (Button)findViewById(R.id.btnMAINTE_BACK);
    	ListView lstHitokoto = (ListView)findViewById(R.id.LvHITOKOTO);

    	//
    	btnDelete.setOnClickListener(this);
    	btnMainte_Back.setOnClickListener(this);

    	//
    	lstHitokoto.setOnClickListener(this);

    	//
    	this.setDBValuetoList(lstHitokoto);

    }



	/**
	 * 引数のListViewにDBのデータをセット
	 * @param lstHitokoto 対象となるListView
	 */
	private void setDBValuetoList(ListView lstHitokoto){

		SQLiteCursor cursor = null;

		//
		if(sdb == null) {
			helper = new MySQLiteOpenHelper(getApplicationContext());
		}
		try{
			sdb = helper.getWritableDatabase();
		}catch(SQLiteException e){
			//
			Log.e("ERROR", e.toString());
		}
		//
		cursor = this.helper.selectHitokotoList(sdb);

		//
		int db_layout = android.R.layout.simple_list_item_activated_1;
		//
		String[]from = {"phrase"};
		//
		int[] to = new int[]{android.R.id.text1};

		//
		//
		SimpleCursorAdapter adapter =
			new SimpleCursorAdapter(this,db_layout,cursor,from,to,0);

		//
		lstHitokoto.setAdapter(adapter);

	}


	/**
	 * @param AdapterView<?> parent クリックしたListView
	 * @param View view クリックしたListViewの中の各行
	 * @param int position 何行目をクリックしたか
	 * @param long viewid 未使用
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long viewid) {
	//

		//
		if (this.selectedID!=-1){
			parent.getChildAt(this.lastPosition).setBackgroundColor(0);
		}
		//
		view.setBackgroundColor(android.graphics.Color.LTGRAY);

		//
		SQLiteCursor cursor = (SQLiteCursor)parent.getItemAtPosition(position);
		//
		this.selectedID = cursor.getInt(cursor.getColumnIndex("_id"));
		//
		this.lastPosition = position;
	}


    @Override
    public void onClick(View v) {
    	//

    	switch(v.getId()) { //
    	case R.id.btnDELETE: //

	    	//
	    	if(this.selectedID != -1){
	    		this.deleteFromHitokoto(this.selectedID);
	    		ListView lstHitokoto = (ListView)findViewById(R.id.LvHITOKOTO);
	    		//
	    		this.setDBValuetoList(lstHitokoto);
	    		//
	    		this.selectedID = -1;
	    		this.lastPosition = -1;
	    	}
	    	else{
	    		//
	    		Toast.makeText(MaintenanceActivity.this, "削除する行を選んでください", Toast.LENGTH_SHORT).show();
	    	}
	    break;
    	case R.id.btnMAINTE_BACK: //
    		//
    		finish();
    	}
    }

    private void deleteFromHitokoto(int id){
    	//
    	if(sdb == null) {
    		helper = new MySQLiteOpenHelper(getApplicationContext());
    	}
    	try{
    		sdb = helper.getWritableDatabase();
    	}catch(SQLiteException e){
    		//
    		Log.e("ERROR", e.toString());
    	}
    	//
    	this.helper.deleteHitokoto(sdb, id);
    }












}
