package com.example.fair2share.data_models

enum class Valutas {
    EURO{
        override fun getSymbol(): String = "€"
    },
    DOLLAR{
        override fun getSymbol(): String = "$"
    },
    POUND{
        override fun getSymbol(): String = "£"
    };

    abstract fun getSymbol(): String
}