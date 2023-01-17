package com.example.trashapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

val DATABASE_VERSION: Int = 1

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "TrashAppDatabase", null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        // Create tables here
        db?.execSQL("CREATE TABLE cleaningcompany ( \n" +
                "     nip CHAR(10) PRIMARY KEY, \n" +
                "     email TEXT NOT NULL, \n" +
                "     phone INTEGER NOT NULL, \n" +
                "     country TEXT, \n" +
                "     city TEXT, \n" +
                "     district TEXT, \n" +
                "     street TEXT, \n" +
                "     flat_number TEXT, \n" +
                "     post_code TEXT \n" +
                " );")
        db?.execSQL("CREATE TABLE users ( \n" +
                "     login TEXT PRIMARY KEY, \n" +
                "     password TEXT NOT NULL, \n" +
                "     email TEXT NOT NULL, \n" +
                "     phone TEXT, \n" +
                "     fullname TEXT NOT NULL, \n" +
                "     country TEXT, \n" +
                "     city TEXT, \n" +
                "     district TEXT, \n" +
                "     street TEXT, \n" +
                "     flat_number TEXT, \n" +
                "     post_code TEXT \n" +
                " );")
        db?.execSQL("CREATE TABLE role ( \n" +
                "     role_name TEXT PRIMARY KEY \n" +
                " );")
        db?.execSQL("CREATE TABLE usertorole ( \n" +
                "     user_login TEXT NOT NULL REFERENCES users(login), \n" +
                "     role_name TEXT NOT NULL REFERENCES role(role_name), \n" +
                "     PRIMARY KEY (user_login, role_name)\n" +
                " );")
        db?.execSQL("CREATE TABLE cleaningcrew ( \n" +
                "     id INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                "     crew_name TEXT NOT NULL, \n" +
                "     meet_date DATETIME NOT NULL, \n" +
                "     meeting_localization TEXT  \n" +
                " );")
        db?.execSQL("CREATE TABLE usergroup ( \n" +
                "     user_login TEXT NOT NULL REFERENCES users(login), \n" +
                "     cleaningcrew_id INTEGER NOT NULL REFERENCES cleaningcrew(id), \n" +
                "     PRIMARY KEY (user_login, cleaningcrew_id)\n" +
                " );")
        db?.execSQL("CREATE TABLE vehicle ( \n" +
                "     id INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                "     in_use INTEGER NOT NULL, \n" +
                "     localization TEXT, \n" +
                "     filling REAL NOT NULL \n" +
                " );")
        db?.execSQL("CREATE TABLE trash (\n" +
                "     id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "     localization TEXT NOT NULL,\n" +
                "     creation_date TIMESTAMP NOT NULL,\n" +
                "     trash_size INTEGER,\n" +
                "     vehicle_id INTEGER REFERENCES vehicle(id),\n" +
                "     user_login_report TEXT REFERENCES users(login),\n" +
                "     cleaningcrew_id INTEGER REFERENCES cleaningcrew(id),\n" +
                "     user_login TEXT REFERENCES users(login),\n" +
                "     collection_date TIMESTAMP,\n" +
                "     CHECK (((vehicle_id IS NOT NULL)\n" +
                "                 AND (user_login IS NULL)\n" +
                "                 AND (cleaningcrew_id IS NULL))\n" +
                "                OR ((user_login IS NOT NULL)\n" +
                "                    AND (vehicle_id IS NULL)\n" +
                "                    AND (cleaningcrew_id IS NULL))\n" +
                "                OR ((cleaningcrew_id IS NOT NULL)\n" +
                "                    AND (vehicle_id IS NULL)\n" +
                "                    AND (user_login IS NULL)))\n" +
                ");")
        db?.execSQL("CREATE TABLE image ( \n" +
                "     id INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                "     mime_type TEXT NOT NULL, \n" +
                "     content BLOB NOT NULL, \n" +
                "     trash_id INTEGER REFERENCES trash(id) \n" +
                " );")
        db?.execSQL("CREATE TABLE trashcollectingpoint ( \n" +
                "     localization TEXT PRIMARY KEY, \n" +
                "     bus_empty INTEGER NOT NULL, \n" +
                "     processing_type TEXT NOT NULL, \n" +
                "     trash_id INTEGER REFERENCES trash(id) \n" +
                " );")
        db?.execSQL("CREATE TABLE trashtype ( \n" +
                "     typename TEXT PRIMARY KEY \n" +
                " );")
        db?.execSQL("CREATE TABLE trashtotrashtype ( \n" +
                "     trash_id INTEGER NOT NULL REFERENCES trash(id), \n" +
                "     trashtype_name TEXT NOT NULL REFERENCES trashtype(typename) \n" +
                " );")
        db?.execSQL("CREATE TABLE collectingpointtotrashtype ( \n" +
                "     trashcollectingpoint_localization TEXT NOT NULL REFERENCES trashcollectingpoint(localization), \n" +
                "     trashtype_name TEXT NOT NULL REFERENCES trashtype(typename) \n" +
                " );")
        db?.execSQL("CREATE TABLE worker ( \n" +
                "     fullname TEXT NOT NULL, \n" +
                "     birthdate DATE NOT NULL, \n" +
                "     job_start_time TEXT NOT NULL, \n" +
                "     job_end_time TEXT NOT NULL, \n" +
                "     company_nip INTEGER NOT NULL REFERENCES cleaningcompany(nip), \n" +
                "     vehicle_id INTEGER NOT NULL REFERENCES vehicle(id),\n" +
                "     PRIMARY KEY (fullname, birthdate)\n" +
                " );")

        // Create indexes
        db?.execSQL("CREATE INDEX users_login_idx ON users(login);")
        db?.execSQL("CREATE INDEX users_pwd_idx ON users(password);")
        db?.execSQL("CREATE INDEX role_idx ON role(role_name);")
        db?.execSQL("CREATE INDEX usertorole_us_login_idx ON usertorole(user_login);")
        db?.execSQL("CREATE INDEX usertorole_role_idx ON usertorole(role_name);")
        db?.execSQL("CREATE INDEX cleaningcrew_idx ON cleaningcrew(meet_date);")
        db?.execSQL("CREATE INDEX usergroup_us_login_idx ON usergroup(user_login);")
        db?.execSQL("CREATE INDEX usergroup_cc_id_idx ON usergroup(cleaningcrew_id);")
        db?.execSQL("CREATE INDEX vehicle_fill_idx ON vehicle(filling);")
        db?.execSQL("CREATE INDEX vehicle_use_idx ON vehicle(in_use);")
        db?.execSQL("CREATE INDEX trash_create_date_idx ON trash(creation_date);")
        db?.execSQL("CREATE INDEX trash_veh_id_idx ON trash(vehicle_id);")
        db?.execSQL("CREATE INDEX trash_us_log_rep_idx ON trash(user_login_report);")
        db?.execSQL("CREATE INDEX trash_clean_id_idx ON trash(cleaningcrew_id);")
        db?.execSQL("CREATE INDEX trash_us_log_idx ON trash(user_login);")
        db?.execSQL("CREATE INDEX trash_collect_date_idx ON trash(collection_date);")
        db?.execSQL("CREATE INDEX image_trash_id_idx ON image(trash_id);")
        db?.execSQL("CREATE INDEX trashtotrashtype_trash_id_idx ON trashtotrashtype(trash_id);")
        db?.execSQL("CREATE INDEX trashtotrashtype_trash_type_name_idx ON trashtotrashtype(trashtype_name);")
        db?.execSQL("CREATE INDEX collectpointtotrashtype_tr_col_local_idx ON collectingpointtotrashtype(trashcollectingpoint_localization);")
        db?.execSQL("CREATE INDEX collectpointtotrashtype_tr_col_local__typename_idx ON collectingpointtotrashtype(trashtype_name);")
        db?.execSQL("CREATE INDEX worker_start_job_idx ON worker(job_start_time);")
        db?.execSQL("CREATE INDEX worker_end_job_idx ON worker(job_end_time);")
        db?.execSQL("CREATE INDEX worker_company_nip_idx ON worker(company_nip);")
        db?.execSQL("CREATE INDEX worker_veh_id_idx ON worker(vehicle_id);")

        // Create procedures and functions

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Handle database upgrades here
    }

    fun fill(db: SQLiteDatabase?){
        // TODO - fill the database with sample data (not urgent)
    }

    fun clear(db: SQLiteDatabase?){
        db?.execSQL("DROP TABLE IF EXISTS cleaningcompany")
        db?.execSQL("DROP TABLE IF EXISTS users")
        db?.execSQL("DROP TABLE IF EXISTS role")
        db?.execSQL("DROP TABLE IF EXISTS usertorole")
        db?.execSQL("DROP TABLE IF EXISTS cleaningcrew")
        db?.execSQL("DROP TABLE IF EXISTS usergroup")
        db?.execSQL("DROP TABLE IF EXISTS vehicle")
        db?.execSQL("DROP TABLE IF EXISTS trash")
        db?.execSQL("DROP TABLE IF EXISTS image")
        db?.execSQL("DROP TABLE IF EXISTS trashcollectingpoint")
        db?.execSQL("DROP TABLE IF EXISTS trashtype")
        db?.execSQL("DROP TABLE IF EXISTS trashtotrashtype")
        db?.execSQL("DROP TABLE IF EXISTS collectingpointtotrashtype")
        db?.execSQL("DROP TABLE IF EXISTS worker")
    }
}
