(ns clj-adk.sessions
  (:import [com.google.adk.sessions Session InMemorySessionService]))

(defn create-session
  "Creates a new Session."
  [id]
  (-> (Session/builder id)
      (.build)))

(defn in-memory-session-service
  "Creates an InMemorySessionService."
  []
  (InMemorySessionService.))
