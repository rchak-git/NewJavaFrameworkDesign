enerate test data as a JSON array of scenario objects. Each scenario must have:  
"scenario": a descriptive string for the scenario name
"steps": an array of step objects (see below)
Each step object must have:  
"fieldKey": the control or field name
"intent": "populate", "verify", "click", "read", etc.
"value" if intent is "populate"; "expected" and "validationType" if intent is "verify"
For the scenario "valid_form_submission":  
Populate all fields with valid data
Verify "lastName" and "userEmail" with TEXT_EQUALS validation
Output: [ { "scenario": "valid_form_submission", "steps": [ { "fieldKey": "firstName", "intent": "populate", "value": "John" }, ... ] } ] . Please follow the opened files for further contract alignment