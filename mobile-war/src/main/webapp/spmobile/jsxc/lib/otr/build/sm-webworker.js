/*
 * Copyright (C) 2000 - 2021 Silverpeas
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of
 * the GPL, you may redistribute this Program in connection with Free/Libre
 * Open Source Software ("FLOSS") applications as described in Silverpeas's
 * FLOSS exception.  You should have received a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * "http://www.silverpeas.org/docs/core/legal/floss_exception.html"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

;(function (root) {
  "use strict";

  root.OTR = {}

  var hasCrypto = false
  if (root.crypto)
    hasCrypto = true
  else
    root.crypto = {
      randomBytes: function () {
        throw new Error("Haven't seeded yet.")
      }
    }

  // default imports
  var imports = [
      'vendor/salsa20.js'
    , 'vendor/bigint.js'
    , 'vendor/crypto.js'
    , 'vendor/eventemitter.js'
    , 'lib/const.js'
    , 'lib/helpers.js'
    , 'lib/sm.js'
  ]

  function wrapPostMessage(method) {
    return function () {
      postMessage({
          method: method
        , args: Array.prototype.slice.call(arguments, 0)
      })
    }
  }

  var sm
  onmessage = function (e) {
    var data = e.data
    switch (data.type) {
      case 'seed':
        if (data.imports) imports = data.imports
        importScripts.apply(root, imports)

        if (hasCrypto)
          break

        // use salsa20 when there's no prng in webworkers
        var state = new root.Salsa20(
          data.seed.slice(0, 32),
          data.seed.slice(32)
        )
        root.crypto.randomBytes = function (n) {
          return state.getBytes(n)
        }
        break
      case 'init':
        sm = new root.OTR.SM(data.reqs)
        ;['trust','question', 'send', 'abort'].forEach(function (m) {
          sm.on(m, wrapPostMessage(m));
        })
        break
      case 'method':
        sm[data.method].apply(sm, data.args)
        break
    }
  }

}(this))