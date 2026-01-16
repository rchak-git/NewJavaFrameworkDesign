# DeanRaj Framework — Current State Snapshot

> A collaborative learning framework by Rajib & Dean  
> Purpose: Deep understanding of test automation architecture, not just test execution.

---

## 📌 Current Status (Stable Milestone)

**As of:** <add date if you like>

The framework has reached a **clean, stable checkpoint**.  
Core TestData and Table Verification architecture is complete and validated.

This document exists to:
- prevent loss of context during pauses
- make resumption effortless
- record key design decisions
- guide future phases intentionally

---

## ✅ Completed Work

### TestData Model
- Introduced structured TestData model under `model.testdata`
- One test case identified by:
    - `name` (test scope)
- Table verifications identified by:
    - `id` (verification intent)
- Test cases may contain:
    - atomic/composite page field data only
    - table verification data only
    - both (mixed tests)

### Table Verification Pipeline (End-to-End)
- `TableVerificationLoader`
    - Loads by `testCaseName + tableVerificationId`
    - Fail-fast behavior
- `TableJsonVerifier`
    - Owns all verification behavior
    - No logic inside model classes
- Verified assertion types:
    - `EQUALS`
    - `CONTAINS`

### Execution Flow (Locked)
1. Load TestData root
2. Select TestCase by `name`
3. Select table verification by `id`
4. Resolve table via `tableKey`
5. Resolve row via match criteria
6. Assert using verifier logic

### Proof of Stability
- End-to-end tests passing using:
    - `DemoFormPage.json`
    - Standard HTML tables
    - Custom grid tables
- `CONTAINS` assertion validated successfully

---

## 🔒 Locked Design Decisions (Do Not Revisit Lightly)

- **Model classes are passive**
    - No behavior inside TestData models
- **Behavior lives in executors/verifiers**
- **One test case = one `name`**
- **Multiple table verifications allowed per test case**
- **Table selection uses `id`, not name duplication**
- **Explicit is better than implicit**
    - No auto-inference of test data

---

## ⚠ Known Pain Points (Captured, Not Solved Yet)

These are acknowledged and intentionally deferred:

- Raw JSON authoring is not user-friendly
- Easy to introduce syntax errors manually
- Loader error messages can be improved
- No authoring aids for end users yet

---

## 🔥 Backlog Burner (Future Enhancements)

Not for immediate implementation:

1. **TestDataBuilder utilities**
    - Java-based fluent builders to generate valid TestData
    - Reduce direct JSON authoring
2. **Improved loader diagnostics**
    - Friendly, contextual error messages
3. **TestData templates / examples**
4. **Optional YAML support**
5. **Long-term ideas**
    - Excel → TestData bridge
    - UI-based authoring (very long term)

---

## 🧭 Planned Next Phases (When Resumed)

1. TestData usability layer (builders/helpers)
2. Documentation of TestData contract
3. Optional discussion: method-scoped TestData
4. Framework ergonomics (not core redesign)

---

## ▶ How to Resume Framework Work

When returning after a pause:

1. Read this file (`FRAMEWORK_STATE.md`)
2. Re-run DemoFormPage table verification tests
3. Start with TestData usability (not core changes)

---

## 🧠 Guiding Principle

> The DeanRaj Framework prioritizes **clarity, correctness, and learning**  
> over speed, shortcuts, or premature tooling.

This framework is a long-running collaboration and learning artifact.
