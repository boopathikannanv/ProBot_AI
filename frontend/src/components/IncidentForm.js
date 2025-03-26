import React, { useState } from 'react';
import axios from 'axios';

const IncidentForm = () => {
  const [incidentId, setIncidentId] = useState('');
  const [response, setResponse] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    const dialogflowPayload = {
      session: "projects/<project-id>/locations/<location-id>/agents/<agent-id>/sessions/123456",
      queryResult: {
        parameters: {
          incident_id: incidentId
        }
      }
    };

    try {
      const res = await axios.post("https://<your-cloud-function-url>/webhook", dialogflowPayload);
      const text = res.data.fulfillment_response.messages[0].text.text[0];
      setResponse(text);
    } catch (err) {
      setResponse("Error contacting webhook");
    }
  };

  return (
    <div>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          value={incidentId}
          onChange={(e) => setIncidentId(e.target.value)}
          placeholder="Enter Incident ID"
        />
        <button type="submit">Submit</button>
      </form>
      <div>{response}</div>
    </div>
  );
};

export default IncidentForm;