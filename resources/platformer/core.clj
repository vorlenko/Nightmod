(load-game-file "entities.clj")

(defn update-screen!
  [screen entities]
  (doseq [{:keys [x y height me? to-destroy]} entities]
    (when me?
      (x! screen x)
      (when (< y (- height))
        (restart-game!))))
  entities)

(defscreen main-screen
  :on-show
  (fn [screen entities]
    (->> (orthogonal-tiled-map "level.tmx" (/ 1 16))
         (update! screen :camera (orthographic) :renderer))
    (create-player))
  :on-render
  (fn [screen entities]
    (clear! 0.5 0.5 1 1)
    (->> entities
         (map (fn [entity]
                (->> entity
                     (move screen)
                     (prevent-move screen)
                     (animate screen))))
         (render! screen)
         (update-screen! screen)))
  :on-resize
  (fn [{:keys [width height] :as screen} entities]
    (orthographic! screen
                   :set-to-ortho
                   false
                   (* vertical-tiles (/ width height))
                   vertical-tiles)))

(set-game-screen! main-screen)
