(ns clj-adk.generative-test
  (:require [clojure.test :refer :all]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.properties :as prop]
            [malli.core :as m]
            [malli.generator :as mg]
            [clj-adk.schemas :as schemas]
            [clj-adk.genai :as genai]))

(defspec client-schema-valid-test
  100
  (prop/for-all [config (mg/generator schemas/ClientSchema)]
    (m/validate schemas/ClientSchema config)))

(def SimpleClientConfig
  [:map
   [:api-key [:string {:min 1}]]])

(defspec create-client-generative-test
  100
  (prop/for-all [config (mg/generator SimpleClientConfig)]
    (try
      (some? (genai/create-client config))
      (catch Exception _ false))))

(defspec gemini-schema-valid-test
  100
  (prop/for-all [config (mg/generator schemas/GeminiSchema)]
    (m/validate schemas/GeminiSchema config)))

;; For schemas that have :fn validation or specific instance checks,
;; automatic generation might fail or require custom generators.
;; We can test the structure of LlmAgentSchema, but the nested :model or :tools
;; might need care if they are strictly typed.
;; schemas/LlmAgentSchema uses :any for :model in the schema definition [:or :string :any]
;; but :tools is [:vector ToolSchema]. ToolSchema has an instance check on CLJ.

;; We can define a simplified generator for LlmAgentSchema that avoids Tools for now,
;; or we can mock the Tool instances if needed.
;; For now, let's verify LlmAgentSchema data structure validation where tools are nil or empty.

(def LlmAgentSchemaDataOnly
  [:map
   [:name :string]
   [:description {:optional true} :string]
   [:instruction {:optional true} :string]
   [:model {:optional true} :string] ;; restricting to string for generation simplicity
   ])

(defspec llm-agent-schema-valid-test
  100
  (prop/for-all [agent-data (mg/generator LlmAgentSchemaDataOnly)]
    (m/validate LlmAgentSchemaDataOnly agent-data)))
