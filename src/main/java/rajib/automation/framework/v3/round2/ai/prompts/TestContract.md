{
"$schema": "http://json-schema.org/draft-07/schema#",
"title": "Practice Form Test Scenario",
"type": "object",
"properties": {
"scenario": { "type": "string" },
"steps": {
"type": "array",
"items": {
"type": "object",
"properties": {
"fieldKey": { "type": "string" },
"intent": { "type": "string", "enum": ["populate", "verify", "click", "read"] },
"value": {},
"expected": {},
"validationType": { "type": "string", "enum": ["TEXT_EQUALS", "TEXT_CONTAINS"] }
},
"required": ["fieldKey", "intent"]
}
}
},
"required": ["scenario", "steps"]
}