(ns clj-adk.genai
  #?(:clj (:import [com.google.genai Client]
                   [com.google.genai.types Content Part GenerateContentResponse])))

(defn create-content
  "Creates a Content object from a string."
  [text]
  #?(:clj (-> (Content/builder)
              (.addPart (-> (Part/builder) (.text text) .build))
              (.build))
     :cljs {:contents [{:role "user" :parts [{:text text}]}]}))

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
     :cljs {:api-key api-key :vertex-ai vertex-ai :project project :location location}))

(defn generate-content
  "Generates content using the provided client, model-id, and content (string or Content object).
   Returns a Promise in CLJS."
  ([client model-id content]
   (generate-content client model-id content nil))
  ([client model-id content config]
   #?(:clj (let [c (if (instance? Content content) content (create-content content))]
             (.generateContent (.models client) model-id c config))
      :cljs (let [api-key (:api-key client)
                  base-url "https://generativelanguage.googleapis.com/v1beta/models"
                  url (str base-url "/" model-id ":generateContent?key=" api-key)
                  body (if (string? content)
                         (create-content content)
                         content)
                  opts {:method "POST"
                        :headers {"Content-Type" "application/json"}
                        :body (js/JSON.stringify (clj->js body))}]
              (js/fetch url (clj->js opts))))))
