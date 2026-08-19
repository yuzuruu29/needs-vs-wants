package com.needsvswants.app.ui.screens.input

import androidx.compose.runtime.Composable
import com.needsvswants.app.ui.theme.AppTheme
import com.needsvswants.app.ui.theme.PremiumDialog

@Composable
fun ReceiptProGateDialog(
    onDismiss: () -> Unit,
    onOpenPaywall: () -> Unit
) {
    val palette = AppTheme.colors

    PremiumDialog(
        onDismissRequest = onDismiss,
        eyebrow = "PRO & MAX EXCLUSIVE",
        eyebrowColor = palette.gold,
        title = "Receipt Sorter",
        body = "Snap your supermarket or store receipts, parse line items with on-device OCR, and rapidly sort each purchase into Needs vs Wants in seconds.\n\nUnlimited receipt scans and batch ledger logging are available on Pro and Max memberships.",
        confirmLabel = "Go Pro / Max",
        onConfirm = {
            onDismiss()
            onOpenPaywall()
        },
        dismissLabel = "Maybe later"
    )
}
