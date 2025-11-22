(ns clj-adk.genai
  #?(:clj (:import [com.google.genai Client]
                   [com.google.genai.types Content Part GenerateContentResponse])
     :cljs (:require ["@google/generative-ai" :refer [GoogleGenerativeAI]])))

(defn create-content
  "Creates a Content object from a string."
  [text]
  #?(:clj (-> (Content/builder)
              (.addPart (-> (Part/builder) (.text text) .build))
              (.build))
     :cljs {:role "user" :parts [{:text text}]}))

(defn create-client
  "Creates a GenAI client from a config map.
   Config can contain: :api-key, :vertex-ai, :project, :location."
  [{:keys [api-key vertex-ai project location]}]
  #?(:clj (let [builder (Client/builder)]
            (when api-key (.apiKey builder api-key))
            (when vertex-ai (.vertexAI builder vertex-ai))
            (when project (.project builder project))
            (when location (.location builder location))
            (.build builder))
     :cljs (GoogleGenerativeAI. api-key)))

(defn generate-content
  "Generates content using the provided client, model-id, and content (string or Content object).
   Returns a Promise in CLJS."
  ([client model-id content]
   (generate-content client model-id content nil))
  ([client model-id content config]
   #?(:clj (let [c (if (instance? Content content) content (create-content content))]
             (.generateContent (.models client) model-id c config))
      :cljs (let [model (.getGenerativeModel client #js {:model model-id})
                  c (if (string? content) content (clj->js content))]
              (.generateContent model c)))))
