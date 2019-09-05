(ns wisteria.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [wisteria.events :as events]
   [wisteria.routes :as routes]
   [wisteria.views :as views]
   [wisteria.config :as config]
   [wisteria.config :as game]
   ))


(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn dispatch-update-timer-event
  []
  (let [now (js/Date.)]
    (re-frame/dispatch [:update-timer now])))

(defn init []
  (routes/app-routes)
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root)
  (re-frame/dispatch-sync [::events/initialize-game]))
