(ns clj-adk.models
  (:require [clj-adk.interop :as interop]
            [clj-adk.schemas :as schemas]
            [malli.core :as m])
  (:import [com.google.adk.models Gemini]))

(defn- gemini-model
  "Creates a Gemini model instance."
  [config]
  (if (m/validate schemas/GeminiSchema config)
    (interop/props->java Gemini (Gemini/builder) config)
    (throw (ex-info "Invalid Gemini configuration" (m/explain schemas/GeminiSchema config)))))

(defmulti create
  "Creates a model based on the :type key in the config."
  (fn [config]
    (let [type (:type config)]
      (if (m/validate schemas/ModelType type)
        type
        :invalid))))

(defmethod create :gemini [config]
  (gemini-model (dissoc config :type)))

(defmethod create :invalid [config]
  (throw (ex-info "Invalid or missing model type in config"
                  {:config config
                   :allowed-types (m/form schemas/ModelType)})))
