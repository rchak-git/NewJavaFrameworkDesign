# PageSchema Generation Prompt

## Purpose
Generate a standardized `PageSchema` for our automation framework based on the page URL, page purpose, business flow, and DOM snippet.

## Inputs
- **Page name:** `PaymentHistory`
- **Page URL:** `http://localhost:8080/payments`
- **Page purpose:** `Provides information about Payment History`
- **Business flow / user journey:** `Display payment records so the user can verify the created payment appears in the history table.`
- **Relevant HTML DOM snippet:**

```html
<body>
<h2>Payment History</h2>

<table>
    <thead>
    <tr>
        <th>Payment ID</th>
        <th>Customer Name</th>
        <th>Amount</th>
        <th>Scenario</th>
        <th>Status</th>
        <th>Created At</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>PAY-1001</td>
        <td>Ravi</td>
        <td>100.00</td>
        <td>SUCCESS</td>
        <td>SUCCESS</td>
        <td>2026-05-29T14:52:26</td>
    </tr>
    <tr>
        <td>PAY-1002</td>
        <td>Anita</td>
        <td>250.50</td>
        <td>FAILURE</td>
        <td>FAILED</td>
        <td>2026-05-29T14:52:26</td>
    </tr>
    <tr>
        <td>PAY-1003</td>
        <td>John</td>
        <td>75.25</td>
        <td>PENDING</td>
        <td>PENDING</td>
        <td>2026-05-29T14:52:26</td>
    </tr>
    <tr>
        <td>PAY-2A7E40F4</td>
        <td>Ravi</td>
        <td>100.00</td>
        <td>SUCCESS</td>
        <td>SUCCESS</td>
        <td>2026-06-14T22:49:32</td>
    </tr>
    <tr>
        <td>PAY-033AF14F</td>
        <td>Ravi</td>
        <td>100.00</td>
        <td>SUCCESS</td>
        <td>SUCCESS</td>
        <td>2026-06-14T22:49:54</td>
    </tr>
    <tr>
        <td>PAY-07935F7B</td>
        <td>Ravi</td>
        <td>100.00</td>
        <td>SUCCESS</td>
        <td>SUCCESS</td>
        <td>2026-06-14T22:51:35</td>
    </tr>
    <tr>
        <td>PAY-95FEF2A7</td>
        <td>Ravi</td>
        <td>100.00</td>
        <td>SUCCESS</td>
        <td>SUCCESS</td>
        <td>2026-06-14T22:53:03</td>
    </tr>
    <tr>
        <td>PAY-9D8E20EF</td>
        <td>Ravi</td>
        <td>100.00</td>
        <td>SUCCESS</td>
        <td>SUCCESS</td>
        <td>2026-06-14T22:53:41</td>
    </tr>
    <tr>
        <td>PAY-E50904D4</td>
        <td>Ravi</td>
        <td>100.00</td>
        <td>SUCCESS</td>
        <td>SUCCESS</td>
        <td>2026-06-14T22:56:28</td>
    </tr>
    <tr>
        <td>PAY-ACE001AD</td>
        <td>Ravi</td>
        <td>100.00</td>
        <td>SUCCESS</td>
        <td>SUCCESS</td>
        <td>2026-06-17T21:55:33</td>
    </tr>
    <tr>
        <td>PAY-AFE34536</td>
        <td>Ravi</td>
        <td>100.00</td>
        <td>SUCCESS</td>
        <td>SUCCESS</td>
        <td>2026-06-20T11:41:22</td>
    </tr>
    <tr>
        <td>PAY-C8D66A12</td>
        <td>Ravi</td>
        <td>100.00</td>
        <td>SUCCESS</td>
        <td>SUCCESS</td>
        <td>2026-06-20T12:44:02</td>
    </tr>
    </tbody>
</table>

</body>
```

## Instructions
1. Analyze the page purpose and the DOM snippet.
2. Identify all meaningful UI elements that should be represented in the schema.
3. Classify each element as one of the following:
   - field control
   - button/action
   - dropdown/select
   - checkbox/radio
   - table/grid
   - text/label for verification, if needed
4. Generate a structured `PageSchema` that includes:
   - page/schema name
   - control key
   - control type
   - locator information derived from the DOM
   - any metadata needed by the page class generator
5. Prefer the existing naming conventions already used in the framework.
6. Do not invent controls that are not supported by the DOM.
7. If the DOM snippet is incomplete or ambiguous, clearly note the uncertainty rather than guessing.

## Output
- Return only the `PageSchema` in the agreed structured format.
- Do not include explanatory text unless something is ambiguous.

## Notes
- This prompt is intended for GitHub Copilot Chat in Ask mode.
- Replace the placeholder values with the actual page details before use.
- Keep the output aligned with the framework’s existing schema conventions.