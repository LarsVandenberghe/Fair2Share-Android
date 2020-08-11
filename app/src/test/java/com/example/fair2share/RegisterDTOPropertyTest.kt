package com.example.fair2share

import com.example.fair2share.exceptions.InvalidFormDataException
import com.example.fair2share.models.dto_models.RegisterDTOProperty
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class RegisterDTOPropertyTestsShouldFail(
    private val email: String,
    private val password: String,
    private val firstName: String,
    private val lastName: String,
    private val passwordConfirmation: String,
    private val testName: String
) {
    @Test(expected = InvalidFormDataException::class)
    fun parameterized() {
        RegisterDTOProperty(
            email,
            password,
            firstName,
            lastName,
            passwordConfirmation
        )
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: {5}")
        fun data(): Collection<Array<String>> {
            return listOf(
                arrayOf("", "", "", "", "", "empty form"),
                arrayOf(
                    "日本人@invalid.email",
                    "testPassword1",
                    "name",
                    "name",
                    "testPassword1",
                    "unicode in email"
                ),
                arrayOf(
                    "invalidemail",
                    "testPassword1",
                    "name",
                    "name",
                    "testPassword1",
                    "No at or dot in email"
                ),
                arrayOf(
                    "invalid@email",
                    "testPassword1",
                    "name",
                    "name",
                    "testPassword1",
                    "No dot in email"
                ),
                arrayOf(
                    "invalid@email.",
                    "testPassword1",
                    "name",
                    "name",
                    "testPassword1",
                    "No domain end"
                ),
                arrayOf("@.", "testPassword1", "name", "name", "testPassword1", "Only at and dot"),
                arrayOf(
                    "invalidEmail@.com",
                    "testPassword1",
                    "name",
                    "name",
                    "testPassword1",
                    "No domain name"
                ),
                arrayOf(
                    "valid@email.com",
                    "",
                    "name",
                    "name",
                    "testPassword1",
                    "No password given"
                ),
                arrayOf(
                    "valid@email.com",
                    "admin123",
                    "name",
                    "name",
                    "testPassword1",
                    "Passwords don't match"
                ),
                arrayOf(
                    "valid@email.com",
                    "admin123",
                    "name",
                    "name",
                    "admin123",
                    "Password doesn't contain capital"
                ),
                arrayOf(
                    "valid@email.com",
                    "Pass1",
                    "name",
                    "name",
                    "Pass1",
                    "Password is too short"
                ),
                arrayOf(
                    "valid@email.com",
                    "Password",
                    "name",
                    "name",
                    "Password",
                    "Password doesn't contain number."
                ),
                arrayOf(
                    "valid@email.com",
                    "password1",
                    "name",
                    "name",
                    "password1",
                    "Password doesn't contain capital"
                ),
                arrayOf(
                    "valid@email.com",
                    "testPassword1",
                    "",
                    "name",
                    "testPassword1",
                    "first name is not given"
                ),
                arrayOf(
                    "valid@email.com",
                    "testPassword1",
                    "name",
                    "",
                    "testPassword1",
                    "last name is not given"
                )
            )
        }
    }
}

@RunWith(Parameterized::class)
class RegisterDTOPropertyTestsShouldPass(
    private val email: String,
    private val password: String,
    private val firstName: String,
    private val lastName: String,
    private val passwordConfirmation: String,
    private val testName: String
) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: {5}")
        fun data(): Collection<Array<String>> {
            return listOf(
                arrayOf(
                    "valid@email.com",
                    "testPas1",
                    "name",
                    "name",
                    "testPas1",
                    "password is exactly 8 chars"
                ),
                arrayOf(
                    "valid@email.com",
                    "testPassword1",
                    "L.",
                    "V.",
                    "testPassword1",
                    "using shortnames"
                ),
                arrayOf(
                    "valid@email.com",
                    "testPassword1",
                    "日本人.",
                    "氏名.",
                    "testPassword1",
                    "using unicode names"
                ),
                arrayOf(
                    "valid@Email.com",
                    "testPassword1",
                    "name",
                    "name",
                    "testPassword1",
                    "Capital letter in email"
                ),
                arrayOf(
                    "valid@email.be",
                    "testPas1",
                    "name",
                    "name",
                    "testPas1",
                    "Belgian domain .be"
                )
            )
        }
    }

    @Test
    fun parameterized() {
        RegisterDTOProperty(
            email,
            password,
            firstName,
            lastName,
            passwordConfirmation
        )
    }
}