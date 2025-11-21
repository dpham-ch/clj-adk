(ns clj-adk.artifacts
  (:import [com.google.adk.artifacts InMemoryArtifactService]))

(defn in-memory-artifact-service
  "Creates an InMemoryArtifactService."
  []
  (InMemoryArtifactService.))
