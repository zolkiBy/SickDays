package com.android.dev.yashchuk.sickleaves.login

import com.android.dev.yashchuk.sickleaves.R
import com.android.dev.yashchuk.sickleaves.callbacks.OnUserAuthListener
import com.android.dev.yashchuk.sickleaves.capture
import com.android.dev.yashchuk.sickleaves.data.source.remote.net.AuthApi
import com.android.dev.yashchuk.sickleaves.eq
import com.android.dev.yashchuk.sickleaves.utils.Injection
import com.google.firebase.auth.FirebaseUser
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations


class LoginPresenterTest {

    @Mock
    private lateinit var view: LoginContract.View
    @Mock
    private lateinit var api: AuthApi
    @Mock
    private lateinit var user: FirebaseUser
    @Captor
    private lateinit var authListenerCaptor: ArgumentCaptor<OnUserAuthListener>

    private lateinit var presenter: LoginPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        presenter = Injection.provideLoginPresenter(view, api)
    }

    @Test
    fun attemptCreateUser_valid_successCreateUser() {
        presenter.attemptCreateUser("vfvdfvd33__43rbsdba2@mail.com", "8327bhjsdc(8-0")

        verify(view).showProgress(true)
        verify(api).createUser(
                eq("vfvdfvd33__43rbsdba2@mail.com"),
                eq("8327bhjsdc(8-0"),
                capture(authListenerCaptor))

        authListenerCaptor.value.onSuccess()

        verify(view).openListScreen()
        verify(view).finishActivity()
    }

    @Test
    fun attemptCreateUser_emailNull_shouldShowEmailError() {
        presenter.attemptCreateUser(null, "3233243sdcs")

        verify(view).showEmailError(R.string.error_field_required)
        verify(view, never()).showEmailError(R.string.error_invalid_email)
        verify(view, never()).showPasswordError(R.string.error_invalid_password)
    }

    @Test
    fun attemptCreateUser_notValidEmail_shouldShowEmailError() {
        presenter.attemptCreateUser("dcsd@ru", "3233243sdcs")

        verify(view).showEmailError(R.string.error_invalid_email)
        verify(view, never()).showEmailError(R.string.error_field_required)
        verify(view, never()).showPasswordError(R.string.error_invalid_password)
    }

    @Test
    fun attemptCreateUser_emptyEmail_shouldShowEmailError() {
        presenter.attemptCreateUser("", "3233243sdcs")

        verify(view).showEmailError(R.string.error_field_required)
        verify(view, never()).showEmailError(R.string.error_invalid_email)
        verify(view, never()).showPasswordError(R.string.error_invalid_password)
    }

    @Test
    fun attemptCreateUser_passwordNull_shouldShowPasswordError() {
        presenter.attemptCreateUser("bb77XNX_232@mail.com", null)

        verify(view).showPasswordError(R.string.error_invalid_password)
        verify(view, never()).showEmailError(R.string.error_invalid_email)
        verify(view, never()).showEmailError(R.string.error_field_required)
    }

    @Test
    fun attemptCreateUser_notValidPassword_shouldShowPasswordError() {
        presenter.attemptCreateUser("bb77XNX_232@mail.com", "23b_")

        verify(view).showPasswordError(R.string.error_invalid_password)
        verify(view, never()).showEmailError(R.string.error_invalid_email)
        verify(view, never()).showEmailError(R.string.error_field_required)
    }

    @Test
    fun attemptCreateUser_emptyPassword_shouldShowPasswordError() {
        presenter.attemptCreateUser("bb77XNX_232@mail.com", "")

        verify(view).showPasswordError(R.string.error_invalid_password)
        verify(view, never()).showEmailError(R.string.error_invalid_email)
        verify(view, never()).showEmailError(R.string.error_field_required)
    }

    @Test
    fun attemptCreateUser_notValidValues_shouldShowEmailPasswordError() {
        presenter.attemptCreateUser("23jdnjcs.ru", "232")

        verify(view).showEmailError(R.string.error_invalid_email)
        verify(view).showPasswordError(R.string.error_invalid_password)
    }

    @Test
    fun checkUser_nonNull_shouldOpenListScreenAndFinishCurrentScreen() {
        presenter.checkUser(user)

        verify(view).openListScreen()
        verify(view).finishActivity()
    }

    @Test
    fun checkUser_null_shouldNotOpenListScreen() {
        presenter.checkUser(null)

        verify(view, never()).openListScreen()
        verify(view, never()).finishActivity()
    }

    @Test
    fun saveUserIdToPrefs_nonNull_shouldSaveUserToPrefs() {
        presenter.saveUserIdToPrefs("csdcscsdcds")

        verify(view).saveUserIdToPrefs("csdcscsdcds")
    }

    @Test
    fun saveUserIdToPrefs_null_shouldSaveUserToPrefs() {
        presenter.saveUserIdToPrefs(null)

        verify(view, never()).saveUserIdToPrefs("csdcscsdcds")
    }
}