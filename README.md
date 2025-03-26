# 🧠 ServiceNow Chat App (Java + React + Dialogflow CX)

This project is a full-stack application that integrates:
- **Dialogflow CX** for conversational AI
- **Java (Spring Boot)** backend using Firestore
- **React** frontend UI
- **Google Cloud Functions** compatible webhook for ticket handling

---

## 📦 Project Structure

servicenow-chat-app/ ├── backend/ # Java Spring Boot webhook │ ├── build.gradle │ └── src/ │ └── main/java/com/example/webhook/DialogflowWebhookController.java ├── frontend/ # React frontend │ ├── package.json │ └── src/components/IncidentForm.jsx └── README.md


---

## 🛠 Backend Setup (Spring Boot + Firestore)

### 📁 Prerequisites
- Java 17+
- Gradle
- Firebase project with Firestore enabled
- A Firebase **service account key JSON** file

### ⚙️ Configuration

1. Place your Firebase service account file:

backend/src/main/resources/serviceAccountKey.json


2. Modify `DialogflowWebhookController.java`:
```java
FileInputStream serviceAccount = new FileInputStream("src/main/resources/serviceAccountKey.json");


3. Ensure Firestore contains a tickets collection with documents like:
Document ID: INC123
Fields:
  - status: "Open"
  - description: "User cannot login"

## Run Backend
cd backend
./gradlew bootRun

Your Spring Boot backend will run on http://localhost:8080

## Dialogflow CX Setup
Go to Dialogflow CX Console

Create an agent and add:

Entity: incident_id

Intent: with sample phrases like:
Get incident status for INC123
What’s the update on ticket INC456?

3. Set your webhook to:
https://<your-backend-host>/webhook

## Frontend Setup (React)
Prerequisites
Node.js (v18 recommended)
npm

 # Install Dependencies
cd frontend
npm install


 # Run the App
npm start
Visit: http://localhost:3000

# Update Code
const res = await axios.post("https://<your-cloud-function-url>/webhook", dialogflowPayload);

with your backend URL if running locally or deployed.

# Example Ticket Payload
{
  "session": "projects/<project-id>/locations/global/agents/<agent-id>/sessions/123456",
  "queryResult": {
    "parameters": {
      "incident_id": "INC123"
    }
  }
}

## Features
🔍 Query ticket details by ID

🤖 AI chatbot integration via Dialogflow CX

🔗 Firebase Firestore ticket data

🌐 React UI for manual testing


## Deploying Backend to Google Cloud Functions (Optional)
You can build your Spring Boot project as a JAR and wrap it in a Google Cloud Function using spring-cloud-function-adapter-gcp.

Let me know if you want deployment steps!

##  Tech Stack
Layer	Tech
Frontend	React, Axios
Backend	Java, Spring Boot
Cloud	Google Cloud Firestore, Dialogflow CX
Auth/Data	Firebase Admin SDK

## To Do
 Secure key handling (move to env/Secret Manager)

 Add multi-turn Dialogflow support

 Connect to real ServiceNow API

 Deployment scripts for GCP Cloud Run / Firebase Hosting



## Let me know if you want this as a file download or if you want it adapted for deployment on Firebase or Cloud Run.











