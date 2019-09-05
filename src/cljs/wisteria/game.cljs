(ns wisteria.game
   (:require ["Phaser" :as phaser]))


(defn prime-game-update [this]
  (js/console.log "Updating..."))

(defn prime-game-stage [game]
  (reify
    Object
    (preload [this]
      (js/console.log "Preloading..."))
    (create [this]
      (js/console.log "Creating..."))
    (update [this]
      (prime-game-update this))    ))

;(set! (.. js/Phaser.Stage -prototype -constructor) prime-game-stage)


(defn init-phaser []
  (js/console.log "Initializing Phaser 3...")
  (let [game (js/Phaser.Game.
              (clj->js
               {:type js/Phaser.AUTO
                :width 800
                :height 600
                :scene [prime-game-stage]}))]))
