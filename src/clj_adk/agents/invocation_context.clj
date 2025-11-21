(ns clj-adk.agents.invocation-context
  (:import [com.google.adk.agents InvocationContext RunConfig RunConfig$Builder]
           [com.google.adk.sessions InMemorySessionService Session]
           [com.google.adk.artifacts InMemoryArtifactService]
           [com.google.adk.agents BaseAgent]
           [com.google.adk.agents LiveRequestQueue]
           [com.google.adk.sessions Session$Builder]
           [java.util UUID]))

(defn- default-run-config []
  (.build (RunConfig/builder)))

(defn- default-session []
  (-> (Session/builder (.toString (UUID/randomUUID)))
      (.build)))

(defn create
  "Creates an InvocationContext.
   Arguments are passed in a map.
   Keys:
     :session-service (optional, defaults to InMemorySessionService)
     :artifact-service (optional, defaults to InMemoryArtifactService)
     :agent (required) - The agent to invoke.
     :session (optional, defaults to a new Session)
     :live-request-queue (optional, defaults to null)
     :run-config (optional, defaults to default RunConfig)"
  [{:keys [session-service artifact-service agent session live-request-queue run-config]
    :or {session-service (InMemorySessionService.)
         artifact-service (InMemoryArtifactService.)
         session (default-session)
         run-config (default-run-config)}}]
  (InvocationContext/create
   session-service
   artifact-service
   agent
   session
   live-request-queue
   run-config))
