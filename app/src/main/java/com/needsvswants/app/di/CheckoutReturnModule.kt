package com.needsvswants.app.di

import com.needsvswants.app.data.entitlement.CheckoutReturnSync
import com.needsvswants.app.data.entitlement.EntitlementSync
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Binds [EntitlementSync] as the [CheckoutReturnSync] seam used by the paywall
 * ViewModel. Kept in its own module so the provider-agnostic checkout-return
 * sync stays decoupled from the billing-provider wiring in [EntitlementModule].
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class CheckoutReturnModule {

    @Binds
    abstract fun bindCheckoutReturnSync(impl: EntitlementSync): CheckoutReturnSync
}
