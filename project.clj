(defproject nightmod "0.1.0-SNAPSHOT"
  :description "A tool for making live-moddable games in Clojure"
  :dependencies [[com.badlogicgames.gdx/gdx "1.0.1"]
                 [com.badlogicgames.gdx/gdx-backend-lwjgl "1.0.1"]
                 [com.badlogicgames.gdx/gdx-box2d "1.0.1"]
                 [com.badlogicgames.gdx/gdx-box2d-platform "1.0.1"
                  :classifier "natives-desktop"]
                 [com.badlogicgames.gdx/gdx-bullet "1.0.1"]
                 [com.badlogicgames.gdx/gdx-bullet-platform "1.0.1"
                  :classifier "natives-desktop"]
                 [com.badlogicgames.gdx/gdx-platform "1.0.1"
                  :classifier "natives-desktop"]
                 [nightcode "0.3.5"
                  :exclusions [leiningen
                               lein-ancient
                               lein-cljsbuild
                               lein-clr
                               lein-droid
                               lein-fruit
                               lein-typed
                               play-clj/lein-template]]
                 [org.clojars.oakes/clojail "1.0.6"]
                 [org.clojure/clojure "1.6.0"]
                 ;[org.eclipse.jgit "3.2.0.201312181205-r"]
                 [play-clj "0.3.5-SNAPSHOT"]
                 [seesaw "1.4.4"]]
  :uberjar-exclusions [#"clojure-clr.*\.zip"]
  :resource-paths ["resources"]
  :source-paths ["src/clojure"]
  :java-source-paths ["src/java"]
  :javac-options ["-target" "1.6" "-source" "1.6" "-Xlint:-options"]
  :aot [nightmod.core]
  :main ^:skip-aot nightmod.Nightmod)
