package com.needsvswants.app.di

import com.needsvswants.app.BuildConfig
import com.needsvswants.app.data.auth.AuthSessionStore
import com.needsvswants.app.data.auth.CredentialManagerGoogleIdTokenProvider
import com.needsvswants.app.data.auth.GoogleIdTokenProvider
import com.needsvswants.app.data.billing.BillingController
import com.needsvswants.app.data.billing.PlayBillingController
import com.needsvswants.app.data.entitlement.EntitlementLocalStore
import com.needsvswants.app.data.entitlement.EntitlementRemote
import com.needsvswants.app.data.entitlement.SupabaseEntitlementRemote
import com.needsvswants.app.data.prefs.AppPreferences
import com.needsvswants.app.data.remote.HttpSupabaseAuth
import com.needsvswants.app.data.remote.SupabaseAuth
import com.needsvswants.app.data.remote.SupabaseConfig
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Wiring for the Pro subscription + Google auth seams.
 *
 * All values come from BuildConfig placeholders (empty by default), so the app
 * runs fully offline/local: the auth/entitlement clients self-disable and the
 * billing controller reports Unavailable.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class EntitlementModule {

    @Binds
    abstract fun bindSupabaseAuth(impl: HttpSupabaseAuth): SupabaseAuth

    @Binds
    abstract fun bindEntitlementRemote(impl: SupabaseEntitlementRemote): EntitlementRemote

    @Binds
    abstract fun bindBillingController(impl: PlayBillingController): BillingController

    @Binds
    abstract fun bindGoogleIdTokenProvider(
        impl: CredentialManagerGoogleIdTokenProvider
    ): GoogleIdTokenProvider

    companion object {
        @Provides
        @Singleton
        fun provideEntitlementLocalStore(prefs: AppPreferences): EntitlementLocalStore = prefs

        @Provides
        @Singleton
        fun provideAuthSessionStore(prefs: AppPreferences): AuthSessionStore = prefs

        @Provides
        @Singleton
        fun provideSupabaseConfig(): SupabaseConfig = SupabaseConfig(
            url = BuildConfig.SUPABASE_URL,
            anonKey = BuildConfig.SUPABASE_ANON_KEY,
            proTrialProductId = BuildConfig.PRO_TRIAL_PRODUCT_ID,
            proMonthlyProductId = BuildConfig.PRO_MONTHLY_PRODUCT_ID,
            maxMonthlyProductId = BuildConfig.PRO_MAX_MONTHLY_PRODUCT_ID,
            googleWebClientId = BuildConfig.GOOGLE_WEB_CLIENT_ID
        )
    }
}