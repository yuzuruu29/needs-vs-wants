package com.needsvswants.app.data.prefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.needsvswants.app.domain.QuotaState
import java.io.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Pins the pure TTL gate behind the durable PayPal-return pending flag:
 * active strictly within the TTL, expired at the boundary, and 0 = inactive.
 * Also round-trips the persisted daily-quota state (incl. the carry field).
 */
@OptIn(ExperimentalCoroutinesApi::class)
class AppPreferencesTest {

    private var dataStoreScope: CoroutineScope? = null
    private var dataStoreFile: File? = null

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        dataStoreScope?.cancel()
        dataStoreScope = null
        Dispatchers.resetMain()
        dataStoreFile?.let { runCatching { it.delete() } }
    }

    private fun buildPrefs(): AppPreferences {
        val file = File.createTempFile("nvw-prefs-test", ".preferences_pb")
        dataStoreFile = file
        dataStoreScope = CoroutineScope(UnconfinedTestDispatcher())
        val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
            scope = dataStoreScope!!
        ) { file }
        return AppPreferences(dataStore)
    }

    private val dayMillis = 24 * 60 * 60 * 1000L

    @Test
    fun paypalReturnPendingActive_trueWithinTtl() {
        val now = 100_000_000L
        assertTrue(paypalReturnPendingActive(storedAt = now - 1_000L, nowMillis = now))
        // One millisecond before the boundary is still active.
        assertTrue(paypalReturnPendingActive(storedAt = now - dayMillis + 1L, nowMillis = now))
    }

    @Test
    fun paypalReturnPendingActive_falseAtTtlBoundaryAndBeyond() {
        val now = 100_000_000L
        // Exactly at the boundary the flag is stale (strictly-less-than TTL).
        assertFalse(paypalReturnPendingActive(storedAt = now - dayMillis, nowMillis = now))
        assertFalse(paypalReturnPendingActive(storedAt = now - dayMillis - 1_000L, nowMillis = now))
    }

    @Test
    fun paypalReturnPendingActive_falseWhenZeroOrNeverStored() {
        val now = 100_000_000L
        assertFalse(paypalReturnPendingActive(storedAt = 0L, nowMillis = now))
        assertFalse(paypalReturnPendingActive(storedAt = -5L, nowMillis = now))
    }

    @Test
    fun paypalReturnPendingActive_honorsCustomTtl() {
        val now = 100_000_000L
        assertTrue(paypalReturnPendingActive(storedAt = now - 9_000L, nowMillis = now, ttlMillis = 10_000L))
        assertFalse(paypalReturnPendingActive(storedAt = now - 10_000L, nowMillis = now, ttlMillis = 10_000L))
    }

    @Test
    fun quotaState_roundTripsDay_logsCreated_andCarriedLogs() = runTest {
        val prefs = buildPrefs()
        val state = QuotaState(
            day = "2026-08-10",
            logsCreated = 3,
            carriedLogs = 2,
            bonusLogs = 8,
            adsWatched = 1
        )
        prefs.setQuotaState(state)
        val read = prefs.quotaState.first()
        assertEquals("2026-08-10", read.day)
        assertEquals(3, read.logsCreated)
        assertEquals(2, read.carriedLogs)
        assertEquals(8, read.bonusLogs)
        assertEquals(1, read.adsWatched)
    }

    @Test
    fun resetQuotaForDay_zeroesCountersAndCarry() = runTest {
        val prefs = buildPrefs()
        prefs.setQuotaState(
            QuotaState(day = "2026-08-10", logsCreated = 3, carriedLogs = 2, bonusLogs = 8, adsWatched = 1)
        )
        prefs.resetQuotaForDay("2026-08-11")
        val read = prefs.quotaState.first()
        assertEquals("2026-08-11", read.day)
        assertEquals(0, read.logsCreated)
        assertEquals(0, read.carriedLogs)
        assertEquals(0, read.bonusLogs)
        assertEquals(0, read.adsWatched)
    }
}
