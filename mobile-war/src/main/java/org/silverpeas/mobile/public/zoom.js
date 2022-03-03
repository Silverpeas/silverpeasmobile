/*
 * Copyright (C) 2000 - 2022 Silverpeas
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

/**
 *
 * User: svu
 * Date: 24/03/16
 */

document.addEventListener('DOMContentLoaded', function () {


    var scale = 1,
        gestureArea = document.getElementById('gesture-area'),
        scaleElement = document.getElementById('scale-element'),
        realElement = document.getElementById('real-element'),
        resetTimeout;

    /* resize the content to fit the screen */
    var scale = (screen.width  / realElement.clientWidth) * 0.95;
    scaleElement.style.webkitTransform = scaleElement.style.transform = 'scale(' + scale + ')';
    scaleElement.style.webkitTransformOrigin = scaleElement.style.transformOrigin = '0 0';

    interact(gestureArea)
        .gesturable({
            onstart: function (event) {
                clearTimeout(resetTimeout);
                scaleElement.classList.remove('reset');
            },
            onmove: function (event) {
                scale = scale * (1 + event.ds);

                scaleElement.style.webkitTransform =
                    scaleElement.style.transform =
                        'scale(' + scale + ')';

                dragMoveListener(event);
            },
            onend: function (event) {
                resetTimeout = setTimeout(reset, 1000);
                scaleElement.classList.add('reset');
            }
        })
        .draggable({onmove: dragMoveListener});

});

function reset () {
    scale = 1;
    scaleElement.style.webkitTransform =
        scaleElement.style.transform =
            'scale(1)';
}

function dragMoveListener (event) {
    var target = event.target,
    // keep the dragged position in the data-x/data-y attributes
        x = (parseFloat(target.getAttribute('data-x')) || 0) + event.dx,
        y = (parseFloat(target.getAttribute('data-y')) || 0) + event.dy;

    // translate the element
    target.style.webkitTransform =
        target.style.transform =
            'translate(' + x + 'px, ' + y + 'px)';

    // update the posiion attributes
    target.setAttribute('data-x', x);
    target.setAttribute('data-y', y);
}

