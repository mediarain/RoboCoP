package com.museami.android.hookd.provider;

import com.museami.android.hookd.database.HookdDatabase;

import com.museami.android.hookd.database.table.*;

import android.provider.BaseColumns;
import android.text.TextUtils;
import android.content.ContentUris;
import android.database.sqlite.SQLiteQueryBuilder;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class HookdProvider extends ContentProvider {
    public static final String AUTHORITY = "com.museami.android.hookd.provider.hookd";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    public static final Uri YOUTUBEVIDEO_CONTENT_URI = Uri.withAppendedPath(HookdProvider.AUTHORITY_URI, YoutubeVideoContent.CONTENT_PATH);

    public static final Uri YOUTUBECOMMENT_CONTENT_URI = Uri.withAppendedPath(HookdProvider.AUTHORITY_URI, YoutubeCommentContent.CONTENT_PATH);

    public static final Uri ARTIST_CONTENT_URI = Uri.withAppendedPath(HookdProvider.AUTHORITY_URI, ArtistContent.CONTENT_PATH);

    public static final Uri SONG_CONTENT_URI = Uri.withAppendedPath(HookdProvider.AUTHORITY_URI, SongContent.CONTENT_PATH);

    public static final Uri RECORDEDVIDEO_CONTENT_URI = Uri.withAppendedPath(HookdProvider.AUTHORITY_URI, RecordedVideoContent.CONTENT_PATH);

    public static final Uri YOUTUBEVIDEO_JOIN_YOUTUBECOMMENT_CONTENT_URI = Uri.withAppendedPath(HookdProvider.AUTHORITY_URI, YoutubeVideoJoinYoutubeCommentContent.CONTENT_PATH);

    public static final Uri ARTIST_JOIN_SONG_CONTENT_URI = Uri.withAppendedPath(HookdProvider.AUTHORITY_URI, ArtistJoinSongContent.CONTENT_PATH);

    private static final UriMatcher URI_MATCHER;
    private HookdDatabase mDatabase;

    private static final int YOUTUBEVIDEO_DIR = 0;
    private static final int YOUTUBEVIDEO_ID = 1;

    private static final int YOUTUBECOMMENT_DIR = 2;
    private static final int YOUTUBECOMMENT_ID = 3;

    private static final int ARTIST_DIR = 4;
    private static final int ARTIST_ID = 5;

    private static final int SONG_DIR = 6;
    private static final int SONG_ID = 7;

    private static final int RECORDEDVIDEO_DIR = 8;
    private static final int RECORDEDVIDEO_ID = 9;

    private static final int YOUTUBEVIDEO_JOIN_YOUTUBECOMMENT_DIR = 10;

    private static final int ARTIST_JOIN_SONG_DIR = 12;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

        URI_MATCHER.addURI(AUTHORITY, YoutubeVideoContent.CONTENT_PATH, YOUTUBEVIDEO_DIR);
        URI_MATCHER.addURI(AUTHORITY, YoutubeVideoContent.CONTENT_PATH + "/#",    YOUTUBEVIDEO_ID);

        URI_MATCHER.addURI(AUTHORITY, YoutubeCommentContent.CONTENT_PATH, YOUTUBECOMMENT_DIR);
        URI_MATCHER.addURI(AUTHORITY, YoutubeCommentContent.CONTENT_PATH + "/#",    YOUTUBECOMMENT_ID);

        URI_MATCHER.addURI(AUTHORITY, ArtistContent.CONTENT_PATH, ARTIST_DIR);
        URI_MATCHER.addURI(AUTHORITY, ArtistContent.CONTENT_PATH + "/#",    ARTIST_ID);

        URI_MATCHER.addURI(AUTHORITY, SongContent.CONTENT_PATH, SONG_DIR);
        URI_MATCHER.addURI(AUTHORITY, SongContent.CONTENT_PATH + "/#",    SONG_ID);

        URI_MATCHER.addURI(AUTHORITY, RecordedVideoContent.CONTENT_PATH, RECORDEDVIDEO_DIR);
        URI_MATCHER.addURI(AUTHORITY, RecordedVideoContent.CONTENT_PATH + "/#",    RECORDEDVIDEO_ID);

        URI_MATCHER.addURI(AUTHORITY, YoutubeVideoJoinYoutubeCommentContent.CONTENT_PATH, YOUTUBEVIDEO_JOIN_YOUTUBECOMMENT_DIR);

        URI_MATCHER.addURI(AUTHORITY, ArtistJoinSongContent.CONTENT_PATH, ARTIST_JOIN_SONG_DIR);
    }

    public static final class YoutubeVideoContent implements BaseColumns {
        public static final String CONTENT_PATH = "youtubevideo";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.hookd.youtubevideo";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.hookd.youtubevideo";
    }

    public static final class YoutubeCommentContent implements BaseColumns {
        public static final String CONTENT_PATH = "youtubecomment";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.hookd.youtubecomment";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.hookd.youtubecomment";
    }

    public static final class ArtistContent implements BaseColumns {
        public static final String CONTENT_PATH = "artist";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.hookd.artist";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.hookd.artist";
    }

    public static final class SongContent implements BaseColumns {
        public static final String CONTENT_PATH = "song";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.hookd.song";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.hookd.song";
    }

    public static final class RecordedVideoContent implements BaseColumns {
        public static final String CONTENT_PATH = "recordedvideo";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.hookd.recordedvideo";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.hookd.recordedvideo";
    }

    public static final class YoutubeVideoJoinYoutubeCommentContent implements BaseColumns {
        public static final String CONTENT_PATH = "youtubevideo_join_youtubecomment";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.hookd.youtubevideo_join_youtubecomment";
    }

    public static final class ArtistJoinSongContent implements BaseColumns {
        public static final String CONTENT_PATH = "artist_join_song";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.hookd.artist_join_song";
    }

    @Override
    public final boolean onCreate() {
        mDatabase = new HookdDatabase(getContext());
        return true;
    }

    @Override
    public final String getType(final Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case YOUTUBEVIDEO_DIR:
                return YoutubeVideoContent.CONTENT_TYPE;
            case YOUTUBEVIDEO_ID:
                return YoutubeVideoContent.CONTENT_ITEM_TYPE;

            case YOUTUBECOMMENT_DIR:
                return YoutubeCommentContent.CONTENT_TYPE;
            case YOUTUBECOMMENT_ID:
                return YoutubeCommentContent.CONTENT_ITEM_TYPE;

            case ARTIST_DIR:
                return ArtistContent.CONTENT_TYPE;
            case ARTIST_ID:
                return ArtistContent.CONTENT_ITEM_TYPE;

            case SONG_DIR:
                return SongContent.CONTENT_TYPE;
            case SONG_ID:
                return SongContent.CONTENT_ITEM_TYPE;

            case RECORDEDVIDEO_DIR:
                return RecordedVideoContent.CONTENT_TYPE;
            case RECORDEDVIDEO_ID:
                return RecordedVideoContent.CONTENT_ITEM_TYPE;

            case YOUTUBEVIDEO_JOIN_YOUTUBECOMMENT_DIR:
                return YoutubeVideoJoinYoutubeCommentContent.CONTENT_TYPE;

            case ARTIST_JOIN_SONG_DIR:
                return ArtistJoinSongContent.CONTENT_TYPE;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public final Cursor query(final Uri uri, String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
        final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        final SQLiteDatabase dbConnection = mDatabase.getReadableDatabase();

        switch (URI_MATCHER.match(uri)) {
            case YOUTUBEVIDEO_ID:
                queryBuilder.appendWhere(YoutubeVideoTable._ID + "=" + uri.getLastPathSegment());
            case YOUTUBEVIDEO_DIR:
                queryBuilder.setTables(YoutubeVideoTable.TABLE_NAME);
                break;

            case YOUTUBECOMMENT_ID:
                queryBuilder.appendWhere(YoutubeCommentTable._ID + "=" + uri.getLastPathSegment());
            case YOUTUBECOMMENT_DIR:
                queryBuilder.setTables(YoutubeCommentTable.TABLE_NAME);
                break;

            case ARTIST_ID:
                queryBuilder.appendWhere(ArtistTable._ID + "=" + uri.getLastPathSegment());
            case ARTIST_DIR:
                queryBuilder.setTables(ArtistTable.TABLE_NAME);
                break;

            case SONG_ID:
                queryBuilder.appendWhere(SongTable._ID + "=" + uri.getLastPathSegment());
            case SONG_DIR:
                queryBuilder.setTables(SongTable.TABLE_NAME);
                break;

            case RECORDEDVIDEO_ID:
                queryBuilder.appendWhere(RecordedVideoTable._ID + "=" + uri.getLastPathSegment());
            case RECORDEDVIDEO_DIR:
                queryBuilder.setTables(RecordedVideoTable.TABLE_NAME);
                break;

            case YOUTUBEVIDEO_JOIN_YOUTUBECOMMENT_DIR:
                queryBuilder.setTables(YoutubeVideoTable.TABLE_NAME + " LEFT OUTER JOIN " + YoutubeCommentTable.TABLE_NAME + " ON (" + YoutubeVideoTable.TABLE_NAME + "." + YoutubeVideoTable.ID + "=" + YoutubeCommentTable.TABLE_NAME + "." + YoutubeCommentTable.VIDEO_ID + ")");

                projection = new String[] {
                        YoutubeVideoTable.TABLE_NAME + "." + YoutubeVideoTable._ID + " || " + YoutubeCommentTable.TABLE_NAME + "." + YoutubeCommentTable._ID + " AS " + YoutubeVideoTable._ID,

                        YoutubeVideoTable.TABLE_NAME + "._id AS " + YoutubeVideoTable.TABLE_NAME + "__id",

                        YoutubeVideoTable.TABLE_NAME + "." + YoutubeVideoTable.ID + " AS " + YoutubeVideoTable.TABLE_NAME + "_" + YoutubeVideoTable.ID,

                        YoutubeVideoTable.TABLE_NAME + "." + YoutubeVideoTable.PUBLISHED_TIME + " AS " + YoutubeVideoTable.TABLE_NAME + "_" + YoutubeVideoTable.PUBLISHED_TIME,

                        YoutubeVideoTable.TABLE_NAME + "." + YoutubeVideoTable.TITLE + " AS " + YoutubeVideoTable.TABLE_NAME + "_" + YoutubeVideoTable.TITLE,

                        YoutubeVideoTable.TABLE_NAME + "." + YoutubeVideoTable.DESCRIPTION + " AS " + YoutubeVideoTable.TABLE_NAME + "_" + YoutubeVideoTable.DESCRIPTION,

                        YoutubeVideoTable.TABLE_NAME + "." + YoutubeVideoTable.UPLOADER_CHANNEL_ID + " AS " + YoutubeVideoTable.TABLE_NAME + "_" + YoutubeVideoTable.UPLOADER_CHANNEL_ID,

                        YoutubeVideoTable.TABLE_NAME + "." + YoutubeVideoTable.UPLOADER + " AS " + YoutubeVideoTable.TABLE_NAME + "_" + YoutubeVideoTable.UPLOADER,

                        YoutubeVideoTable.TABLE_NAME + "." + YoutubeVideoTable.UPLOADER_THUMBNAIL + " AS " + YoutubeVideoTable.TABLE_NAME + "_" + YoutubeVideoTable.UPLOADER_THUMBNAIL,

                        YoutubeVideoTable.TABLE_NAME + "." + YoutubeVideoTable.VIEWS + " AS " + YoutubeVideoTable.TABLE_NAME + "_" + YoutubeVideoTable.VIEWS,

                        YoutubeVideoTable.TABLE_NAME + "." + YoutubeVideoTable.LIKES + " AS " + YoutubeVideoTable.TABLE_NAME + "_" + YoutubeVideoTable.LIKES,

                        YoutubeVideoTable.TABLE_NAME + "." + YoutubeVideoTable.THUMBNAIL_URL + " AS " + YoutubeVideoTable.TABLE_NAME + "_" + YoutubeVideoTable.THUMBNAIL_URL,

                        YoutubeVideoTable.TABLE_NAME + "." + YoutubeVideoTable.LIKED + " AS " + YoutubeVideoTable.TABLE_NAME + "_" + YoutubeVideoTable.LIKED,

                        YoutubeVideoTable.TABLE_NAME + "." + YoutubeVideoTable.HAS_COMMENTS + " AS " + YoutubeVideoTable.TABLE_NAME + "_" + YoutubeVideoTable.HAS_COMMENTS,

                        YoutubeVideoTable.TABLE_NAME + "." + YoutubeVideoTable.COMMENT_COUNT + " AS " + YoutubeVideoTable.TABLE_NAME + "_" + YoutubeVideoTable.COMMENT_COUNT,

                        YoutubeVideoTable.TABLE_NAME + "." + YoutubeVideoTable.STREAM_TYPE + " AS " + YoutubeVideoTable.TABLE_NAME + "_" + YoutubeVideoTable.STREAM_TYPE,

                        YoutubeCommentTable.TABLE_NAME + "._id AS " + YoutubeCommentTable.TABLE_NAME + "__id",

                        YoutubeCommentTable.TABLE_NAME + "." + YoutubeCommentTable.VIDEO_ID + " AS " + YoutubeCommentTable.TABLE_NAME + "_" + YoutubeCommentTable.VIDEO_ID,

                        YoutubeCommentTable.TABLE_NAME + "." + YoutubeCommentTable.PUBLISHED_TIME + " AS " + YoutubeCommentTable.TABLE_NAME + "_" + YoutubeCommentTable.PUBLISHED_TIME,

                        YoutubeCommentTable.TABLE_NAME + "." + YoutubeCommentTable.AUTHOR_CHANNEL_ID + " AS " + YoutubeCommentTable.TABLE_NAME + "_" + YoutubeCommentTable.AUTHOR_CHANNEL_ID,

                        YoutubeCommentTable.TABLE_NAME + "." + YoutubeCommentTable.AUTHOR + " AS " + YoutubeCommentTable.TABLE_NAME + "_" + YoutubeCommentTable.AUTHOR,

                        YoutubeCommentTable.TABLE_NAME + "." + YoutubeCommentTable.AUTHOR_THUMBNAIL + " AS " + YoutubeCommentTable.TABLE_NAME + "_" + YoutubeCommentTable.AUTHOR_THUMBNAIL,

                        YoutubeCommentTable.TABLE_NAME + "." + YoutubeCommentTable.COMMENT + " AS " + YoutubeCommentTable.TABLE_NAME + "_" + YoutubeCommentTable.COMMENT,

                        YoutubeCommentTable.TABLE_NAME + "." + YoutubeCommentTable.TOP_COMMENT + " AS " + YoutubeCommentTable.TABLE_NAME + "_" + YoutubeCommentTable.TOP_COMMENT,
                };
                break;

            case ARTIST_JOIN_SONG_DIR:
                queryBuilder.setTables(ArtistTable.TABLE_NAME + " LEFT OUTER JOIN " + SongTable.TABLE_NAME + " ON (" + ArtistTable.TABLE_NAME + "." + ArtistTable.NAME + "=" + SongTable.TABLE_NAME + "." + SongTable.ARTIST_NAME + ")");

                projection = new String[] {
                        ArtistTable.TABLE_NAME + "." + ArtistTable._ID + " || " + SongTable.TABLE_NAME + "." + SongTable._ID + " AS " + ArtistTable._ID,

                        ArtistTable.TABLE_NAME + "._id AS " + ArtistTable.TABLE_NAME + "__id",

                        ArtistTable.TABLE_NAME + "." + ArtistTable.NAME + " AS " + ArtistTable.TABLE_NAME + "_" + ArtistTable.NAME,

                        ArtistTable.TABLE_NAME + "." + ArtistTable.ALBUM_ARTWORK_URI + " AS " + ArtistTable.TABLE_NAME + "_" + ArtistTable.ALBUM_ARTWORK_URI,

                        ArtistTable.TABLE_NAME + "." + ArtistTable.BLURRED_ALBUM_ARTWORK_URI + " AS " + ArtistTable.TABLE_NAME + "_" + ArtistTable.BLURRED_ALBUM_ARTWORK_URI,

                        ArtistTable.TABLE_NAME + "." + ArtistTable.ARTIST_LOGO_URI + " AS " + ArtistTable.TABLE_NAME + "_" + ArtistTable.ARTIST_LOGO_URI,

                        ArtistTable.TABLE_NAME + "." + ArtistTable.ARTIST_SPLASH_URI + " AS " + ArtistTable.TABLE_NAME + "_" + ArtistTable.ARTIST_SPLASH_URI,

                        ArtistTable.TABLE_NAME + "." + ArtistTable.BLURRED_ARTIST_SPLASH_URI + " AS " + ArtistTable.TABLE_NAME + "_" + ArtistTable.BLURRED_ARTIST_SPLASH_URI,

                        SongTable.TABLE_NAME + "._id AS " + SongTable.TABLE_NAME + "__id",

                        SongTable.TABLE_NAME + "." + SongTable.ARTIST_NAME + " AS " + SongTable.TABLE_NAME + "_" + SongTable.ARTIST_NAME,

                        SongTable.TABLE_NAME + "." + SongTable.TITLE + " AS " + SongTable.TABLE_NAME + "_" + SongTable.TITLE,

                        SongTable.TABLE_NAME + "." + SongTable.MUSIC_URI + " AS " + SongTable.TABLE_NAME + "_" + SongTable.MUSIC_URI,

                        SongTable.TABLE_NAME + "." + SongTable.VOCALS_URI + " AS " + SongTable.TABLE_NAME + "_" + SongTable.VOCALS_URI,

                        SongTable.TABLE_NAME + "." + SongTable.LYRICS_URI + " AS " + SongTable.TABLE_NAME + "_" + SongTable.LYRICS_URI,

                        SongTable.TABLE_NAME + "." + SongTable.CONFIG_URI + " AS " + SongTable.TABLE_NAME + "_" + SongTable.CONFIG_URI,

                        SongTable.TABLE_NAME + "." + SongTable.LOCKED + " AS " + SongTable.TABLE_NAME + "_" + SongTable.LOCKED,
                };
                break;

            default :
                throw new IllegalArgumentException("Unsupported URI:" + uri);
        }

        Cursor cursor = queryBuilder.query(dbConnection, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;

    }

    @Override
    public final Uri insert(final Uri uri, final ContentValues values) {
        final SQLiteDatabase dbConnection = mDatabase.getWritableDatabase();

        try {
            dbConnection.beginTransaction();

            switch (URI_MATCHER.match(uri)) {
                case YOUTUBEVIDEO_DIR:
                case YOUTUBEVIDEO_ID:
                    final long youtubevideoId = dbConnection.insertOrThrow(YoutubeVideoTable.TABLE_NAME, null, values);
                    final Uri newYoutubeVideoUri = ContentUris.withAppendedId(YOUTUBEVIDEO_CONTENT_URI, youtubevideoId);
                    getContext().getContentResolver().notifyChange(newYoutubeVideoUri, null);
                    getContext().getContentResolver().notifyChange(YOUTUBEVIDEO_JOIN_YOUTUBECOMMENT_CONTENT_URI, null);

                    dbConnection.setTransactionSuccessful();
                    return newYoutubeVideoUri;

                case YOUTUBECOMMENT_DIR:
                case YOUTUBECOMMENT_ID:
                    final long youtubecommentId = dbConnection.insertOrThrow(YoutubeCommentTable.TABLE_NAME, null, values);
                    final Uri newYoutubeCommentUri = ContentUris.withAppendedId(YOUTUBECOMMENT_CONTENT_URI, youtubecommentId);
                    getContext().getContentResolver().notifyChange(newYoutubeCommentUri, null);
                    getContext().getContentResolver().notifyChange(YOUTUBEVIDEO_JOIN_YOUTUBECOMMENT_CONTENT_URI, null);

                    dbConnection.setTransactionSuccessful();
                    return newYoutubeCommentUri;

                case ARTIST_DIR:
                case ARTIST_ID:
                    final long artistId = dbConnection.insertOrThrow(ArtistTable.TABLE_NAME, null, values);
                    final Uri newArtistUri = ContentUris.withAppendedId(ARTIST_CONTENT_URI, artistId);
                    getContext().getContentResolver().notifyChange(newArtistUri, null);
                    getContext().getContentResolver().notifyChange(ARTIST_JOIN_SONG_CONTENT_URI, null);

                    dbConnection.setTransactionSuccessful();
                    return newArtistUri;

                case SONG_DIR:
                case SONG_ID:
                    final long songId = dbConnection.insertOrThrow(SongTable.TABLE_NAME, null, values);
                    final Uri newSongUri = ContentUris.withAppendedId(SONG_CONTENT_URI, songId);
                    getContext().getContentResolver().notifyChange(newSongUri, null);
                    getContext().getContentResolver().notifyChange(ARTIST_JOIN_SONG_CONTENT_URI, null);

                    dbConnection.setTransactionSuccessful();
                    return newSongUri;

                case RECORDEDVIDEO_DIR:
                case RECORDEDVIDEO_ID:
                    final long recordedvideoId = dbConnection.insertOrThrow(RecordedVideoTable.TABLE_NAME, null, values);
                    final Uri newRecordedVideoUri = ContentUris.withAppendedId(RECORDEDVIDEO_CONTENT_URI, recordedvideoId);
                    getContext().getContentResolver().notifyChange(newRecordedVideoUri, null);
                    dbConnection.setTransactionSuccessful();
                    return newRecordedVideoUri;

                default :
                    throw new IllegalArgumentException("Unsupported URI:" + uri);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbConnection.endTransaction();
        }

        return null;
    }

    @Override
    public final int update(final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs) {
        final SQLiteDatabase dbConnection = mDatabase.getWritableDatabase();
        int updateCount = 0;
        List<Uri> joinUris = new ArrayList<Uri>();

        try {
            dbConnection.beginTransaction();

            switch (URI_MATCHER.match(uri)) {
                case YOUTUBEVIDEO_DIR :
                    updateCount = dbConnection.update(YoutubeVideoTable.TABLE_NAME, values, selection, selectionArgs);

                    joinUris.add(YOUTUBEVIDEO_JOIN_YOUTUBECOMMENT_CONTENT_URI);

                    dbConnection.setTransactionSuccessful();
                    break;
                case YOUTUBEVIDEO_ID :
                    final long youtubevideoId = ContentUris.parseId(uri);
                    updateCount = dbConnection.update(YoutubeVideoTable.TABLE_NAME, values,
                            YoutubeVideoTable._ID + "=" + youtubevideoId + (TextUtils.isEmpty(selection) ? "" : " AND (" + selection + ")"), selectionArgs);

                    joinUris.add(YOUTUBEVIDEO_JOIN_YOUTUBECOMMENT_CONTENT_URI);

                    dbConnection.setTransactionSuccessful();
                    break;

                case YOUTUBECOMMENT_DIR :
                    updateCount = dbConnection.update(YoutubeCommentTable.TABLE_NAME, values, selection, selectionArgs);

                    joinUris.add(YOUTUBEVIDEO_JOIN_YOUTUBECOMMENT_CONTENT_URI);

                    dbConnection.setTransactionSuccessful();
                    break;
                case YOUTUBECOMMENT_ID :
                    final long youtubecommentId = ContentUris.parseId(uri);
                    updateCount = dbConnection.update(YoutubeCommentTable.TABLE_NAME, values,
                            YoutubeCommentTable._ID + "=" + youtubecommentId + (TextUtils.isEmpty(selection) ? "" : " AND (" + selection + ")"), selectionArgs);

                    joinUris.add(YOUTUBEVIDEO_JOIN_YOUTUBECOMMENT_CONTENT_URI);

                    dbConnection.setTransactionSuccessful();
                    break;

                case ARTIST_DIR :
                    updateCount = dbConnection.update(ArtistTable.TABLE_NAME, values, selection, selectionArgs);

                    joinUris.add(ARTIST_JOIN_SONG_CONTENT_URI);

                    dbConnection.setTransactionSuccessful();
                    break;
                case ARTIST_ID :
                    final long artistId = ContentUris.parseId(uri);
                    updateCount = dbConnection.update(ArtistTable.TABLE_NAME, values,
                            ArtistTable._ID + "=" + artistId + (TextUtils.isEmpty(selection) ? "" : " AND (" + selection + ")"), selectionArgs);

                    joinUris.add(ARTIST_JOIN_SONG_CONTENT_URI);

                    dbConnection.setTransactionSuccessful();
                    break;

                case SONG_DIR :
                    updateCount = dbConnection.update(SongTable.TABLE_NAME, values, selection, selectionArgs);

                    joinUris.add(ARTIST_JOIN_SONG_CONTENT_URI);

                    dbConnection.setTransactionSuccessful();
                    break;
                case SONG_ID :
                    final long songId = ContentUris.parseId(uri);
                    updateCount = dbConnection.update(SongTable.TABLE_NAME, values,
                            SongTable._ID + "=" + songId + (TextUtils.isEmpty(selection) ? "" : " AND (" + selection + ")"), selectionArgs);

                    joinUris.add(ARTIST_JOIN_SONG_CONTENT_URI);

                    dbConnection.setTransactionSuccessful();
                    break;

                case RECORDEDVIDEO_DIR :
                    updateCount = dbConnection.update(RecordedVideoTable.TABLE_NAME, values, selection, selectionArgs);

                    dbConnection.setTransactionSuccessful();
                    break;
                case RECORDEDVIDEO_ID :
                    final long recordedvideoId = ContentUris.parseId(uri);
                    updateCount = dbConnection.update(RecordedVideoTable.TABLE_NAME, values,
                            RecordedVideoTable._ID + "=" + recordedvideoId + (TextUtils.isEmpty(selection) ? "" : " AND (" + selection + ")"), selectionArgs);

                    dbConnection.setTransactionSuccessful();
                    break;

                default :
                    throw new IllegalArgumentException("Unsupported URI:" + uri);
            }
        } finally {
            dbConnection.endTransaction();
        }

        if (updateCount > 0) {
            getContext().getContentResolver().notifyChange(uri, null);

            for (Uri joinUri : joinUris) {
                getContext().getContentResolver().notifyChange(joinUri, null);
            }
        }

        return updateCount;

    }

    @Override
    public final int delete(final Uri uri, final String selection, final String[] selectionArgs) {
        final SQLiteDatabase dbConnection = mDatabase.getWritableDatabase();
        int deleteCount = 0;
        List<Uri> joinUris = new ArrayList<Uri>();

        try {
            dbConnection.beginTransaction();

            switch (URI_MATCHER.match(uri)) {
                case YOUTUBEVIDEO_DIR :
                    deleteCount = dbConnection.delete(YoutubeVideoTable.TABLE_NAME, selection, selectionArgs);

                    joinUris.add(YOUTUBEVIDEO_JOIN_YOUTUBECOMMENT_CONTENT_URI);

                    dbConnection.setTransactionSuccessful();
                    break;
                case YOUTUBEVIDEO_ID :
                    deleteCount = dbConnection.delete(YoutubeVideoTable.TABLE_NAME, YoutubeVideoTable.WHERE_ID_EQUALS, new String[] { uri.getLastPathSegment() });

                    joinUris.add(YOUTUBEVIDEO_JOIN_YOUTUBECOMMENT_CONTENT_URI);

                    dbConnection.setTransactionSuccessful();
                    break;

                case YOUTUBECOMMENT_DIR :
                    deleteCount = dbConnection.delete(YoutubeCommentTable.TABLE_NAME, selection, selectionArgs);

                    joinUris.add(YOUTUBEVIDEO_JOIN_YOUTUBECOMMENT_CONTENT_URI);

                    dbConnection.setTransactionSuccessful();
                    break;
                case YOUTUBECOMMENT_ID :
                    deleteCount = dbConnection.delete(YoutubeCommentTable.TABLE_NAME, YoutubeCommentTable.WHERE_ID_EQUALS, new String[] { uri.getLastPathSegment() });

                    joinUris.add(YOUTUBEVIDEO_JOIN_YOUTUBECOMMENT_CONTENT_URI);

                    dbConnection.setTransactionSuccessful();
                    break;

                case ARTIST_DIR :
                    deleteCount = dbConnection.delete(ArtistTable.TABLE_NAME, selection, selectionArgs);

                    joinUris.add(ARTIST_JOIN_SONG_CONTENT_URI);

                    dbConnection.setTransactionSuccessful();
                    break;
                case ARTIST_ID :
                    deleteCount = dbConnection.delete(ArtistTable.TABLE_NAME, ArtistTable.WHERE_ID_EQUALS, new String[] { uri.getLastPathSegment() });

                    joinUris.add(ARTIST_JOIN_SONG_CONTENT_URI);

                    dbConnection.setTransactionSuccessful();
                    break;

                case SONG_DIR :
                    deleteCount = dbConnection.delete(SongTable.TABLE_NAME, selection, selectionArgs);

                    joinUris.add(ARTIST_JOIN_SONG_CONTENT_URI);

                    dbConnection.setTransactionSuccessful();
                    break;
                case SONG_ID :
                    deleteCount = dbConnection.delete(SongTable.TABLE_NAME, SongTable.WHERE_ID_EQUALS, new String[] { uri.getLastPathSegment() });

                    joinUris.add(ARTIST_JOIN_SONG_CONTENT_URI);

                    dbConnection.setTransactionSuccessful();
                    break;

                case RECORDEDVIDEO_DIR :
                    deleteCount = dbConnection.delete(RecordedVideoTable.TABLE_NAME, selection, selectionArgs);

                    dbConnection.setTransactionSuccessful();
                    break;
                case RECORDEDVIDEO_ID :
                    deleteCount = dbConnection.delete(RecordedVideoTable.TABLE_NAME, RecordedVideoTable.WHERE_ID_EQUALS, new String[] { uri.getLastPathSegment() });

                    dbConnection.setTransactionSuccessful();
                    break;

                default :
                    throw new IllegalArgumentException("Unsupported URI:" + uri);
            }
        } finally {
            dbConnection.endTransaction();
        }

        if (deleteCount > 0) {
            getContext().getContentResolver().notifyChange(uri, null);

            for (Uri joinUri : joinUris) {
                getContext().getContentResolver().notifyChange(joinUri, null);
            }
        }

        return deleteCount;
    }
}