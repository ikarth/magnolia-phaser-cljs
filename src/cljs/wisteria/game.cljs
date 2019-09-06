(ns wisteria.game
   (:require ["Phaser" :as phaser]))

(defn collectStar [player, star]
  (.disableBody star true true))


(defn prime-game-create [this]
  (js/console.log "Creating...")
  (. (. (. this -add) image 0 0 "sky")
     setOrigin 0 0)
  ;(.image (.-add this) 400 300 "star")
  (js/console.log this)
  (.create (. this -anims) (clj->js {:key       "left"
                                     :frames    (.generateFrameNumbers (.-anims this)
                                                                       "dude"
                                                                       #js {:start 0
                                                                            :end   3})
                                     :frameRate 10
                                     :repeat    -1}))
  (.create (. this -anims) (clj->js {:key       "right"
                                     :frames    (.generateFrameNumbers (.-anims this)
                                                                       "dude"
                                                                       #js {:start 5
                                                                            :end   8})
                                     :frameRate 10
                                     :repeat    -1}))
  (.create (. this -anims) (clj->js {:key       "turn"
                                     :frames    (.generateFrameNumbers (.-anims this)
                                                                       "dude"
                                                                       #js {:start 4
                                                                            :end   4})
                                     :frameRate 20}))

  ;; I can't figure out an easy way to have the player and platform objects
  ;; referenced without storing them as part of the Stage object, given
  ;; what order they need to be initialized in.
  ;; Note that player, for example, is referenced in (update).
  ;; I guess I could wrap the whole thing in a let or something?
  (set! (.-myplatforms this) (. (. (. this -physics) -add) staticGroup))
  (. (. (. (.-myplatforms this) create 400, 568, "ground") setScale 2) refreshBody)
  (. (.-myplatforms this) create 600, 400, "ground")
  (. (.-myplatforms this) create 50, 250, "ground")
  (. (.-myplatforms this) create 750, 220, "ground")
  (set! (.-myplayer this) (.sprite (.-add(.-physics this)) 100 450 "dude" ))
  (.setBounce (.-myplayer this) 0.2)
  (.setCollideWorldBounds (.-myplayer this) true)
  (.collider (.-add (. this -physics)) (.-myplayer this) (.-myplatforms this))
  (set! (.-mycursors this) (.createCursorKeys (.-keyboard (.-input this))))
  (set! (.-mystars this) (. (.. this -physics -add)
                            group
                            (clj->js {:key "star"
                                      :repeat 11
                                      :setXY {:x 12 :y 0 :stepX 70}})))
  (.iterate (.-children (.-mystars this)) (fn [child]
                                            (.setBounceY child (js/Phaser.Math.FloatBetween 0.4 0.8))))
  (.collider (.-add (. this -physics)) (.-mystars this) (.-myplatforms this))
  (.overlap (.-add (. this -physics)) (.-myplayer this) (.-mystars this) collectStar nil this)
     )

(defn prime-game-update [this]
  ;(js/console.log this)
  (if (.. this -mycursors -left -isDown)
    (do
      (.setVelocityX (.-myplayer this) -160)
      (.play (.-anims (.-myplayer this)) "left" true))
    (if (.. this -mycursors -right -isDown)
      (do
        (.setVelocityX (.-myplayer this) 160)
        (.play (.-anims (.-myplayer this)) "right" true))  
      (do
        (.setVelocityX (.-myplayer this) 0)
        (.play (.-anims (.-myplayer this)) "turn")
        )
      ))
  (if (and (.. this -myplayer -body -touching -down) (.. this -mycursors -up -isDown))
    (.setVelocityY (.-myplayer this) -330))
  this)

(defn prime-game-stage [game]
  (reify
    Object;js/Phaser.Scene
    (preload [this]
      (js/console.log "Preloading...")
      (. (. this -load) image "sky" "assets/sky.png")
      (. (. this -load) image "ground" "assets/platform.png")
      (. (. this -load) image "star" "assets/star.png")
      (. (. this -load) image "bomb" "assets/bomb.png")
      (. (. this -load) spritesheet "dude" "assets/dude.png" #js {:frameWidth 32 :frameHeight 48})
            )
    (create [this]
      (prime-game-create this))    
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
                :physics {:default "arcade"
                          :arcade {:gravity {:y 300}
                                   :debug false}
                          }
                :scene [prime-game-stage]}))]))
