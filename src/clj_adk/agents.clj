(ns clj-adk.agents
  (:require [clj-adk.core :as core]
            [clj-adk.schemas :as schemas]
            [malli.core :as m])
  (:import [com.google.adk.agents LlmAgent SequentialAgent ParallelAgent LoopAgent BaseAgent]
           [com.google.adk.agents InvocationContext]))

;; Construction

(defmulti create
  "Creates an agent based on the :type key in the config."
  (fn [config]
    (let [type (:type config)]
      (if (m/validate schemas/AgentType type)
        type
        :invalid))))

(defmethod create :llm [config]
  (core/llm-agent (dissoc config :type)))

(defmethod create :sequential [config]
  (core/sequential-agent (dissoc config :type)))

(defmethod create :parallel [config]
  (core/parallel-agent (dissoc config :type)))

(defmethod create :loop [config]
  (core/loop-agent (dissoc config :type)))

(defmethod create :invalid [config]
  (throw (ex-info "Invalid or missing agent type in config"
                  {:config config
                   :allowed-types (m/form schemas/AgentType)})))

;; Common Execution

(defn run-live
  "Runs the agent synchronously.
   Args:
     invocation-context: The InvocationContext."
  [^BaseAgent agent {:keys [invocation-context]}]
  (.runLive agent invocation-context))

(defn run-async
  "Runs the agent asynchronously.
   Args:
     invocation-context: The InvocationContext."
  [^BaseAgent agent {:keys [invocation-context]}]
  (.runAsync agent invocation-context))

;; Multimethod Accessors

(defmulti instruction
  "Returns the instruction of the agent."
  class)

(defmethod instruction LlmAgent [^LlmAgent agent]
  (.instruction agent))

(defmethod instruction :default [_] nil)


(defmulti tools
  "Returns the tools of the agent."
  class)

(defmethod tools LlmAgent [^LlmAgent agent]
  (.tools agent))

(defmethod tools :default [_] nil)


(defmulti model
  "Returns the model of the agent."
  class)

(defmethod model LlmAgent [^LlmAgent agent]
  (.model agent))

(defmethod model :default [_] nil)


(defmulti sub-agents
  "Returns the list of sub-agents."
  class)

(defmethod sub-agents SequentialAgent [^SequentialAgent agent]
  (.subAgents agent))

(defmethod sub-agents ParallelAgent [^ParallelAgent agent]
  (.subAgents agent))

(defmethod sub-agents LoopAgent [^LoopAgent agent]
  (.subAgents agent))

(defmethod sub-agents :default [_] nil)
