# DeanRaj Framework — Current State Snapshot

> A collaborative learning framework by Rajib & Dean  
> Purpose: Deep understanding of test automation architecture, not just test execution.

---

## 📌 Current Status (Stable Milestone)

**As of:** 2026-06-29

The framework has reached a **clean, stable checkpoint** for the current round2 architecture.  
The command pipeline now supports keyword-extensible verification through a generic attribute model, and table verification has been validated end-to-end.

This document exists to:
- prevent loss of context during pauses
- make resumption effortless
- record key design decisions
- guide future phases intentionally

---

## ✅ Completed Work

### TestData Model
- Introduced structured `TestStepData` under `v3.round2.testdatamodels`
- One test step identified by:
  - `fieldKey`
  - `intent`
- Step data may contain:
  - `value`
  - `expected`
  - `matchBy`
  - `validationType`
  - `actionType`
  - `populationType`

### Generic Command Pipeline
- `StepConverters` translates `TestStepData` into `ControlCommand`
- `ControlCommand` now carries:
  - stable core fields: `action`, `fieldKey`, `value`, `type`
  - extensible `attributes` map for keyword-specific data
- This reduces the need to keep adding hardwired fields to the core command model

### Table Verification Pipeline (End-to-End)
- `TableControl`
  - reads table rows into structured maps
  - uses `matchBy` to find the target row
  - uses `expected` to verify the row contents
- Verified assertion types:
  - `DEFAULT` → treated as text equality
  - `TEXT_EQUALS`
  - `STATIC_TEXT`
  - `TEXT_CONTAINS`

### Loader / Expansion Flow
- `TestDataLoaderR2`
  - loads scenario definitions by `scenarioId`
  - expands test cases by `testName`
  - supports parameter substitution for nested data
  - now resolves nested map values such as `matchBy`

### Proof of Stability
- Payment table verification passed after the round2 pipeline alignment was fixed
- The issue was caused by stale compiled classes in the IDE, resolved by rebuilding the project
- The current end-to-end flow is now validated:
  1. JSON
  2. `TestStepData`
  3. `TestDataLoaderR2`
  4. `StepConverters`
  5. `ControlCommand`
  6. `TableControl`

---

## 🔒 Standards / Business Rules (Keep These Stable)

- **Keep the core pipeline generic**
  - do not add a new hardwired field to the command model for every keyword
- **Keyword-specific data belongs in `ControlCommand.attributes`**
  - controls interpret the attributes they understand
- **Behavior stays in executors / controls, not in model classes**
  - models should remain passive data carriers
- **Explicit data mapping is preferred over inference**
  - no hidden guessing or automatic reinterpretation of unrelated fields
- **Table verification must separate row selection from row assertion**
  - `matchBy` identifies the row
  - `expected` verifies the selected row
- **Stability over shortcut fixes**
  - if a change reveals a recurring pattern, prefer the structural fix over a one-off patch

---

## ⚠ Known Notes / Gaps

These are known and intentionally not treated as framework defects right now:

- `mvn clean test` may still need separate environment troubleshooting
  - possible JDK / Maven alignment issue
  - possible local dependency or workspace build-state issue
- IDE rebuild may be needed if stale compiled classes are suspected
- The keyword-attribute model is in place, but there is not yet a formal handler registry for all future widgets/keywords

---

## 🧭 Planned Next Steps

1. **Validate the keyword-attribute pattern with another complex widget**
   - confirm the current design scales beyond tables
2. **Define a lightweight keyword handling convention**
   - decide which data belongs in core fields vs attributes
3. **Document the round2 step contract**
   - clarify how test data should be authored for populate / verify / action steps
4. **Optional future enhancement: keyword handler registry**
   - make widget-specific keyword processing more plug-in like
5. **Investigate Maven build alignment separately if needed**
   - only if `mvn clean test` continues to fail locally
6. **Future backlog: Scenario Flow abstraction**
   - evaluate a higher-level flow layer that composes reusable scenarios from multiple test data files into multi-page business journeys
   - implement only if it clearly improves readability, reuse, maintainability, and debugging clarity over the current step/scenario model
   - keep the current rule that flow orchestration should remain outside the core runtime until its benefit is proven

---

## ▶ How to Resume Framework Work

When returning after a pause:

1. Read this file (`FRAMEWORK_STATE.md`)
2. Re-run the payment table verification test
3. Confirm the same pipeline behavior still holds
4. Add the next complex widget only after the current contract stays stable

---

## 🧠 Guiding Principle

> The DeanRaj Framework prioritizes **clarity, correctness, and learning**  
> over speed, shortcuts, or premature tooling.

This framework is a long-running collaboration and learning artifact.
