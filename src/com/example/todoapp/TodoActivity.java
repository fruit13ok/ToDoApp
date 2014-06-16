package com.example.todoapp;
//this project need a auto gen project call appcompat_v7
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
/*
 * 1).
 * in activity_todo.xml
 * create ListView, EditText, and Button
 * 
 * in TodoActivity.java
 * create and populate ArrayList
 * create adapter to translate data to View
 * create and access/handle ListView  
 * 		populate it by our adapter
 * 
 * 2). start at video 22m0s
 * in activity_todo.xml
 * create onClick method for the Button to add new item
 * 
 * in TodoActivity.java
 * create and access/handle EditText
 * create onClick method in Java to access onClick method in XML
 * 		onClick will add the input from EditText to the list of item
 * create a long click listener method to remove item, put inside onCreate()
 * 
 * 3). start at video 32m10s
 * download commons-io-2.4.jar from Canvas or commons.apache.org
 * 		drag it into libs folder
 * in TodoActivity.java
 * create readItems() and writeItems() for load and save
 * 		readItems() use the commons-io-2.4.jar library to access the file
 * remove the populateArrayItems()
 * 		replace the method call by readItems() so it load on beginning
 * when add or remove item call writeItems()
 * 
 * you can find the file todo.txt in DDMS -> data -> data -> our App path -> files
 * 		when pull it to your computer from AVD or phone, it look like a String
 */
public class TodoActivity extends Activity
{
	//Java ArrayList to store items
	private ArrayList<String> todoItems;
	//use ArrayaAdapter to translate array of objects or Strings to views
	private ArrayAdapter<String> todoAdapter;
	private ListView lvItems;
	private EditText etNewitem;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo);
		//access/handle EditText
		etNewitem = (EditText) findViewById(R.id.etNewitem);
		//access/handle ListView
		lvItems = (ListView) findViewById(R.id.lvItems);
//		populateArrayItems();
		//replace populate by load from file when this App run
		readItems();
		//3 arguments
		//context:	Activity, this or getBaseContext()
		//resource:	the View to display the item, we use build in View
		//	have create our own view and get it by R.something,
		//	or use build in view, the View is a TextView
		//		http://stackoverflow.com/questions/3663745/what-is-android-r-layout-simple-list-item-1
		//objects:	array of items to display in list
		todoAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, todoItems);
		//attach our adapter to ListView
		lvItems.setAdapter(todoAdapter);
		//can add item through adapter
		//adapter will also automatically update View
		//todoAdapter.add("Item 4");
		
		setupListViewListener();
	}

//	private void populateArrayItems()
//	{
//		todoItems = new ArrayList<String>();
//		todoItems.add("Item 1");
//		todoItems.add("Item 2");
//		todoItems.add("Item 3");
//	}
	
	//need to be public to listen to click, 
	//need View of the Button I pressed, name it v
	public void onAddedItem(View v)
	{
		String itemText = etNewitem.getText().toString();
		//add input from EditText to adapter/ArrayList
		todoAdapter.add(itemText);
		//clear the EditText
		etNewitem.setText("");
		//save to file add item
		writeItems();
	}
	
	//listen to longClick to delete item, 
	//use anonymous inner class OnItemLongClickListener
	private void setupListViewListener()
    {
		lvItems.setOnItemLongClickListener(new OnItemLongClickListener()
		{
			//4 arguments
			//adapter:	The AdapterView/ListView where the click happened
			//item:		The view within the ListView that was clicked
			//position:	The position of the view in the list
			//id:		The row id of the item that was clicked
			@Override
            public boolean onItemLongClick(AdapterView<?> adapter, View item, int position, long id)
            {
				//remove the data of actual ArrayList,
				//tell adapter to notify the UI Thread, by look at ArrayList again, and repopulate the view
				todoItems.remove(position);
				todoAdapter.notifyDataSetChanged();
				//save to file remove item
				writeItems();
	            return true;
            }
			
		});
    }
	
	//load, get access to a file
	private void readItems()
	{
		//access android file system of this App
		//return absolute path where file can be created
		File fileDir = getFilesDir();
		//create a file base on that path
		File todoFile = new File(fileDir, "todo.txt");
		//when there is a file, load stored item to ArrayList
		//not use todoAdapter, try it out later
		try
		{
			//set the ArrayList to the loaded result
			//FileUtils is from commons-io-2.4.jar
			todoItems = new ArrayList<String>(FileUtils.readLines(todoFile));
		}
		//when there was no file, create a empty ArrayList
		catch(IOException e)
		{
			todoItems = new ArrayList<String>();
		}
	}
	
	//save, write to file
	private void writeItems()
	{
		File fileDir = getFilesDir();
		File todoFile = new File(fileDir, "todo.txt");
		try
		{
			//automatically serialize items from ArrayList into todoFile
			//write todoItems to todoFile
			FileUtils.writeLines(todoFile, todoItems);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
