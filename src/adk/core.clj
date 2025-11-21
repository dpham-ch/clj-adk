(ns adk.core
  (:require [malli.core :as m]
            [camel-snake-kebab.core :as csk])
  (:import [com.google.adk.agents LlmAgent SequentialAgent ParallelAgent LoopAgent BaseAgent]
           [com.google.adk.models Gemini Model]
           [com.google.adk.tools FunctionTool]
           [java.util Map List]))

;; Schemas

(def GeminiSchema
  [:map
   [:model-name :string]
   [:api-key {:optional true} :string]])

(def ToolSchema
  [:fn #(instance? com.google.adk.tools.BaseTool %)])

(def AgentSchema
  [:fn #(instance? BaseAgent %)])

(def LlmAgentSchema
  [:map
   [:name :string]
   [:description {:optional true} :string]
   [:instruction {:optional true} :string]
   [:model {:optional true} [:or :string [:fn #(instance? Model %)]]]
   [:tools {:optional true} [:vector ToolSchema]]])

(def SequentialAgentSchema
  [:map
   [:name :string]
   [:description {:optional true} :string]
   [:agents [:vector AgentSchema]]])

(def ParallelAgentSchema
  [:map
   [:name :string]
   [:description {:optional true} :string]
   [:agents [:vector AgentSchema]]])

(def LoopAgentSchema
  [:map
   [:name :string]
   [:description {:optional true} :string]
   [:agent AgentSchema]
   [:max-iterations {:optional true} :int]])

;; Factories

(defn gemini-model
  "Creates a Gemini model instance."
  [config]
  (if (m/validate GeminiSchema config)
    (let [builder (Gemini/builder)]
      (.modelName builder (:model-name config))
      (when (:api-key config)
        (.apiKey builder (:api-key config)))
      (.build builder))
    (throw (ex-info "Invalid Gemini configuration" (m/explain GeminiSchema config)))))

(defn function-tool
  "Creates a function tool."
  [clazz method-name]
  (FunctionTool/create clazz method-name))

(defn llm-agent
  "Creates an LlmAgent instance."
  [config]
  (if (m/validate LlmAgentSchema config)
    (let [builder (LlmAgent/builder)]
      (.name builder (:name config))
      (when (:description config)
        (.description builder (:description config)))
      (when (:instruction config)
        (.instruction builder (:instruction config)))
      (when (:model config)
        (.model builder (:model config)))
      (when (:tools config)
        (doseq [tool (:tools config)]
          (.addTool builder tool)))
      (.build builder))
    (throw (ex-info "Invalid LlmAgent configuration" (m/explain LlmAgentSchema config)))))

(defn sequential-agent
  "Creates a SequentialAgent instance."
  [config]
  (if (m/validate SequentialAgentSchema config)
    (let [builder (SequentialAgent/builder)]
      (.name builder (:name config))
      (when (:description config)
        (.description builder (:description config)))
      (.subAgents builder ^List (:agents config))
      (.build builder))
    (throw (ex-info "Invalid SequentialAgent configuration" (m/explain SequentialAgentSchema config)))))

(defn parallel-agent
  "Creates a ParallelAgent instance."
  [config]
  (if (m/validate ParallelAgentSchema config)
    (let [builder (ParallelAgent/builder)]
      (.name builder (:name config))
      (when (:description config)
        (.description builder (:description config)))
      (.subAgents builder ^List (:agents config))
      (.build builder))
    (throw (ex-info "Invalid ParallelAgent configuration" (m/explain ParallelAgentSchema config)))))

(defn loop-agent
  "Creates a LoopAgent instance."
  [config]
  (if (m/validate LoopAgentSchema config)
    (let [builder (LoopAgent/builder)]
      (.name builder (:name config))
      (when (:description config)
        (.description builder (:description config)))
      ;; LoopAgent builder has subAgents which takes List or varargs.
      ;; But logically it might wrap one agent? Or multiple?
      ;; The schema I defined has :agent (singular).
      ;; Java API says subAgents.
      ;; Let's check if we can pass a list with one agent.
      (.subAgents builder ^List [(:agent config)])
      (when (:max-iterations config)
        (.maxIterations builder (:max-iterations config)))
      (.build builder))
    (throw (ex-info "Invalid LoopAgent configuration" (m/explain LoopAgentSchema config)))))
