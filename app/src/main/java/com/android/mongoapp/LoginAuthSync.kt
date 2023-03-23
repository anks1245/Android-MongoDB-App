package com.android.mongoapp

import io.realm.mongodb.App
import io.realm.mongodb.Credentials
import io.realm.mongodb.User


class LoginAuthSync {
    suspend fun loginAsync(app: App, credentials: Credentials): User {
        return app.login(credentials)
    }
}