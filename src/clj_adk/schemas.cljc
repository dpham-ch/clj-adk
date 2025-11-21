(ns clj-adk.schemas
  #?(:clj (:import [com.google.adk.agents BaseAgent]
                   [com.google.adk.models Model]
                   [com.google.adk.tools BaseTool])))

(def AgentType
  [:enum :llm :sequential :parallel :loop])

(def ModelType
  [:enum :gemini])

(def ClientSchema
  [:map
   [:api-key {:optional true} :string]
   [:vertex-ai {:optional true} :boolean]
   [:project {:optional true} :string]
   [:location {:optional true} :string]])

(def GeminiSchema
  [:map
   [:model-name :string]
   [:api-key {:optional true} :string]])

(def ToolSchema
  #?(:clj [:fn #(instance? BaseTool %)]
     :cljs [:any]))

(def AgentSchema
  #?(:clj [:fn #(instance? BaseAgent %)]
     :cljs [:any]))

(def LlmAgentSchema
  [:map
   [:name :string]
   [:description {:optional true} :string]
   [:instruction {:optional true} :string]
   [:model {:optional true} [:or :string :any]]
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
