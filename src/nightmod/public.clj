(ns nightmod.public
  (:require [clojail.core :as jail]
            [clojure.java.io :as io]
            [nightmod.screens :as s]
            [nightmod.utils :as u]
            [play-clj.core :refer :all]))

(defn ^:private wrap
  [f args]
  (try (apply f args)
    (catch Exception e
      (when (nil? @u/error) (reset! u/error e)))))

(defn set-game-screen!
  [& screens]
  (->> (set-screen-with-options! s/nightmod
                                 (conj (vec screens) s/overlay-screen)
                                 :wrap wrap)
       (fn [])
       (app! :post-runnable)))

(defmacro load-game-file
  [n]
  (some->> (or (io/resource n)
               (throw (Throwable. (str "File not found:" n))))
           slurp
           (format "(do %s\n)")
           jail/safe-read))