(ns clj-adk.tools
  (:import [com.google.adk.tools FunctionTool]))

(defn function-tool
  "Creates a function tool."
  [clazz method-name]
  (FunctionTool/create clazz method-name))
