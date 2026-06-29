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
- define canonical framework standards
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

## 🧩 Canonical Round2 Contract

These rules define how Round2 step execution must behave.

### Placeholder Syntax
- Use only:
    - `${name}`
- No alternate placeholder styles are supported in the canonical design.

### Step Contract
- `POPULATE` steps must use:
    - `value`
- `VERIFY` steps must use:
    - `expected`
- `ACTION` steps must use:
    - `actionType`

### Placeholder Resolution Rule
- Placeholder resolution must happen **before** control execution.
- Controls must never parse or resolve placeholders themselves.
- A single shared resolver must handle both `value` and `expected`.

### Supported Data Shapes
The resolver must support recursive resolution for:
- `String`
- `Map`
- `List`
- nested combinations of these

### Resolution Source
Placeholders must resolve from runtime execution context, which may include:
- scenario parameters
- generated values
- previously stored values
- shared execution state

### Control Boundary Rule
Controls are responsible only for:
- populate
- verify
- action
- read

Controls are **not** responsible for:
- placeholder substitution
- scenario parameter interpolation
- test data interpretation

### Table Verification Rule
- Table verification steps must provide expected row data as a map/object.
- Table controls must receive already-resolved values.
- Verification logic compares actual rows against resolved expected rows only.

---

## 🧩 Updated Placeholder and Runtime Value Contract

The framework now distinguishes between authoring-time placeholders and runtime-context references.

### Supported value forms

#### 1. Literal
A plain JSON value is used as-is.

Example:
```json
"status": "COMPLETED"