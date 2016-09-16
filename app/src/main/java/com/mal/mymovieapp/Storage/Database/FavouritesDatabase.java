package com.mal.mymovieapp.Storage.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mal.mymovieapp.Models.Movie;

import java.util.ArrayList;

public class FavouritesDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MyFavouriteMovies";
    private static final String TABLE_MOVIES = "movies";

    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String KEY_POSTER_URL = "poster_url";
    private static final String KEY_BACKDROP_URL = "backdrop_url";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_ORIGINAL_TITLE = "original_title";
    private static final String KEY_LANGUAGE = "language";
    private static final String KEY_VOTE_COUNT = "vote_count";
    private static final String KEY_POPULARITY = "popularity";
    private static final String KEY_RATE = "rate";
    private static final String KEY_ADULT = "adult";
    private static final String KEY_VIDEO = "video";

    private static final String[] COLUMNS = {
            KEY_ID,
            KEY_TITLE,
            KEY_RELEASE_DATE,
            KEY_POSTER_URL,
            KEY_BACKDROP_URL,
            KEY_OVERVIEW,
            KEY_ORIGINAL_TITLE,
            KEY_LANGUAGE,
            KEY_VOTE_COUNT,
            KEY_POPULARITY,
            KEY_RATE,
            KEY_ADULT,
            KEY_VIDEO
    };

    public FavouritesDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MOVIES_TABLE =
                        String.format("CREATE TABLE IF NOT EXISTS %s (", TABLE_MOVIES) +
                        String.format("%s TEXT PRIMARY KEY, ", KEY_ID) +
                        String.format("%s TEXT, ", KEY_TITLE) +
                        String.format("%s TEXT, ", KEY_RELEASE_DATE) +
                        String.format("%s TEXT, ", KEY_POSTER_URL) +
                        String.format("%s TEXT, ", KEY_BACKDROP_URL) +
                        String.format("%s TEXT, ", KEY_OVERVIEW) +
                        String.format("%s TEXT, ", KEY_ORIGINAL_TITLE) +
                        String.format("%s TEXT, ", KEY_LANGUAGE) +
                        String.format("%s TEXT, ", KEY_VOTE_COUNT) +
                        String.format("%s TEXT, ", KEY_POPULARITY) +
                        String.format("%s TEXT, ", KEY_RATE) +
                        String.format("%s TEXT, ", KEY_ADULT) +
                        String.format("%s ", KEY_VIDEO) +
                        ")";
        db.execSQL(CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);
        onCreate(db);
    }

    public boolean addToFavourites(Movie movie){
        String movieID = String.valueOf(movie.getId());
        if (movieExists(movieID)) {
            return false;
        }
        else {
            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues values = getValues(movie);
            database.insert(TABLE_MOVIES, null, values);
            database.close();
            return true;
        }
    }

    public boolean removeFromFavourites(Movie movie){
        String movieID = String.valueOf(movie.getId());
        if (movieExists(movieID)) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_MOVIES, KEY_ID + " = ?",
                    new String[] { String.valueOf(movie.getId()) });
            return true;
        }
        else {
            return false;
        }
    }

    public boolean movieExists(String _KEY_ID){
        ArrayList<Movie> movies = new ArrayList<>();
        String query = String.format("SELECT * FROM %s where %s = %s", TABLE_MOVIES, KEY_ID, _KEY_ID);
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do {
                movies.add(getMovie(cursor));
            } while (cursor.moveToNext());
        }
        return !movies.isEmpty();
    }

    public ArrayList<Movie> getAllMovies(){
        ArrayList<Movie> movies = new ArrayList<>();
        String query = String.format("SELECT * FROM %s", TABLE_MOVIES);
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do {
                movies.add(getMovie(cursor));
            } while (cursor.moveToNext());
        }
        return movies;
    }

    public ContentValues getValues(Movie movie){
        ContentValues values = new ContentValues();
        values.put(KEY_ID, movie.getId());
        values.put(KEY_TITLE, movie.getTitle());
        values.put(KEY_RELEASE_DATE, movie.getReleaseDate());
        values.put(KEY_POSTER_URL, movie.getPosterURL());
        values.put(KEY_BACKDROP_URL, movie.getBackdropURL());
        values.put(KEY_OVERVIEW, movie.getOverview());
        values.put(KEY_ORIGINAL_TITLE, movie.getOriginalTitle());
        values.put(KEY_LANGUAGE, movie.getLanguage());
        values.put(KEY_VOTE_COUNT, String.valueOf(movie.getVoteCount()));
        values.put(KEY_POPULARITY, String.valueOf(movie.getPopularity()));
        values.put(KEY_RATE, String.valueOf(movie.getRate()));
        values.put(KEY_ADULT, movie.isAdult() ? "True" : "False");
        values.put(KEY_VIDEO, movie.isVideo() ? "True" : "False");
        return values;
    }

    public Movie getMovie(Cursor cursor){
        Movie movie = new Movie();
        movie.setId(Long.parseLong(cursor.getString(0)));
        movie.setTitle(cursor.getString(1));
        movie.setReleaseDate(cursor.getString(2));
        movie.setPosterURL(cursor.getString(3));
        movie.setBackdropURL(cursor.getString(4));
        movie.setOverview(cursor.getString(5));
        movie.setOriginalTitle(cursor.getString(6));
        movie.setLanguage(cursor.getString(7));
        movie.setVoteCount(Integer.parseInt(cursor.getString(8)));
        movie.setPopularity(Float.parseFloat(cursor.getString(9)));
        movie.setRate(Float.parseFloat(cursor.getString(10)));
        movie.setAdult(cursor.getString(11).equals("True"));
        movie.setVideo(cursor.getString(12).equals("True"));
        return movie;
    }
}
