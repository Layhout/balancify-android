package com.example.balancify

import android.app.Application
import com.clerk.api.Clerk

class BalancifyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Clerk.initialize(
            this,
            publishableKey = "pk_test_Y2FyaW5nLWNyb3ctOC5jbGVyay5hY2NvdW50cy5kZXYk"
        )
    }
}