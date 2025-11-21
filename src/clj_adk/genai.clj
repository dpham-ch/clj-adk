(ns clj-adk.genai
  (:import [com.google.genai.types Content Part]))

(defn create-content
  "Creates a Content object from a string."
  [text]
  (-> (Content/builder)
      (.addPart (-> (Part/builder) (.text text) .build))
      (.build)))
