package com.example.gym;

import com.example.gym.services.CredentialsService;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CredentialsService Tests")
class CredentialsServiceTest {

    private CredentialsService credentialsService;

    @BeforeEach
    void setUp() {
        credentialsService = new CredentialsService();
    }

    @Nested
    @DisplayName("generateUsername()")
    class GenerateUsernameTests {

        @Test
        @DisplayName("Should generate base username when user does not exist")
        void shouldGenerateBaseUsernameWhenUserDoesNotExist() {
            String username = credentialsService.generateUsername("John", "Doe", false, 0);

            assertThat(username).isEqualTo("John.Doe");
        }

        @Test
        @DisplayName("Should append serial number when user exists")
        void shouldAppendSerialNumberWhenUserExists() {
            String username = credentialsService.generateUsername("John", "Doe", true, 1);

            assertThat(username).isEqualTo("John.Doe1");
        }

        @Test
        @DisplayName("Should append correct serial number")
        void shouldAppendCorrectSerialNumber() {
            String username = credentialsService.generateUsername("Jane", "Smith", true, 5);

            assertThat(username).isEqualTo("Jane.Smith5");
        }

        @Test
        @DisplayName("Should format username as firstName.lastName")
        void shouldFormatUsernameAsFirstNameDotLastName() {
            String username = credentialsService.generateUsername("Alice", "Johnson", false, 0);

            assertThat(username).contains(".");
            assertThat(username).startsWith("Alice");
            assertThat(username).endsWith("Johnson");
        }
    }

    @Nested
    @DisplayName("generatePassword()")
    class GeneratePasswordTests {

        @Test
        @DisplayName("Should generate password of length 10")
        void shouldGeneratePasswordOfLength10() {
            String password = credentialsService.generatePassword();

            assertThat(password).hasSize(10);
        }

        @Test
        @DisplayName("Should generate password with only valid characters")
        void shouldGeneratePasswordWithOnlyValidCharacters() {
            String password = credentialsService.generatePassword();

            assertThat(password).matches("[A-Za-z0-9]+");
        }

        @RepeatedTest(5)
        @DisplayName("Should generate different passwords on each call")
        void shouldGenerateDifferentPasswordsOnEachCall() {
            String password1 = credentialsService.generatePassword();
            String password2 = credentialsService.generatePassword();

            assertThat(password1).isNotNull();
            assertThat(password2).isNotNull();
            assertThat(password1).hasSize(10);
            assertThat(password2).hasSize(10);
        }

        @Test
        @DisplayName("Should not return null")
        void shouldNotReturnNull() {
            String password = credentialsService.generatePassword();

            assertThat(password).isNotNull();
        }
    }
}
