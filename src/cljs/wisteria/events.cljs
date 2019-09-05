(ns wisteria.events
  (:require
   [re-frame.core :as re-frame]
   [wisteria.db :as db]
   [wisteria.game :as game]
   [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]

   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
            db/default-db))

(re-frame/reg-event-db
 ::set-active-panel
 (fn-traced [db [_ active-panel]]
   (assoc db :active-panel active-panel)))

(re-frame/reg-event-db
 ::initialize-game
 (fn-traced [db _]
            (game/init-phaser)
            db))

;; (defn start-phaser
  ;; (js/Phaser.Game.))
