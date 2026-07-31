/** Shared ledger column header (D8). Port of LedgerHeader.swift. */
import { memo } from "react";
import { StyleSheet, View } from "react-native";
import { Eyebrow } from "./Eyebrow";

export const LedgerHeader = memo(function LedgerHeader() {
  return (
    <View style={styles.header}>
      <View style={styles.timeCol}>
        <Eyebrow>TIME</Eyebrow>
      </View>
      <View style={styles.itemCol}>
        <Eyebrow>ITEM</Eyebrow>
      </View>
      <View style={styles.costCol}>
        <Eyebrow>COST</Eyebrow>
      </View>
      <View style={styles.typeCol}>
        <Eyebrow>TYPE</Eyebrow>
      </View>
      <View style={styles.deleteCol} />
    </View>
  );
});

const styles = StyleSheet.create({
  header: {
    flexDirection: "row",
    alignItems: "center",
    paddingVertical: 6,
  },
  timeCol: { width: 48 },
  itemCol: { flex: 1 },
  costCol: { width: 88, alignItems: "flex-end" },
  typeCol: { width: 42, alignItems: "center" },
  deleteCol: { width: 32 },
});
