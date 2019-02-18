package br.ufc.great.contextplayer.model;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

//POJO  que representa uma musica
public class Song {
    private String album;
    private String artist;
    private String composer;
    private String duration; //duracao da musica, em ms
    private String track; //numero da faixa do album, se existir
    private String year;

    private String data; //endereco para o arquivo no telefone
    private String dateAdded; //data adicionada expressa em segundos desde 1970
    private String dateModified; //data de modificacao expressa em segundos desde 1970
    private String displayName; //nome do arquivo
    private String size; //tamanho da musica, em bytes
    private String title; //titulo da musica
    private String audio_id; //id do audio para playlists

    public Song() {
    }

    public static Song fromCursor(Cursor cursor){
        Song s = new Song();

        //TODO: usar os setters e ler o cursor;
        s.setAlbum(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
        s.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
        s.setComposer(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.COMPOSER)));
        s.setData(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
        s.setDateAdded(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED)));
        s.setDateModified(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_MODIFIED)));
        s.setDisplayName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
        s.setDuration(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
        s.setSize(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)));
        s.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
        s.setTrack(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TRACK)));
        s.setYear(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.YEAR)));
        s.setAudio_id(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
        return s;
    }

    public static Song fromAudioId(Context context, long audio_id){
        ContentResolver resolver = context.getContentResolver();
        Uri externalSongs = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songList = resolver.query(externalSongs, new String[]{"*"}, MediaStore.Audio.Media._ID + " =" + audio_id,
                null, null);
        if (songList != null || songList.getCount() > 0) {
            songList.moveToFirst();
            return fromCursor(songList);
        } else {
            return null;
        }

    }


    public ContentValues asContentValues(int playOrder){
        ContentValues values = new ContentValues();

        values.put(MediaStore.Audio.Playlists.Members.AUDIO_ID, getAudio_id());
        values.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, playOrder);
        return values;
    }
    @Override
    public String toString() {
        return "Title: " + title + "\nArtist: " + artist + "\nComposer: " + composer + "\nAlbum: " +
                album + "\nYear: " + year;
    }

    public String getAlbum() {
        return album;
    }

    public Song setAlbum(String album) {
        this.album = album;
        return this;
    }

    public String getArtist() {
        return artist;
    }

    public Song setArtist(String artist) {
        this.artist = artist;
        return this;
    }

    public String getComposer() {
        return composer;
    }

    public Song setComposer(String composer) {
        this.composer = composer;
        return this;
    }

    public String getDuration() {
        return duration;
    }

    public Song setDuration(String duration) {
        this.duration = duration;
        return this;
    }

    public String getTrack() {
        return track;
    }

    public Song setTrack(String track) {
        this.track = track;
        return this;
    }

    public String getYear() {
        return year;
    }

    public Song setYear(String year) {
        this.year = year;
        return this;
    }

    public String getData() {
        return data;
    }

    public Song setData(String data) {
        this.data = data;
        return this;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public Song setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
        return this;
    }

    public String getDateModified() {
        return dateModified;
    }

    public Song setDateModified(String dateModified) {
        this.dateModified = dateModified;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Song setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getSize() {
        return size;
    }

    public Song setSize(String size) {
        this.size = size;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Song setTitle(String title) {
        this.title = title;
        return this;
    }


    public String getAudio_id() {
        return audio_id;
    }

    public void setAudio_id(String audio_id) {
        this.audio_id = audio_id;
    }
}
