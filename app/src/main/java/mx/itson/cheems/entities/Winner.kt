package mx.itson.cheems.entities

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.ContactsContract
import android.util.Log
import mx.itson.cheems.persistence.CheemsDB

class Winner {

    var id = 0
    var name : String = ""
    var nickname : String = ""

    constructor()

    constructor(id: Int, name: String, nickname: String){
        this.id = id
        this.name = name
        this.nickname = nickname
    }

    fun save(context: Context, name: String, nickname: String): Boolean {
        try {
            val cheemsDB = CheemsDB(context, "CheemsDB", null, 1)
            val dataBase : SQLiteDatabase = cheemsDB.writableDatabase

            val values = ContentValues()
            values.put("name", name)
            values.put("nickname", nickname)

            dataBase.insert("Winner", null, values)
            return true  // Retorna true si se guardó correctamente



        }catch (ex: Exception) {
            Log.e("Error saving Winner", ex.message.toString())
            return false  // Retorna false si hubo un error
        }
    }



    fun getAll(context: Context) : List<Winner>{
        var winners : MutableList<Winner> = ArrayList()

        try {
            val cheemsDB = CheemsDB(context, "CheemsDB", null, 1)
            val database : SQLiteDatabase = cheemsDB.readableDatabase
            val resultSet = database.rawQuery("SELECT id, name, nickname FROM Winner", null)

            while (resultSet.moveToNext()) {
                val winner = Winner(resultSet.getInt(0), resultSet.getString(1), resultSet.getString(2))
                winners.add(winner)
            }
        }catch (ex: Exception) {
        Log.e("Error getting Winner", ex.message.toString())
        }

        return winners

    }




}