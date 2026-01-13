package rajib.automation.framework.Miscelleneous;

public class CopilotPrompt {
    String pageSchema =
            """
            You are a Page Observation Agent.
    
            INPUT:
            - HTML DOM is provided below.
    
            OUTPUT:
            - Produce ONE JSON object ONLY.
    
            SCHEMA:
            {
              pageName,
              framework: "BasePage_PageField",
              fields[],
              compositeFields[],
              tables[]
            }
    
            RULES:
            - Output ONLY JSON
            - Match field names EXACTLY
            - Do not invent keys
    
            HTML:
            <body>
              ...<body>
    
    <h2>Automation Framework Demo Form</h2>
    
    <div class="section">
    
        <!-- TEXTBOX -->
        <label for="firstName">First Name</label>
        <input id="firstName" type="text" value="Rajib">
    
        <!-- CHECKBOX -->
        <label>
            <input id="subscribe" type="checkbox"> Subscribe to newsletter
        </label>
    
        <!-- RADIO -->
        <label>Gender</label>
        <label><input type="radio" name="gender" id="genderMale"> Male</label>
        <label><input type="radio" name="gender" id="genderFemale"> Female</label>
    
    </div>
    
    <div class="section">
    
        <!-- COMPOSITE DROPDOWN -->
        <label for="state">State</label>
        <select id="state" onchange="onStateChange()">
            <option value="">-- Select State --</option>
            <option value="CA">California</option>
            <option value="TX">Texas</option>
            <option value="NY">New York</option>
        </select>
    
        <label for="city">City</label>
        <select id="city" disabled="">
            <option value="">-- Select City --</option>
        </select>
    
    </div>
    
    <hr>
    
    <h3>Users Table (Standard HTML Table)</h3>
    
    <table id="usersTable" border="1" style="border-collapse: collapse; width: 60%;">
        <thead>
            <tr>
                <th>UserId</th>
                <th>Name</th>
                <th>Status</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>101</td>
                <td>Alice</td>
                <td>ACTIVE</td>
            </tr>
            <tr>
                <td>102</td>
                <td>Bob</td>
                <td>INACTIVE</td>
            </tr>
        </tbody>
    </table>
    
    <hr>
    
    <h3>Users Grid (Custom Div-Based Grid)</h3>
    <div id="usersGrid">
        <div class="row header">
            <div class="cell">UserId</div>
            <div class="cell">Name</div>
            <div class="cell">Status</div>
        </div>
        <div class="row">
            <div class="cell">101</div>
            <div class="cell">Alice</div>
            <div class="cell">
                <a href="#" onclick="this.textContent='CLICKED'">CLICKED</a>
            </div>
        </div>
    </div>
    
    
    <div class="section">
    
        <!-- ACTION -->
        <button id="submitBtn" onclick="alert('Form Submitted!')">
            Submit
        </button>
    
    </div>
    
    
    
    </body>
            </body>
    
           EXAMPLE OUTPUT (STRICT FORMAT):
    
    {
      "pageName": "SamplePage",
      "framework": "BasePage_PageField",
      "fields": [
        {
          "key": "firstName",
          "fieldType": "TEXTBOX",
          "logicalName": "First Name",
          "locator": {
            "strategy": "id",
            "value": "firstName"
          }
        }
      ],
      "compositeFields": [
        {
          "key": "stateCity",
          "compositeType": "COMPOSITE_DROPDOWN",
          "components": [
            {
              "key": "state",
              "fieldType": "DROPDOWN",
              "logicalName": "State",
              "locator": {
                "strategy": "id",
                "value": "state"
              }
            },
            {
              "key": "city",
              "fieldType": "DROPDOWN",
              "logicalName": "City",
              "locator": {
                "strategy": "id",
                "value": "city"
              }
            }
          ]
        }
      ],
      "tables": [
        {
          "key": "usersTable",
          "tableLocator": {
            "strategy": "id",
            "value": "usersTable"
          },
          "rowLocator": {
            "strategy": "css",
            "value": "tbody tr"
          },
          "cellLocator": {
            "strategy": "css",
            "value": "td"
          },
          "columns": [
            {
              "name": "UserId",
              "index": 0,
              "type": "TEXT"
            }
          ]
        }
      ]
    }
    TASK:
    Using the EXACT SAME JSON STRUCTURE as the example above,
    generate PageSchema JSON for the HTML DOM below.
    
    RULES:
    - Output ONLY JSON
    - Match field names EXACTLY
    - Do not invent keys or names   
            """;
    //Generate JSON schema for the above page HTML










 }
