(ns clj-adk.models
  (:require [clj-adk.core :as core]
            [clj-adk.schemas :as schemas]
            [malli.core :as m]))

(defmulti create
  "Creates a model based on the :type key in the config."
  (fn [config]
    (let [type (:type config)]
      (if (m/validate schemas/ModelType type)
        type
        :invalid))))

(defmethod create :gemini [config]
  (core/gemini-model (dissoc config :type)))

(defmethod create :invalid [config]
  (throw (ex-info "Invalid or missing model type in config"
                  {:config config
                   :allowed-types (m/form schemas/ModelType)})))
