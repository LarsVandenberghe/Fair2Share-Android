package com.example.fair2share

import com.example.fair2share.activity.exceptions.InvalidFormDataException
import com.example.fair2share.models.Valutas
import com.example.fair2share.models.dto_models.ActivityDTOProperty
import org.junit.Test

class ActivityDTOPropertyTest {
    @Test
    fun `Activity with a name, a description and currencyType should work`() {
        ActivityDTOProperty(
            null,
            "activity",
            "description",
            0,
            null,
            null
        )
    }

    @Test
    fun `Activity description is allowed to be null`() {
        ActivityDTOProperty(
            null,
            "activity",
            "",
            0,
            null,
            null
        )
    }

    @Test
    fun `Activity description is allowed to be empty`() {
        ActivityDTOProperty(
            null,
            "activity",
            "",
            0,
            null,
            null
        )
    }

    @Test(expected = InvalidFormDataException::class)
    fun `Activity without a name throws an InvalidFormDataException`() {
        ActivityDTOProperty(
            null,
            "",
            "description",
            0,
            null,
            null
        )
    }

    @Test
    fun `Activity without only 3 letters in the name should work`() {
        ActivityDTOProperty(
            null,
            "abc",
            "description",
            0,
            null,
            null
        )
    }

    @Test(expected = InvalidFormDataException::class)
    fun `Activity without only 2 letters throws an InvalidFormDataException`() {
        ActivityDTOProperty(
            null,
            "ab",
            "description",
            0,
            null,
            null
        )
    }

    @Test
    fun `Activity name only containing digits is allowed`() {
        ActivityDTOProperty(
            null,
            "123",
            "description",
            0,
            null,
            null
        )
    }

    @Test
    fun `Activity name only containing unicode is allowed`() {
        ActivityDTOProperty(
            null,
            "日本人の氏名",
            "description",
            0,
            null,
            null
        )
    }

    @Test(expected = InvalidFormDataException::class)
    fun `Activity with a currencyType that does not exist throws InvalidFormDataException`() {
        ActivityDTOProperty(
            null,
            "activity",
            "description",
            Valutas.values().size,
            null,
            null
        )
    }

    @Test
    fun `Activity with a the last available currencyType is allowed`() {
        ActivityDTOProperty(
            null,
            "activity",
            "description",
            Valutas.values().size - 1,
            null,
            null
        )
    }

    @Test(expected = InvalidFormDataException::class)
    fun `Activity with a negative currencyType throws InvalidFormDataException`() {
        ActivityDTOProperty(
            null,
            "activity",
            "description",
            -1,
            null,
            null
        )
    }
}