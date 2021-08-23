package com.example.trazeapp.data.model

data class Name(
    val first: String,
    val last: String,
    /*
      ma optional ni siya kung nag initialized naka sa iyaha daan
      then once i called siya dayon mabutang dayon ang middle as null  values

     */
    val middle: String?,
    val suffix: String?
)