package com.example.project_evote

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, "tokens", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE tokens (id INTEGER PRIMARY KEY AUTOINCREMENT, token VARCHAR NOT NULL )")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS tokens")
    }

    fun insertData(token: String): Boolean {
        var db = this.writableDatabase
        var cv = ContentValues()
        cv.put("token", token)
        val result = db.insert("tokens", null, cv)
        if (result == (-1).toLong())
            return false
        return true
    }

//    @SuppressLint("Range")
    fun checkToken(): String {
        val db = this.writableDatabase
        val query = "SELECT token FROM tokens ORDER BY id DESC"
        val cursor = db.rawQuery(query, null)
        if (cursor.count <= 0) {
            cursor.close()
            return ""
        }

        cursor.moveToFirst()
        val token = cursor.getString(cursor.getColumnIndex("token"))
        cursor.close()

        return token
    }
}