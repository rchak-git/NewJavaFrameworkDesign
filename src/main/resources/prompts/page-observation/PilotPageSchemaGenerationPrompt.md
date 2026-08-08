Generate a JSON object only.

Start with { and end with }.
Return it in the editor as raw JSON, not chat prose.

Use this DOM:
<body>
    <div class="form-container">
        <h2>Create Payment</h2>

        <form action="/payment/create" method="post">
            <div class="form-group">
                <label for="customerName">Customer Name</label>
                <input type="text" id="customerName" name="customerName">
            </div>

            <div class="form-group">
                <label for="amount">Amount</label>
                <input type="number" id="amount" name="amount" step="0.01">
            </div>

            <div class="form-group">
                <label for="scenario">Scenario</label>
                <select id="scenario" name="scenario">
                    <option value="SUCCESS">SUCCESS</option>
                    <option value="FAILURE">FAILURE</option>
                    <option value="PENDING">PENDING</option>
                </select>
            </div>

            <button type="submit">Submit</button>
        </form>
    </div>
</body>

Schema:
- fields
- tables

Do not use:
- compositeFields
- components
- legacy structures

Use:
- locators.main
- TEXTBOX for customerName and amount
- DROPDOWN for scenario
- BUTTON for submit