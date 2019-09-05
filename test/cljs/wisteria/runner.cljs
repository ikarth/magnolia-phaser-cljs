(ns wisteria.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [wisteria.core-test]))

(doo-tests 'wisteria.core-test)
