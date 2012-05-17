package net.tapi.handynotes;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;

public class NewNote extends Activity {
	private Integer widgetId;
	private EditText editText;

	@Override
	protected void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState);
        
		setContentView(R.layout.new_note);

        editText = (EditText) findViewById(R.id.newNoteText);
        
        Button button = (Button) findViewById(R.id.add_button);
        button.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				saveNote();
				addWidget();				
			}
		});
        
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            widgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, 
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
	}
	
	private void saveNote() {
	    NotesDbAdapter db = new NotesDbAdapter(this);
        db.open();
        db.createNote(widgetId, editText.getText().toString());
        db.close();
	}
	
	private void addWidget() {
		Context context = getBaseContext();
		
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.show_note);

		views.setTextViewText(R.id.showNoteText, editText.getText());
		
		Intent editIntent = new Intent(context, EditNote.class);
		editIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

		PendingIntent pendingIntent = PendingIntent.getActivity(context, widgetId, editIntent, 0);
		views.setOnClickPendingIntent(R.id.showNote, pendingIntent);

		appWidgetManager.updateAppWidget(widgetId, views);
		
		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
		setResult(RESULT_OK, resultValue);
		finish();
		
	}
}
