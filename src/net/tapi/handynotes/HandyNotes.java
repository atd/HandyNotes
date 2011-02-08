package net.tapi.handynotes;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class HandyNotes extends AppWidgetProvider {
    private NotesDbAdapter db;

	@Override
	public void onUpdate(Context context,
			             AppWidgetManager appWidgetManager,
			             int[] appWidgetIds) {
		
        db = new NotesDbAdapter(context);
        db.open();
		
		for (int appWidgetId : appWidgetIds) {
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.show_note);
			
			String text = db.showNoteText(appWidgetId);

            views.setTextViewText(R.id.showNoteText, text);
            
			Intent editIntent = new Intent(context, EditNote.class);
			editIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId, editIntent, 0);
			
			views.setOnClickPendingIntent(R.id.showNote, pendingIntent);

			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
		
		db.close();
	}

	@Override
	public void onDeleted(Context context,
			             int[] appWidgetIds) {
		
        db = new NotesDbAdapter(context);
        db.open();
        
		for (int appWidgetId : appWidgetIds) {
			db.deleteNote(appWidgetId);
		}
        
		db.close();
	}
}
