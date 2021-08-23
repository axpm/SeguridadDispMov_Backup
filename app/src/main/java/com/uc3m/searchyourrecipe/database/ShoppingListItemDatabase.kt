package com.uc3m.searchyourrecipe.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.uc3m.searchyourrecipe.models.ShoppingListItem

@Database(entities = [ShoppingListItem::class], version = 1, exportSchema = false)
abstract class ShoppingListItemDatabase: RoomDatabase() {

    abstract fun shoppingListItemDAO(): ShoppingListItemDAO

    companion object{
        @Volatile
        private var INSTANCE: ShoppingListItemDatabase? = null
        //Creo una instancia de la bbdd. Si existe, devuelve la instancia (conexion a la bbdd)
// y si no existe, la crea con el Room.databaseBuilder especificando el contexto, el tipo de entidad (ShoppingListItem) y el nombre de la bbdd
        fun getDatabase(context: Context): ShoppingListItemDatabase {
            synchronized(this){
                var instance = INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(
                            context.applicationContext,
                            ShoppingListItemDatabase::class.java,
                            "shoppingListItem_database" //no hay que poner name, sale solo
                    ).fallbackToDestructiveMigration().build()
                }
                return instance
            }

        }
    }
}