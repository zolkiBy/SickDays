package com.android.dev.yashchuk.sickleaves.login

import com.android.dev.yashchuk.sickleaves.data.source.remote.net.AuthApi
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations


class LoginPresenterTest {

    @Mock
    private lateinit var view: LoginContract.View
    @Mock
    private lateinit var api: AuthApi

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun validateEmail() {

    }
}