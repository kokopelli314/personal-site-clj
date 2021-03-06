(defproject personal-site-clj "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :resource-paths ["resources"]
  :dependencies [[org.clojure/clojure "1.9.0"]
								 [org.clojure/clojurescript "1.10.238" :scope "provided"]
								 [org.clojure/tools.nrepl "0.2.12"]
                 [org.clojure/core.async "0.4.474"]
								 [com.cognitect/transit-clj "0.8.309"]
								 [cljfmt "0.5.7"]
                 [ring "1.6.3"]
                 [ring/ring-defaults "0.3.1"]
                 [bk/ring-gzip "0.3.0"]
                 [radicalzephyr/ring.middleware.logger "0.6.0"]
                 [clj-logging-config "1.9.12"]
                 [compojure "1.6.1"]
                 [environ "1.1.0"]
                 [com.stuartsierra/component "0.3.2"]
                 [org.danielsz/system "0.4.1"]
								 [org.clojure/tools.namespace "0.2.11"]
								 [quil "2.7.1"]
								 [cljs-ajax "0.7.5"]
								 [hiccup "1.0.5"]
								 [markdown-clj "1.0.5"]
								 [com.draines/postal "2.0.2"]
                 [binaryage/devtools "0.9.10"]
                 [figwheel-sidecar "0.5.15"]
                 [reloaded.repl "0.2.4"]]

	:plugins [[lein-cljsbuild "1.1.7"]
						[cider/cider-nrepl "0.18.0"]]

  :min-lein-version "2.6.1"

  :source-paths ["src/clj" "src/cljs" "src/cljc"]

  :test-paths ["test/clj" "test/cljc"]

  :clean-targets ^{:protect false} [:target-path :compile-path "resources/public/js" "dev-target"]

  :uberjar-name "personal-site-clj.jar"

  ;; Use `lein run` if you just want to start a HTTP server, without figwheel
  :main personal-site-clj.application

  ;; nREPL by default starts in the :main namespace, we want to start in `user`
  ;; because that's where our development helper functions like (go) and
  ;; (browser-repl) live.
  :repl-options {:init-ns user}

  :cljsbuild {:builds
              ;; [
              ;;  ;; {:id "app"
              ;;  ;;  :source-paths ["src/cljs" "src/cljc" "dev"]

              ;;  ;;  :figwheel {:on-jsload "personal-site-clj.system/reset"}

              ;;  ;;  :compiler {:main cljs.user
              ;;  ;;             :asset-path "/js/compiled/out"
              ;;  ;;             :output-to "dev-target/public/js/compiled/personal_site_clj.js"
              ;;  ;;             :output-dir "dev-target/public/js/compiled/out"
              ;;  ;;             :source-map-timestamp true}}
              ;;  ]
              {:home-page
               {:id "home-page" ;; min
                :source-paths ["src/cljs" "src/cljc" "dev"]
                ;;:jar true
                :figwheel {:on-jsload "personal-site-clj.system/reset"}
                :compiler {:main cljs.user
                           :output-to "resources/public/js/compiled/home_page.js"
                           :output-dir "resources/public/js/compiled/out"
                           :source-map-timestamp true
                           :asset-path "/js/compiled/out"
                           ;;:optimizations :advanced
                           ;;:closure-defines {goog.DEBUG false}
                           ;;:pretty-print false
                           }}
               :articles
               {:id "articles"
                :source-paths ["src/cljs" "src/cljc" "dev"]
                ;;:jar true
                :figwheel {:on-jsload "personal-site-clj.system/reset"}
                :compiler {:main cljs.user
                           :output-to "resources/public/js/compiled/articles.js"
                           :output-dir "resources/public/js/compiled/out/articles"
                           :source-map-timestamp true
                           :asset-path "/js/compiled/out"
                           ;;:optimizations :advanced
                           ;;:closure-defines {goog.DEBUG false}
                           ;;:pretty-print false
                           }}}
               }

  ;; When running figwheel from nREPL, figwheel will read this configuration
  ;; stanza, but it will read it without passing through leiningen's profile
  ;; merging. So don't put a :figwheel section under the :dev profile, it will
  ;; not be picked up, instead configure figwheel here on the top level.

  :figwheel {;; :http-server-root "public"       ;; serve static assets from resources/public/
             ;; :server-port 3449                ;; default
             ;; :server-ip "127.0.0.1"           ;; default
             :css-dirs ["resources/public/css"]  ;; watch and update CSS

             ;; Start an nREPL server into the running figwheel process. We
             ;; don't do this, instead we do the opposite, running figwheel from
             ;; an nREPL process, see
             ;; https://github.com/bhauman/lein-figwheel/wiki/Using-the-Figwheel-REPL-within-NRepl
             ;; :nrepl-port 7888

             ;; To be able to open files in your editor from the heads up display
             ;; you will need to put a script on your path.
             ;; that script will have to take a file path and a line number
             ;; ie. in  ~/bin/myfile-opener
             ;; #! /bin/sh
             ;; emacsclient -n +$2 $1
             ;;
             ;; :open-file-command "myfile-opener"

             :server-logfile "log/figwheel.log"}

  :doo {:build "articles"}

  :profiles {:dev
             {:dependencies [[figwheel "0.5.15"]
                             [figwheel-sidecar "0.5.15"]
                             [com.cemerick/piggieback "0.2.2"]
                             [org.clojure/tools.nrepl "0.2.13"]
                             [lein-doo "0.1.10"]
                             [reloaded.repl "0.2.4"]]

              :plugins [[lein-figwheel "0.5.15"]
                        [lein-doo "0.1.10"]]

              :source-paths ["dev"]
              :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}}

             :uberjar
             {:source-paths ^:replace ["src/clj" "src/cljc"]
              :prep-tasks ["compile"
                           ["cljsbuild" "once" "home-page"]
                           ["cljsbuild" "once" "articles"]]
              :hooks []
              :omit-source true
              :aot :all}})

