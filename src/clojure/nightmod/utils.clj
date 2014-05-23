(ns nightmod.utils
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.string :as string]
            [nightcode.dialogs :as dialogs]
            [nightcode.editors :as editors]
            [nightcode.ui :as ui]
            [nightcode.utils :as nc-utils]
            [seesaw.core :as s])
  (:import [java.awt BorderLayout]
           [java.text SimpleDateFormat]))

(def ^:const window-width 1200)
(def ^:const window-height 768)
(def ^:const editor-width 700)
(def ^:const core-file "core.clj")
(def ^:const settings-file "settings.edn")
(def ^:const docs-name "*Docs*")
(def ^:const repl-name "*REPL*")
(def ^:const game-ns 'nightmod.run)
(def ^:const out-char-limit 5000)

(def main-dir (atom nil))
(def project-dir (atom nil))
(def error (atom nil))
(def out (atom ""))
(def stack-trace? (atom false))
(def editor (atom nil))

(defn get-data-dir
  []
  (.getCanonicalPath (io/file (System/getProperty "user.home") "Nightmod")))

(defn format-date
  [unix-time]
  (.format (SimpleDateFormat. "yyyy.MM.dd HH:mm:ss") unix-time))

(defn format-project-dir
  [project-name]
  (-> project-name
      (string/replace " " "-")
      nc-utils/format-project-name))

(defn new-project-name!
  [template]
  (dialogs/show-text-field-dialog!
    (nc-utils/get-string :enter-project-name)
    (nc-utils/get-string template)))

(defn new-project-dir!
  [project-name]
  (let [dir-name (format-project-dir project-name)
        project-file (io/file @main-dir dir-name)]
    (cond
      (= 0 (count dir-name))
      (dialogs/show-simple-dialog! (nc-utils/get-string :invalid-name))
      (.exists project-file)
      (dialogs/show-simple-dialog! (nc-utils/get-string :file-exists))
      :else
      project-file)))

(defn new-project!
  [template project-name project-file]
  (.mkdirs project-file)
  (doseq [file-name (-> (str template "/_files.edn")
                        io/resource
                        slurp
                        edn/read-string)]
    (-> (str template "/" file-name)
        io/resource
        io/input-stream
        (io/copy (io/file project-file file-name))))
  (->> (format (slurp (io/resource settings-file)) project-name)
       (spit (io/file project-file settings-file)))
  (.getCanonicalPath project-file))

(defn editor-hidden?
  []
  (-> @editor .getParent nil?))

(defn toggle-editor!
  ([]
    (toggle-editor! (editor-hidden?)))
  ([show?]
    (s/invoke-later
      (if show?
        (.add (.getContentPane @ui/root) @editor BorderLayout/EAST)
        (.remove (.getContentPane @ui/root) @editor))
      (.revalidate @ui/root)
      ; focus on the root so the game can receive keyboard events
      (when-not show?
        (s/request-focus! @ui/root)))))

(defn append-to-out!
  [s]
  (swap! out
         (fn [out-old new-str]
           (let [out-new (str out-old new-str)
                 out-overflow (- (count out-new) out-char-limit)]
             (if (> out-overflow 0)
               (subs out-new out-overflow)
               out-new)))
         s))
