package com.example.fair2share

import com.example.fair2share.activity.exceptions.InvalidFormDataException
import com.example.fair2share.models.dto_models.ProfileDTOProperty
import com.example.fair2share.models.dto_models.TransactionDTOProperty
import org.junit.Test

class TransactionDTOPropertyTest {
    private val paidByProfile = ProfileDTOProperty(1, "Lars", "Vandenberghe", null, null, null, null, 0)

    @Test
    fun `Transaction with a name, a description and currencyType should work`(){
        TransactionDTOProperty(
            null,
            "transaction",
            "description",
            null,
            0.0,
            null,
            paidByProfile
        )
    }

    @Test
    fun `Transaction without a description should work`(){
        TransactionDTOProperty(
            null,
            "transaction",
            null,
            null,
            0.0,
            null,
            paidByProfile
        )
    }

    @Test
    fun `Transaction with an empty a description should work`(){
        TransactionDTOProperty(
            null,
            "transaction",
            null,
            null,
            0.0,
            null,
            paidByProfile
        )
    }

    @Test
    fun `Transaction with large payment should work`(){
        TransactionDTOProperty(
            null,
            "transaction",
            null,
            null,
            Double.MAX_VALUE,
            null,
            paidByProfile
        )
    }

    @Test(expected = InvalidFormDataException::class)
    fun `Transaction with negative payment throws InvalidFormDataException`(){
        TransactionDTOProperty(
            null,
            "transaction",
            null,
            null,
            -1.0,
            null,
            paidByProfile
        )
    }

    @Test(expected = InvalidFormDataException::class)
    fun `Transaction name with 2 characters throws InvalidFormDataException`(){
        TransactionDTOProperty(
            null,
            "tr",
            null,
            null,
            0.0,
            null,
            paidByProfile
        )
    }

    @Test
    fun `Transaction name with 3 characters is allowed`(){
        TransactionDTOProperty(
            null,
            "tra",
            null,
            null,
            0.0,
            null,
            paidByProfile
        )
    }

    @Test
    fun `Transaction name with 3 numbers is allowed`(){
        TransactionDTOProperty(
            null,
            "123",
            null,
            null,
            0.0,
            null,
            paidByProfile
        )
    }
}