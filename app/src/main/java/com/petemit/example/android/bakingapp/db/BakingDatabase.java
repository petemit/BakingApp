package com.petemit.example.android.bakingapp.db;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

   /*
    **
            * Uses the Schematic (https://github.com/SimonVT/schematic) library to create a database with one
            * table for messages
            */

@Database(version = BakingDatabase.VERSION)
public class BakingDatabase {

        public static final int VERSION = 1;

        @Table(BakingContract.class)
        public static final String JSONSource = "jsonSource";

}
