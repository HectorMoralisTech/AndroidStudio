package live.secnet.taskmonitor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(@Nullable Context context) {
        super(context, "tasks.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase _db) {

        String tableCommand = "";

        _db.execSQL(tableCommand);
    }

    @Override
    public void onUpgrade(SQLiteDatabase _db, int i, int i1) {

    }
}
