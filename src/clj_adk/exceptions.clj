(ns clj-adk.exceptions
  (:import [com.google.adk.exceptions AdkException]))

(defn adk-exception
  "Creates an AdkException."
  [message]
  (AdkException. message))
