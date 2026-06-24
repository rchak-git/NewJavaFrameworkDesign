# Page Class Generation Prompt

## Purpose
Generate a complete Java page class for our automation framework using the previously created `PageSchema` file as the source of truth.

## Current Framework Contract
For the current POC-1 phase, use only the supported abstractions present in the schema:
- field controls
- table controls

Do not reintroduce legacy composite/component-based patterns unless explicitly requested.

## Source Schema
- **Schema file name:** `PaymentHistoryPageSchema.json`

## Task
1. Locate and read the schema file named `PaymentHistoryPageSchema.json`.
2. Treat that schema as the source of truth.
3. Generate the corresponding **full Java page class**.
4. The page class **must extend `BasePageR2`**.
5. Use the framework’s existing page-object style and control registration patterns.
6. Create members for all declared `fields` and `tables`.
7. Use the schema’s metadata for table behavior such as:
    - identifier column
    - row selector
    - column mapping
8. Preserve schema keys as canonical control names wherever possible.
9. Do not invent controls or abstractions not present in the schema.
10. If any required implementation detail is missing from the schema, flag the gap instead of guessing.

## Output Requirements
- Generate a **complete Java class**, not a fragment.
- Include the **package declaration**.
- Include all **required imports**.
- Include the **class declaration**.
- The class must **extend `BasePageR2`**.
- Include any required **annotations** used by the framework.
- Include the **constructor**.
- Include initialization for `pageFields` and `tables`.
- Do **not** return only schema snippets or partial code.
- Return the **full compilable class implementation**.

## Notes
- The page class must remain compatible with the existing command-driven framework.
- Keep naming and structure aligned with existing framework conventions.
- If the page includes a table, generate the table control in a way that supports row-based verification and column lookup from the schema.