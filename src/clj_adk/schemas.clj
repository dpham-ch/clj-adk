(ns clj-adk.schemas
  (:import [com.google.adk.agents BaseAgent]
           [com.google.adk.models Model]
           [com.google.adk.tools BaseTool]))

(def GeminiSchema
  [:map
   [:model-name :string]
   [:api-key {:optional true} :string]])

(def ToolSchema
  [:fn #(instance? BaseTool %)])

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
