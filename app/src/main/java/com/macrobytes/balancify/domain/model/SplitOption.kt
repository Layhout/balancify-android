package com.macrobytes.balancify.domain.model

/**
 * SPLIT_EQUALLY = 'SPLIT_EQUALLY',
 *   CUSTOM
 */

enum class SplitOption(val label: String) {
    SPLIT_EQUALLY("Equally"),
    CUSTOM("Custom"),
}