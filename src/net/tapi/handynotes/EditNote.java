package net.tapi.handynotes;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;

public class EditNote extends Activity {
	private Integer widgetId;
	private EditText editText;
	private NotesDbAdapter db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState);
        
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            widgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, 
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
     
		setContentView(R.layout.edit_note);

        editText = (EditText) findViewById(R.id.editNoteText);
        
	    db = new NotesDbAdapter(this);

        db.open();
        String text = db.showNoteText(widgetId);
        
        editText.setText(text);
        db.close();
        
        Button button = (Button) findViewById(R.id.updateButton);
        button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				saveNote();
				updateWidget();				
			}
		});
  
	}
	
	private void saveNote() {
		db.open();
		db.updateNote(widgetId, editText.getText().toString());
		db.close();
	}
	
	private void updateWidget() {
		Context context = getBaseContext();
		
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);	
		
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.show_note);
		
		views.setTextViewText(R.id.showNoteText, editText.getText());
		
		appWidgetManager.updateAppWidget(widgetId, views);
		
		finish();
	}
}
