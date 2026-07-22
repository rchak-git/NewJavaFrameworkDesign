# PageSchema Generation Prompt

## Purpose
Generate a standardized `PageSchema` for our automation framework based on the page URL, page purpose, business flow, and DOM snippet.

## Inputs
- **Page name:** `PaymentForm`
- **Page URL:** `http://localhost:8080/payment/form`
- **Page purpose:** `Allows the user to create a payment record by entering customer name, amount, and scenario.`
- **Business flow / user journey:** `Create a payment using the form fields so the record can later appear in payment history.`
- **Relevant HTML DOM snippet:**
```html
<html>
  <head>
    <title>Payment Form</title>
  </head>
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
</html>